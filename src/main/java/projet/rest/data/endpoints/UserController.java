package projet.rest.data.endpoints;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import projet.rest.data.repositories.ConfirmationTokenRepository;
import projet.rest.data.repositories.UserRepository;
import projet.rest.data.services.SendEmailService;
import projet.rest.data.services.UserService;

@Controller
@Data
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService service ;
	
	@Autowired
	SendEmailService SendEmailService;
	
	public String CheckRole () {
		Collection<? extends GrantedAuthority> authorities;
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    authorities = auth.getAuthorities();
	     
	    return authorities.toArray()[0].toString();
	}
	
	public String getUserUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username ;
		if (principal instanceof UserDetails) {
		 username = ((UserDetails)principal).getUsername();
		} else {
		 username = principal.toString();
		}
		return username;
	}
	public void setUserUsername(String username,String password) {
	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
	SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	UserRepository userrepo;
	@GetMapping("/add-categories")
	public String AddCategories(Model model) {
		CategoryEntity c = new CategoryEntity() ;
		model.addAttribute("Category",c);
	    return "user/add-categories";
	}

	@GetMapping("/home")
	public String userindex(Model model,RedirectAttributes redirAttrs) {
				
		if (CheckRole().equals("NOTVERIFIED")) 
			{System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$ aaaaaaaaaaaaaaaaa");
			return "redirect:/logout";
	    	}
		List <ProductEntity> products = service.getAllProduct();
	    ProductEntity prod = new ProductEntity();
	    List <ProductEntity> prods = products.subList(Math.max(products.size() - 3, 0), products.size());
	    model.addAttribute("prods", prods);
	    model.addAttribute("prod", prod);
	    List <AvisEntity> AllReviews = service.getAllReviews();
	    AvisEntity review = new AvisEntity();
	    List <AvisEntity> reviews = AllReviews.subList(Math.max(AllReviews.size() - 9, 0), AllReviews.size());
	    model.addAttribute("reviews", reviews);
	    model.addAttribute("review", review);
		UserEntity user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);

	    	
	    return "user/userindex";
	}

	
	@GetMapping("/Products")
	public String AllProducts(Model model ) { 
		List <ProductEntity> products = service.getAllProduct();
		ProductEntity product = new ProductEntity();
		model.addAttribute("product", product);
		model.addAttribute("products", products);
		UserEntity user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
		List <CategoryEntity> categories = service.getAllCategories() ;
			CategoryEntity category = new CategoryEntity();
			model.addAttribute("category", category);
			if(categories.isEmpty()==false)
			{
			model.addAttribute("categories",categories);
		    return "user/products";
		}
			else {
			
		return "user/productsnotfounds";}
	}
	
	@GetMapping("/add-product")
	public String addProduct(Model model) {
				
		if (CheckRole().equals("NOTVERIFIED")) 
			{
			return "redirect:/logout";
	    	}
		List<CategoryEntity> categories = service.getAllCategories();
		model.addAttribute("categories",categories);
		CategoryEntity cat = new CategoryEntity ();
		model.addAttribute("category",cat);
		UserEntity user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
		return "user/add-product";
	}
	
	@PostMapping("/add-product")
	public String registerSuccess( @RequestParam ("pcat") String catname , @RequestParam ("pname") String nom , @RequestParam("marque") String marque, @RequestParam("desc") String description , @RequestParam ("file") MultipartFile file ) {
		service.createProduct(catname,nom,marque,description,file,getUserUsername());
		return "redirect:/user/Products";
	}
	@PostMapping("/add-categories")
	public String registerSuccess(@ModelAttribute("Category") CategoryEntity Category) {
		service.createCategory(Category);
		return "user/productcreated";
	}
	
	@GetMapping("/add-review/{id}")
	public String addReview(Model model,@PathVariable int id ) {
		AvisEntity a = new AvisEntity() ;
		
		
		ProductEntity p = service.getProductById(id);
		model.addAttribute("product",p);
		UserEntity user = userrepo.findByUsername(getUserUsername());
	a.setUser(user);
		model.addAttribute("user",user);
		model.addAttribute("avis",a);
		return "user/add-review";
		
	}
	@PostMapping("/add-review/{id}")
	public String ReviewSuccess(@ModelAttribute("avis") AvisEntity a , @PathVariable("id") int idp ) {
		/*service.createProduct(a);*/
		a.setUser(userrepo.findByUsername(getUserUsername()));
		
		try {
			//a.setProduct(p);
			service.createAvis(idp, a);
			//a.toString();
		}
		catch(NoSuchElementException e) {
			return "user/ProductNotFound";
		}
		ProductEntity p = service.getProductById(idp);
		p.setRate(service.rate(idp));
		return "redirect:/user/add-review/"+idp;
	}

	
	@GetMapping("/Contact")
	public String Contact(Model model) {
		UserEntity user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
	    return "user/contact";
	}
	
	@PostMapping("/Contact")
	public String ContactMail(@RequestParam("message") String body, @RequestParam("subject") String topic) {
		System.out.println("$$$$$$hello2");
		UserEntity user = userrepo.findByUsername(getUserUsername());
		System.out.println("Sending : "+user.getEmail()+getUserUsername()+body+" "+topic);
		SendEmailService.sendEmail(user.getEmail(),body,"By "+getUserUsername()+": "+topic);
		//System.out.println("Success : "+to+" "+body+" "+topic);
	    return "redirect:/Contact";
	}
	@GetMapping("/Account")
	public String Account(Model model) {
		UserEntity user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
	    return "user/Account";
	}
 	ConfirmationTokenRepository conftrepo ;

	@PostMapping("/upd_account")
	public String EditAccount( Model model ,@RequestParam ("username") String username ,
			@RequestParam ("email") String email , @RequestParam("password") String password,
			@RequestParam("phone") String phone,@RequestParam ("birthDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date birthDate  ,
			RedirectAttributes redirAttrs) {
		UserEntity olduser = userrepo.findByUsername(getUserUsername());
		UserEntity newuser =new UserEntity();
		 newuser.setUsername(username);
		 newuser.setEmail(email);
			UserEntity existingMail = userrepo.findByEmail(newuser.getEmail());
			UserEntity existingUsername = userrepo.findByUsername(newuser.getUsername());
	        if((existingMail != null)&&(existingMail != olduser))
	        {	
	        	redirAttrs.addFlashAttribute("error", "Mail already exists");
	        	return "redirect:/user/Account";
	        }
	        else if((existingUsername != null)&&(existingUsername != olduser))
	        {	
	        	redirAttrs.addFlashAttribute("error", "Username already exists");
	        	return "redirect:/user/Account";
	        }
	        
	        else
	        {
	       System.out.println("password = '"+password+"'");
		 newuser.setPassword(password);
		 newuser.setPhone(phone);
		 newuser.setBirthDate(birthDate);
		 service.modifyUserEntity(olduser.getId(), newuser);
		 
		 setUserUsername(newuser.getUsername(), newuser.getPassword());
		 
		 if (!newuser.getEmail().equals(olduser.getEmail())) {
			 ConfirmationToken confirmationToken = new ConfirmationToken(olduser);
	            conftrepo.save(confirmationToken);
	            String text="To confirm your email, please click here : "
	                    +"http://localhost:9090/confirm-Email/"+confirmationToken.getConfirmationToken()+"/"
	                    +newuser.getEmail();
	            SendEmailService.verifyEmail(newuser.getEmail(),"Mail Verified!",text);
	           redirAttrs.addFlashAttribute("success", "Email Changed! Check your mail to Verifie it");
	            return "redirect:/Login";
		 }
		return "redirect:/user/Account";
		}
	}
}
