package validation;

import java.math.BigDecimal;
import java.util.List;

import builders.firm.FakturaBuilder;
import rs.ac.uns.ftn.xws.cbs.faktura.Faktura;
import rs.ac.uns.ftn.xws.cbs.faktura.FakturaStavka;
import rs.ac.uns.ftn.xws.cbs.faktura.FakturaZaglavlje;

/**
 * Validator for Faktura
 * @author Nikola
 *
 */
public class FakturaValidator implements DataValidator {

	private BigDecimal total;
	private BigDecimal parts;

	@Override
	public Faktura validate(Object obj) {

		if(obj==null || !(obj instanceof Faktura))
			return null;
		
		Faktura faktura = (Faktura) obj;
		Faktura retVal = new Faktura();
		
		extractPrices(faktura);
		
		if(isPurchaseOrder(faktura)) {
			retVal=FakturaBuilder.buildPrice(faktura);
		}
		else {
			checkPriceValidity();
			retVal=faktura;
		}
		
		resetPrices();
		
		return retVal;
	}
	
	private void extractPrices(Faktura faktura) {
		
		FakturaZaglavlje fz = faktura.getZaglavlje();
		List<FakturaStavka> stavke = faktura.getStavke();
		
		parts = new BigDecimal(0);
		
		for(FakturaStavka fs : stavke) {
			parts=parts.add(fs.getVrednost());
		}
		
		total = fz.getUkupnoRobaIUsluge();
	}
	
	private boolean isPurchaseOrder(Faktura faktura) {
		
		boolean retVal = false;
		
		if(total.doubleValue()==0) {
			
			if(parts.doubleValue()==0) {
				
				retVal=true;
			}
		}
		
		return retVal;
	}
	
	private void checkPriceValidity() {
		
		int res = total.compareTo(parts);
		
		if(res!=0) {
			throw new RuntimeException("Total price doesn't match individual prices.");
		}
	}
	
	private void resetPrices() {
		
		total = BigDecimal.valueOf(0);
		parts = BigDecimal.valueOf(0);
	}
}
