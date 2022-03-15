import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class ClapColor implements Behavior {
	float soundThreshold;
	float[] colorID; //0 = none, 2 = blue, 3 = green, 5 = red, 6 = white
	NXTSoundSensor soundSensor;
	EV3ColorSensor colorSensor;
	float[] soundLevel;
	int[] colors; //0 = red, 1 = green, 2 = blue, thinks red is 0, thinks green is 1, thinks blue is 2
	int clap;
	MovePilot pilot;
	
	
	public ClapColor(float soundThreshold, NXTSoundSensor soundSensor, EV3ColorSensor colorSensor, MovePilot pilot) {
		this.soundThreshold = soundThreshold;
		this.colorID = new float[1];
		this.soundSensor = soundSensor;
		this.colorSensor = colorSensor;
		this.soundLevel = new float[1];
		this.colors = new int[3];
		this.colors[0] = 0;
		this.colors[1] = 1;
		this.colors[2] = 2;
		this.pilot = pilot;
		this.clap = -1;
	}
	
	@Override
	public boolean takeControl() {
		this.soundSensor.fetchSample(soundLevel, 0);
		this.colorSensor.getColorIDMode().fetchSample(colorID, 0);
		boolean detected = false;
		if (this.clap == -1 || this.clap == 3) {
			if (this.colors[0] == (int)this.colorID[0]) {
				detected = true;
			}
		} else {
			if (this.colors[this.clap] == (int)this.colorID[0]) {
				detected = true;
			}
		}
		if ((this.soundLevel[0] > this.soundThreshold) && detected) {
			if (this.clap == 3 || this.clap == -1) {
				this.clap = 0;
			} else {
				this.clap++;
			}
			return true;
		} else {
			return false;
		}		
	}

	@Override
	public void action() {
		LCD.drawString("Following " + Integer.toString(this.clap), 1, 3); //following 3 ??????
		Delay.msDelay(2000);
		while (this.colors[this.clap] == (int) this.colorID[0]) {
			this.colorSensor.fetchSample(colorID, 0);
			this.pilot.forward();
			Delay.msDelay(1000);
			this.pilot.stop();
			if (Button.ENTER.isDown()) {
				System.exit(0);
			}
		}
	}

	@Override
	public void suppress() {
		
	}

}
