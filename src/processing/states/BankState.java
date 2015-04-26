package processing.states;

import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import rs.ac.uns.ftn.xws.cbs.banka_app_mapper.BankaAppMapper;
import rs.ac.uns.ftn.xws.cbs.izvod.Izvod;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import rs.ac.uns.ftn.xws.cbs.mt900.Mt900;
import rs.ac.uns.ftn.xws.cbs.mt910.Mt910;
import rs.ac.uns.ftn.xws.cbs.nalog_za_placanje.NalogZaPlacanje;
import rs.ac.uns.ftn.xws.cbs.zahtev_za_izvod.ZahtevZaIzvod;

public class BankState extends ProcessingState {
	
	private static int mt102Count = 0;
	private static final int maxMt102Count = 5;
	
	@Override
	public Object process(Mt102 mt102) throws JAXBException, IOException,
			SAXException, Exception {
		// TODO Auto-generated method stub
		return super.process(mt102);
	}

	@Override
	public Object process(Mt103 mt103) throws JAXBException, IOException,
			SAXException, Exception {
		// TODO Auto-generated method stub
		return super.process(mt103);
	}

	@Override
	public Object process(Mt900 mt900) throws JAXBException, IOException,
			SAXException, Exception {
		// TODO Auto-generated method stub
		return super.process(mt900);
	}

	@Override
	public Object process(Mt910 mt910) throws JAXBException, IOException,
			SAXException, Exception {
		// TODO Auto-generated method stub
		return super.process(mt910);
	}

	@Override
	public Object process(Izvod izvod) throws JAXBException, IOException,
			SAXException, Exception {
		// TODO Auto-generated method stub
		return super.process(izvod);
	}

	@Override
	public Object process(NalogZaPlacanje nalog) throws JAXBException,
			IOException, SAXException, Exception {
		// TODO Auto-generated method stub
		return super.process(nalog);
	}

	@Override
	public Object process(ZahtevZaIzvod zahtev) throws JAXBException,
			IOException, SAXException, Exception {
		// TODO Auto-generated method stub
		return super.process(zahtev);
	}

	@Override
	public void initialSetup(Properties properties) throws JAXBException, IOException, SAXException, Exception {
		
		BankaAppMapper bam = new BankaAppMapper();
		String swift = properties.getProperty("swift");
		String nazivAplikacije = properties.getProperty("war.name");
		
		bam.setIdSwiftBanke(swift);
		bam.setNazivAplikacije(nazivAplikacije);
		
		dbFacade.storeInDatabase(bam);
	}
	
	
}
