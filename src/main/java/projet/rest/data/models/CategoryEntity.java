package projet.rest.data.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table( name = "Category")
public class CategoryEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private int id ;
    
    @Column(unique = true )
    private String Title ; 
    
    @OneToMany( mappedBy = "category")
    private  List<ProductEntity> product;
    private String carac1;
    private String carac2;
    private String carac3;
    private String carac4;
    private String carac5;
  
}
