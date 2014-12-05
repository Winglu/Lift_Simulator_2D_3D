package view2D;


import LiftModel.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;



/**
 * GUI use control the speed
 * @author louis
 * @version 1.1
 */
public class SpeedControllPanel extends JPanel implements ActionListener{
    private JRadioButton b01;
    private JRadioButton b025;
    private JRadioButton b05;
    private JRadioButton b1;
    private JRadioButton b2;
    private JRadioButton b3;
    private JRadioButton b4;
    private JRadioButton b5;
    private JRadioButton b6;
    private JButton applyButton;
    private JLabel label;
    private double speed=1;

    /**
     * Building
     */
    public static Building building;
    
    /**
     * Default constructor
     */
    public SpeedControllPanel(){
        initButtonLabel();
        setupLayout();
        this.setMaximumSize(new Dimension(330,200));
        this.setPreferredSize(new Dimension(330,200));
    }
    
    private void setupLayout(){
        
        this.setLayout(new MigLayout());
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(
                       loweredetched, "Speed Controll");
        title.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(title);
        this.add(label, "span");

        //set button
        ButtonGroup speedGroup = new ButtonGroup();
        speedGroup.add(b01);
        speedGroup.add(b025);
        speedGroup.add(b05);
        speedGroup.add(b1);
        speedGroup.add(b2);
        speedGroup.add(b3);
        speedGroup.add(b4);
        speedGroup.add(b5);
        speedGroup.add(b6);
        
        speedGroup.setSelected(b1.getModel(), true);
        
        Enumeration<AbstractButton> rButtons = speedGroup.getElements();
        while(rButtons.hasMoreElements()){
            rButtons.nextElement().addActionListener(this);
        }
        
        this.add(b01,"dock center");
        this.add(b025,"dock center");
        this.add(b05,"wrap,dock center");
        this.add(b1,"dock center");
        this.add(b2,"dock center");
        this.add(b3,"wrap,dock center");
        this.add(b4,"dock center");
        this.add(b5,"dock center");
        this.add(b6,"wrap,dock center");
        this.add(applyButton,"dock south");
        
        
    }
    private void initButtonLabel(){
        //
        b01 = new JRadioButton("0.5");
        b025 = new JRadioButton("0.6");
        b05 = new JRadioButton("0.8");
        b1 = new JRadioButton("1");
        b2 = new JRadioButton("2");
        b3 = new JRadioButton("3");
        b4 = new JRadioButton("4");
        b5 = new JRadioButton("5");
        b6 = new JRadioButton("6");
        
        
        
        
        applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(speed != 0){
                    Lift.speedFactor = (float) speed;
                    Clock.setSpeed((float) speed);
                    SimulationTimer.setSpeed((float) speed);
                    PassengerAssigner.setSpeed((long) speed);
                    for(Lift l:building.lifts){

                        l.pause();

                        l.setSpeedFactor((float) speed);
                        l.continueWork();
                    }
                
                }
                
                
            }
        });
        
        label = new JLabel("Please select a speed");
        
    }
    
    /**
     *
     * @param args test purpose only
     */
    public static void main(String [] args){
        JFrame frame = new JFrame();
        frame.setSize(400,400);
        frame.add(new SpeedControllPanel());
        frame.setVisible(true);
    
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selected = e.getActionCommand();
        switch (Integer.parseInt(selected)*10){
            case 5:
                speed = 0.5;
                break;
            case 6:
                speed = 0.6;
                break;
            case 8:
                speed = 0.8;
                
                break;
            case 10:
                speed = 1;
                break;
            case 20:
                speed = 2;
                break;
            case 30:
                speed = 3;
                break;
            case 40:
                speed = 4;
                break;
            case 50:
                speed = 5;
                break;
            case 60:
                speed = 6;
                break;
                       
        }
        
            
                        
    }
}
