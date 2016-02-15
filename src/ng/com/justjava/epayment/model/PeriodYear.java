package ng.com.justjava.epayment.model;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
public class PeriodYear {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	private int year;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
