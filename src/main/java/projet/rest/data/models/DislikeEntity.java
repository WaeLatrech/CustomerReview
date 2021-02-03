package projet.rest.data.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class DislikeEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    (name="Dislikeid")
    private long Dislikeid;
	
	private int idavis;
	private int iduser;
	
	public DislikeEntity(int iduser,int idavis) {
        this.idavis = idavis;
        this.iduser = iduser;
    }
    public DislikeEntity() {
        
    }
}
