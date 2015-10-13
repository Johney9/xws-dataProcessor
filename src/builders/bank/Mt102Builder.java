package builders.bank;

import java.util.List;

import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102PojedinacniNalog;
import rs.ac.uns.ftn.xws.cbs.nalog_za_placanje.NalogZaPlacanje;

public class Mt102Builder {
	
	public static Mt102 buildMt102(NalogZaPlacanje nalog, String swiftDuznika, String swiftPoverioca, String obracunskiRacunDuznika, String obracunskiRacunPoverioca) {
		
		//TODO finish (add fusing of multiple Mt102Pojedinacni into one Mt102
		Mt102 retVal = new Mt102();
		Mt102PojedinacniNalog pojedinacniNalog = new Mt102PojedinacniNalog();
		List<Mt102PojedinacniNalog> nalozi = retVal.getPojedinacniNalozi();
		
		retVal.setDatum(nalog.getDatumNaloga());
		retVal.setDatumValute(nalog.getDatumValute());
		retVal.setIdPoruke(nalog.getIdPoruke());
		retVal.setObracunskiRacunBankeDuznika(obracunskiRacunDuznika);
		retVal.setObracunskiRacunBankePoverioca(obracunskiRacunPoverioca);
		retVal.setSifraValute("189");
		retVal.setSwiftKodBankeDuznika(swiftDuznika);
		retVal.setSwiftKodBankePoverioca(swiftPoverioca);
		retVal.setUkupanIznos(nalog.getIznos());
		
		pojedinacniNalog.setDatumNaloga(nalog.getDatumNaloga());
		pojedinacniNalog.setDuznikNalogodavac(nalog.getDuznikNalogodavac());
		pojedinacniNalog.setIdNalogaZaPlacanje(nalog.getIdPoruke());
		pojedinacniNalog.setIznos(nalog.getIznos());
		pojedinacniNalog.setModelOdobrenja(nalog.getModelOdobrenja());
		pojedinacniNalog.setModelZaduzenja(nalog.getModelZaduzenja());
		pojedinacniNalog.setPozivNaBrojOdobrenja(nalog.getPozivNaBrojOdobrenja());
		pojedinacniNalog.setPozivNaBrojZaduzenja(nalog.getPozivNaBrojZaduzenja());
		pojedinacniNalog.setPrimalacPoverilac(nalog.getPrimalacPoverilac());
		pojedinacniNalog.setRacunDuznika(nalog.getRacunDuznika());
		pojedinacniNalog.setRacunPoverioca(nalog.getRacunPoverioca());
		pojedinacniNalog.setSifraValute("189");
		pojedinacniNalog.setSvrhaPlacanja(nalog.getSvrhaPlacanja());
		
		nalozi.add(pojedinacniNalog);
		
		return retVal;
	}
	
	public static Mt102 fuseMultipleMt102(Mt102 source, Mt102 target) {
		
		Mt102 retVal=null;
		
		for(Mt102PojedinacniNalog pojedinacni : source.getPojedinacniNalozi()) {
			
			String pojedinacniRacunDuznika = pojedinacni.getRacunDuznika();
			String pojedinacniRacunPoverioca = pojedinacni.getRacunPoverioca();
			
			if(target.getObracunskiRacunBankeDuznika().contains(pojedinacniRacunDuznika) &&
				target.getObracunskiRacunBankePoverioca().contains(pojedinacniRacunPoverioca)) 
			{
				
				target.getPojedinacniNalozi().add(pojedinacni);
				target.setUkupanIznos(target.getUkupanIznos().add(pojedinacni.getIznos()));
				
				retVal=target;
			}
			
		}
		
		return retVal;
	}
	
	
}
