package util;

public class ISO7064Mod97ChecksumBuilder {
	public static long build(long number) {
		return (98 - (number*100)%97)%97;
	}
	
	public static long build(String number) {
		long accountNumber = Long.parseLong(number);
		return (98 - (accountNumber*100)%97)%97;
	}
	
	public static String buildString(long number) {
		Long i = (98 - (number*100)%97)%97;
		if(i<10) {
			return "0"+i;
		}
		return i.toString();
	}
	
	public static String buildString(String number) {
		long accountNumber = Long.parseLong(number);
		Long i = (98 - (accountNumber*100)%97)%97;
		if(i<10) {
			return "0"+i;
		}
		return i.toString();
	}
}
