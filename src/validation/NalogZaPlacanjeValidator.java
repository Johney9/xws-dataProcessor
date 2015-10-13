package validation;

import java.math.BigDecimal;

import javax.security.auth.login.AccountException;

import rs.ac.uns.ftn.xws.cbs.korisnik.Korisnik;
import rs.ac.uns.ftn.xws.cbs.nalog_za_placanje.NalogZaPlacanje;

public class NalogZaPlacanjeValidator implements DataValidator {

	//TODO: TEST!
	@SuppressWarnings("unused")
	@Override
	public NalogZaPlacanje validate(Object obj) throws Exception {
		
		if(obj==null || !(obj instanceof NalogZaPlacanje))
			return null;
		
		NalogZaPlacanje nalog = (NalogZaPlacanje) obj;
		Korisnik duznik = BankAccountValidator.checkValidRacun(nalog.getRacunDuznika());
		Korisnik poverioc = BankAccountValidator.checkValidRacun(nalog.getRacunPoverioca());
		BankAccountValidator.checkSufficientFunds(duznik, nalog.getIznos());
		checkIznos(nalog.getIznos());
		
		return nalog;
	}
	
	private void checkIznos(BigDecimal iznos) throws AccountException {
		if(iznos.signum()<0) {
			throw new AccountException("Iznos must be non negative.");
		}
	}
}
