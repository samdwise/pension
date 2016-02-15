package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.util.*;

public class ViewDetailAction extends OnSelectElementBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		//List list = getSelectedObjects();
		String name = getCollectionElementView().getModelName();
		
		System.out.println(" The Model Name "+getView().getModelName());
		System.out.println(" All Values == "+getView().getAllValues());
		
		PensionFundAdministrator  pfa = null;
		Long pfaId = null;
		Corporate corporate = null;
		System.out.println(" about to show modal " + name + " selected row==" + getRow());
		PensionFundCustodian pfc = UserManager.getPFCOfLoginUser();
		String lastFilter = "";
		if(Is.equalAsStringIgnoreCase(getView().getModelName(), "MonthlyUpload")){
			Long id = (Long) getView().getAllValues().get("id");
			lastFilter = " AND ${upload.id}="+id;
		}
		
		if(Is.equalAsString("PensionFundAdministrator", name)){
		
				pfa = (PensionFundAdministrator) getCollectionElementView().getCollectionObjects().get(getRow());
				pfaId = pfa!=null?pfa.getId():0;
				corporate = UserManager.getCorporateOfLoginUser();
				
		}else if(Is.equalAsString("Corporate", name)){
			pfa = UserManager.getPFAOfLoginUser();
			pfaId = pfa!=null?pfa.getId():0;
			corporate = (Corporate) getCollectionElementView().getCollectionObjects().get(getRow());
		}
		System.out.println(" pfa here " + pfa);
		
		if(pfc==null){
			
			System.out.println(" Showing the dialog as expected=====");
			showDialog();
		}else{
			pfaId = (Long) getRequest().getSession().getAttribute("pfaId");
			corporate = (Corporate) getCollectionElementView().getCollectionObjects().get(getRow());
		}
		
		if(pfaId==null || pfaId ==0)
			return;
		
		Map key = new HashMap();
		key.put("id", pfaId);

		getView().setTitle(pfa.getName() + " Details");
		getView().setModelName("PensionFundAdministrator"); // 2
		getView().setViewName("pfaHolders");
		String condition="${pfa.id}=" + pfaId + " AND ${corporate.id}="+corporate.getId() +lastFilter;
		//String condition="${pfa.id}=3 AND ${corporate.id}=1";
		getView().setValues(key); // 3
		getView().findObject(); // 4
		getView().getSubview("holders").getCollectionTab().setBaseCondition(condition);
		getView().setKeyEditable(false);
/*		for (Object object : list) {
			PensionFundAdministrator pfa = (PensionFundAdministrator) object;
			System.out.println(" The total ===== " + pfa.getName());
		}*/
		addActions("Return.close");
		
	}

}
