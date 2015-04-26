package processing.states;

import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import rs.ac.uns.ftn.xws.cbs.banka_app_mapper.BankaAppMapper;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import rs.ac.uns.ftn.xws.cbs.mt900.Mt900;
import rs.ac.uns.ftn.xws.cbs.mt910.Mt910;
import utility.MtCoupler;
import validation.Mt102Validator;
import validation.Mt103Validator;
import builders.central_bank.Mt900Builder;
import builders.central_bank.Mt910Builder;

public class CentralBankState extends ProcessingState {

	@Override
	public Object process(Mt102 mt102) throws JAXBException, IOException, SAXException, Exception {
		
		//validation
		Mt102Validator validator = new Mt102Validator();
		mt102=validator.validate(mt102);
		//every transaction is stored
		dbFacade.storeInDatabase(mt102);
		//build an mt910
		Mt910 mt910 = Mt910Builder.buildMt910(mt102);
		//build an mt900
		Mt900 mt900 = Mt900Builder.buildMt900(mt102);
		
		MtCoupler retVal = new MtCoupler(mt900, mt910);
		
		//TODO: grouping of multiple mt102's (idea: store all of them in the db, then after a set interval or number of calls, read from the db and then merge them)
		return retVal;
	}	
	
	@Override
	public Object process(Mt103 mt103) throws JAXBException, IOException, SAXException, Exception {
		
		//validation
		Mt103Validator validator = new Mt103Validator();
		mt103=validator.validate(mt103);
		//every transaction is stored
		dbFacade.storeInDatabase(mt103);
		//build an mt910
		Mt910 mt910 = Mt910Builder.buildMt910(mt103);
		//build an mt900
		Mt900 mt900 = Mt900Builder.buildMt900(mt103);
		
		MtCoupler retVal = new MtCoupler(mt900, mt910);

		return retVal;
	}

	@Override
	public void initialSetup(Properties properties) throws JAXBException,
			IOException, SAXException, Exception {

		BankaAppMapper bam = new BankaAppMapper();
		String swift = properties.getProperty("swift");
		String nazivAplikacije = properties.getProperty("war.name");
		
		bam.setIdSwiftBanke(swift);
		bam.setNazivAplikacije(nazivAplikacije);
		
		dbFacade.storeInDatabase(bam);
	}
	
}
