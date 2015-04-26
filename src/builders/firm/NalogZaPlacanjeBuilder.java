package builders.firm;

import java.io.IOException;
import java.math.BigDecimal;

import javax.xml.bind.JAXBException;

import rs.ac.uns.ftn.xws.cbs.faktura.Faktura;
import rs.ac.uns.ftn.xws.cbs.korisnik.Korisnik;
import rs.ac.uns.ftn.xws.cbs.nalog_za_placanje.NalogZaPlacanje;
import facades.DatabaseFacade;

public class NalogZaPlacanjeBuilder {
	
	//TODO: decide if a new id should be assigned here, or if it should be delegated from faktura.. also, hitno
	public static NalogZaPlacanje buildNalog(Faktura faktura) throws JAXBException, IOException, Exception {
		
		NalogZaPlacanje retVal = new NalogZaPlacanje();
		BigDecimal ukupno = faktura.getZaglavlje().getUkupnoRobaIUsluge();
		boolean hitno = false;
		
		if (ukupno.compareTo(BigDecimal.valueOf(250000))>0) {
			hitno=true;
		}
		
		retVal.setDatumNaloga(faktura.getZaglavlje().getDatumRacuna());
		retVal.setDatumValute(faktura.getZaglavlje().getDatumValute());
		retVal.setHitno(hitno);
		retVal.setDuznikNalogodavac(faktura.getZaglavlje().getNazivKupca()+" "+faktura.getZaglavlje().getAdresaKupca());
		retVal.setIdPoruke(faktura.getZaglavlje().getIdPoruke());
		retVal.setIznos(faktura.getZaglavlje().getIznosZaUplatu());
		retVal.setOznakaValute(faktura.getZaglavlje().getOznakaValute());
		retVal.setPrimalacPoverilac(faktura.getZaglavlje().getNazivDobavljaca()+" "+faktura.getZaglavlje().getAdresaDobavljaca());
		Korisnik kupac = null;
		DatabaseFacade dbf = new DatabaseFacade();
		kupac=dbf.readFromDatabase(kupac, faktura.getZaglavlje().getPibKupca());
		retVal.setRacunDuznika(kupac.getBrojRacuna());
		retVal.setRacunPoverioca(faktura.getZaglavlje().getUplataNaRacun());
		retVal.setSvrhaPlacanja("uplata za kupovinu robe/usluge");
		
		return retVal;
	}
}
