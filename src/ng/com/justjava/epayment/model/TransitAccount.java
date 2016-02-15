package ng.com.justjava.epayment.model;

import java.io.*;
import java.math.*;
import java.security.*;

import javax.crypto.*;
import javax.persistence.*;

import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.etranzact.fundgate.ws.*;
import com.etranzact.fundgate.ws.Transaction;

@Entity
@Views({@View(members="bank;terminalId;pin"),@View(name="embeded",members="bank;terminalId")})
@Tab(filter=LoginUserCorporateFilter.class,baseCondition = "${enable}=1 AND ${corporate.id}=?")

public class TransitAccount extends Approvable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	@DisplaySize(15)
	private String terminalId;
	
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	private BigDecimal balance;
	
	@Transient
	public String getDisplay(){
		return (getBank()!=null?getBank().getName() + "(" + getTerminalId() + ")":getTerminalId());
	}
	
	@DescriptionsList
	@ManyToOne
	private Bank bank;
	
	@Column(length=4)
	@DisplaySize(4)
	private String pin;
	
	@ManyToOne
	@JoinColumn(name = "accountOwnerId")
	private Corporate corporate;
	
	
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	

	
	public void updateCurrentBalance(){
		boolean eTranzact = Is.equalAsStringIgnoreCase(XavaPreferences.getInstance().getXavaProperty("mainGateway", "eTranzact"), "eTranzact");
		System.out.println(" The result of the test here ===" + eTranzact);
		setBalance(eTranzact?new BigDecimal(getBalanceeTranzact()):new BigDecimal(0.00));
		System.out.println(" The result of the updated balance ===" + getBalance());
		XPersistence.getManager().merge(this);
		XPersistence.commit();
	}
	public boolean approve(){
		setEnable(true);
		XPersistence.getManager().merge(this);
		return true; 
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public Corporate getCorporate() {
		return corporate;
	}
	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}
	
	public double getBalanceeTranzact(){
        
		System.out.println("0000000000000000000The result from the eTranzact here =====");
		if(getPin()==null)
			return 0.00;
		
        FundGate port = WebserviceUtil.getPort();

        FundRequest request = new FundRequest();
        request.setAction("BE");

        System.out.println("1111111111111111The result from the eTranzact here =====");
        Transaction t = new Transaction();
        String plainPin = getPin();
        String masterKey = XavaPreferences.getInstance().getXavaProperty("masterKey", "R90aaowC0PrB2zILxzV1uw==");
        String terminalId = getTerminalId();
        String pin =  "";
        request.setTerminalId(terminalId);//20000000054
        try {
			pin = Cryptor.encrypt(plainPin, masterKey);
			System.out.println("22222222222222222222The result from the eTranzact here =====pin==" + pin);
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
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
		}
        t.setPin(pin);//0012
        try {
			t.setReference(Cryptor.generateKey());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        request.setTransaction(t);
        FundResponse result = port.process(request);
        String message = result.getMessage();
        
        System.out.println(" The result from the eTranzact here =====" + message + " for Card Number=" + terminalId);
        
        BigDecimal bd = new BigDecimal(message);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();        

	}
	
}
