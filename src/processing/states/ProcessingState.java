package processing.states;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import processing.DataProcessing;
import rs.ac.uns.ftn.xws.cbs.faktura.Faktura;
import rs.ac.uns.ftn.xws.cbs.izvod.Izvod;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import rs.ac.uns.ftn.xws.cbs.mt900.Mt900;
import rs.ac.uns.ftn.xws.cbs.mt910.Mt910;
import rs.ac.uns.ftn.xws.cbs.nalog_za_placanje.NalogZaPlacanje;
import rs.ac.uns.ftn.xws.cbs.zahtev_za_izvod.ZahtevZaIzvod;
import utility.NullClass;
import facades.DBReaderFacade;
import facades.DBWriterFacade;

@SuppressWarnings("rawtypes")
public abstract class ProcessingState implements DataProcessing {

	DBReaderFacade dbReader;
	DBWriterFacade dbWriter;
	
	@Override
	public Object process(Mt102 mt102) throws JAXBException, IOException, SAXException, Exception {
		return new NullClass();
	}

	@Override
	public Object process(Mt103 mt103) throws JAXBException, IOException, SAXException, Exception {
		return new NullClass();
	}

	@Override
	public Object process(Mt900 mt900) {
		return new NullClass();
	}

	@Override
	public Object process(Mt910 mt910) {
		return new NullClass();
	}

	@Override
	public Object process(Izvod izvod) {
		return new NullClass();
	}

	@Override
	public Object process(NalogZaPlacanje nalog) {
		return new NullClass();
	}

	@Override
	public Object process(ZahtevZaIzvod zahtev) {
		return new NullClass();
	}
	
	@Override 
	public Object process(Faktura faktura) throws JAXBException, IOException, SAXException, Exception {
		return new NullClass();
	}
	
	protected <T> void storeInDatabase(T target) throws JAXBException, IOException, SAXException, Exception {
		dbWriter = new DBWriterFacade<T>(target);
		dbWriter.save();
		dbWriter=null;
	}

}
