package ng.com.justjava.epayment.model;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity  
public class MailManager {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;

	private boolean sent;
	
	private String toAddress;
	private String subject;
	private String content;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	//String toAddress, String subject,String content){

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
	public void reSend(){
		SystemWideSetup.sendMail(toAddress, subject, content);
	}
}
