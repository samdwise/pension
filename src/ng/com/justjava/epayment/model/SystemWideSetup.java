package ng.com.justjava.epayment.model;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import ng.com.justjava.test.*;
import oneapi.client.impl.*;
import oneapi.config.Configuration;
import oneapi.model.*;

import org.kamranzafar.otp.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

@Entity
@View(members="email; Pension Setup{pensionSystemSetup} PasswordRule{passwordRule}")
@Tab(properties="name")
public class SystemWideSetup {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	@Embedded
	private PasswordRule passwordRule;
	
	@Embedded
	private PensionSystemSetup pensionSystemSetup;
	
	private String url;
	
	private String name;

	private String email;
	
	@Embedded
	//@Required
	private ProcessingFeeParameters processingFee;
	
/*	@Embedded
	@Required
	private EmailParameter emailParameter;
	*/
	private String nibssURL;
	
   
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNibssURL() {
		return nibssURL;
	}

	public void setNibssURL(String nibssURL) {
		this.nibssURL = nibssURL;
	}
	
	public static SystemWideSetup getSystemWideSetup(){
		String ejbQL = " FROM SystemWideSetup s ";
		SystemWideSetup systemWideSetup = null;
		try {
			systemWideSetup = (SystemWideSetup) XPersistence.getManager().createQuery(ejbQL).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return systemWideSetup;
	}

	public ProcessingFeeParameters getProcessingFee() {
		return processingFee;
	}

	public void setProcessingFee(ProcessingFeeParameters processingFee) {
		this.processingFee = processingFee;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

/*	public EmailParameter getEmailParameter() {
		return emailParameter;
	}

	public void setEmailParameter(EmailParameter emailParameter) {
		this.emailParameter = emailParameter;
	}*/
	
	public static void sendMail(String toAddress, String subject,String content){
		try {
			CrunchifyJavaMailExample.generateAndSendEmail(toAddress, "info@justjava.com.ng", content, subject);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}		
		
	}

	public static void sendNotification(String toAddress, String subject,String content,String smsBody,String mobileNo){
		
		try {
			sendMail(toAddress, subject, content);
			String smsUserName = XavaPreferences.getInstance().getXavaProperty("smsUserName", "justjava1");
			String smsPassword = XavaPreferences.getInstance().getXavaProperty("smsPassword", "changeme1A");
			Configuration configuration = new Configuration (smsUserName, smsPassword);
			configuration.setApiUrl("http://oneapi.infobip.com");
			SMSClient smsClient = new SMSClient(configuration);
			
			
			if(mobileNo.startsWith("0")){
				mobileNo = "+234"+mobileNo.substring(1);
			}else
				mobileNo = mobileNo.startsWith("+")?mobileNo:"+"+mobileNo;
			
			
			SMSRequest smsRequest = new SMSRequest("+2347062023181",
					smsBody,mobileNo);
			
			// Store request id because we can later query for the delivery status with it:
			SendMessageResult sendMessageResult = smsClient.getSMSMessagingClient().sendSMS(smsRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public PensionSystemSetup getPensionSystemSetup() {
		return pensionSystemSetup;
	}

	public void setPensionSystemSetup(PensionSystemSetup pensionSystemSetup) {
		this.pensionSystemSetup = pensionSystemSetup;
	}

	public PasswordRule getPasswordRule() {
		return passwordRule;
	}

	public void setPasswordRule(PasswordRule passwordRule) {
		this.passwordRule = passwordRule;
	}

}
