package validation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import rs.ac.uns.ftn.xws.cbs.faktura.Faktura;
import rs.ac.uns.ftn.xws.cbs.faktura.FakturaStavka;
import rs.ac.uns.ftn.xws.cbs.faktura.FakturaZaglavlje;
import builders.firm.FakturaBuilder;

/**
 * Validator for Faktura
 * @author Nikola
 *
 */
public class FakturaValidator implements DataValidator {

	private BigDecimal total;
	private BigDecimal parts;
	private static final BigDecimal BIG_DECIMAL_ZERO=BigDecimal.valueOf(0);
	private Properties properties;

	public FakturaValidator() {
		super();
	}
	
	public FakturaValidator(Properties configProperties) {
		this.properties=configProperties;
	}
	
	@Override
	public Faktura validate(Object obj) {

		if(obj==null || !(obj instanceof Faktura))
			return null;
		
		Faktura faktura = (Faktura) obj;
		Faktura retVal = new Faktura();
		
		resetPrices();
		
		extractPrices(faktura);
		
		if(isPurchaseOrder(faktura)) {
			
			retVal=FakturaBuilder.buildPrice(faktura);
			retVal.getZaglavlje().setPibDobavljaca(properties.getProperty("pib"));
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
		
		for(FakturaStavka fs : stavke) {
			if(fs.getVrednost()==null) {
				return;
			}
			parts=parts.add(fs.getVrednost());
		}
		
		total = fz.getUkupnoRobaIUsluge();
	}
	
	protected boolean isPurchaseOrder(Faktura faktura) {
		
		boolean retVal = false;
		
		if(total.compareTo(BIG_DECIMAL_ZERO)<=0) {
			
			if(parts.compareTo(BIG_DECIMAL_ZERO)<=0) {
				
				retVal=true;
			}
		}
		
		return retVal;
	}
	
	private void checkPriceValidity() {
		
		int res = total.compareTo(parts);
		
		if(res!=0) {
			throw new RuntimeException("Total price doesn't match individual prices in validation. FakturaValidator");
		}
	}
	
	private void resetPrices() {
		
		total = BigDecimal.valueOf(0);
		parts = BigDecimal.valueOf(0);
	}
}
