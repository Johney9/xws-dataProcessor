package processing.states;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import rs.ac.uns.ftn.xws.cbs.faktura.Faktura;
import rs.ac.uns.ftn.xws.cbs.firma_app_mapper.FirmaAppMapper;
import rs.ac.uns.ftn.xws.cbs.korisnik.Korisnik;
import validation.DataValidator;
import validation.FakturaValidator;

public class FirmState extends ProcessingState {

	public Object process(Faktura faktura) throws JAXBException, IOException, SAXException, Exception {
		
		DataValidator validator = new FakturaValidator(configProperties);
		
		Faktura retVal = (Faktura) validator.validate(faktura);
		
		dbFacade.storeInDatabase(retVal);
		
		return retVal;
	}

	@Override
	public void initialSetup(Properties properties) throws JAXBException,
			IOException, SAXException, Exception {
		
		FirmaAppMapper fam = new FirmaAppMapper();
		configProperties=properties;
		String pib = properties.getProperty("pib");
		String nazivAplikacije = properties.getProperty("war.name");
		
		System.out.println("properties u firmState: "+properties);
		System.out.println("**pib: "+pib);
		System.out.println("**nazivAplikacije: "+nazivAplikacije);
		
		fam.setIdPibFirme(pib);
		fam.setNazivAplikacije(nazivAplikacije);
		Korisnik firma = new Korisnik();
		firma.setId(pib);
		firma.setBrojRacuna(pib.substring(5, pib.length()));
		firma.setSwiftKodBankeVlasnice(properties.getProperty("swift"));
		firma.setStanje(BigDecimal.valueOf(65560));
		
		
		dbFacade.storeInDatabase(fam,pib);
		dbFacade.storeInDatabase(firma, firma.getBrojRacuna());
	}
}
