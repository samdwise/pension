package ng.com.justjava.epayment.model;

import java.io.*;

import javax.persistence.*;


@Entity
public class PayItem implements Serializable{
	
	@Id @Column(length=15)  
	private String code;
	
	private String name;
	
	private boolean compulsory;
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCompulsory() {
		return compulsory;
	}

	public void setCompulsory(boolean compulsory) {
		this.compulsory = compulsory;
	}   

}
