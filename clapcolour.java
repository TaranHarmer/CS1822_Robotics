package main_project;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
public class clapcolour implements Behavior 
{
	float soundThreshold;
	float colorThreshold;
	NXTSoundSensor soundSensor;
	EV3ColorSensor colorSensor;
	float[] soundLevel;
	float[] colorLevel; //0 = red, 1 = green, 2 = blue
	int clap;
	MovePilot pilot;
	
	
	public clapcolour(float soundThreshold, NXTSoundSensor soundSensor, EV3ColorSensor colorSensor, float colorThreshold, MovePilot pilot) {
		this.soundThreshold = soundThreshold;
		this.colorThreshold = colorThreshold;
		this.soundSensor = soundSensor;
		this.colorSensor = colorSensor;
		this.soundLevel = new float[1];
		this.colorLevel = new float[3];
		this.pilot = pilot;
		this.clap = 0;
	}
	
	@Override
	public boolean takeControl() {
		this.soundSensor.fetchSample(soundLevel, 0);
		this.colorSensor.getRGBMode().fetchSample(colorLevel, 0);
		if ((this.soundLevel[0] > this.soundThreshold) && (this.colorLevel[this.clap] > this.colorThreshold)) {
			if (this.clap == 0 || this.clap == 2) {
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
		while (this.colorLevel[this.clap] > this.colorThreshold) {
			this.pilot.forward();
		}
	}

	@Override
	public void suppress() {
		
	}

}
	
	

