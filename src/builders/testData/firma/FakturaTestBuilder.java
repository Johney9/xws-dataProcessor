package builders.testData.firma;

import java.math.BigDecimal;
import java.math.BigInteger;

import rs.ac.uns.ftn.xws.cbs.faktura.Faktura;
import rs.ac.uns.ftn.xws.cbs.faktura.FakturaStavka;
import rs.ac.uns.ftn.xws.cbs.faktura.FakturaZaglavlje;
import util.DataConstants;
import util.RacunBuilder;

public class FakturaTestBuilder {
	protected Faktura faktura;
	protected FakturaZaglavlje fz;
	protected FakturaStavka fs;
	
	public Faktura buildPass() {
		initialise();
		StackTraceElement ste=Thread.currentThread().getStackTrace()[1];
		System.out.printf("$$$ class: %s, method: %s %n",ste.getClassName(),ste.getMethodName());
		
		for (int i = 0; i < 5; i++) {
			FakturaStavka fs = new FakturaStavka();
			fs.setNazivRobeIliUsluge(i+" sargarepa");
			fs.setKolicina(new BigDecimal(100));
			fs.setJedinicaMere("gram");
			fs.setRedniBroj(BigInteger.valueOf(i));
			faktura.getStavke().add(fs);
		}
		
		FakturaZaglavlje fz = new FakturaZaglavlje();
		fz.setBrojRacuna(RacunBuilder.buildRacunChecksum(DataConstants.FIRMA_B_RACUN_NC));
		fz.setAdresaKupca("bez adrese");
		fz.setNazivKupca("kupac");
		fz.setPibKupca(DataConstants.FIRMA_A_PIB);
		fz.setIdPoruke("firma a prolaz");
		fz.setPibDobavljaca(DataConstants.FIRMA_B_PIB);
		fz.setNazivDobavljaca("dobavljac");
		fz.setAdresaDobavljaca("dobavljacka bb");
		
		faktura.setZaglavlje(fz);
		
		return faktura;
	}
	
	public Faktura buildFailInvalidData() {
		initialise();
		StackTraceElement ste=Thread.currentThread().getStackTrace()[1];
		System.out.printf("$$$ class: %s, method: %s %n",ste.getClassName(),ste.getMethodName());
		
		return faktura;
	}
	
	private void initialise() {
		faktura = new Faktura();
		fz = new FakturaZaglavlje();
		//fs = new FakturaStavka();
		faktura.setZaglavlje(fz);
		//faktura.getStavke().add(fs);
	}
}
