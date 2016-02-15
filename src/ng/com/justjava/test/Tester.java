package ng.com.justjava.test;

import ng.com.justjava.epayment.utility.*;

import org.apache.commons.lang.math.*;
import org.apache.commons.lang3.*;
import org.apache.cxf.endpoint.*;
import org.apache.cxf.jaxrs.*;
import org.apache.cxf.jaxrs.lifecycle.*;

//import com.nbiss.main.*;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			try {
				
				System.out.println("Key: " + Cryptor.generateKey());
				String pageName = "menu";
				String jsp = pageName + "\".jsp\"";
				System.out.println(" The Long ==" + RandomUtils.nextInt() + " The JSP==" + jsp);
				
				
				
/*				
		            JAXRSServerFactoryBean  sf = new JAXRSServerFactoryBean();
		            sf.setResourceClasses(TestWebService.class);
		            sf.setResourceProvider(TestWebService.class, 
		                new SingletonResourceProvider(new TestWebService()));
		            sf.setAddress("http://localhost:9999/calcrest/");
		            Server server = sf.create();*/
		            
				/*NiBss nibss = NiBss.getInstance("");*/
				//String response= nibss.uploadPaymentSchedule("");
				
				
/*				SmsSender sender = new SmsSender();
				sender.sendSMS("1222222", "2347062023181", "Just Testing");*/
				
				//System.out.println("  The Response Here===" + RandomStringUtils.randomAlphanumeric(15));
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
