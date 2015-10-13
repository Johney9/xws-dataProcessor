package util;

import processing.states.BankState;
import processing.states.ProcessingState;
import rs.ac.uns.ftn.xws.cbs.mt102.Mt102;


public class CountDownTimer extends Thread {

	private ProcessingState state;
	private static final int TIMER_LENGHT=10*100; //ms
	
	public CountDownTimer(BankState state) {
		this.state=state;
	}
	
	@Override
	public void run() {
		try {
			this.sleep(TIMER_LENGHT);
			try {
				state.process(new Mt102());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			
		}
	}

}
