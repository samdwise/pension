package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.RemitPension.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;

public class OnChnageMonthlyUpload extends OnChangePropertyBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Months month = (Months) getView().getValue("month");
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		String condition = "${upload.month}=" +(month!=null?month.ordinal():0)
				+" AND ${corporate.id}="+(corporate!=null?corporate.getId():0) +" AND ${upload.status}=0";
		//condition = "${corporate.id}=3";
		System.out.println(" The condition =="+condition);
		getView().getSubview("holders").getCollectionTab().setBaseCondition(condition);
		getView().getSubview("holders").refresh();
	}

}
