package ng.com.justjava.epayment.utility;

import ng.com.justjava.epayment.model.*;

import org.openxava.jpa.*;

import com.googlecode.jcsv.annotations.*;

public class PFAValueProcessor implements ValueProcessor<PensionFundAdministrator> {

	public PensionFundAdministrator processValue(String uniqueIdentifier) {
		// TODO Auto-generated method stub
		PensionFundAdministrator result = null;
		String sql = "from PensionFundAdministrator p where p.uniqueIdentifier='"+uniqueIdentifier+"'";
		try {
			result = (PensionFundAdministrator) XPersistence.getManager().createQuery(sql).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}