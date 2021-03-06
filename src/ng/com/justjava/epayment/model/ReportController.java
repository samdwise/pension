package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.model.Payment.Status;

import org.openxava.annotations.*;

@View(members ="report;format;custodian;fundAdministrator;corporate;from;to")
public class ReportController {

	public enum Format {
		PDF, Excel, RTF
	};
    
	public enum Report {
		PFACompany,RSAHolders, PFAInstitutions
	};
	
	@Required
	private Format format;

	@Required
	private Report report;
	
	@Required
	@DescriptionsList(descriptionProperties="name")
	@NoCreate
	@NoModify
	@ManyToOne
	private PensionFundCustodian custodian;
	
	@Required
	@DescriptionsList(depends="custodian",condition="${custodian.id} = ?",descriptionProperties="name")
	@NoCreate
	@NoModify
	@ManyToOne
	private PensionFundAdministrator fundAdministrator;
	
	//depends="fundAdministrator",condition="${fundAdministrator.} = ?",
	@Required
	@DescriptionsList(descriptionProperties="name")
	@NoCreate
	@NoModify
	@ManyToOne
	private Corporate corporate;
	
	private Date from;
	
	private Date to;
	
	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}


	public PensionFundAdministrator getFundAdministrator() {
		return fundAdministrator;
	}

	public void setFundAdministrator(PensionFundAdministrator fundAdministrator) {
		this.fundAdministrator = fundAdministrator;
	}

	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}

	public PensionFundCustodian getCustodian() {
		return custodian;
	}

	public void setCustodian(PensionFundCustodian custodian) {
		this.custodian = custodian;
	}
}
