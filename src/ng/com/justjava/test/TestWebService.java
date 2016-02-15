package ng.com.justjava.test;

import java.io.*;
import java.lang.reflect.*;
import java.math.*;
import java.security.*;

import javax.crypto.*;
import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.apache.cxf.helpers.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import com.googlecode.jcsv.*;
import com.googlecode.jcsv.annotations.internal.*;
import com.openxava.naviox.model.*;

@Path("/chamsPay")
public class TestWebService {
 
	@Context
	private HttpServletRequest httpRequest;
	
	
    @GET
    @Path("/add/{a}/{b}")
    @Produces(MediaType.TEXT_PLAIN)
    public String addPlainText(@PathParam("a") double a, @PathParam("b") double b) {
        return (a + b) + "";
    }
     
    @GET
    @Path("/add/{a}/{b}")
    @Produces(MediaType.TEXT_XML)
    public String add(@PathParam("a") double a, @PathParam("b") double b) {
        return "<?xml version=\"1.0\"?>" + "<result>" +  (a + b) + "</result>";
    }
     
    @GET
    @Path("/sub/{a}/{b}")
    @Produces(MediaType.TEXT_PLAIN)
    public String subPlainText(@PathParam("a") double a, @PathParam("b") double b) {
        return (a - b) + "";
    }
     
    @GET
    @Path("/sub/{a}/{b}")
    @Produces(MediaType.TEXT_XML)
    public String sub(@PathParam("a") double a, @PathParam("b") double b) {
        return "<?xml version=\"1.0\"?>" + "<result>" +  (a - b) + "</result>";
    }
    
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/biller/{uniqueId}")
    public Response getBiller(@PathParam("uniqueId") String uniqueId){
    	
    	
    	
    	
    	System.out.println("  The sent biller to search is===" + uniqueId);
    	
    	String ejbQL = "FROM Biller b WHERE b.uniqueIdentifier='"+uniqueId+"'";
    	Biller biller = null;
		try {
			biller = (Biller) XPersistence.getManager().createQuery(ejbQL).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return Response.status(200).entity(biller).build();
    }
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/billers/")
    public Response getBillers(){ 
    	
    	AllBillers billers = new AllBillers();
    	System.out.println(" REQUESTING FOR THE WHOLE BILLERS ....................................");
    	return Response.status(200).entity(billers).build();
    } 
      
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/makePayment")
    public String paymentFile(MakePayment payment){

    	System.out.println(" payment Sent===" + payment.getCollectionItem());
    	return "OK";
    }
    
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/payment")
    public String paymentFile(byte[] content){
    	
    	String response = "OK";
    	
    	
    	System.out.println(" The Token exchanged Here==== " + httpRequest.getHeader("token"));
    	
    	String encryptCorporate =  httpRequest.getHeader("corporate");
    	String key = XavaPreferences.getInstance().getXavaProperty("symmetryKey", "Pct9GF1sbINbHxE7CKslwQ==");
    	String decryptCorporate =  null;
    	
    	try {
			decryptCorporate = Cryptor.decrypt(encryptCorporate, key);
			
			if(!Is.equalAsStringIgnoreCase("ChamsPayroll", decryptCorporate))
				return "UNKNOWN SITE";
			else
				return "OK";
		} catch (KeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidAlgorithmParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalBlockSizeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BadPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	try {
    		File file = new File("paymentupload"+System.currentTimeMillis()+".csv");

    		java.nio.file.Files.write(file.toPath(), content, java.nio.file.StandardOpenOption.CREATE);
    		
    		CSVStrategy strategy = new CSVStrategy(',', '"', '#', true, true);
    		
    		System.out.println("000000 It is finally fired..................................");
			if(file.getName().toLowerCase().contains("csv")){
				//file = new File("data/paymentupload.csv");
				
				FileInputStream fis = new FileInputStream(file);
				Reader csvFile = new InputStreamReader(fis);

				System.out.println("1111111 It is finally fired..................................");
				
				
				ValueProcessorProvider vpp = new ValueProcessorProvider();
				vpp.registerValueProcessor(Account.class, new AccountValueProcessor());
				vpp.registerValueProcessor(PaymentInstruction.class,
						new PaymentInstructionValueProcessor());
				vpp.registerValueProcessor(Bank.class,	new BankValueProcessor());
				vpp.registerValueProcessor(BigDecimal.class,new BigDecimalValueProcessor());
				
				String className="ng.com.justjava.epayment.model.PaymentUpload";
				System.out.println(" The Model name aroun here===" + className);
				
				System.out.println("22222222 It is finally fired..................................");
				Class cls = Class.forName(className);
				
				Method method = cls.getDeclaredMethod("saveUpload", Reader.class,CSVStrategy.class,
						ValueProcessorProvider.class);
				System.out.println("3333333 It is finally fired..................................");
				//method.invoke(null, csvFile,strategy,vpp);
				Object result = method.invoke(null, csvFile,strategy,vpp);
				
				response = String.valueOf(result);

				
				//vpp.registerValueProcessor(BigDecimal.class,new BigDecimalValueProcessor());


			}    		
			//FileOutputStream out = new FileOutputStream(file);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return response;
    }
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/customer/{customerId}")
    public Response getCustomer(@PathParam("customerId") String customerId){
    	Customer customer = new Customer();
    	customer.setCustomerId("111111");
    	customer.setStateIdentification("222222");
    	User user = new User();
    	user.setName("kazeem");
    	user.setPassword("password");
    	user.setEmail("akinrinde@justjava.com.ng");
    	user.setGivenName("kazeem");
    	user.setFamilyName("Akinrinde");
    	customer.setUser(user);
 
    	System.out.println(" REQUESTING FOR THE Customer ....................................");
    	return Response.status(200).entity(customer).build();
    }
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/collection/{collectionRef}")
    public Response getCollection(@PathParam("collectionRef") String collectionRef){
    	MakePayment payment = new MakePayment();
    	payment.setAmount(new BigDecimal(0.00));
    	payment.setIdentifier("TIN");
    	payment.setValue("120920922");
    	System.out.println(" REQUESTING FOR THE Payment ....................................");
    	return Response.status(200).entity(payment).build();
    } 
    
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/avs/{number}/{name}/{bankCode}")
    public String getAccountName(@PathParam("number") String number,
    		@PathParam("name") String name,@PathParam("bankCode") String bankCode){


		try {
	    	Account account = new Account();
	    	Bank bank = new Bank();
	    	bank.setCode(bankCode);
	    	account.setId(System.currentTimeMillis());
	    	account.setName(name);
	    	account.setNumber(number);
	    	account.setBank(bank);

	    	String accountName = account.getNibssFetchedAccountName();
	    	System.out.println(" Account Verification Result ===" + accountName);
	    	if(accountName !=null)
	    		return accountName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return "Unable to Verify the Account";
    }    
    
}