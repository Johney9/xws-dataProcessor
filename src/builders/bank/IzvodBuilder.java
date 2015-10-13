package builders.bank;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import rs.ac.uns.ftn.xws.cbs.izvod.Izvod;
import rs.ac.uns.ftn.xws.cbs.izvod.IzvodStavka;
import rs.ac.uns.ftn.xws.cbs.izvod.IzvodZaglavlje;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102PojedinacniNalog;
import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import rs.ac.uns.ftn.xws.cbs.nalog_za_placanje.NalogZaPlacanje;
import rs.ac.uns.ftn.xws.cbs.zahtev_za_izvod.ZahtevZaIzvod;

public class IzvodBuilder {
	public static Izvod buildIzvod(ZahtevZaIzvod zahtev, Izvod izvod) throws JAXBException, SAXException, IOException, Exception {
		
		int presek = zahtev.getRedniBrojPreseka().intValue();
		
		Izvod retVal = new Izvod();
		
		retVal.setZaglavlje(izvod.getZaglavlje());
		retVal.getStavke().add(izvod.getStavke().get(presek));
		
		return retVal;
		//TODO: pokupi sve transakcije (iz jedne seme ili nekako drugacije) i strpaj u izvestaj.
		//dve opcije: raditi to u bazi, raditi programski. u bazi podrazumeva sastavljanje upita.
		//programski podrazumeva menjanje nacina na koji se zapisuju podaci, od toga ima dve opcije:
		//1) sema ce da sadrzi id + datum (otezava ocitavanje svih transakcija u okviru jednog racuna npr)
		//2) fajl ce da sadrzi ime fajla + datum (otezava citanje fajlova)
	}
	
	public static Izvod buildNewIzvod(String brojRacuna, NalogZaPlacanje nalog) {
		
		Izvod izvod = new Izvod();
		IzvodZaglavlje iz = new IzvodZaglavlje();
		izvod.setZaglavlje(iz);
		BigInteger bigNumber1 = BigInteger.valueOf(1);
		iz.setBrojPreseka(bigNumber1);
		
		iz.setBrojRacuna(brojRacuna);
		iz.setDatumNaloga(nalog.getDatumNaloga());

		iz.setUkupnoNaTeret(nalog.getIznos());
		iz.setUkupnoUKorist(BigDecimal.valueOf(0));
		iz.setBrojPromenaNaTeret(bigNumber1);
		iz.setBrojPromenaUKorist(BigInteger.valueOf(0));
		
		IzvodStavka is = new IzvodStavka();
		is.setDatumNaloga(nalog.getDatumNaloga());
		is.setDatumValute(nalog.getDatumValute());
		is.setDuznikNalogodavac(nalog.getDuznikNalogodavac());
		is.setIznos(nalog.getIznos());
		is.setModelOdobrenja(nalog.getModelOdobrenja());
		is.setModelZaduzenja(nalog.getModelZaduzenja());
		is.setPozivNaBrojOdobrenja(nalog.getPozivNaBrojOdobrenja());
		is.setPozivNaBrojZaduzenja(nalog.getPozivNaBrojZaduzenja());
		is.setPrimalacPoverilac(nalog.getPrimalacPoverilac());
		is.setRacundDuznika(nalog.getRacunDuznika());
		is.setRacunPoverioca(nalog.getRacunPoverioca());

		is.setSmer(determineSmer(-1));
		is.setSvrhaPlacanja(nalog.getSvrhaPlacanja());
		
		izvod.getStavke().add(new IzvodStavka());
		izvod.getStavke().add(is);
		
		return izvod;
	}
	
	public static Izvod buildNewIzvodWithMt102(Mt102 mt102) throws IOException {
		
		IzvodStavka is = new IzvodStavka();
		Mt102PojedinacniNalog nalog = mt102.getPojedinacniNalozi().get(0);
		
		Izvod izvod = new Izvod();
		IzvodZaglavlje iz = new IzvodZaglavlje();
		izvod.setZaglavlje(iz);
		BigInteger bigNumber1 = BigInteger.valueOf(1);
		iz.setBrojPreseka(bigNumber1);

		iz.setDatumNaloga(nalog.getDatumNaloga());
		iz.setUkupnoNaTeret(BigDecimal.valueOf(0));
		iz.setUkupnoUKorist(BigDecimal.valueOf(0));
		
		is.setDatumNaloga(nalog.getDatumNaloga());
		is.setDatumValute(mt102.getDatumValute());
		is.setDuznikNalogodavac(nalog.getDuznikNalogodavac());
		is.setIznos(nalog.getIznos());
		is.setModelOdobrenja(nalog.getModelOdobrenja());
		is.setModelZaduzenja(nalog.getModelZaduzenja());
		is.setPozivNaBrojOdobrenja(nalog.getPozivNaBrojOdobrenja());
		is.setPozivNaBrojZaduzenja(nalog.getPozivNaBrojZaduzenja());
		is.setPrimalacPoverilac(nalog.getPrimalacPoverilac());
		is.setRacundDuznika(nalog.getRacunDuznika());
		is.setRacunPoverioca(nalog.getRacunPoverioca());
		Integer signum = nalog.getIznos().signum();
		is.setSmer(determineSmer(signum));
		is.setSvrhaPlacanja(nalog.getSvrhaPlacanja());
		
		izvod.getStavke().add(new IzvodStavka());
		izvod.getStavke().add(is);
		iz.setBrojRacuna(determineRacun(signum, nalog.getRacunDuznika(), nalog.getRacunPoverioca()));
		setBrojPromenaInitial(izvod);
		
		return izvod;
	}
	
	public static Izvod buildNewIzvodWithMt103(Mt103 nalog) throws IOException {
		Izvod izvod = new Izvod();
		IzvodZaglavlje iz = new IzvodZaglavlje();
		izvod.setZaglavlje(iz);
		BigInteger bigNumber1 = BigInteger.valueOf(1);
		iz.setBrojPreseka(bigNumber1);
		iz.setDatumNaloga(nalog.getDatumNaloga());
		iz.setUkupnoNaTeret(BigDecimal.valueOf(0));
		iz.setUkupnoUKorist(BigDecimal.valueOf(0));
		
		IzvodStavka is = new IzvodStavka();
		
		is.setDatumNaloga(nalog.getDatumNaloga());
		is.setDatumValute(nalog.getDatumValute());
		is.setDuznikNalogodavac(nalog.getDuznikNalogodavac());
		is.setIznos(nalog.getIznos());
		is.setModelOdobrenja(nalog.getModelOdobrenja());
		is.setModelZaduzenja(nalog.getModelZaduzenja());
		is.setPozivNaBrojOdobrenja(nalog.getPozivNaBrojOdobrenja());
		is.setPozivNaBrojZaduzenja(nalog.getPozivNaBrojZaduzenja());
		is.setPrimalacPoverilac(nalog.getPrimalacPoverilac());
		is.setRacundDuznika(nalog.getRacunDuznika());
		is.setRacunPoverioca(nalog.getRacunPoverioca());
		Integer signum = nalog.getIznos().signum();
		is.setSmer(determineSmer(signum));
		is.setSvrhaPlacanja(nalog.getSvrhaPlacanja());
		
		izvod.getStavke().add(new IzvodStavka());
		izvod.getStavke().add(is);
		iz.setBrojRacuna(determineRacun(signum, nalog.getRacunDuznika(), nalog.getRacunPoverioca()));
		setBrojPromenaInitial(izvod);
		
		return izvod;
	}
	
	public static Izvod updateIzvod(Izvod updatee, Izvod updater) {
		Izvod retVal=updatee;
		IzvodZaglavlje updateeZaglavlje = updatee.getZaglavlje();
		IzvodZaglavlje updaterZaglavlje = updater.getZaglavlje();
		IzvodStavka updateeStavka = updatee.getStavke().get(1);
		for(IzvodStavka is : updater.getStavke()) {
			if(checkSameIzvod(updateeStavka, is)) updatee.getStavke().add(is);
		}
		updateeZaglavlje.setBrojPromenaNaTeret(updateeZaglavlje.getBrojPromenaNaTeret().add(updaterZaglavlje.getBrojPromenaNaTeret()));
		updateeZaglavlje.setBrojPromenaUKorist(updateeZaglavlje.getBrojPromenaUKorist().add(updaterZaglavlje.getBrojPromenaUKorist()));
		updateeZaglavlje.setNovoStanje(updateeZaglavlje.getNovoStanje().add(updaterZaglavlje.getNovoStanje()));
		updateeZaglavlje.setPrethodnoStanje(updateeZaglavlje.getPrethodnoStanje().add(updaterZaglavlje.getPrethodnoStanje()));
		updateeZaglavlje.setUkupnoNaTeret(updateeZaglavlje.getUkupnoNaTeret().add(updaterZaglavlje.getUkupnoNaTeret()));
		updateeZaglavlje.setUkupnoUKorist(updateeZaglavlje.getUkupnoUKorist().add(updaterZaglavlje.getUkupnoUKorist()));
		
		return retVal;
	}
	
	private static boolean checkSameIzvod(IzvodStavka updatee, IzvodStavka updater) {
		boolean retVal = false;
		if(updatee.getRacundDuznika().contains(updater.getRacundDuznika()) && 
				updatee.getRacunPoverioca().contains(updater.getRacunPoverioca()) &&
				updatee.getDatumNaloga().equals(updater.getDatumNaloga())) 
		{
			retVal=true;
		}
		return retVal;
	}
	
	private static String determineRacun(int signum, String racunDuznika, String racunPoverioca) throws IOException {
		switch(signum) {
		case	-1:		return racunDuznika;
		case	0:		throw new IOException("Invalid MT9X0 nalog.");
		case	1:		return racunPoverioca;
		}
		return null;
	}
	
	private static void setBrojPromenaInitial(Izvod izvod) {
		IzvodZaglavlje iz = izvod.getZaglavlje();
		int signum = izvod.getStavke().get(1).getIznos().signum();
		BigInteger bigOne = BigInteger.valueOf(1);
		BigInteger bigZero = BigInteger.valueOf(0);
		
		switch(signum) {
		
		case	-1:	iz.setBrojPromenaNaTeret(bigOne);iz.setBrojPromenaUKorist(bigZero);break;
		case	1:	iz.setBrojPromenaNaTeret(bigZero);iz.setBrojPromenaUKorist(bigOne);
		}
	}
	
	private static String determineSmer(int signum) {
		String retVal = "N";
		switch (signum) {
		case -1:	
			
			retVal="O";
			break;

		case 1:
			
			retVal="D";
			break;
		}
		return retVal;
	}
}
