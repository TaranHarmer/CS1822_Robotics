import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;

public class Interrupt implements Behavior {

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return Button.LEFT.isDown();
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		System.exit(0);
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
	}

}
