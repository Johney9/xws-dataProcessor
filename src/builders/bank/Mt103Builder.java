package builders.bank;

import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import rs.ac.uns.ftn.xws.cbs.nalog_za_placanje.NalogZaPlacanje;

public class Mt103Builder {

	public static Mt103 buildMt103(NalogZaPlacanje nalog, String swiftDuznika, String swiftPoverioca, String obracunskiRacunDuznika, String obracunskiRacunPoverioca) {
		
		Mt103 retVal = new Mt103();
		
		retVal.setDatumNaloga(nalog.getDatumNaloga());
		retVal.setDatumValute(nalog.getDatumValute());
		retVal.setIdPoruke(nalog.getIdPoruke());
		retVal.setObracunskiRacunBankeDuznika(obracunskiRacunDuznika);
		retVal.setObracunskiRacunBankePoverioca(obracunskiRacunPoverioca);
		retVal.setSifraValute("189");
		retVal.setSwiftKodBankeDuznika(swiftDuznika);
		retVal.setSwiftKodBankePoverioca(swiftPoverioca);
		retVal.setIznos(nalog.getIznos());
		retVal.setModelOdobrenja(nalog.getModelOdobrenja());
		retVal.setModelZaduzenja(nalog.getModelZaduzenja());
		retVal.setDuznikNalogodavac(nalog.getDuznikNalogodavac());
		retVal.setPozivNaBrojOdobrenja(nalog.getPozivNaBrojOdobrenja());
		retVal.setPozivNaBrojZaduzenja(nalog.getPozivNaBrojZaduzenja());
		retVal.setPrimalacPoverilac(nalog.getPrimalacPoverilac());
		retVal.setRacunDuznika(nalog.getRacunDuznika());
		retVal.setRacunPoverioca(nalog.getRacunPoverioca());
		retVal.setSvrhaPlacanja(nalog.getSvrhaPlacanja());
		
		return retVal;
	}
}
