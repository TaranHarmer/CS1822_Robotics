package main_project;
import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class batterylevel implements Behavior {
	private boolean SUR = false;
	private float LOW_BAT = 6.1f;
	
	public batterylevel(float volts) {
		this.LOW_BAT = volts;
	}
	@Override
	public boolean takeControl() {
		float voltLev = Battery.getVoltage();
		return (voltLev < LOW_BAT);
	}

	@Override
	public void action() {
		SUR = false;
		while (!SUR && Button.ESCAPE.isUp()) {
			LCD.drawString("Warning: battery low", 1, 2);
			Sound.beep();
		}
	}

	@Override
	public void suppress() {
		this.SUR = true;
		
	}

}
