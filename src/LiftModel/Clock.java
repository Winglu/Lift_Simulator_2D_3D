package LiftModel;

import java.awt.Dimension;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * A very simple GUI (graphical user interface) for the clock display.
 * In this implementation, time runs at about 3 minutes per second, so that
 * testing the display is a little quicker.
 * 
 * Lu Chen implements thread interface and add seconds display to the original works
 * 
 * @author Michael Kölling and David J. Barnes and Lu Chen
 * @version 1.1
 */
public class Clock extends JPanel implements Runnable
{
    private JLabel label;
    private ClockDisplay clock;
    private boolean clockRunning = false;
    private Thread timerThread;

    /**
     *
     */
    public static float speedFactor;
    private volatile boolean pauseWorking = false;  
    private final Object pauseLock = new Object();
    private static long rTime = 1000;
    private static long time = 1000;
    /**
     * Constructor for objects of class Clock
     */
    public Clock()
    {
        makeFrame();
        clock = new ClockDisplay();
        speedFactor = 1;
    }

    /**
     *
     * @return clock
     */
    public ClockDisplay getClok(){
        return this.clock;
    }

    /**
     *
     * @param num speed factor
     */
    public static void setSpeed(float num){
        time = (long) (rTime/num);
    }
    /**
     * start
     */
    public void start()
    {
        if(timerThread == null){
            clockRunning = true;
            timerThread = new Thread(this);
            timerThread.start();
        
        }else{
            timerThread.interrupt();
        }
        
    }
    

    
    /**
     * stop
     */
    private void end()
    {
        clockRunning = false;
    }
    
    /**
     * second plus 1
     */
    private void step()
    {
        clock.timeTick();
        label.setText(clock.getTime());
    }
    
    
    /**
     * Quit function: quit the application.
     */
    private void quit()
    {
        System.exit(0);
    }

    
    /**
     * Create the Swing frame and its content.
     */
    private void makeFrame()
    {
        //makeMenuBar(frame);
        
        // Specify the layout manager with nice spacing
        //setLayout(new BorderLayout(1, 1));
        
        // Create the image pane in the center
        label = new JLabel("00:00:00", SwingConstants.LEFT);
        Font displayFont = label.getFont().deriveFont(20.0f);
        label.setFont(displayFont);
        //imagePanel.setBorder(new EtchedBorder());
        add(label);
        this.setMinimumSize(new Dimension(270,100));
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(
                       loweredetched, "System Time");
        title.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(title);
        //this.setSize(new Dimension(100,100));
    }

    /**
     * stop
     */
    public void pause()  {  
        pauseWorking = true;  
    }

    /**
     * continue
     */
    public void continueWork(){  
     pauseWorking = false;  

        synchronized(pauseLock)  
        {  
             pauseLock.notify();  
         }  
    } 

    

    public void run()
    {
        while (clockRunning) {
            synchronized(pauseLock){
                while (pauseWorking){  
                    try {
                        pauseLock.wait(); //pause scheduler till told to continue  
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 

            }

                step();
                pauseW();
        }
    }

        
        
    private void pauseW()
    {
        try {
            Thread.sleep((long) (time));   // pause for 300 milliseconds
        }
        catch (InterruptedException exc) {
        }
    }
    
    

}