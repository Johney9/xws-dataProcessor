package startup;

import java.util.Properties;

import facades.DatabaseWriter;
import rs.ac.uns.ftn.xws.cbs.banka_app_mapper.BankaAppMapper;
import rs.ac.uns.ftn.xws.cbs.firma_app_mapper.FirmaAppMapper;

public class StartupProcessor {
	
	
	public void setupFirm(Properties properties) {
		FirmaAppMapper fam = new FirmaAppMapper();
		String pib = properties.getProperty("pib");
		String nazivAplikacije = properties.getProperty("war.name");
		
		fam.setIdPibFirme(pib);
		fam.setNazivAplikacije(nazivAplikacije);
		
		DatabaseWriter<FirmaAppMapper> dbWriter = new DatabaseWriter<FirmaAppMapper>(fam);
		try {
			dbWriter.store();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setupBank(Properties properties) {
		BankaAppMapper bam = new BankaAppMapper();
		String swift = properties.getProperty("swift");
		String nazivAplikacije = properties.getProperty("war.name");
		
		bam.setIdSwiftBanke(swift);
		bam.setNazivAplikacije(nazivAplikacije);
		
		DatabaseWriter<BankaAppMapper> dbWriter = new DatabaseWriter<BankaAppMapper>(bam);
		try {
			dbWriter.store();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
