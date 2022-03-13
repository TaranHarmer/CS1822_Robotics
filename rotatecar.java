package main_project;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class rotatecar implements Behavior {
	private float colorThreshold;
	private MovePilot pilot;
	private EV3ColorSensor colorSensor; //0 = red, 1 = green, 2 = blue
	private float[] colorLevel;
 	public rotatecar(float colorThreshold, MovePilot pilot, EV3ColorSensor colorSensor) {
		this.colorThreshold = colorThreshold;
		this.pilot = pilot;
		this.colorSensor = colorSensor;
		this.colorLevel = new float[3];
	}
	@Override
	public boolean takeControl() {
		this.colorSensor.getRGBMode().fetchSample(colorLevel, 0);
		boolean hasColor = false;
		for (float level : colorLevel) {
			if (level > this.colorThreshold) {
				hasColor = true;
			}
		}
		if (hasColor) {
			return false;
		}
		return true;
	}

	@Override
	public void action() {
		boolean detected = false;
		int count = 0;
		while (true && Button.ENTER.isUp() && count < 6) {
			this.pilot.rotate(10);
			this.colorSensor.getRGBMode().fetchSample(colorLevel, 0);	
			if (this.colorLevel[0] > this.colorThreshold) {
				detected = true;
				break;
			}
			if (this.colorLevel[1] > this.colorThreshold) {
				detected = true;
				break;
			}
			if (this.colorLevel[2] > this.colorThreshold) {
				detected = true;
				break;
			}
			count++;
		}
		count = 0;
		while (true && Button.ENTER.isUp() && count < 12 && !detected) {
			this.pilot.rotate(-10);
			this.colorSensor.getRGBMode().fetchSample(colorLevel, 0);	
			if (this.colorLevel[0] > this.colorThreshold) {
				break;
			}
			if (this.colorLevel[1] > this.colorThreshold) {
				break;
			}
			if (this.colorLevel[2] > this.colorThreshold) {
				break;
			}
			count++;
		}
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
