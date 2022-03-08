package main_project;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

class backup implements Behavior {
	float distanceValue = 10000;
	float [] distance = new float[2];
	private MovePilot pilot;
	private SampleProvider sp;
	public backup(MovePilot pilot, SampleProvider sp) {
		this.sp = sp;
		this.pilot = pilot;
	}
	public boolean takeControl() {
		sp.fetchSample(distance, 0);
        distanceValue = distance[0];
        return distanceValue <= 0.1;
	}
	public void action() {
		pilot.travel(-200f);
		int temp = (Math.random() <= 0.5) ? 1 : 2;
		if(temp == 1) {pilot.rotate(90);}
		if(temp == 2) {pilot.rotate(-90);}
	}
	public void suppress() {
	}

}