package view2D;


import LiftModel.Building;
import LiftModel.Clock;
import LiftModel.Lift;
import LiftModel.SimulationTimer;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;



/**
 * GUI to show simulator clock and time elapse
 * @author luis_chen
 * @version 1.1
 */
public class ToolKitPanel extends JPanel {
    
    /**
     *
     */
    public static Clock clock;

    /**
     *
     */
    public static SimulationTimer st;
    private JSlider js;

    /**
     *
     */
    public Building building;
    private DetialedLift dl;
    
    /**
     *
     */
    public ToolKitPanel(){
        this.setLayout(new GridLayout(0,1));
        
        dl = new DetialedLift();

        
        add(clock);
        //c.fill = GridBagConstraints.HORIZONTAL;
 
        add(st);
        //c.fill = GridBagConstraints.HORIZONTAL;

        add(createSpeedControllPanel());
        add(dl);
        
        new Thread(dl).start();
    }
    
    private JPanel createSpeedControllPanel(){
        GridLayout gl = new GridLayout(1,3);
        
        JPanel scPanel = new JPanel(new GridLayout(1,3));
        JLabel speedVary = new JLabel("Speed Controll");
        js = new JSlider(JSlider.HORIZONTAL, 1,10,1);
        JButton spButton = new JButton("Change");
        scPanel.add(speedVary);
        scPanel.add(js);
        scPanel.add(spButton);
        spButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                int speedFactor = js.getValue();
                Clock.speedFactor = speedFactor;
                SimulationTimer.speedFactor = speedFactor;
                for(Lift l:building.lifts){
                    try {
                        l.activeLift.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ToolKitPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    l.setSpeedFactor(speedFactor);
                    l.activeLift.interrupt();
                }
            }
        });
        
        return scPanel;
        
    }
}
