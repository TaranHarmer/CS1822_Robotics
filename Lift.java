
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;


public class Lift
{
    public static void main(String[] args)
    {
    	int     button, angle = 0;
    
        Sound.beepSequenceUp();    	// makes sound when ready.

        Button.waitForAnyPress();
        
        // create a motor object to control the motor we are using
        EV3LargeRegulatedMotor moveMotor = new EV3LargeRegulatedMotor(MotorPort.C);
        
        do
        {
            button = Button.waitForAnyPress();
        	
            // Process any other buttons.
       	    switch (button)
            {
                // Down button causes arm to move down until zero position.
                case Button.ID_DOWN:
                    if (angle >= 10)
                    {
                        moveMotor.rotate(-10);
                        angle -= 10;
                    }
        			
                    break;
        			
                // Up button causes arm to move up until it is at 160 degrees
                case Button.ID_UP:
                    if (angle < 160)
                    {
                        moveMotor.rotate(10);
                        angle += 10;
                    }
        			
                    break;
            }
        	
        } while (!(button == Button.ID_ESCAPE));
        
        Button.waitForAnyPress();
        
        
        moveMotor.close();
        
        Sound.beepSequence();    // makes sound when done
    }
}

