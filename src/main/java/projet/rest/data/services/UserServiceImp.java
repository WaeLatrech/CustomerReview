package projet.rest.data.services;

 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import projet.rest.data.models.AvisEntity;
import projet.rest.data.models.CategoryEntity;
import projet.rest.data.models.ConfirmationToken;
import projet.rest.data.models.ProductEntity;
import projet.rest.data.models.ReportEntity;
import projet.rest.data.models.UserEntity;
import projet.rest.data.repositories.AvisRepository;
import projet.rest.data.repositories.CategoryRepository;
import projet.rest.data.repositories.ConfirmationTokenRepository;
import projet.rest.data.repositories.ProductRepository;
import projet.rest.data.repositories.ReportRepository;
import projet.rest.data.repositories.UserRepository;

 

 


@Service
public class UserServiceImp implements UserService {

 
	 @Autowired
	   	private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository reposUser;
    private CategoryRepository reposCat ; 
    private ProductRepository reposProduct ; 
    private AvisRepository reposAvis ; 
    private ConfirmationTokenRepository repostoken;
    private ReportRepository reporepo;
    
    @Autowired
    public UserServiceImp(ReportRepository reporepo,ConfirmationTokenRepository repostoken,UserRepository reposUser, CategoryRepository reposCat, ProductRepository reposProduit,
            AvisRepository reposAvis) {
        super();
        this.repostoken=repostoken;
        this.reposUser = reposUser;
        this.reposCat = reposCat;
        this.reposProduct = reposProduit;
        this.reposAvis = reposAvis;
        this.reporepo = reporepo;
    }
    
    
    @Override
    public List<UserEntity> getAllUserEntity() {
        return reposUser.findAll();
    }
    
    @Override
    public List<ReportEntity> getAllReports() {
        return reporepo.findAll();
    }



    @Override
    public UserEntity getUserEntityById(long id) {
        Optional<UserEntity> opt = reposUser.findById(id);
        
        UserEntity entity;
        if (opt.isPresent())
            entity = opt.get();
        else
            throw new NoSuchElementException("User with id : "+id+" is not found");
        return entity; 
    }
   
       @Override
       public UserEntity createUserEntity(UserEntity entity) {
       	String password = bCryptPasswordEncoder.encode(entity.getPassword());
       	entity.setPassword(password);
       	entity.setRole("NOTVERIFIED");
                   UserEntity user = reposUser.save(entity);
                                   
                   return user;
       }

      

    @Override
    public void modifyUserEntity(long id, UserEntity newUser) {
        UserEntity oldUser = this.getUserEntityById(id);
        System.out.println("password 2 = '"+newUser.getPassword()+"'");	
        if (!newUser.getUsername().equals(""))    
            oldUser.setUsername(newUser.getUsername());

        if (!newUser.getPassword().equals(""))
        {   System.out.println("password 3 ="+newUser.getPassword());
        	oldUser.setPassword(newUser.getPassword());
        	String password = bCryptPasswordEncoder.encode(newUser.getPassword());
        	oldUser.setPassword(password);
        }
        if (!newUser.getPhone().equals(""))    
            oldUser.setPhone(newUser.getPhone());
        if (newUser.getBirthDate() != null)
            oldUser.setBirthDate(newUser.getBirthDate());
        if (newUser.getRole() != null)
            oldUser.setRole(newUser.getRole());
        if (newUser.getImageU() != null)
            oldUser.setImageU(newUser.getImageU());
        reposUser.save(oldUser);
        //return reposUser.save(oldUser); // 
    }
    @Override
    public UserEntity deleteUserEntity(long id) {
        UserEntity entity = this.getUserEntityById(id);
        reposUser.deleteById(id);
        return entity;
    }
    /************  Category *****************/
    @Override
    public List<CategoryEntity> getAllCategories() {
        
        return reposCat.findAll() ;
    }

	@Override
	public List<ProductEntity> getProductsByCategory(String Title) {
		List<ProductEntity>	 products = reposProduct.findAll();
		List<ProductEntity>  productbycat = new ArrayList<ProductEntity>();
		for(ProductEntity p : products )
		{
			if(p.getCatname().equalsIgnoreCase(Title))
				productbycat.add(p);
			
		}
	return productbycat;
	}



    @Override
    public CategoryEntity getCategoryById(int s) {
    Optional<CategoryEntity> opt = reposCat.findById(s);
        
        CategoryEntity catEntity;
        if (opt.isPresent())
            catEntity = opt.get();
        else
            throw new NoSuchElementException("Categorie with this id is not found");
        return catEntity; 
    }
    

 

    @Override
    public CategoryEntity createCategory(CategoryEntity catEntity,String c1,String c2,String c3,String c4,String c5) {
 
    	catEntity.setCarac1(c1);
    	catEntity.setCarac2(c2);
    	catEntity.setCarac3(c3);
    	catEntity.setCarac4(c4);
    	catEntity.setCarac5(c5);

    	
    	
    
        CategoryEntity category = reposCat.save(catEntity) ; 
        
        return category;
    }

 

    

 

    @Override
    public CategoryEntity deleteCategoryEntity(int id) {
    CategoryEntity catEntity = this.getCategoryById(id);
        reposCat.deleteById(id);
        return catEntity;

 

         
    }

 

    @Override
    public List<ProductEntity> getAllProduct() {
        // TODO Auto-generated method stub
        return reposProduct.findAll(); 
        }

 

    @Override
    public ProductEntity getProductById(int id) {
Optional<ProductEntity> opt = reposProduct.findById(id);
        
        ProductEntity productentity;
        if (opt.isPresent())
            productentity = opt.get();
        else
            throw new NoSuchElementException("Product with this id is not found with this id is not found");
        return productentity; 
    } 
    @Override
    public ProductEntity modifyProduct(int id, ProductEntity newEntityProd) {
          ProductEntity oldproduct = this.getProductById(id);
           if (newEntityProd.getMarque()!= null )
               oldproduct.setMarque(newEntityProd.getMarque());
            
        
          if (newEntityProd.getNom()!= null )
            oldproduct.setNom(newEntityProd.getNom());
          if (newEntityProd.getDescription()!= null )
              oldproduct.setDescription(newEntityProd.getDescription());
          if (newEntityProd.getImg()!= null )
              oldproduct.setImg(newEntityProd.getImg());
          
             
          
          
          
           return reposProduct.save(oldproduct);
        
    }

 

    @Override
    public ProductEntity deleteProductEntity(int id) {
        ProductEntity p = this.getProductById(id);
        reposProduct.deleteById(id);
    	return p;
    }
    
    
    
    
    @Override
    public List<AvisEntity> getAllAvisEntity(int id) {
        Optional<ProductEntity> opt = reposProduct.findById(id);
        
        ProductEntity product;
        if (opt.isPresent()) {

 

             product = opt.get();
             return product.getAvisss();
        }
        else
            throw new NoSuchElementException("User with id : "+id+" is not found"); 
    }
    @Override
    public List<AvisEntity> getAllReviews() {
    	return reposAvis.findAll() ;
    }
    @Override
public ProductEntity createProduct(String cat ,String nom, String marque , String description , MultipartFile file ,String username) {
        
    	ProductEntity productEntity = new ProductEntity ();
    	productEntity.setCatname(cat);
    	productEntity.setNom(nom);
    	productEntity.setMarque(marque);
    	productEntity.setDescription(description);
    	productEntity.setUserp(reposUser.findByUsername(username));
    	String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
    	if(FileName.contains("..")) {
    		System.out.println("not a proper file ");
    	}
    	try {
			productEntity.setImg(Base64.getEncoder().encodeToString(file.getBytes()));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	List<CategoryEntity> l = this.getAllCategories();
        for(CategoryEntity c : l)
        {
            if (productEntity.getCatname().equalsIgnoreCase(c.getTitle())) {
                productEntity.setCategory(c);
                break;
            }
            
        }
        ProductEntity product=reposProduct.save(productEntity);    
                return product;
    }

 

    @Override
    public ProductEntity createAvis(int id, AvisEntity a ) {
        ProductEntity productEntity = getProductById(id);
        AvisEntity a1= new AvisEntity();
        float note;
     
        note=(a.getC1()+a.getC2()+a.getC3()+a.getC4()+a.getC5())/5;
        a1.setNote(note);
        a1.setProduct(productEntity);
        a1.setLikedBy(new ArrayList<>());
        //a.getLikedBy().add(0);
        a1.setDislikedBy(new ArrayList<>());
        //a.getDislikedBy().add(0);
      a1.setUser(a.getUser());
     a1.setComment(a.getComment());
     a1.setDateofcreation(a.getDateofcreation());
     a1.setC1(a.getC1());
     a1.setC2(a.getC2());
     a1.setC3(a.getC3());
     a1.setC4(a.getC4());
     a1.setC5(a.getC5());
     a1.getProduct().getAvisss().add(a1);
     a1.getProduct().setRatee();
        reposAvis.save(a1);
        
        return productEntity;
    
    }
    @Override
    public AvisEntity deleteAvisEntity(int id) {
         
    	AvisEntity a = this.getAvisById(id);
    	a.getProduct().setRateAfterDelete(a.getNote());
    a.getProduct().getAvisss().remove(a);
        reposAvis.deleteById(id);
    	return a;
    }
    
    public Float rate(int id) {
        ProductEntity p = this.getProductById(id);
        float rate=0;
        int i=0;
        //if (p.getCategory().equalsIgnoreCase("smartphone"))
            for(AvisEntity a : this.getAllAvisEntity(id)) {
                rate=rate+a.getNote();
                i++;
                
            }
            p.setRate(rate/i);
            this.modifyProduct(id, p)    ;
            return rate/i;
        
    }
    
    @Override
    public AvisEntity getAvisById(int id) {
Optional<AvisEntity> opt = reposAvis.findById(id);
        
    AvisEntity avisentity;
        if (opt.isPresent())
            avisentity = opt.get();
        else
            throw new NoSuchElementException("Avis with this id is not found");
        return avisentity; 
    }
    
    @Override
    public AvisEntity modifyAvis(int id, AvisEntity newEntityAvis) {
        AvisEntity oldavis = this.getAvisById(id);
           if (newEntityAvis.getNblike()!= 0 )
               oldavis.setNblike(newEntityAvis.getNblike());
           if (newEntityAvis.getNbdislike()!= 0 )
               oldavis.setNbdislike(newEntityAvis.getNbdislike());
           if (newEntityAvis.getComment()!= null )
               oldavis.setComment(newEntityAvis.getComment());
           if (newEntityAvis.getC1()!= -1 )
               oldavis.setC1(newEntityAvis.getC1());
           if (newEntityAvis.getC2()!= -1 )
               oldavis.setC2(newEntityAvis.getC2());
           if (newEntityAvis.getC3()!= -1 )
               oldavis.setC3(newEntityAvis.getC3());
           if (newEntityAvis.getRepport()!= 0 )
               oldavis.setRepport(newEntityAvis.getRepport());
           
           
         return reposAvis.save(oldavis);
        
    }

 


    
    public AvisEntity addLike(int id , long userid) {
        AvisEntity a = this.getAvisById(id);
        boolean testDislike = false,test = false;
        
        if (a.getDislikedBy()!=null) {
            
        for (UserEntity i : a.getDislikedBy()) {
            if (i.getId()==userid) {
                a.getDislikedBy().remove(i);
                a.getLikedBy().add(i);
                a.setNblike(a.getNblike()+1);
                a.setNbdislike(a.getNbdislike()-1);
                testDislike = true;
                break;
            }
        }
        }
        
        if (testDislike == false && a.getLikedBy()!=null) {
            
        for (UserEntity i : a.getLikedBy()) {
            if (i.getId()==userid) {
                a.getLikedBy().remove(i);
                a.setNblike(a.getNblike()-1);
                test = true;
                break;
            }
        }
        }
        if (test==false && testDislike == false) {
            a.getLikedBy().add(this.getUserEntityById(userid));
            a.setNblike(a.getNblike()+1);
        }
            
        return this.modifyAvis(id, a);
    }


	@Override
	public void DelTokenByIdUser(long i ) {
		// TODO Auto-generated method stub
		
		List<ConfirmationToken> tokens = repostoken.findAll();
		for(ConfirmationToken t : tokens )
		{
			if(t.getUser().getId()==i)
				repostoken.deleteById(t.getTokenid());
		}}


	
		
	
    
    /*public AvisEntity addDisLike(int id , int userid) {
        AvisEntity a = this.getAvisById(id);
        boolean testLike = false,test = false;
        if (a.getLikedBy()!=null) {

 

        for (int i : a.getLikedBy()) {
            if (userid==i) {
                a.getDislikedBy().add(i);
                a.getLikedBy().remove(i);
                a.setNblike(a.getNblike()-1);
                a.setNbdislike(a.getNbdislike()+1);
                testLike = true;
                break;
            }
        }
        }
        if (testLike == false && a.getDislikedBy()!=null) {
            
        for (int i : a.getDislikedBy()) {
            if (userid==i) {
                a.getDislikedBy().remove(i);
                a.setNbdislike(a.getNbdislike()-1);
                test = true;
                break;
            }
        }
        }
        if (test==false && testLike == false) {
            a.getDislikedBy().add(userid);
            a.setNbdislike(a.getNbdislike()+1);
        }
            
        return this.modifyAvis(id, a);
    }
    */
}