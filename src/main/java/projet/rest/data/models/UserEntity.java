package projet.rest.data.models;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.Data;
import lombok.ToString;


@Data
@Entity
@Table (name="UserEntity")
public class UserEntity {
@Id //primary key
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id; //auto generé
//@Column(name = "First Name",length = 50,nullable = false)
private String username;
private String password; //prof qal mayet7atech el password fel table houni
@DateTimeFormat(pattern="yyyy-MM-dd")
private Date birthDate;
private String email;
private String phone;
private String Role;
@ManyToMany(mappedBy = "LikedBy", cascade = CascadeType.REMOVE)
private List<AvisEntity> Likedavis;
@ManyToMany(mappedBy = "dislikedBy", cascade = CascadeType.REMOVE)
private List<AvisEntity> DisLikedavis;
}