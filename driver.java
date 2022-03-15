import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.remote.ev3.EV3Reply;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class Driver {
	final static float WHEEL_DIAMETER = 56; // The diameter (mm) of the wheels
	final static float AXLE_LENGTH = 123; // The distance (mm) your two driven wheels
	final static float ANGULAR_SPEED = 70; // How fast around corners (degrees/sec)
	final static float LINEAR_SPEED = 900;
	static float maxColourLevel = 1f;
	static float minColourLevel = 0f;
	static float[] avgColourLevels;
	static float[] colourID; //0 = none, 2 = blue, 3 = green, 5 = red, 6 = white, thinks red is 0, thinks green is 1, thinks blue is 2
	static float minSoundLevel = 0;
	static float maxSoundLevel = 0;
	static float[] soundLevel;
 	static float[] colourLevel;
	static SampleProvider spColor;
	static SampleProvider spSound;
	public static void main(String[] args) {
		colourLevel = new float[3]; //0 = red, 1 = green, 2 = blue
		soundLevel = new float[1];
		avgColourLevels = new float[3];
		colourID = new float[1]; //0 = none, 2 = blue, 3 = green, 5 = red
		EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);
		NXTSoundSensor sound = new NXTSoundSensor(SensorPort.S3);
		EV3UltrasonicSensor us1 = new EV3UltrasonicSensor(SensorPort.S2);
		final SampleProvider sp = us1.getDistanceMode();
		BaseRegulatedMotor mL = new EV3LargeRegulatedMotor(MotorPort.A);
		Wheel wLeft = WheeledChassis.modelWheel(mL, WHEEL_DIAMETER).offset(-AXLE_LENGTH/2);
		BaseRegulatedMotor mR = new EV3LargeRegulatedMotor(MotorPort.D);
		Wheel wRight = WheeledChassis.modelWheel(mR, WHEEL_DIAMETER).offset(AXLE_LENGTH/2);
		Chassis chassis = new WheeledChassis((new Wheel[] {wRight, wLeft}),WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot pilot = new MovePilot(chassis);
		pilot.setLinearSpeed(LINEAR_SPEED);
		pilot.setAngularSpeed(ANGULAR_SPEED);
		spSound = sound.getDBAMode();
		spColor = color.getColorIDMode();
		calibrateColour();
		Delay.msDelay(1000);
		calibrateSound();
		float soundAvg = (minSoundLevel + maxSoundLevel)/2;
		Behavior clapColor = new ClapColor(soundAvg, sound, color, colourID, pilot);
		Behavior backup = new Backup(pilot, sp);
		Behavior dark = new Dark(pilot, sp);
		Behavior batteryLevel = new BatteryLevel(Battery.getVoltage());
		Behavior rotateCar = new RotateCar(avgColourLevels, pilot, color);
		Behavior interrupt = new Interrupt();
		Behavior[] ar = {rotateCar, clapColor, interrupt};
		Arbitrator ab = new Arbitrator(ar);
		ab.go();
		us1.close();
	}
	
	public static void calibrateColour() {
		LCD.drawString("Press Enter Once Calibrated", 2, 0);
		Delay.msDelay(1000);
		while (Button.ENTER.isUp()) {
			spColor.fetchSample(colourID, 0);
			LCD.drawInt((int) colourID[0], 1, 3);
			Delay.msDelay(1000);
			LCD.clear();
		}
		LCD.drawString("Calibrated", 1, 1);
		Delay.msDelay(1000);
		LCD.clear();
	}
	
	public static void calibrateSound() {
		LCD.drawString("Press Enter Once Calibrated", 2, 0);
		Delay.msDelay(1000);
		while (Button.ENTER.isUp()) {
			
			spSound.fetchSample(soundLevel, 0);
			
			if((minSoundLevel == 0) && (maxSoundLevel == 0) ) {
				minSoundLevel = soundLevel[0];
				maxSoundLevel = soundLevel[0];				
			} 			
			
			if(soundLevel[0] < minSoundLevel) {
				minSoundLevel = soundLevel[0];
				LCD.drawString("Min Sound Lvl:" + Float.toString(minSoundLevel), 1, 3);
			}
			
			if(soundLevel[0] > maxSoundLevel) {
				maxSoundLevel = soundLevel[0];
				LCD.drawString("Max Sound Lvl:" + Float.toString(maxSoundLevel), 1, 4);
			}
			
		}
		LCD.drawString("Calibrated", 1, 1);
		Delay.msDelay(1000);
		LCD.clear();
	}
		

}
