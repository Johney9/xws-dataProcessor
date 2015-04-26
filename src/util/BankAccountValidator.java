package util;

public class BankAccountValidator {
	
	public static boolean validate(String bankAccount) {
		
		boolean retVal=false;
		
		retVal=validateBankAccount(bankAccount);
		
		return retVal;
	}
	
	public static boolean sameBank(String bankAccountOne, String bankAccountTwo) {
		
		boolean retVal=false;
		boolean accountOneValid = validateBankAccount(bankAccountOne);
		boolean accountTwoValid = validateBankAccount(bankAccountTwo);
		
		if(accountOneValid && accountTwoValid) {
			
			String bankOneString = bankAccountOne.substring(0, 2);
			String bankTwoString = bankAccountTwo.substring(0, 2);
			
			int bankOne=Integer.parseInt(bankOneString);
			int bankTwo=Integer.parseInt(bankTwoString);
			
			if(bankOne==bankTwo) {
				retVal=true;
			}
		}
		
		return retVal;
	}
	
	private static boolean validateBankAccount(String bankAccount) {
		
		int length = bankAccount.length();
		
		if(length==18) {
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
		}
		return false;
	}
}
