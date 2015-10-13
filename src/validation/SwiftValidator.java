package validation;

public class SwiftValidator {
	
	public static boolean validate(String swiftString) {
		
		if(swiftString == null) {
			throw new RuntimeException("MT10X invalid, no swift code available.");
		}
		
		int lenght = swiftString.length();
		boolean retVal=false;
		
		if(lenght==8) {
				
			String regex = "([A-Z]){6}([A-Z0-9]){2}";
			retVal=swiftString.matches(regex);
			
		}
		
		return retVal;
	}
}
