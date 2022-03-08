import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class LineFollower {
	
	static float[] level = new float[1];
	static EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S1);
	static SampleProvider colour = sensor.getRedMode();

	public static float Calibrate() {

		LCD.drawString("Press Enter Once Claibrated", 2, 0);
		
		float minColourLevel = 0f;
		float maxColourLevel = 1f;

		while(Button.ENTER.isUp()) {
			
			colour.fetchSample(level, 0);
			
			if(level[0] < minColourLevel) {
				minColourLevel = level[0];				
			}
			
			if(level[0] > maxColourLevel) {
				minColourLevel = level[0];
			}
			
		}
		
		LCD.drawString("Min: " + minColourLevel, 1 , 1);
		LCD.drawString("Max: " + maxColourLevel, 1 , 2);
		Delay.msDelay(1000);
		return ((minColourLevel + maxColourLevel)/2);

	}
	
	public static void main(String[] args) {
		
		BaseRegulatedMotor mLeft = new EV3LargeRegulatedMotor(MotorPort.A);
		BaseRegulatedMotor mRight = new EV3LargeRegulatedMotor(MotorPort.B);
		
		float calibratedColour = Calibrate();
		
		while(Button.ENTER.isUp()) {
			colour.fetchSample(level, 0);
			float testLight = level[0];
			LCD.drawString("Now = " + Float.toString(testLight), 1, 3);
			LCD.drawString("Avg = " + Float.toString(calibratedColour), 1, 4);
			if(testLight > calibratedColour) {
				mLeft.setSpeed(100);
				mRight.setSpeed(70);
				mLeft.forward();
				mRight.backward();
			}else {
				mRight.setSpeed(100);
				mLeft.setSpeed(70);
				mRight.forward();
				mLeft.backward();
			}
		}
		sensor.close();
		mLeft.close();
		mRight.close();		
	}

}