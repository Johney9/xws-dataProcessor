package util;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;

import org.xml.sax.SAXException;

import rs.ac.uns.ftn.xws.cbs.korisnik.Korisnik;
import facades.DatabaseFacade;

public class SwiftAndBankAccountLocator {
	
	private DatabaseFacade dbf;
	private Korisnik korisnik;

	/**
	 * Get SWIFT from ID korisnika
	 * @param id korisnika
	 * @return located SWIFT code
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws IOException
	 * @throws Exception
	 */
	public SwiftAndBankAccountLocator(String id) throws JAXBException, SAXException, IOException, Exception {
		
		dbf = new DatabaseFacade();
		korisnik = new Korisnik();
		try {
			korisnik=dbf.readFromDatabase(korisnik, id);
		} catch (NullPointerException e) {
			reportFailure(id);
		} catch (UnmarshalException e) {
			reportFailure(id);
		}
	}
	
	
	public String locateSwift() {
		String retVal="";
		
		retVal=korisnik.getSwiftKodBankeVlasnice();
		
		return retVal;
	}
	
	public String locateBankAccount() {
		return korisnik.getBrojRacuna();
	}
	
	private void reportFailure(String id) {
		throw new RuntimeException("Korisnik(firma) with the given id: "+id+", not found.");
	}
}
