package ng.com.justjava.epayment.model;

import javax.persistence.*;

import org.openxava.annotations.*;

@Embeddable
public class PensionSystemSetup {
	
	private double employeeContributionPercent;
	private double employerContributionPercent;	
	
	
	public double getEmployeeContributionPercent() {
		return employeeContributionPercent;
	}
	public void setEmployeeContributionPercent(double employeeContributionPercent) {
		this.employeeContributionPercent = employeeContributionPercent;
	}
	public double getEmployerContributionPercent() {
		return employerContributionPercent;
	}
	public void setEmployerContributionPercent(double employerContributionPercent) {
		this.employerContributionPercent = employerContributionPercent;
	}

}
