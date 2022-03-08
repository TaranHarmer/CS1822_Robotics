package main_project;

import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class dark implements Behavior {
	
	
	private MovePilot pilot;
	private SampleProvider sp;
	static float[] level = new float[1];
	
	
	public dark(MovePilot pilot, SampleProvider sp) {
		this.sp = sp;
		this.pilot = pilot;
	}	
	
	public boolean takeControl() {
		sp.fetchSample(level, 0);
		return !(pilot.getLinearSpeed() < 100 && level[0] < 0.5f);
	}

	public void action() {
		pilot.setAngularSpeed(50);
		
	}

	public void suppress() {		
	}
}