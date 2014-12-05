package LiftModel;


import java.awt.BorderLayout;
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
 * Use to show the time elapse
 * @author luis_chen
 * @version 1.1
 */
public class SimulationTimer extends JPanel implements Runnable{

    /**
     * time elapse
     */
    public long timeEclpse;
    private JLabel label;
    private JLabel timeLapse;
    private Thread t;

    /**
     * speed factor
     */
    public static float speedFactor;
    private volatile boolean pauseWorking = false;
    private final Object pauseLock = new Object();
    private static long rTime;
    private static long time;
    
    /**
     * default constructor
     */
    public SimulationTimer(){
        speedFactor =1;
        timeEclpse = 0;
        time = 1000;
        rTime = 1000;
        JPanel panel = new JPanel(new BorderLayout());
        //label = new JLabel("Time Lapse:",SwingConstants.LEFT);
        //label = new JLabel("00:00", SwingConstants.LEFT);
        timeLapse = new JLabel("0",SwingConstants.CENTER);
        Font displayFont = timeLapse.getFont().deriveFont(20.0f);
        timeLapse.setFont(displayFont);
        //add(label);
        add(timeLapse,BorderLayout.CENTER);
        this.setMinimumSize(new Dimension(270,100));
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(
                       loweredetched, "Time Lapse");
        title.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(title);
    }
    
    /**
     *
     * @param num speed factor
     */
    public static void setSpeed(float num){
        time = (long) (rTime/num);
    }

    /**
     * stop
     */
    public void stop(){
        
        if(t!=null){
            try {
                t.sleep(1000000000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimulationTimer.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        
    }

    /**
     * start
     */
    public void start(){
        t = new Thread(this);
        if(t!=null){
            t.start();
        }else{
            t.interrupt();
        }
        
        
        
    }

    /**
     * pause
     */
    public void pause(){  
        this.pauseWorking = true;  
    } 

    /**
     * continue work
     */
    public void continueWork(){  
        this.pauseWorking = false;  
  
       synchronized(pauseLock)  
       {  
            pauseLock.notify();  
        }  
    } 
    @Override
    public void run() {

        while(true){
            synchronized(pauseLock){
                while (pauseWorking){  
                    try {
                        pauseLock.wait(); //pause scheduler till told to continue  
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SimulationTimer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
            
            }
            timeEclpse++;
            timeLapse.setText(timeEclpse+"");
            try{
                Thread.sleep((long) (time));

                //System.out.println(speedFactor+"factor");
            }catch(InterruptedException e){
            
            }
        }
    }
      
}
