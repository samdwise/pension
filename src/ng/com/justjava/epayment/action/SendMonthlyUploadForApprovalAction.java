package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.MonthlyUpload.Status;
import ng.com.justjava.epayment.model.RemitPension.Months;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;

public class SendMonthlyUploadForApprovalAction extends ViewBaseAction{

	public void execute() throws Exception {
		System.out.println(" The id of this entity here is this =="+
					((MonthlyUpload)getView().getEntity()).getId()+
					" all values =="+getView().getAllValues());
		Months month = (Months) getView().getValue("month");
		
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		if(corporate==null)
			return;
		
		if(corporate.getHighestApprovalLevel() <= 0){
			addError("Approval Route Not Yet Setup", null);
			return;
		}
		String sql = " FROM MonthlyUpload m WHERE m.month="+month.ordinal() +
				" AND m.corporate.id="+corporate.getId() +" AND m.status=0";
		MonthlyUpload upload = null;
		try {
			upload = (MonthlyUpload) XPersistence.getManager().createQuery(sql).getSingleResult();
			upload.setStatus(Status.awaitingApproval);
			upload.setLevelReached(upload.getLevelReached()+1);
			upload = XPersistence.getManager().merge(upload);
			addMessage("The " + month + " Upload Is Sent For Approval", null);
			getView().reset();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		
	}
	
}
