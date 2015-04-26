package processing.states;

import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import rs.ac.uns.ftn.xws.cbs.faktura.Faktura;
import rs.ac.uns.ftn.xws.cbs.firma_app_mapper.FirmaAppMapper;
import validation.DataValidator;
import validation.FakturaValidator;

public class FirmState extends ProcessingState {
	
	public Object process(Faktura faktura) throws JAXBException, IOException, SAXException, Exception {
		
		DataValidator validator = new FakturaValidator();
		
		Faktura retVal = (Faktura) validator.validate(faktura);
		
		dbFacade.storeInDatabase(faktura);
		
		return retVal;
	}

	@Override
	public void initialSetup(Properties properties) throws JAXBException,
			IOException, SAXException, Exception {
		
		FirmaAppMapper fam = new FirmaAppMapper();
		String pib = properties.getProperty("pib");
		String nazivAplikacije = properties.getProperty("war.name");
		
		fam.setIdPibFirme(pib);
		fam.setNazivAplikacije(nazivAplikacije);
		
		dbFacade.storeInDatabase(fam);
	}
}
