package main_project;
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

public class driver {
	final static float WHEEL_DIAMETER = 56; // The diameter (mm) of the wheels
	final static float AXLE_LENGTH = 123; // The distance (mm) your two driven wheels
	final static float ANGULAR_SPEED = 100; // How fast around corners (degrees/sec)
	final static float LINEAR_SPEED = 900;
	static float maxColourLevel = 0f;
	static float minColourLevel = 1f;
	static float minSoundLevel = 0;
	static float maxSoundLevel = 0;
	static float[] soundLevel;
 	static float[] colourLevel;
	static SampleProvider spColor;
	static SampleProvider spSound;
	public static void main(String[] args) {
		colourLevel = new float[3]; //0 = red, 1 = green, 2 = blue
		soundLevel = new float[1];
		EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);
		NXTSoundSensor sound = new NXTSoundSensor(SensorPort.S3);
		spSound = sound.getDBAMode();
		spColor = color.getRGBMode();
		float minSum = 0;
		float maxSum = 0;
		float colourAvg = 0;
		calibrateColour(0);
		minSum += minColourLevel;
		maxSum += maxColourLevel;
		calibrateColour(1);
		minSum += minColourLevel;
		maxSum += maxColourLevel;
		calibrateColour(2);
		minSum += minColourLevel;
		maxSum += maxColourLevel;
		colourAvg = (minSum + maxSum)/2;
		calibrateSound();
		float soundAvg = (minSoundLevel + maxSoundLevel)/2;
		EV3UltrasonicSensor us1 = new EV3UltrasonicSensor(SensorPort.S2);
		final SampleProvider sp = us1.getDistanceMode();
		BaseRegulatedMotor mL = new EV3LargeRegulatedMotor(MotorPort.A);
		Wheel wLeft = WheeledChassis.modelWheel(mL, WHEEL_DIAMETER).offset(-AXLE_LENGTH/2);
		BaseRegulatedMotor mR = new EV3LargeRegulatedMotor(MotorPort.D);
		Wheel wRight = WheeledChassis.modelWheel(mR, WHEEL_DIAMETER).offset(AXLE_LENGTH/2);
		Chassis chassis = new WheeledChassis((new Wheel[] {wRight, wLeft}),WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot pilot = new MovePilot(chassis);
		pilot.setLinearSpeed(LINEAR_SPEED);
		clapcolour clapColor = new clapcolour(soundAvg, sound, color, colourAvg, pilot);
		Behavior backup = new backup(pilot, sp);
		Behavior dark = new dark(pilot, sp);
		Behavior batteryLevel = new batterylevel(Battery.getVoltage());
		Behavior rotateCar = new rotatecar(colourAvg, pilot, color);
		Behavior[] ar = {clapColor, backup, dark, batteryLevel, rotateCar};
		Arbitrator ab = new Arbitrator(ar);
		ab.go();
	}
	
	public static void calibrateColour(int c) {
		while (Button.ENTER.isUp()) {
			spColor.fetchSample(colourLevel, 0);
			if (colourLevel[c] > maxColourLevel) {
				maxColourLevel = colourLevel[c];
				LCD.drawString("Mx Clr Lvl:" + Float.toString(maxColourLevel), 1, 1);
			}
			if (colourLevel[c] < minColourLevel) {
				minColourLevel = colourLevel[c];
				LCD.drawString("Mn Clr Lvl:" + Float.toString(minColourLevel), 1, 2);
			}
			LCD.clear();
		}
	}
	
	public static void calibrateSound() {
		while (Button.ENTER.isUp()) {
			
			spSound.fetchSample(soundLevel, 0);
			
			if((minSoundLevel == 0) & (maxSoundLevel == 0) ) {
				minSoundLevel = soundLevel[0];
				maxSoundLevel = soundLevel[0];				
			} 			
			
			if(soundLevel[0] < minSoundLevel) {
				minSoundLevel = soundLevel[0];
				LCD.drawString("Min Sound Lvl:" + Float.toString(minSoundLevel), 1, 1);
			}
			
			if(soundLevel[0] > maxSoundLevel) {
				maxSoundLevel = soundLevel[0];
				LCD.drawString("Max Sound Lvl:" + Float.toString(maxSoundLevel), 1, 2);
			}
			
		}
	}
		

}

