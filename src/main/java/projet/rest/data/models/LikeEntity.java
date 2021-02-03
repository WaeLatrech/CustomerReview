package projet.rest.data.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class LikeEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    (name="like_id")
    private long Likeid;
	
	private int idavis;
	private int iduser;
	
	public LikeEntity(int iduser,int idavis) {
        this.idavis = idavis;
        this.iduser = iduser;
    }
    public LikeEntity() {
        
    }
}
