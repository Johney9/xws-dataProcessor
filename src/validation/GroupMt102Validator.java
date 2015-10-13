package validation;

import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;

public class GroupMt102Validator implements DataValidator {

	protected Mt102 baseMt102;
	
	public GroupMt102Validator(Mt102 baseMt102) {
		this.baseMt102=baseMt102;
	}
	
	@Override
	public Boolean validate(Object obj) throws Exception {
		
		boolean retVal = false;
		
		if(obj instanceof Mt102) {
			
			Mt102 validatingMt102 = (Mt102) obj;
			String validatingSwift = validatingMt102.getSwiftKodBankePoverioca();
			String baseSwift = baseMt102.getSwiftKodBankePoverioca();
			
			if (baseSwift.contains(validatingSwift)) {
				retVal = true;
			}
		}
		
		return retVal;
	}

}
