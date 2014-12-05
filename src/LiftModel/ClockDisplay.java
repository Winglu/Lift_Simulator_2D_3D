package LiftModel;

import view2D.NumberDisplay;


/**
 * The ClockDisplay class implements a digital clock display for a
 * European-style 24 hour clock. The clock shows hours and minutes. The 
 * range of the clock is 00:00 (midnight) to 23:59 (one minute before 
 * midnight).
 * 
 * The clock display receives "ticks" (via the timeTick method) every minute
 * and reacts by incrementing the display. This is done in the usual clock
 * fashion: the hour increments when the minutes roll over to zero.
 * 
 * @author Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */
public class ClockDisplay
{
    private NumberDisplay hours;
    private NumberDisplay minutes;
    private NumberDisplay seconds;
    private String displayString;    // simulates the actual display
    
    /**
     * Constructor for ClockDisplay objects. This constructor 
     * creates a new clock set at 00:00.
     */
    public ClockDisplay()
    {
        hours = new NumberDisplay(24,null);
        minutes = new NumberDisplay(60,hours);
        seconds = new NumberDisplay(60,minutes);
        updateDisplay();
    }

    /**
     * Constructor for ClockDisplay objects. This constructor
     * creates a new clock set at the time specified by the 
     * parameters.
     * @param hour hour
     * @param minute minute
     * @param seconds second
     */
    public ClockDisplay(int hour, int minute, int seconds)
    {
        hours = new NumberDisplay(24,null);
        minutes = new NumberDisplay(60,hours);
        this.seconds = new NumberDisplay(60,minutes);
        setTime(hour, minute,seconds);
    }

    /**
     * This method should get called once every minute - it makes
     * the clock display go one minute forward.
     */
    public void timeTick()
    {
        seconds.increment();
        updateDisplay();
    }

    /**
     * Set the time of the display to the specified hour and
     * minute.
     * @param hour hour
     * @param minute minute
     * @param second second
     */
    public void setTime(int hour, int minute,int second)
    {
        hours.setValue(hour);
        minutes.setValue(minute);
        seconds.setValue(second);
        updateDisplay();
    }

    /**
     * Return the current time of this display in the format HH:MM.
     * @return  time
     */
    public String getTime()
    {
        return displayString;
    }
    
    /**
     * Update the internal string that represents the display.
     */
    private void updateDisplay()
    {
        displayString = hours.getDisplayValue() + ":" + 
                        minutes.getDisplayValue()+":"+seconds.getDisplayValue();
    }

    /**
     *
     * @return hours
     */
    public NumberDisplay getHours() {
        return hours;
    }

    /**
     *
     * @return minute
     */
    public NumberDisplay getMinutes() {
        return minutes;
    }

    /**
     *
     * @return second
     */
    public NumberDisplay getSeconds() {
        return seconds;
    }
    
    
}
