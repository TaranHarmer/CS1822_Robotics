import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ColourDetection {
	
	static EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S1);
	static SampleProvider colour = sensor.getRGBMode();
	static float[] level = new float[colour.sampleSize()];
	
	public static void main(String[] args) {
		LCD.drawString("Colour = ", 2, 2);
		while(Button.ENTER.isUp()) {
			colour.fetchSample(level, 0);
			if(level[0] > 0.1 && level[1] > 0.1 && level[2] >0.1) {
				LCD.drawString("WHITE", 2, 3);
			}else if(level[0] > level[1] && level[0] > level[2]) {
				LCD.drawString("RED", 2, 3);
			}else if(level[1] > level[0] && level[1] > level[2]) {
				LCD.drawString("GREEN", 2, 3);
			}else if(level[2] > level[0] && level[2] > level[1]) {
				LCD.drawString("BLUE", 2, 3);
			}Delay.msDelay(250);
			LCD.clear(2, 3, 5);
		}
	}
	
}
