package projet.rest.data.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import projet.rest.data.models.AvisEntity;
import projet.rest.data.models.CategoryEntity;
import projet.rest.data.models.DislikeEntity;
import projet.rest.data.models.LikeEntity;
import projet.rest.data.models.ProductEntity;
import projet.rest.data.models.ReportEntity;
import projet.rest.data.models.UserEntity;

public interface UserService {
    List<UserEntity> getAllUserEntity();
    UserEntity getUserEntityById(long id);
    UserEntity createUserEntity(UserEntity entity );
    UserEntity deleteUserEntity(long id);
    //List<AvisEntity>    getAllAvisEntity();
    
    
    /**** category  *****/ 
    List<CategoryEntity> getAllCategories() ; 
    CategoryEntity getCategoryById (int s) ; 
    CategoryEntity createCategory ( CategoryEntity catEntity, String s1,String s2,String s3,String s4,String s5) ; 
    CategoryEntity deleteCategoryEntity( int  id ) ; 
    List<ProductEntity> getProductsByCategory(String Title)  ; 
    
    /**produit**/
    List<ProductEntity> getAllProduct() ; 
    ProductEntity getProductById( int id) ; 
    ProductEntity createProduct(String cat ,String nom, String marque , String description , MultipartFile file, String Username )   ; 
    ProductEntity modifyProduct ( int id , ProductEntity newEntityProd);
    ProductEntity deleteProductEntity( int id ) ; 
    Float rate( int id);
    
    /**avis**/
    public ProductEntity createAvis(int id, AvisEntity a ) ;
    AvisEntity deleteAvisEntity( int id ) ; 
    List<AvisEntity> getAllAvisEntity(int id);
    List<AvisEntity> getAllReviews();
    AvisEntity getAvisById( int id) ; 
    AvisEntity modifyAvis( int id , AvisEntity newEntityAvis);
    public AvisEntity addLike(int id , long userid);
    public void DelTokenByIdUser(long i) ;
    //public AvisEntity addDisLike(int id , int userid);
	void modifyUserEntity(long id , UserEntity newUser);
	public List<ReportEntity> getAllReports();
	public List<LikeEntity> getAllLikes();
	public List<DislikeEntity> getAllDisLikes();
	float setRaaaaaate1(ProductEntity p ,float a);
    float setRaaaaaate2(ProductEntity p ,float a);
    float setRaaaaaate3(ProductEntity p ,float a);
    float setRaaaaaate4(ProductEntity p ,float a);
    float setRaaaaaate5(ProductEntity p ,float a);
}