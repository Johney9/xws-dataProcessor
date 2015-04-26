package validation;

import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import util.SwiftValidator;

public class Mt103Validator implements DataValidator {

	@Override
	public Mt103 validate(Object obj) {
		
		if(obj==null || !(obj instanceof Mt103))
			return null;
		
		Mt103 mt103 = (Mt103) obj;
		
		validateSwift(mt103);
		
		return mt103;
	}
	
	protected void validateSwift(Mt103 mt103) {
		
		String swiftDuznika = mt103.getSwiftKodBankeDuznika();
		String swiftPoverioca = mt103.getSwiftKodBankePoverioca();
		
		boolean swiftPoveriocaValid = SwiftValidator.validate(swiftPoverioca);
		boolean swiftDuznikaValid = SwiftValidator.validate(swiftDuznika);
		
		if(!swiftPoveriocaValid) {
			throw new RuntimeException("Swift poverioca not valid.");
		}
		
		if(!swiftDuznikaValid) {
			throw new RuntimeException("Swift duznika not valid.");
		}
	}

}