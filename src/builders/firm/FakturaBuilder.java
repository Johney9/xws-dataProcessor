package builders.firm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import rs.ac.uns.ftn.xws.cbs.faktura.Faktura;
import rs.ac.uns.ftn.xws.cbs.faktura.FakturaStavka;
import rs.ac.uns.ftn.xws.cbs.faktura.FakturaZaglavlje;

public class FakturaBuilder {
	
	private static final BigDecimal tax = new BigDecimal(18/100,new MathContext(2));
	
	public static Faktura buildPrice(Faktura faktura) {
		
		Faktura retVal = new Faktura();
		FakturaZaglavlje retZaglavlje = faktura.getZaglavlje();
		
		List<FakturaStavka> retStavke = retVal.getStavke();
		
		BigDecimal zgVrednost = BigDecimal.valueOf(0);
		BigDecimal zgPorez = BigDecimal.valueOf(0);
		BigDecimal zgRabat = BigDecimal.valueOf(0);
		BigDecimal zgUkupno = BigDecimal.valueOf(0);
		
		
		for(FakturaStavka stavka : faktura.getStavke()) {
			
			BigDecimal kolicina = stavka.getKolicina();
			BigDecimal cena = BigDecimal.valueOf(Math.random()%200000000);
			
			BigDecimal vrednost = cena.multiply(kolicina);
			BigDecimal procenatRabata = new BigDecimal(Math.random()%100, new MathContext(2));
			BigDecimal rabat = vrednost.multiply(procenatRabata.divide(BigDecimal.valueOf(100)));
			BigDecimal umanjenoZaRabat = vrednost.subtract(rabat);
			BigDecimal ukupanPorez = umanjenoZaRabat.multiply(tax);
			
			
			stavka.setProcenatRabata(procenatRabata);
			stavka.setJedinicnaCena(cena);
			stavka.setVrednost(vrednost);
			stavka.setUkupanPorez(ukupanPorez);
			stavka.setUmanjenoZaRabat(umanjenoZaRabat);
			
			retStavke.add(stavka);
			
			zgVrednost.add(vrednost);
			zgPorez.add(ukupanPorez);
			zgRabat.add(rabat);
			zgUkupno.add(umanjenoZaRabat);
		}
		
		retZaglavlje.setUkupanPorez(zgPorez);
		retZaglavlje.setUkupanRabat(zgRabat);
		retZaglavlje.setUkupnoRobaIUsluge(zgVrednost);
		retZaglavlje.setIznosZaUplatu(zgUkupno);
		
		retVal.setZaglavlje(retZaglavlje);
		
		return retVal;
	}
}
