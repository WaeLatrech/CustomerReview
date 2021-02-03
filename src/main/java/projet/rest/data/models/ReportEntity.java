package projet.rest.data.models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ReportEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    (name="report_id")
    private long reportid;
	
	private int idavis;
	private int iduser;
	
	public ReportEntity(int iduser,int idavis) {
        this.idavis = idavis;
        this.iduser = iduser;
    }
    public ReportEntity() {
        
    }
}
