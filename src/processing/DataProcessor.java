package processing;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import processing.states.ProcessingState;
import rs.ac.uns.ftn.xws.cbs.faktura.Faktura;
import rs.ac.uns.ftn.xws.cbs.izvod.Izvod;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import rs.ac.uns.ftn.xws.cbs.mt900.Mt900;
import rs.ac.uns.ftn.xws.cbs.mt910.Mt910;
import rs.ac.uns.ftn.xws.cbs.nalog_za_placanje.NalogZaPlacanje;
import rs.ac.uns.ftn.xws.cbs.zahtev_za_izvod.ZahtevZaIzvod;

public class DataProcessor implements DataProcessing {

	protected ProcessingState state;
	
	public DataProcessor(ProcessingState state) {
		this.state=state;
	}
	
	@Override
	public Object process(Mt102 mt102) throws JAXBException, IOException, SAXException, Exception {
		return state.process(mt102);
	}

	@Override
	public Object process(Mt103 mt103) throws JAXBException, IOException, SAXException, Exception {
		return state.process(mt103);
	}

	@Override
	public Object process(Mt900 mt900) {
		return state.process(mt900);
	}

	@Override
	public Object process(Mt910 mt910) {
		return state.process(mt910);
	}

	@Override
	public Object process(Izvod izvod) {
		return state.process(izvod);
	}

	@Override
	public Object process(NalogZaPlacanje nalog) {
		return state.process(nalog);
	}

	@Override
	public Object process(ZahtevZaIzvod zahtev) {
		return state.process(zahtev);
	}

	@Override
	public Object process(Faktura faktura) {
		return state.process(faktura);
	}

}
