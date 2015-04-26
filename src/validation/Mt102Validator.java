package validation;

import java.math.BigDecimal;

import rs.ac.uns.ftn.xws.cbs.mt102.Mt102PojedinacniNalog;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import util.BankAccountValidator;
import util.SwiftValidator;

public class Mt102Validator implements DataValidator {

	@Override
	public Mt102 validate(Object obj) {
		
		if(obj==null || !(obj instanceof Mt102))
			return null;
		
		Mt102 mt102 = (Mt102) obj;
		
		validateSwift(mt102);
		validateUkupanIznos(mt102);
		validateRacuniPoveriocaUIstojBanci(mt102);
		
		return mt102;
	}

	protected void validateSwift(Mt102 mt102) {
		
		String swiftDuznika = mt102.getSwiftKodBankeDuznika();
		String swiftPoverioca = mt102.getSwiftKodBankePoverioca();
		
		boolean swiftPoveriocaValid = SwiftValidator.validate(swiftPoverioca);
		boolean swiftDuznikaValid = SwiftValidator.validate(swiftDuznika);
		
		if(!swiftPoveriocaValid) {
			throw new RuntimeException("Swift poverioca nije validan.");
		}
		
		if(!swiftDuznikaValid) {
			throw new RuntimeException("Swift duznika nije validan.");
		}
	}
	
	protected void validateUkupanIznos(Mt102 mt102) {

		BigDecimal sum = new BigDecimal(0);
		
		for(Mt102PojedinacniNalog nalog : mt102.getPojedinacniNalozi()) {
			
			BigDecimal iznos = nalog.getIznos();
			sum=sum.add(iznos);
		}
		
		BigDecimal ukupanIznos = mt102.getUkupanIznos();
		
		if(sum.compareTo(ukupanIznos)!=0) {
			throw new RuntimeException("Neispravan ukupan iznos.");
		}
	}
	
	protected void validateRacuniPoveriocaUIstojBanci(Mt102 mt102) {
		
		String racunPoverioca = mt102.getObracunskiRacunBankePoverioca();
		
		for(Mt102PojedinacniNalog nalog : mt102.getPojedinacniNalozi()) {
			
			String pojedinacniRacunPoverioca = nalog.getRacunPoverioca();
			
			if(!BankAccountValidator.sameBank(racunPoverioca, pojedinacniRacunPoverioca)) {
				throw new RuntimeException("Racun: "+pojedinacniRacunPoverioca+" nije iz iste banke kao ostali.");
			}
		}
	}
}
