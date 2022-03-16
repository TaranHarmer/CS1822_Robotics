import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColourDetection {
	
	static EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S1);
	static SampleProvider colour = sensor.getRGBMode();
	static float[] level = new float[colour.sampleSize()];
	
	public static void main(String[] args) {
		while(Button.ENTER.isUp()) {
			colour.fetchSample(level, 0);
			if(level[0] > level[1] && level[0] > level[2]) {
				LCD.drawString("Colour = RED", 2, 2);
			}else if(level[1] > level[0] && level[1] > level[2]) {
				LCD.drawString("Colour = GREEN", 2, 2);
			}else if(level[2] > level[0] && level[2] > level[1]) {
				LCD.drawString("Colour = BLUE", 2, 2);
			}
		}
	}
	
}
