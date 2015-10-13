package processing.states;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.util.Properties;

import javax.security.auth.login.AccountException;
import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import rs.ac.uns.ftn.xws.cbs.banka_app_mapper.BankaAppMapper;
import rs.ac.uns.ftn.xws.cbs.izvod.Izvod;
import rs.ac.uns.ftn.xws.cbs.korisnik.Korisnik;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import rs.ac.uns.ftn.xws.cbs.mt900.Mt900;
import rs.ac.uns.ftn.xws.cbs.mt910.Mt910;
import rs.ac.uns.ftn.xws.cbs.nalog_za_placanje.NalogZaPlacanje;
import rs.ac.uns.ftn.xws.cbs.zahtev_za_izvod.ZahtevZaIzvod;
import util.RacunBuilder;
import util.SwiftAndBankAccountLocator;
import validation.BankAccountValidator;
import validation.DataValidator;
import validation.NalogZaPlacanjeValidator;
import validation.ZahtevZaIzvodValidator;
import builders.bank.IzvodBuilder;
import builders.bank.Mt102Builder;
import builders.bank.Mt103Builder;
import facades.IdGeneratorFacade;

public class BankState extends ProcessingState {

	private BankaAppMapper bam;
	protected DataValidator validator;
	
	@Override
	public Object process(Mt102 mt102) throws JAXBException, IOException,
			SAXException, Exception {
		// do nothing
		return super.process(mt102);
	}

	@Override
	public Object process(Mt103 mt103) throws JAXBException, IOException,
			SAXException, Exception {
		// do nothing
		return super.process(mt103);
	}

	@Override
	public Object process(Mt900 nalog) throws JAXBException, IOException,
			SAXException, Exception {
		
		//TODO: TEST
		System.out.println("****Starting nalog processing.****");
		dbFacade.storeInDatabase(nalog,nalog.getIdPoruke());
		
		String idNaloga = nalog.getIdPoruke();
		String tipNaloga = nalog.getIdPorukeNaloga();
		BigDecimal iznos = nalog.getIznos();
		
		logAndStoreIzvod(idNaloga, tipNaloga, iznos);
		
		return true;
	}

	@Override
	public Object process(Mt910 nalog) throws JAXBException, IOException,
			SAXException, Exception {

		//TODO: TEST
		dbFacade.storeInDatabase(nalog,nalog.getIdPoruke());
		
		String idNaloga = nalog.getIdPoruke();
		String tipNaloga = nalog.getIdPorukeNaloga();
		BigDecimal iznos = nalog.getIznos();
		
		logAndStoreIzvod(idNaloga, tipNaloga, iznos);
		
		return true;
	}
	
	@Override
	public Object process(Izvod izvod) throws JAXBException, IOException,
			SAXException, Exception {
		//do nothing
		return super.process(izvod);
	}

	@Override
	public Object process(NalogZaPlacanje nalog) throws JAXBException,
			IOException, SAXException, Exception {
		
		//TODO: find a better way to identify if it's mt102 or mt103
		validator = new NalogZaPlacanjeValidator();
		nalog = (NalogZaPlacanje) validator.validate(nalog);
		
		dbFacade.storeInDatabase(nalog);
		
		boolean sameBank = determineIfSameBank(nalog);
		
		if(sameBank) {
			
			transferFundsLocally(nalog);
			
			return nalog;
		}
		else {
			SwiftAndBankAccountLocator duznik = new SwiftAndBankAccountLocator(nalog.getRacunDuznika());
			SwiftAndBankAccountLocator poverioc = new SwiftAndBankAccountLocator(nalog.getRacunPoverioca());
			
			String obracunskiRacunDuznika=duznik.locateBankAccount();
			String obracunskiRacunPoverioca=poverioc.locateBankAccount();
			String swiftDuznika=duznik.locateSwift();
			String swiftPoverioca=poverioc.locateSwift();
			
			if(nalog.isHitno()) {
				Mt103 mt103 = Mt103Builder.buildMt103(nalog, swiftDuznika, swiftPoverioca,obracunskiRacunDuznika, obracunskiRacunPoverioca);
				return mt103;
			}
			else {
				Mt102 mt102 = Mt102Builder.buildMt102(nalog, swiftDuznika, swiftPoverioca, obracunskiRacunDuznika, obracunskiRacunPoverioca);
				return mt102;
			}
		}
	}

	@Override
	public Object process(ZahtevZaIzvod zahtev) throws JAXBException,
			IOException, SAXException, Exception {
		
		validator = new ZahtevZaIzvodValidator();
		zahtev = (ZahtevZaIzvod) validator.validate(zahtev);
		
		dbFacade.storeInDatabase(zahtev);
		
		Izvod izvodOld = new Izvod();
		String izvodKey = generateIzvodKey(zahtev.getBrojRacuna(), zahtev.getDatum().toString());
		
		try {
			izvodOld = dbFacade.readFromDatabase(new Izvod(), izvodKey);
		} catch (NullPointerException e) {
			throw new RuntimeException("Izvod with key: "+izvodKey+"; racun: "+zahtev.getBrojRacuna()+", and date: "+zahtev.getDatum().toString()+", is not available.");
		}
		
		Izvod retVal = IzvodBuilder.buildIzvod(zahtev, izvodOld);
		
		return retVal;
	}

	private void logAndStoreIzvod(String idNaloga, String tipNaloga, BigDecimal iznos) throws JAXBException, SAXException, IOException, Exception {
		Izvod izvod = constructIzvod(tipNaloga, idNaloga);
		String brojRacuna = izvod.getZaglavlje().getBrojRacuna();
		izvod = transferFundsAndFinishIzvod(brojRacuna, iznos, izvod);
		izvod = combineWithExistingIzvod(brojRacuna, izvod);
		storeIzvod(brojRacuna, izvod);
	}
	
	private Izvod constructIzvod(String tipNaloga, String idNaloga) throws JAXBException, SAXException, IOException, Exception {
		Izvod izvodNew = new Izvod();
		if(tipNaloga.contains("MT103")) {
			Mt103 nalog = new Mt103();
			nalog = dbFacade.readFromDatabase(new Mt103(), idNaloga);
			izvodNew = IzvodBuilder.buildNewIzvodWithMt103(nalog);
			} else if (tipNaloga.contains("MT102")) {
			Mt102 mt102 = new Mt102();
			mt102 = dbFacade.readFromDatabase(new Mt102(), idNaloga);
			izvodNew = IzvodBuilder.buildNewIzvodWithMt102(mt102);
		}
		return izvodNew;
	}
	
	private Izvod transferFundsAndFinishIzvod(String racun, BigDecimal iznos, Izvod izvod) throws JAXBException, IOException, Exception {
		
		System.err.println("** trasfer izvod, param racun: "+racun);
		Korisnik korisnik = dbFacade.readFromDatabase(new Korisnik(), racun);
		
		BigDecimal stanje = korisnik.getStanje();
		izvod.getZaglavlje().setPrethodnoStanje(stanje);
		korisnik.setStanje(stanje.add(iznos));
		checkFundsOnAccount(korisnik);
		izvod.getZaglavlje().setNovoStanje(korisnik.getStanje());
		izvod.getZaglavlje().setBrojRacuna(racun);
		dbFacade.storeInDatabase(korisnik,racun);
		
		return izvod;
	}

	private Izvod combineWithExistingIzvod(String brojRacuna, Izvod izvod) throws JAXBException, SAXException, IOException, Exception {
		
		Izvod retVal = izvod;
		String izvodKey = generateIzvodKey(brojRacuna, izvod);
		
		try {
			Izvod izvodOld = dbFacade.readFromDatabase(izvod, izvodKey);
			retVal = IzvodBuilder.updateIzvod(izvodOld, izvod);
		} catch (NullPointerException e) {
			System.err.printf("Izvod with key: %s; racun: %s, and date: %s is not available.");
		}
		
		return retVal;
	}

	private void storeIzvod(String brojRacuna, Izvod izvod) throws JAXBException, IOException, SAXException, Exception {
		String key = generateIzvodKey(brojRacuna, izvod);
		dbFacade.storeInDatabase(izvod, key);
	}

	private String generateIzvodKey(String brojRacuna, Izvod izvod) {
		String key = "";
		String date = izvod.getZaglavlje().getDatumNaloga().toString();
		key = IdGeneratorFacade.generateIdXWS(brojRacuna,date);
		return key;
	}
	
	private String generateIzvodKey(String brojRacuna, String date) {
		String key = "";
		key = IdGeneratorFacade.generateIdXWS(brojRacuna,date);
		return key;
	}
	
	private boolean determineIfSameBank(NalogZaPlacanje nalog) throws AccountException {
		String racunDuznika = nalog.getRacunDuznika();
		String racunPoverioca = nalog.getRacunPoverioca();
		
		boolean sameBank = BankAccountValidator.sameBank(racunDuznika, racunPoverioca);
		
		return sameBank;
	}
	
	private void transferFundsLocally(NalogZaPlacanje nalog) throws JAXBException, IOException, Exception {
		
		String racunDuznika = nalog.getRacunDuznika();
		String racunPoverioca = nalog.getRacunPoverioca();
		
		Izvod izvodDuznika = IzvodBuilder.buildNewIzvod(racunDuznika, nalog);
		Izvod izvodPoverioca = IzvodBuilder.buildNewIzvod(racunPoverioca, nalog);
		izvodDuznika = transferFundsAndFinishIzvod(racunDuznika, nalog.getIznos().negate(), izvodDuznika);
		izvodPoverioca = transferFundsAndFinishIzvod(racunPoverioca, nalog.getIznos(), izvodPoverioca);
		
		izvodDuznika=combineWithExistingIzvod(generateIzvodKey(racunDuznika, izvodDuznika), izvodDuznika);
		izvodPoverioca=combineWithExistingIzvod(generateIzvodKey(racunPoverioca, izvodPoverioca), izvodPoverioca);
		
		storeIzvod(racunDuznika, izvodDuznika);
		storeIzvod(racunPoverioca, izvodPoverioca);
	}
	
	private void checkFundsOnAccount(Korisnik korisnik) throws AccountException {
		if(korisnik.getStanje().signum()<0) {
			throw new AccountException("Insufficient funds on account. ID: " +korisnik.getId()+" . Broj racuna: "+korisnik.getBrojRacuna());
		}
	}
	
	@Override
	public void initialSetup(Properties properties) throws JAXBException, IOException, SAXException, Exception {
		
		bam = new BankaAppMapper();
		String swift = properties.getProperty("swift");
		String nazivAplikacije = properties.getProperty("war.name");
		String brojRacuna = properties.getProperty("broj.racuna");
		brojRacuna = RacunBuilder.buildRacunChecksum(brojRacuna);
		
		bam.setIdSwiftBanke(swift);
		bam.setNazivAplikacije(nazivAplikacije);
		bam.setBrojRacuna(brojRacuna);
		bam.setIpAddress(Inet4Address.getLocalHost().getHostAddress());
		
		Korisnik kor = new Korisnik();
		kor.setBrojRacuna(brojRacuna);
		kor.setId(brojRacuna.substring(0, 3));
		kor.setStanje(BigDecimal.valueOf(100000));
		kor.setSwiftKodBankeVlasnice(swift);
		
		dbFacade.storeInDatabase(bam, bam.getIdSwiftBanke());
		dbFacade.storeInDatabase(kor, brojRacuna);
	}
}
