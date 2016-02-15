package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.annotations.*;
import org.openxava.util.*;

@Entity
@View(members="biller")
@Tab(properties="biller.name,registeredDate,registerBy")
public class RegisterBiller {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;

	@OneToOne
	@AsEmbedded
	private Biller biller;
	
	private Date registeredDate;
	private String registerBy;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public String getRegisterBy() {
		return registerBy;
	}

	public void setRegisterBy(String registerBy) {
		this.registerBy = registerBy;
	}
	
	@PreCreate
	public void preCreate(){
		setRegisterBy(Users.getCurrent());
		setRegisteredDate(Dates.createCurrent());
	}

	public Biller getBiller() {
		return biller;
	}

	public void setBiller(Biller biller) {
		this.biller = biller;
	}
}
