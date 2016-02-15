package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.RemitPension.Months;
import ng.com.justjava.epayment.utility.*;

import org.apache.commons.collections.map.*;
import org.openxava.actions.*;

public class InitMonthlyUploadAction extends ViewBaseAction{

	public void execute() throws Exception {
		
		//PeriodYear year = new PeriodYear();
		//year.setYear(2016);
		Map year = new HashedMap();
		year.put("year", 2016);
		year.put("id",26);

		getView().setValue("year", year);
		Months month = (Months) getView().getValue("month");
		
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		String condition = "${upload.month}=" +(month!=null?month.ordinal():-1)
				+" AND ${corporate.id}="+(corporate!=null?corporate.getId():0) +" AND ${upload.status}=0";
		
		System.out.println(" The condition =="+condition);
		getView().getSubview("holders").getCollectionTab().setBaseCondition(condition);
		// TODO Auto-generated method stub
		
	}

}
