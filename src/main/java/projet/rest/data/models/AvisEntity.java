package projet.rest.data.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor

public  class AvisEntity {


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private int id;
    private int nblike;
    private int nbdislike;
    private float note;
    private String comment;
   @JsonIgnore
 @ManyToOne( cascade = CascadeType.DETACH )
 UserEntity user;
    @ManyToMany
    @JoinTable(name = "LikedBy")
    @JsonIgnore
    private List<UserEntity> LikedBy;
    @ManyToMany
    @JoinTable(name = "DislikedBy")
    @JsonIgnore
    private List<UserEntity> dislikedBy;

    @JsonIgnore
    @ManyToOne
    private ProductEntity product;
    


    private float c1;
    private float c2 ;
    private float c3;
    private float c4;
    private float c5 ;
 
    @CreationTimestamp
    private Date dateofcreation ;
	
	
}
