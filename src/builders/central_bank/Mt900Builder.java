package builders.central_bank;

import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import rs.ac.uns.ftn.xws.cbs.mt900.Mt900;

public class Mt900Builder {
	public static Mt900 buildMt900(Mt102 mt102) {
		//mt900
		Mt900 mt900 = new Mt900();
		mt900.setDatumValute(mt102.getDatumValute());
		mt900.setIdPoruke(mt102.getIdPoruke());
		mt900.setIdPorukeNaloga("MT102");
		mt900.setIznos(mt102.getUkupanIznos());
		mt900.setObracunskiRacunBankeDuznika(mt102.getObracunskiRacunBankeDuznika());
		mt900.setSifraValute(mt102.getSifraValute());
		mt900.setSwiftKodBankeDuznika(mt102.getSwiftKodBankeDuznika());
		return mt900;		
	}
	
	public static Mt900 buildMt900(Mt103 mt103) {
		//mt900
		Mt900 mt900 = new Mt900();
		mt900.setDatumValute(mt103.getDatumValute());
		mt900.setIdPoruke(mt103.getIdPoruke());
		mt900.setIdPorukeNaloga("MT103");
		mt900.setIznos(mt103.getIznos());
		mt900.setObracunskiRacunBankeDuznika(mt103.getObracunskiRacunBankeDuznika());
		mt900.setSifraValute(mt103.getSifraValute());
		mt900.setSwiftKodBankeDuznika(mt103.getSwiftKodBankeDuznika());
		
		return mt900;		
	}
}
