package projet.rest.data.endpoints;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import projet.rest.data.models.AvisEntity;
import projet.rest.data.models.CategoryEntity;
import projet.rest.data.models.ConfirmationToken;
import projet.rest.data.models.ProductEntity;
import projet.rest.data.models.UserEntity;
import projet.rest.data.services.UserService;
@Controller
@Data
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminControler {
	@Autowired
	UserService service ;
	
	public String CheckRole () {
		Collection<? extends GrantedAuthority> authorities;
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    authorities = auth.getAuthorities();
	     
		return authorities.toArray()[0].toString();
	}
	 
    
	@GetMapping("/home")
	public String returnindexadmin() {
		String myRole = CheckRole();
	    if (myRole.equals("USER")||myRole.equals("NOTVERIFIED")) {
	        return "redirect:/user/home";
	    }
	    return "admin/indexadmin";
	}
	
	/*Users*/
	
	@GetMapping("/userlist")
	public String AllUsers(Model model) {
		List<UserEntity> users =  service.getAllUserEntity();
		model.addAttribute("users",users);
		UserEntity user = new UserEntity();
		model.addAttribute("user",user);
		
	    return "admin/userlistadmin";
	}
	@GetMapping("/adduser")
	public String AddUsers(Model model) {
		UserEntity user = new UserEntity();
		model.addAttribute("user",user);
	    return "admin/adduseradmin";
	}
	@PostMapping("/adduser")
	public String UserregisterSuccess(@ModelAttribute("user") UserEntity user, Model model,@RequestParam("file")MultipartFile file ) {
		String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
    	if(FileName.contains("..")) {
    		System.out.println("not a proper file ");
    	}
    	try {
			user.setImageU(Base64.getEncoder().encodeToString(file.getBytes()));
			System.out.println("cv");
		} catch (IOException e) {
			System.out.println("dowiw");
			e.printStackTrace();
		}
	
		service.createUserEntity(user);
		
		return "redirect:/admin/userlist";
	}
	@GetMapping("/deluser/{id}")
	public String DelUsers(@PathVariable("id") Long id, Model model) {
	    service.DelTokenByIdUser(id);
		service.deleteUserEntity(id);
		return "redirect:/admin/userlist";
	}
	@GetMapping("/upduser/{id}")
	public String UpdUsers(@PathVariable("id") int id, Model model) {
		UserEntity user = service.getUserEntityById(id);
		model.addAttribute("user", user);
	    return "admin/upduseradmin";
	}
	@PostMapping("/upduseradmin/{id}")
	public String EditSuucesUser( Model model ,@PathVariable("id") long id ,@RequestParam ("username") String username , @RequestParam ("email") String email , @RequestParam("password") String password, @RequestParam("phone") String phone,@RequestParam ("birthDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date birthDate,@RequestParam("Role") String Role , @RequestParam("file") MultipartFile file  ) {
 
		 UserEntity user =new UserEntity();
		 user.setUsername(username);
		 user.setEmail(email);
		 user.setPassword(password);
		 user.setPhone(phone);
		 user.setBirthDate(birthDate);
		 user.setRole(Role);
		 String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
	    	if(FileName.contains("..")) {
	    		System.out.println("not a proper file ");
	    	}
	    	try {
				user.setImageU(Base64.getEncoder().encodeToString(file.getBytes()));
				System.out.println("cv");
			} catch (IOException e) {
				System.out.println("dowiw");
				e.printStackTrace();
			}
		
		 
		 service.modifyUserEntity(id, user);
		 return "redirect:/admin/userlist"; 
	}
	
	/*Categories*/
	@GetMapping("/categorielist")
	public String AllCategoris(Model model) {
		List<CategoryEntity> categories =  service.getAllCategories();
		model.addAttribute("categories",categories);
		CategoryEntity Category = new CategoryEntity() ;
		model.addAttribute("Category",Category);
	    return "admin/categorielistadmin";
	}
	@GetMapping("/addcategorie")
	public String AddCategories(Model model) {
		CategoryEntity c = new CategoryEntity() ;
		model.addAttribute("Category",c);
	    return "admin/addcategorieadmin";
	}

	@PostMapping("/addcategorie")
	public String registerSuccess(@ModelAttribute("Category") CategoryEntity Category, Model model) {
		service.createCategory(Category);
		return this.AllCategoris(model);
	}
	@GetMapping("/delcategorie/{id}")
	public String DelCategories(@PathVariable("id") int id, Model model ,RedirectAttributes redirAttrs) {
		List<ProductEntity> prods= service.getProductsByCategory(service.getCategoryById(id).getTitle());
		if (prods.isEmpty())
			service.deleteCategoryEntity(id);
		else {
			redirAttrs.addFlashAttribute("error", "Can't delete Category which contains products");
			return "redirect:/admin/categorielist";
		
		}
		return "redirect:/admin/categorielist";
	}
	
	/*Products*/
	@GetMapping("/productlist")
	public String AllProducts(Model model) {
		
			List<ProductEntity> products =  service.getAllProduct();
			model.addAttribute("products",products);
			ProductEntity product = new ProductEntity();
			model.addAttribute("product",product);
			
	    return "admin/productlistadmin";
	}
	
	
	@GetMapping("/addproductadmin")
	public String AddProducts(Model model) {
		List<CategoryEntity> categories = service.getAllCategories();
		model.addAttribute("categories",categories);
		CategoryEntity cat = new CategoryEntity ();
		model.addAttribute("category",cat);
		
	    return "admin/addproductadmin";
	}
	@PostMapping("/addproductadmin")
	public String registerSuccess( @RequestParam ("pcat") String catname , @RequestParam ("pname") String nom , @RequestParam("marque") String marque, @RequestParam("desc") String description , @RequestParam ("file") MultipartFile file , Model model ) {
		System.out.println("$$$$$$$$$$$$$$"+catname+nom+marque+description+file);
		service.createProduct(catname,nom,marque,description,file,"ADMIN");
		
		 return "redirect:/admin/productlist";
	}
	@GetMapping("/delproduct/{id}")
	public String DelProducts(@PathVariable("id") int id, Model model) {
		service.deleteProductEntity(id);
		return "redirect:/admin/productlist";
	}
	
	@GetMapping("/updproduct/{id}")
		public String UpdProducts(@PathVariable("id") int id, Model model) {
			ProductEntity product = service.getProductById(id);
			List<CategoryEntity> categories = service.getAllCategories();
			model.addAttribute("categories", categories);
			model.addAttribute("product", product);
		
	    return "admin/updproductadmin";
	}
	
	@PostMapping("/updproductadmin/{id}")
	public String EditSuuces( Model model ,@PathVariable("id") int i ,@RequestParam ("pcat") String catname , @RequestParam ("pname") String nom , @RequestParam("marque") String marque, @RequestParam("desc") String description,@RequestParam ("file") MultipartFile file  ) {
 
		 ProductEntity p1 = new ProductEntity();
		 p1.setCatname(catname);
		 p1.setNom(nom);
		 p1.setDescription(description);
		 p1.setMarque(marque);
		 String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
			if(FileName.contains("..")) {
				System.out.println("not a proper file ");
			}
			try {
				p1.setImg(Base64.getEncoder().encodeToString(file.getBytes()));
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		 service.modifyProduct(i, p1);
		return "redirect:/admin/productlist";
	}
	
	/*Reviews*/
	
	@GetMapping("/reviewlist")
	public String AllReviews(Model model) {
		List<ProductEntity> products = new ArrayList<ProductEntity>();
		for(ProductEntity p1 : service.getAllProduct())
		{
			if(p1.getAvisss().size()>0)
			{
				products.add(p1);
			}
		}
		ProductEntity p = new ProductEntity();
		AvisEntity a = new AvisEntity();
		model.addAttribute("products",products);
		model.addAttribute("product",p);
		model.addAttribute("a",a);
	
	    return "admin/reviewlistadmin";
	}

	@GetMapping("/delreview/{id}")
	public String DelReviews(@PathVariable("id") int id, Model model) {
		service.deleteAvisEntity(id);
		return "redirect:/admin/reviewlist";
	    
	}
	
	
	@GetMapping("/updreview/{id}")
	public String UpdReviews(@PathVariable("id") int id, Model model) {
		AvisEntity avis = service.getAvisById(id);
		model.addAttribute("avis", avis);
	    return "admin/updreviewadmin";
	}
	@PostMapping("/updreviewadmin/{id}")
	public String EditSuucesReview( Model model ,@PathVariable("id") int id ,@RequestParam ("QalityPrice") float QalityPrice , @RequestParam ("cameraquality") float cameraquality , @RequestParam("design") float design, @RequestParam("comment") String comment  ) {
 
		 AvisEntity a =new AvisEntity();
		 a.setQalityPrice(QalityPrice);
		 a.setCameraquality(cameraquality);
		 a.setDesign(design);
		 a.setComment(comment);
		 
		 service.modifyAvis(id, a);
		return "redirect:/admin/reviewlist";
	}
	
	
	@GetMapping("/reportreview")
	public String ReportReviews() {
	    return "admin/reportreviewadmin";
	}
	
}
