package validation;

import java.security.InvalidParameterException;

import javax.xml.bind.UnmarshalException;

import rs.ac.uns.ftn.xws.cbs.banka_app_mapper.BankaAppMapper;
import facades.DatabaseFacade;

public class BankExistenceValidator implements DataValidator {

	private DatabaseFacade dbf;

	
	public BankExistenceValidator(DatabaseFacade dbf) {
		this.dbf=dbf;
	}
	
	
	/** Verifies existence of bank in database
	 * @param obj must be the Swift number and of object String
	 */
	@Override
	public BankaAppMapper validate(Object obj) throws Exception {
		
		if(!(obj instanceof String)) {
			throw new InvalidParameterException("Function parameter obj must be string.");
		}
		String swift = (String) obj;
		
		if(!SwiftValidator.validate(swift)) {
			throw new RuntimeException("The given swift code: "+swift+" is not valid.");
		}
		
		BankaAppMapper banka = null;
		
		try {
			banka = dbf.readFromDatabase(new BankaAppMapper(), swift);
		} catch (UnmarshalException e) {
			throw new RuntimeException("Bank with swift: "+swift+" doesn't exist.");
		}
		
		return banka;
	}

}
