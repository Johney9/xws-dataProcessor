package validation;

import java.io.IOException;
import java.math.BigDecimal;

import javax.security.auth.login.AccountException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;

import facades.DatabaseFacade;
import rs.ac.uns.ftn.xws.cbs.korisnik.Korisnik;
import util.ISO7064Mod97ChecksumBuilder;

public class BankAccountValidator implements DataValidator {
	
	@Override
	public Boolean validate(Object brojRacuna) throws Exception {
		
		boolean retVal=false;
		String bankAccount = (String) brojRacuna;
		
		retVal=validateBankAccount(bankAccount);
		checkValidRacun(bankAccount);
		
		return retVal;
	}
	
	public static boolean sameBank(String bankAccountOne, String bankAccountTwo) throws AccountException {
		
		boolean retVal=false;
		boolean accountOneValid = validateBankAccount(bankAccountOne);
		boolean accountTwoValid = validateBankAccount(bankAccountTwo);

		if(accountOneValid && accountTwoValid) {
			
			String bankOneString = bankAccountOne.substring(0, 2);
			String bankTwoString = bankAccountTwo.substring(0, 2);
			
			long bankOne=Long.parseLong(bankOneString);
			long bankTwo=Long.parseLong(bankTwoString);
			
			if(bankOne==bankTwo) {
				retVal=true;
			}
		} else throw new AccountException("Bank account invalid");
		
		return retVal;
	}
	
	public static Korisnik validateAccountForTransfer(String bankAccount, BigDecimal iznos) throws AccountException {
		validateBankAccount(bankAccount);
		Korisnik duznik = checkValidRacun(bankAccount);
		checkSufficientFunds(duznik, iznos);
		return duznik;
	}
	
	public static void validateAccountExternal(String bankAccount) throws AccountException {
		validateBankAccount(bankAccount);
		checkValidRacun(bankAccount);
	}
	
	private static boolean validateBankAccount(String bankAccount) {
		
		int length = bankAccount.length();
		
		if(length==18) {
			try {
				
				Long.parseLong(bankAccount);
				String accountString = bankAccount.substring(0, 16);
				String checksumString = bankAccount.substring(16);
				
				long accountNumber = Long.parseLong(accountString);
				long checksumNumber = Long.parseLong(checksumString);
				
				long result = ISO7064Mod97ChecksumBuilder.build(accountNumber);
				
				if(result==checksumNumber) {
					return true;
				}
				
			} catch (NumberFormatException e) {
				//exit
			} 
		}
		return false;
	}
	
	public static Korisnik checkValidRacun(String racun) throws AccountException {
		
		Korisnik korisnik = new Korisnik();
		DatabaseFacade dbf = new DatabaseFacade();
		
		try {
			korisnik=dbf.readFromDatabase(new Korisnik(), racun);
		} catch (UnmarshalException e) {
			throw new AccountException("Account number: "+racun+" does not exist!");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return korisnik;
	}
	
	public static void checkSufficientFunds(Korisnik duznik, BigDecimal iznos) throws AccountException {
		BigDecimal stanje = duznik.getStanje();
		if(stanje.subtract(iznos).signum()<0) {
			throw new AccountException("Insufficient funds on account: "+duznik.getBrojRacuna());
		}
	}
}
