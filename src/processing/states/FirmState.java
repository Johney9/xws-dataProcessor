package processing.states;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import rs.ac.uns.ftn.xws.cbs.faktura.Faktura;
import validation.DataValidator;
import validation.FakturaValidator;
import facades.DBWriterFacade;

public class FirmState extends ProcessingState {
	
	public Faktura process(Faktura faktura) throws JAXBException, IOException, SAXException, Exception {
		
		DataValidator validator = new FakturaValidator();
		
		Faktura retVal = (Faktura) validator.validate(faktura);
		
		//storing to database
		dbWriter = new DBWriterFacade<Faktura>(retVal);
		
		dbWriter.save();
		
		return retVal;
	}
}
