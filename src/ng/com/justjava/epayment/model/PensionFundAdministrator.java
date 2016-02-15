package ng.com.justjava.epayment.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

@Entity
@Views({@View(members="rcNo;name;email;uniqueIdentifier;custodian;Users {users}"),
	@View(name="pfaRecordView" , members="companies"),
	@View(name="rsaCompanyHolders" , members="companyHolders"),
	@View(name="pfaHolders" , members="holders")})
@Tabs({@Tab(properties="name"),
	   @Tab(name="custodianView", properties="name",filter=LoginUserPFCFilter.class,baseCondition = "${custodian.id}=?")})

public class PensionFundAdministrator extends AccountOwnerDetail{


	@DisplaySize(15)
	private String rcNo;



	@Transient
	private MonthlyUpload upload;

	@ReadOnly
	@ViewAction("")
	@OneToMany(mappedBy="pfa")
	@ListProperties("pencommNumber,corporate.name,firstName,secondName,pensionAmount,upload.month")
	@Condition("${pfa.id}=0 AND ${corporate.id}=0")
	private Collection<RSAHolder> holders;


	public int getFullHoldersNumber(){
		return (getHolders()!=null?getHolders().size():0);
	}

	@ListProperties("name,amountSummation,totalNumberOfHolders")
	@Transient
	@ReadOnly
	@ViewAction("")
	@RowAction("RowAction.showDetail")
	public Collection<Corporate> getCompanies(){
		String query = " FROM Corporate c "
				+ "INNER JOIN c.holders h WHERE h.pfa.id="+getId();


		Collection<Object[]> copmanies = null;
		Collection<Corporate> list = new ArrayList<Corporate>();
		try {
			copmanies = XPersistence.getManager().createQuery(query).getResultList();
			for (Object[] object : copmanies) {
				Corporate corporate = (Corporate)object[0];
				RSAHolder holder = (RSAHolder)object[1];
				corporate.addAmountSummation(holder.getPensionAmount());
				corporate.incrementTotalNumberOfHolders();
				if(!list.contains(corporate))
					list.add(corporate);
				System.out.println(" The pensionFundAdministrator name=="+corporate.getName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}



	@ManyToOne
	@DescriptionsList
	@NoCreate
	@NoModify
	@Required
	private PensionFundCustodian custodian;

	private Date registeredDate;
	private String registerBy;




	@OneToMany(mappedBy="pfa",cascade=CascadeType.ALL)
	@AsEmbedded
	@SaveAction("CustomSaveAction.savePFAUser")
	@ListProperties(value="user.givenName,user.email")
	private Collection<PFAUser> users;


	public String getRcNo() {
		return rcNo;
	}

	public void setRcNo(String rcNo) {
		this.rcNo = rcNo;
	}



	public Collection<PFAUser> getUsers() {
		return users;
	}

	public void setUsers(Collection<PFAUser> users) {
		this.users = users;

	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public String getRegisterBy() {
		return registerBy;
	}

	public void setRegisterBy(String registerBy) {
		this.registerBy = registerBy;
	}

	@PreCreate
	public void preCreate(){
		setRegisterBy(Users.getCurrent());
		setRegisteredDate(Dates.createCurrent());
	}


	public static PensionFundAdministrator findPFAByUniqueIdentifier(String uniqueIdentifier){
		PensionFundAdministrator pensionFundAdministrator = null;
		try {
			pensionFundAdministrator = (PensionFundAdministrator) XPersistence.getManager().createQuery
					("FROM PensionFundAdministrator p WHERE p.uniqueIdentifier='"+uniqueIdentifier+"'").getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pensionFundAdministrator;
	}

/*	public Account getPensionAccount() {
		return pensionAccount;
	}

	public void setPensionAccount(Account pensionAccount) {
		this.pensionAccount = pensionAccount;
	}*/

	public PensionFundCustodian getCustodian() {
		return custodian;
	}

	public void setCustodian(PensionFundCustodian custodian) {
		this.custodian = custodian;
	}

	public Collection<RSAHolder> getHolders() {
		return holders;
	}

	public void setHolders(Collection<RSAHolder> holders) {
		this.holders = holders;
	}

	public BigDecimal getAmountSummation() {
		return amountSummation;
	}

	public void addAmountSummation(BigDecimal amountSummation) {
		this.amountSummation = this.amountSummation.add(amountSummation);
	}
	public int getTotalNumberOfHolders() {
		return totalNumberOfHolders;
	}

	public void incrementTotalNumberOfHolders() {
		this.totalNumberOfHolders = this.totalNumberOfHolders + 1;
	}
	public Collection<RSAHolder> getCompanyHolders() {
		return companyHolders;
	}

	public void addCompanyHolders(RSAHolder companyHolder) {
		this.companyHolders.add(companyHolder);

		System.out.println( " Just Added another one and size now===" + companyHolders.size());
		incrementTotalNumberOfHolders();
	}

	@Transient
	public PFAUser getMyAdmin(){
		List<PFAUser> users = XPersistence.getManager().createQuery
							("From PFAUser p WHERE p.pfa.id="+getId()).getResultList();
		PFAUser adminUser = null;
		userLoop:
		for (PFAUser pfaUser : users) {
			for (Role role : pfaUser.getUser().getRoles()) {
				if(Is.equalAsString("pfaAdmin", role.getName())){
					adminUser = pfaUser;
					break userLoop;
				}
			}
		}
		return adminUser;
	}



	public MonthlyUpload getUpload() {
		return upload;
	}

	public void setUpload(MonthlyUpload upload) {
		this.upload = upload;
	}



	@Transient
	@ListProperties("firstName,secondName,pencommNumber,pensionAmount,fromDateJoinedToDate,latestMonthPaid")
	private Collection<RSAHolder> companyHolders = new ArrayList<RSAHolder>();

	@Transient
	private int totalNumberOfHolders;
	@Transient
	private BigDecimal amountSummation = new BigDecimal(0.00);
}