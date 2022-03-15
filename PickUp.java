import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

public class PickUp implements Behavior{

    private State itemLocation;
    private MovePilot pilot;
    private Navigator nav;
    
    public PickUp(Navigator nav, State itemLocation) {
        this.nav = nav;
        pilot = (MovePilot)nav.getMoveController();
        this.itemLocation = itemLocation;
    }
    
    @Override
    public boolean takeControl() {
        return (itemLocation.getState() == States.F && !pilot.isMoving());
    }

    @Override
    public void action() {
        ScreenDisplay.displayFound();
        while (!nav.rotateTo(180)); {}
        pilot.setLinearSpeed(30);
        pilot.backward();
    }

    @Override
    public void suppress() {
    }

}