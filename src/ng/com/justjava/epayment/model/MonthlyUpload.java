package ng.com.justjava.epayment.model;

import java.io.*;
import java.math.*;
import java.security.*;
import java.util.*;

import javax.crypto.*;
import javax.persistence.*;

import org.apache.commons.lang3.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.etranzact.fundgate.ws.*;

import ng.com.justjava.epayment.action.*;
import ng.com.justjava.epayment.model.RemitPension.Months;
import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

@Views({@View(members="year;month;holders"),
	@View(name="approve",members="companyName;payingAccount;paymentSummary;serviceCharge;totalAmount"),
	@View(name="viewOnly",members="paymentSummary;serviceCharge;totalAmount")})
@Tabs({@Tab(properties="month,narration,status"),
	@Tab(name="approve",properties="narration",filter=MultiValueFilter.class,
	baseCondition = "${status}=1 AND ${corporate.id}=? AND ${levelReached}=?")
})
@Entity
public class MonthlyUpload {
	
	public enum Status{
		New,awaitingApproval,approve,reject,sent,errorSending;
	}
	
	public enum Type{
		corporate,personal;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;

	@ManyToOne
	@DescriptionsList(descriptionProperties="year")
	@NoCreate @NoModify
	private PeriodYear year;
	
	
	@ManyToOne
	@NoCreate
	@NoModify
	@DescriptionsList(depends="companyName",condition="${corporate.name}=? AND ${enable}=1",descriptionProperties="display")
	private TransitAccount payingAccount;
	
	@Transient
	public String getNarration(){
		return month + " Pension Contribution Remittance";
	}
	private int levelReached;
	
	private Status status;
	
	@ManyToOne
	private Corporate corporate;
	
	@Transient
	@Stereotype("LABEL")
	public String getCompanyName(){
		return corporate.getName();
	}
	
	@Required
	@OnChange(OnChnageMonthlyUpload.class)
	private Months month;
	 
	@OneToMany(cascade=CascadeType.ALL,mappedBy="upload")
	@ReadOnly//(forViews="approve")
	@ListProperties("fullName,pencommNumber,pfa.name,voluntaryDonation,"
			+ "grossPay,pensionAmount[upload.totalAmount],upload.status,variance,remark")
	@Condition("${upload.id}=-1")
	@RowStyle(style="row-red", property="variance", value="vary") //Create row-red class in custom.css
	private Collection<RSAHolder> holders;
	
	private Date dateEntered;
	private String enteredBy;
	public Months getMonth() {
		return month;
	}
	public void setMonth(Months month) {
		this.month = month;
	}
	public Collection<RSAHolder> getHolders() {
		return holders;
	}
	public void setHolders(Collection<RSAHolder> holders) {
		this.holders = holders;
	}
	public Date getDateEntered() {
		return dateEntered;
	}
	public void setDateEntered(Date dateEntered) {
		this.dateEntered = dateEntered;
	}
	public String getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Corporate getCorporate() {
		return corporate;
	}
	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public int getLevelReached() {
		return levelReached;
	}
	public void setLevelReached(int levelReached) {
		this.levelReached = levelReached;
	}
	public PeriodYear getYear() {
		return year;
	}
	public void setYear(PeriodYear year) {
		this.year = year;
	}
	

	
	@ListProperties("name,totalNumberOfHolders,amountSummation")
	@Transient
	@ReadOnly
	@CollectionView("rsaCompanyHolders")
	@RowAction("RowAction.showDetail")
	@NoModify
	@ViewAction("")
	public Collection<PensionFundAdministrator> getPaymentSummary(){
		Corporate corporate = UserManager.getCorporateOfLoginUser(); 
		String query = " FROM PensionFundAdministrator p "
				+ "INNER JOIN p.holders h WHERE h.corporate.id="+corporate.getId() 
				+ " AND h.upload.id="+getId();
		Collection<Object[]> pfas = null;
		Collection<PensionFundAdministrator> list = new ArrayList<PensionFundAdministrator>();

		try {
			pfas = XPersistence.getManager().
					createQuery(query).getResultList();
			for (Object[] object : pfas) {
				PensionFundAdministrator pfa = (PensionFundAdministrator)object[0];
				
				RSAHolder holder = (RSAHolder)object[1];
				pfa.addAmountSummation(holder.getPensionAmount());
				//totalAmount = totalAmount.add(holder.getPensionAmount());
				pfa.addCompanyHolders(holder);
				if(!list.contains(pfa)){
					list.add(pfa);
				}
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		summaryList = list;
		return list;
	}
	/*@Transient
	private BigDecimal totalAmount=new BigDecimal(0.00);*/
	
	@Transient
	@Stereotype("LABEL")
	public BigDecimal getServiceCharge(){
		String charges = XavaPreferences.getInstance().getXavaProperty("charge", "120.00");
		return new BigDecimal(charges);
	}
	
	@Transient
	@Stereotype("LABEL")
	public BigDecimal getTotalAmount(){
		if(summaryList == null)
			summaryList = getPaymentSummary();
		BigDecimal result = new BigDecimal(0.00);
		for (PensionFundAdministrator pfa : summaryList) {
			result = result.add(pfa.getAmountSummation());
		}
		result = result.add(getServiceCharge());
		return result;
	}
	@Transient
	private Collection<PensionFundAdministrator> summaryList = null;
	
	public TransitAccount getPayingAccount() {
		return payingAccount;
	}
	public void setPayingAccount(TransitAccount payingAccount) {
		this.payingAccount = payingAccount;
	}
	
	public boolean pay(){
		boolean result = false;
		FundResponse response = WebserviceUtil.getPort().process(getBulkFundRequest());
		
        System.out.println("Pay Result Code = "+response.getError());
        System.out.println("Pay Result Message = "+response.getMessage());
        System.out.println("Pay Result Ref = "+response.getReference());
        System.out.println("Pay Result OtherRef = "+response.getOtherReference());
        System.out.println("Pay Result Amount = "+response.getAmount());
        System.out.println("Pay Result TotalFailed = "+response.getTotalFailed());
        System.out.println("Pay Result TotalSuccess = "+response.getTotalSuccess());
        System.out.println("Pay Result Company = "+response.getCompanyId());
        System.out.println("Pay Result Action = "+response.getAction());	

		return result;
	}
	
	public FundRequest getBulkFundRequest(){
		//List<PaymentInstruction> payItems = XPersistence.getManager().createQuery(arg0)
        FundRequest request = new FundRequest();

        request.setAction("BT");
        //String viewPin = getView().getValueString("pin");
        String terminalId = getCorporate().getTerminalId();
        
        String pin = "";
		try {
			pin = Cryptor.encrypt(getPayingAccount().getPin(), corporate.getMasterKey());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        String uniqueId = corporate.getUniqueIdentifier();
        

        //"20000000054"
        request.setTerminalId(terminalId);
        com.etranzact.fundgate.ws.Transaction trans = new com.etranzact.fundgate.ws.Transaction();
        
        
        //"ZhXy4geRgnpqVOH/7V2beg=="
        trans.setPin(pin);  
        //trans.setPin("ZhXy4geRgnpqVOH/7V2beg==");
        
        trans.setToken("N");
        //trans.setReference(this.getReferenceId());
        try {
			//trans.setReference(Cryptor.generateKey());
        	//trans.setReference("y41A1ggg0CE5ddddde");//+StringUtils.trim(RandomStringUtils.randomAlphanumeric(3)));
        	trans.setReference(RandomStringUtils.randomAlphanumeric(18).toLowerCase());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n The parameters are ===="
        		+ "terminalId=="+terminalId + " and pin=="+ pin + "  and  uniqueId=="+ uniqueId 
        		+ "  the reference===" + trans.getReference());        
        
        double bulkAmount = 0.00;
        
        //trans.setCompanyId(getOwner().getName());
        //"00000000000000000018"
        trans.setCompanyId(uniqueId);
        trans.setSenderName(corporate.getName());	
        //trans.setSenderName("eTranzact");
		
		BulkItems bulkItems = new BulkItems();

		Collection<PensionFundAdministrator> pfas = getPaymentSummary();
		
		//List<PaymentInstruction> payItems = (List<PaymentInstruction>) getPaymentInstructions();
		for(PensionFundAdministrator payItem:pfas){
			bulkAmount = bulkAmount + payItem.getAmountSummation().doubleValue(); 
			BulkItem item = new BulkItem();
			item.setBeneficiaryName(payItem.getName());
			item.setAccountId(payItem.getCustodian().getPensionAccount().getNumber());
			item.setAmount(payItem.getAmountSummation().doubleValue());
			item.setBankCode(StringUtils.trim(payItem.getCustodian().getPensionAccount().getBank().getCode()));
			item.setNarration(payItem.getId() + "_" +  
			"Pension Contribution for Month"+month+ " From "+corporate.getName());
			// item.setBankCode("033");
			item.setUniqueId(payItem.getId() + "_" + RandomStringUtils.randomAlphanumeric(18).toLowerCase());
			bulkItems.getBulkItem().add(item);            
		}
		trans.setAmount(bulkAmount);//bulk amount
		trans.setBulkItems(bulkItems);
        request.setTransaction(trans);
		return request;
	}
	private Type type;
	
	@PostCreate
	@PostPersist
	@PostUpdate
	public void setTheType(){
		if(getType() == null){
			setType(Type.corporate);
		}
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}

}
