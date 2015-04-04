package builders.central_bank;

import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import rs.ac.uns.ftn.xws.cbs.mt910.Mt910;

public class Mt910Builder {
	
	public static Mt910 buildMt910(Mt103 mt103) {
		//mt910
		Mt910 mt910 = new Mt910();
		mt910.setDatumValute(mt103.getDatumValute());
		mt910.setIdPoruke(mt103.getIdPoruke());
		mt910.setIdPorukeNaloga("MT103");
		mt910.setIznos(mt103.getIznos());
		mt910.setObracunskiRacunBankePoverioca(mt103.getObracunskiRacunBankePoverioca());
		mt910.setSifraValute(mt103.getSifraValute());
		mt910.setSwiftKodBankePoverioca(mt103.getSwiftKodBankePoverioca());
		return mt910;
	}
	
	
	
	public static Mt910 buildMt910(Mt102 mt102) {
		//mt910
		Mt910 mt910 = new Mt910();
		mt910.setDatumValute(mt102.getDatumValute());
		mt910.setIdPoruke(mt102.getIdPoruke());
		mt910.setIdPorukeNaloga("MT102");
		mt910.setIznos(mt102.getUkupanIznos());
		mt910.setObracunskiRacunBankePoverioca(mt102.getObracunskiRacunBankePoverioca());
		mt910.setSifraValute(mt102.getSifraValute());
		mt910.setSwiftKodBankePoverioca(mt102.getSwiftKodBankePoverioca());
		return mt910;
	}	
}
