import lejos.hardware.Button;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class RotateCar implements Behavior {
	private MovePilot pilot;
	private EV3ColorSensor colorSensor; //0 = red, 1 = green, 2 = blue
	int[] colors; //0 = red, 1 = green, 2 = blue, thinks red is 0, thinks green is 1, thinks blue is 2
	float[] colorID; //0 = none, 2 = blue, 3 = green, 5 = red, 6 = white
 	public RotateCar(float[] colorThreshold, MovePilot pilot, EV3ColorSensor colorSensor) {
		this.pilot = pilot;
		this.colorSensor = colorSensor;
		this.colors = new int[3];
		this.colors[0] = 0;
		this.colors[1] = 1;
		this.colors[2] = 2;
		this.colorID = new float[1];
	}
	@Override
	public boolean takeControl() {
		this.colorSensor.getColorIDMode().fetchSample(colorID, 0);
		boolean hasColor = false;
		for (int ID : colors) {
			if (ID == (int) colorID[0]) {
				hasColor = true;
				break;
			}
		}
		return !(hasColor);
	}

	@Override
	public void action() {
		boolean detected = false;
		int count = 1;
		while (true) {
			if (detected) {
				break;
			}
			for (int i = 0; i < count && Button.ENTER.isUp(); i++) {
				this.colorSensor.getColorIDMode().fetchSample(colorID, 0);	
				Delay.msDelay(1000);
				for (int ID : colors) {
					if (ID == (int) colorID[0]) {
						detected = true;
						break;
					}
				}
				if (detected) {
					break;
				}
				this.pilot.rotate(10);
				Delay.msDelay(1000);
			}
			if (detected) {
				break;
			}
			for (int i = 0; i < (2*count) && Button.ENTER.isUp(); i++) {
				this.colorSensor.getColorIDMode().fetchSample(colorID, 0);
				Delay.msDelay(1000);
				for (int ID : colors) {
					if (ID == (int) colorID[0]) {
						detected = true;
						break;
					}
				}
				if (detected) {
					break;
				}
				this.pilot.rotate(-10);
				Delay.msDelay(1000);		
			}
			count++;
		}

	}

	@Override
	public void suppress() {
	}

}
