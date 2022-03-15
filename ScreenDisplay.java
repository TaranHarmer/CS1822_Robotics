import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class ScreenDisplay {
    
    //Displayed when the robot starts
    public static void displayStartScreen( ) { 
        LCD.clearDisplay();
        LCD.drawString("The Garbage  ", 0, 0);
        LCD.drawString("Collector Robot", 0, 1);
        Delay.msDelay(200);
        LCD.drawString("By: Luke, Kacper", 0, 2);
        LCD.drawString(" & Harveen!", 0, 3);
        Delay.msDelay(200);
        LCD.drawString("Press ENTER", 0, 4);
        }
    
    //Shows the robot is in search mode
    public static void displaySearching() { 
        LCD.clearDisplay();
        LCD.drawString("Searching for item...", 0, 0);
    }
    
    //Shows the robot has detecting and is trying to pick up item
    public static void displayFound( ) { 
        LCD.clearDisplay();
        LCD.drawString("Item is found", 0, 1);
        LCD.drawString("Location of item:" /*+ need to add robots location*/ , 0, 3);

    }
    
    //Shows the robot is lifting an object
    public static void displayPickUp() { 
        LCD.clearDisplay();
        LCD.drawString("Picking up item...", 0, 2);

    }
    
    //Shows the robot is dropping the object
    public static void displayDropDown() {
        LCD.clearDisplay();
        LCD.drawString("Dropping item...", 0, 2);
    }
    
    //Shows the robot is returning home
    public static void displayReturn( ) { 
        LCD.clearDisplay();
        LCD.drawString("Returning Home...", 4, 2);
    }
}