package processing;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import rs.ac.uns.ftn.xws.cbs.faktura.Faktura;
import rs.ac.uns.ftn.xws.cbs.izvod.Izvod;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import rs.ac.uns.ftn.xws.cbs.mt900.Mt900;
import rs.ac.uns.ftn.xws.cbs.mt910.Mt910;
import rs.ac.uns.ftn.xws.cbs.nalog_za_placanje.NalogZaPlacanje;
import rs.ac.uns.ftn.xws.cbs.zahtev_za_izvod.ZahtevZaIzvod;

public interface DataProcessing {
	public Object process(Mt102 mt102) throws JAXBException, IOException, SAXException, Exception;
	public Object process(Mt103 mt103) throws JAXBException, IOException, SAXException, Exception;
	public Object process(Mt900 mt900);
	public Object process(Mt910 mt910);
	public Object process(Izvod izvod);
	public Object process(NalogZaPlacanje nalog);
	public Object process(ZahtevZaIzvod zahtev);
	public Object process(Faktura faktura);
}
