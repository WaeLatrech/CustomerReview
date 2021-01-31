package projet.rest.data.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table ( name = "Product" )
public class ProductEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
	private int idp;
	@Column(nullable=false)
	private String nom;
	private String marque;
	@CreationTimestamp
	private Date dateofcreation ;
	@OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
	private List<AvisEntity> avisss;
	@JsonIgnore
	@ManyToOne
	private CategoryEntity category;
	private String catname;
	private float rate;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String img;
	@JsonIgnore
@ManyToOne(cascade = CascadeType.REMOVE)
	UserEntity userp;
private String description ;

}
