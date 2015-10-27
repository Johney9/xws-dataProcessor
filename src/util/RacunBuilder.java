package util;

import java.util.Random;

public class RacunBuilder {
	
	public static String buildRandomRacun(String brojBanke) {
		if(brojBanke.length()!=3) {
			throw new IllegalArgumentException("Parameter must be lenght of 3 numbers. " + brojBanke + " invalid.");
		}
		Integer.parseInt(brojBanke);
		Random rnd = new Random();
		String brojRacuna="";
		brojRacuna+=brojBanke;
		
		for(int i=0; i<13; i++) {
			brojRacuna+=rnd.nextInt()%9;
		}
		brojRacuna += ISO7064Mod97ChecksumBuilder.buildString(brojRacuna);
		
		return brojRacuna;
	}
	
	public static String buildRacunChecksum(String brojRacuna) {
		if(brojRacuna.length()!=16) {
			throw new IllegalArgumentException("Parameter must be lenght of 16 numbers. " + brojRacuna + " invalid.");
		}
		Long.parseLong(brojRacuna);
		
		brojRacuna+=ISO7064Mod97ChecksumBuilder.buildString(brojRacuna);
		
		return brojRacuna;
	}
}
