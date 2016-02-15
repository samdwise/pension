package ng.com.justjava.epayment.utility;

import com.etranzact.fundgate.ws.*;



public class WebserviceUtil {
	public static FundGate getPort(){
		FundGateImplService service = new FundGateImplService();
		return service.getFundGateImplPort();
	}
}
