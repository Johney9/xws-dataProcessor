package util;

public class SwiftValidator {
	
	public static boolean validate(String swiftString) {
		
		int lenght = swiftString.length();
		boolean retVal=false;
		
		if(lenght==8) {
				
			String regex = "[A-Z]{6}[A-Z0-9]{2}";
			retVal=swiftString.matches(regex);
			
		}
		
		return retVal;
	}
}
