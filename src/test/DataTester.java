package test;

import util.DataConstants;
import util.RacunBuilder;

public class DataTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(RacunBuilder.buildRacunChecksum(DataConstants.FIRMA_A_RACUN_NC));
		System.out.println(RacunBuilder.buildRacunChecksum(DataConstants.FIRMA_B_RACUN_NC));
		System.out.println(RacunBuilder.buildRacunChecksum(DataConstants.FIRMA_B_ISTA_RACUN_NC));
		System.out.println(RacunBuilder.buildRacunChecksum(DataConstants.BANKA_C_RACUN_NC));
		System.out.println(RacunBuilder.buildRacunChecksum(DataConstants.BANKA_D_RACUN_NC));
		
	}

}
