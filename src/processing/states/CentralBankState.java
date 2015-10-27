package processing.states;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;

import org.xml.sax.SAXException;

import rs.ac.uns.ftn.xws.cbs.banka_app_mapper.BankaAppMapper;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102PojedinacniNalog;
import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import rs.ac.uns.ftn.xws.cbs.mt900.Mt900;
import rs.ac.uns.ftn.xws.cbs.mt910.Mt910;
import utility.MtCoupler;
import validation.BankExistenceValidator;
import validation.DataValidator;
import validation.GroupMt102Validator;
import validation.Mt102Validator;
import validation.Mt103Validator;
import builders.central_bank.Mt900Builder;
import builders.central_bank.Mt910Builder;

public class CentralBankState extends ProcessingState {

	protected DataValidator validator;
	
	//TODO: OBRACUN with obracunski racuni, after mt102 and mt103
	
	@Override
	public Object process(Mt102 mt102) throws JAXBException, IOException, SAXException, Exception {
		//add obracunski racun dealings
		//validation
		validator = new Mt102Validator();
		mt102=(Mt102) validator.validate(mt102);
		validator = new BankExistenceValidator(dbFacade);
		validator.validate(mt102.getSwiftKodBankeDuznika());
		validator.validate(mt102.getSwiftKodBankePoverioca());
		//every transaction is stored
		dbFacade.storeInDatabase(mt102,mt102.getIdPoruke());
		Mt102 groupedMt102 = mt102grouping(mt102);
		String currentSwift = mt102.getSwiftKodBankePoverioca();
		dbFacade.storeInDatabase(groupedMt102, currentSwift);

		return groupedMt102;
	}	
	
	@Override
	public Object process(Mt103 mt103) throws JAXBException, IOException, SAXException, Exception {
		
		//validation
		validator = new Mt103Validator();
		mt103=(Mt103) validator.validate(mt103);
		validator = new BankExistenceValidator(dbFacade);
		validator.validate(mt103.getSwiftKodBankeDuznika());
		validator.validate(mt103.getSwiftKodBankePoverioca());
		//every transaction is stored
		dbFacade.storeInDatabase(mt103);
		//build an mt910
		Mt910 mt910 = Mt910Builder.buildMt910(mt103);
		//build an mt900
		Mt900 mt900 = Mt900Builder.buildMt900(mt103);
		
		MtCoupler retVal = new MtCoupler(mt900, mt910);

		return retVal;
	}
	
	/**
	 *  Since everything is stored in one file anyway,
		try to read an MT102 with the given resourceId.
		If it's unsucessful, that means there is no file, so a new write is needed.
		If it's successful, an update of the database MT102 is needed.
	 * @param incomingMt102
	 * @return grouped MT102
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws IOException
	 * @throws Exception
	 */
	protected Mt102 mt102grouping(Mt102 incomingMt102) throws JAXBException, SAXException, IOException, Exception {
		Mt102 retVal = null;
		
		//Object fromDB=dbFacade.readFromDatabase(incomingMt102, incomingMt102.getSwiftKodBankePoverioca());
		
		//if(fromDB!=null) {
		try {
			Object fromDB=dbFacade.readFromDatabase(incomingMt102, incomingMt102.getSwiftKodBankePoverioca());

			Mt102 databaseMt102 = (Mt102) fromDB;
			validator = new GroupMt102Validator(databaseMt102);
			boolean isForSameBank = (Boolean) validator.validate(incomingMt102);
			
			if(isForSameBank) {
				for(Mt102PojedinacniNalog pojedinacniIncomingMt102 : incomingMt102.getPojedinacniNalozi()) {
					
					databaseMt102.getPojedinacniNalozi().add(pojedinacniIncomingMt102);
					BigDecimal oldUkupanIznos = databaseMt102.getUkupanIznos();
					BigDecimal pojedinacniIznos = pojedinacniIncomingMt102.getIznos();
					databaseMt102.setUkupanIznos(oldUkupanIznos.add(pojedinacniIznos));
				}
			}
			
			retVal=databaseMt102;
			
		} catch(NullPointerException e) /*else*/ {
			retVal = incomingMt102;
		} catch(UnmarshalException e) {
			retVal = incomingMt102;
		}

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
		bam.setIpAddress(Inet4Address.getLocalHost().getHostAddress());
		
		dbFacade.storeInDatabase(bam,bam.getIdSwiftBanke());
	}
	
}
