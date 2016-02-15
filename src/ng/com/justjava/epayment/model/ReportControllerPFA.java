package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.model.ReportController.Format;
import ng.com.justjava.epayment.model.ReportController.Report;

import org.openxava.annotations.*;

@View(members ="report;format;corporate;from;to")

public class ReportControllerPFA {

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

	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}

	
}