package processing.states;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;
import rs.ac.uns.ftn.xws.cbs.mt103.Mt103;
import utility.NullClass;
import builders.central_bank.Mt900Builder;
import builders.central_bank.Mt910Builder;

public class CentralBankState extends ProcessingState {

	@Override
	public Object process(Mt102 mt102) throws JAXBException, IOException, SAXException, Exception {
		//every transaction is stored
		storeInDatabase(mt102);
		//build an mt910
		Mt910Builder.buildMt910(mt102);
		//build an mt900
		Mt900Builder.buildMt900(mt102);
		
		//TODO: validation, sending of mt900 to the sender of mt102, sending of mt910 to the reciever of the payment, grouping of multiple mt102's
		return new NullClass();
	}	
	
	@Override
	public Object process(Mt103 mt103) throws JAXBException, IOException, SAXException, Exception {
		//every transaction is stored
		storeInDatabase(mt103);
		//build an mt910
		Mt910Builder.buildMt910(mt103);
		//build an mt900
		Mt900Builder.buildMt900(mt103);
		
		//TODO: validation, sending of mt900 to the sender of mt102, sending of mt910 to the reciever of the payment
		return new NullClass();
	}
}
