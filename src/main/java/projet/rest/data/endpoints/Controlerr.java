package projet.rest.data.endpoints;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
public class Controlerr {
	@Autowired
    UserService service ;
	
	@Autowired
    SendEmailService SendEmailService;
	
	@GetMapping("/")
	public String returnindex() {
		Collection<? extends GrantedAuthority> authorities;
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    authorities = auth.getAuthorities();
	    String myRole = authorities.toArray()[0].toString();
	    String user = "USER";
	    
	    if (myRole.equals(user)) {
	        return "redirect:/user/home";
	    }
	    return "/index";
	}

    @GetMapping("/Products")
	public String AllProducts(Model model ) { 
    	Collection<? extends GrantedAuthority> authorities;
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    authorities = auth.getAuthorities();
	    String myRole = authorities.toArray()[0].toString();
	    String user = "USER";
	    
	    if (myRole.equals(user)) {
	        return "redirect:/user/Products";
	    }
		List <ProductEntity> products = service.getAllProduct();
		ProductEntity product = new ProductEntity();
		model.addAttribute("product", product);
		model.addAttribute("products", products);
		List <CategoryEntity> categories = service.getAllCategories() ;
			CategoryEntity category = new CategoryEntity();
			model.addAttribute("category", category);
			if(categories.isEmpty()==false)
			{
			model.addAttribute("categories",categories);
		    return "Other/products";
		}
			else {
				
		return "Other/productsnotfounds";}
	}


	@GetMapping("/Contact")
	public String Contact(Model model) {
		Collection<? extends GrantedAuthority> authorities;
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    authorities = auth.getAuthorities();
	    String myRole = authorities.toArray()[0].toString();
	    String user = "USER";
	    
	    if (myRole.equals(user)) {
	        return "redirect:/user/Contact";
	    }
	    return "Other/contact";
	}
	
	@PostMapping("/Contact")
	public String ContactMail(@RequestParam("email") String to,@RequestParam("message") String body, @RequestParam("subject") String topic,@RequestParam("name") String name) {
		System.out.println("Sending : "+to+" "+body+" "+topic);
		SendEmailService.sendEmail(to,body,"By "+name+": "+topic);
		System.out.println("Success : "+to+" "+body+" "+topic);
	    return "Other/contact";
	}
	
	@GetMapping("/Login")
	public String login() {
		Collection<? extends GrantedAuthority> authorities;
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    authorities = auth.getAuthorities();
	    String myRole = authorities.toArray()[0].toString();
	    String user = "USER";
	    
	    if (myRole.equals(user)) {
	        return "redirect:/user/home";
	    }
	    
	    return "Other/login";
	}
	
	@GetMapping("/logout-Success")
	public String logout() {
	    return "redirect:/Login";
	}
	
	
	@GetMapping("/forgotpass")
	public String forgotpass(Model model) {
		
	    return "Other/forgot-password";
	}
	@PostMapping("/forgotpass")
	public String forgotpass1(@RequestParam("email") String email, RedirectAttributes redirAttrs) {
		UserEntity user = userrepo.findByEmail(email);
        if(user == null)
        {	
        redirAttrs.addFlashAttribute("error", "email doesn't exist");
        return "redirect:/forgotpass";
        }
        else
        {	//lazem na3mlou table o5ra mta3 tokens teb3a el password
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            conftrepo.save(confirmationToken);
            String text="To Change your password, please click here : "
                    +"http://localhost:9090/change-password/"+confirmationToken.getConfirmationToken();
            SendEmailService.changePassword(email,"Change Password !",text);
    		redirAttrs.addFlashAttribute("success", "Check Your Mail to Confirm new Password");

            return "redirect:/Login";
        }
	}
	
	@GetMapping("/change-password/{confirmationToken}")
	public String updPassword(Model model,@PathVariable String confirmationToken) {
		 ConfirmationToken token = conftrepo.findByConfirmationToken(confirmationToken);
		 if (token.getExpired()==1)
	        	return "redirect:/Login";
		 UserEntity user = userrepo.findByEmail(token.getUser().getEmail());
			model.addAttribute("user",user);
			model.addAttribute("token",token);
			return "Other/forgotpass_part2";
		
	}
	
	@PostMapping("/change-password/{confirmationToken}")
	public String updPassword1(@ModelAttribute("user") UserEntity user ,@PathVariable String confirmationToken, RedirectAttributes redirAttrs) {	
        ConfirmationToken token = conftrepo.findByConfirmationToken(confirmationToken);

        UserEntity olduser = userrepo.findByEmail(token.getUser().getEmail());
        olduser.setPassword(user.getPassword());
        System.out.println("Passworrrrd = "+olduser.getPassword());
        service.modifyUserEntity(olduser.getId(), olduser);
        token.setExpired(1);
		conftrepo.save(token);
		redirAttrs.addFlashAttribute("success", "Password Modified Successfully");

		return "redirect:/Login";
	}
	
	
	
	@RequestMapping("/default")
	public String defaultAfterLogin() {
	    Collection<? extends GrantedAuthority> authorities;
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    authorities = auth.getAuthorities();
	    String myRole = authorities.toArray()[0].toString();
	    String user = "USER";
	    
	    if (myRole.equals(user)) {
	        return "redirect:/user/home";
	    }
	    return "redirect:/admin/home";
	}
	
	@GetMapping("/Sign-up")
	public String SignUp(Model model) {
		Collection<? extends GrantedAuthority> authorities;
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    authorities = auth.getAuthorities();
	    String myRole = authorities.toArray()[0].toString();
	    String user = "USER";
	    
	    if (myRole.equals(user)) {
	        return "redirect:/user/home";
	    }

		UserEntity userentity = new UserEntity();
		model.addAttribute("user",userentity);
		return "Other/Sign-up";
	}
	UserRepository userrepo;
	ConfirmationTokenRepository conftrepo ;
	
	@PostMapping("/Sign-up")
	public String UserregisterSuccess(@ModelAttribute("user") UserEntity user, RedirectAttributes redirAttrs) {
		UserEntity existingMail = userrepo.findByEmail(user.getEmail());
		UserEntity existingUsername = userrepo.findByUsername(user.getUsername());
        if(existingMail != null)
        {	redirAttrs.addFlashAttribute("error", "mail already exists");
        	return "redirect:/Sign-up";
        }
        else if(existingUsername != null)
        {	redirAttrs.addFlashAttribute("error", "Username already exists");
        	return "redirect:/Sign-up";
        }
        else
        {
        	service.createUserEntity(user);
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            conftrepo.save(confirmationToken);
            String text="To confirm your account, please click here : "
                    +"http://localhost:9090/confirm-account/"+confirmationToken.getConfirmationToken();
            SendEmailService.verifyEmail(user.getEmail(),"Complete Registration!",text);
           redirAttrs.addFlashAttribute("success", "Account created! Check your mail to activate Your Account");
            return "redirect:/Login";
  }

	}

	@GetMapping("/confirm-account/{confirmationToken}")
    public String confirmUserAccount(@PathVariable String confirmationToken, RedirectAttributes redirAttrs)
    {	
        ConfirmationToken token = conftrepo.findByConfirmationToken(confirmationToken);
        if (token.getExpired()==1)
        	return "redirect:/Login";
        
        UserEntity user = userrepo.findByEmail(token.getUser().getEmail());
		user.setIsEnabled(1);
		userrepo.save(user);
		SendEmailService.welcomeMail(user.getEmail(),user.getUsername());
		token.setExpired(1);
		conftrepo.save(token);
		redirAttrs.addFlashAttribute("success", "Account Activated! Try to Login");
		return "redirect:/Login";

    }
}
