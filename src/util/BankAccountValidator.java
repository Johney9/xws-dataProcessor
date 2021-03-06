package util;

public class BankAccountValidator {
	public static boolean validate(String bankAccount) {
		
		int lenght = bankAccount.length();
		boolean retVal=false;
		
		if(lenght==18) {
			retVal=validator(bankAccount);
		}
		
		return retVal;
	}
	
	private static boolean validator(String bankAccount) {
		try {
			
			Integer.parseInt(bankAccount);
			String accountString = bankAccount.substring(0, 15);
			String checksumString = bankAccount.substring(16);
			
			int accountNumber = Integer.parseInt(accountString);
			int checksumNumber = Integer.parseInt(checksumString);
			
			int result = (98 - (accountNumber*100)%97)%97;
			
			if(result==checksumNumber) {
				return true;
			}
			
		} catch (NumberFormatException e) {
			//exit
		}
		
		return false;
	}
}
