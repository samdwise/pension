package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;

public class LoadByPencommNumberAction extends OnChangePropertyBaseAction{

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		RSAHolder holder = RSAHolder.findByPencommNumber(getView().getValueString("pencommNumber"));
		if(holder != null){
			getView().setValue("firstName",holder.getFirstName());
			getView().setValue("pencommNumber",holder.getPencommNumber());
			getView().setValue("email",holder.getEmail());
			getView().setValue("lastName",holder.getSecondName());
			getView().setValue("phoneNumber",holder.getPhoneNumber());
		}
	}

}
