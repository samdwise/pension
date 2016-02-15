package ng.com.justjava.epayment.action;

import java.util.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

import ng.com.justjava.epayment.model.*;

public class SavePFAAction extends ViewBaseAction{

	@Override
	public void execute() throws Exception {
				// TODO Auto-generated method stub
				String uniqueIdentifier = getView().getValueString("uniqueIdentifier");
				
				
				Map allValues = getView().getAllValues();
				
				List<Map> items =  (List<Map>) allValues.get("items");
				
				List<Map> newItems =  new ArrayList<Map>();
				System.out.println("1 The Values in All ==="+ allValues + " items length" + items.size());
				for (Map map : items) {
					if(!Is.emptyString((String) map.get("code"))){
						newItems.add(map);
					}
					System.out.println(" item here == " + map);
				}
				

				allValues.put("items", newItems);
				System.out.println("2 The Values in All ==="+ allValues + " items length" + items.size());
				//addMessage("OKAY!!!", null);

				
				if(!Is.emptyString(uniqueIdentifier)){
					ArrayList<PensionFundAdministrator> pensionFundAdministrator = (ArrayList<PensionFundAdministrator>) XPersistence.getManager().
							createQuery(" FROM PensionFundAdministrator p WHERE p.uniqueIdentifier='"+uniqueIdentifier+"'").getResultList();
					if(pensionFundAdministrator != null && pensionFundAdministrator.size()>0){
						
						
						System.out.println(" The allValues==" + allValues);
						
						if(allValues.containsKey("id") && allValues.get("id")!=null){
							MapFacade.setValues("PensionFundAdministrator", allValues, allValues);
						}else{
							addError("PensionFundAdministrator with uniqueIdentifier "+uniqueIdentifier + " Already Exist", null);
							return;
							//allValues.put("id", corporate.get(0).getId());
							//MapFacade.setValues("Corporate", allValues, allValues);
						}
							
					}else{
						MapFacade.create("PensionFundAdministrator", allValues);
					}
					addMessage("PensionFundAdministrator Successfully Saved");
				}else{
					addError("UniqueIdentifier Is Required", null);
					return;
				}
				
			}
		
	}
	


