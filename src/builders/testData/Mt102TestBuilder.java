package builders.testData;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102PojedinacniNalog;
import util.DataConstants;
import util.ISO7064Mod97ChecksumBuilder;
import util.RacunBuilder;

public class Mt102TestBuilder {
	public static Mt102 buildPassTestMt102() {
		Mt102 target = new Mt102();
		
		XMLGregorianCalendar cal=null;
		try {
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
		} catch (DatatypeConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String racunDuznika = DataConstants.FIRMA_A_RACUN_NC;
		String racunPoverioca = DataConstants.FIRMA_B_ISTA_RACUN_NC;
		racunDuznika+=ISO7064Mod97ChecksumBuilder.buildString(racunDuznika);
		racunPoverioca+=ISO7064Mod97ChecksumBuilder.buildString(racunPoverioca);
		
		target.setDatum(cal);
		target.setDatumValute(cal);
		target.setIdPoruke(UUID.randomUUID().toString());
		target.setObracunskiRacunBankeDuznika(RacunBuilder.buildRacunChecksum(DataConstants.BANKA_C_RACUN_NC));
		target.setObracunskiRacunBankePoverioca(RacunBuilder.buildRacunChecksum(DataConstants.BANKA_C_RACUN_NC));
		target.setSifraValute("DIN");
		target.setSwiftKodBankeDuznika("XWSBRS01");
		target.setSwiftKodBankePoverioca("XWSBRS01");
		target.setUkupanIznos(BigDecimal.valueOf(0));
		
		for(int i=0; i<2; i++) {
			
			Mt102PojedinacniNalog e = new Mt102PojedinacniNalog();
			BigDecimal cena = BigDecimal.valueOf(169);
			
			e.setDatumNaloga(cal);
			e.setDuznikNalogodavac("MALOLETNICE INC");
			e.setIdNalogaZaPlacanje(i+":KECA");
			e.setIznos(cena);
			e.setModelOdobrenja("93");
			e.setModelZaduzenja("70");
			e.setPozivNaBrojOdobrenja("99999999999999999999");
			e.setPozivNaBrojZaduzenja("77777777777777777777");
			e.setPrimalacPoverilac("KECA INDUSTRIES");
			e.setRacunDuznika(racunDuznika);
			e.setRacunPoverioca(racunPoverioca);
			e.setSifraValute("123");
			e.setSvrhaPlacanja("ODSTETA");
			
			BigDecimal ukupno = target.getUkupanIznos();
			target.setUkupanIznos(ukupno.add(cena));
			target.getPojedinacniNalozi().add(e);
		}
		return target;
	}
}
