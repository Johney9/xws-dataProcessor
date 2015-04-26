package processing.states;

import java.io.IOException;
import java.util.Properties;

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
import facades.DatabaseFacade;

public abstract class ProcessingState implements DataProcessing {
	
	DatabaseFacade dbFacade = new DatabaseFacade();
	
	@Override
	public Object process(Mt102 mt102) throws JAXBException, IOException,
			SAXException, Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Object process(Mt103 mt103) throws JAXBException, IOException,
			SAXException, Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Object process(Mt900 mt900) throws JAXBException, IOException,
			SAXException, Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Object process(Mt910 mt910) throws JAXBException, IOException,
			SAXException, Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Object process(Izvod izvod) throws JAXBException, IOException,
			SAXException, Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Object process(NalogZaPlacanje nalog) throws JAXBException,
			IOException, SAXException, Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Object process(ZahtevZaIzvod zahtev) throws JAXBException,
			IOException, SAXException, Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Object process(Faktura faktura) throws JAXBException, IOException,
			SAXException, Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void initialSetup(Properties properties) throws JAXBException, IOException, SAXException, Exception {
		//do nothing
	}

}
