package view2D;


import LiftModel.Floor;
import LiftModel.Passenger;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;



/**
 * The floor statistics panel
 * it includes up and down button for normal and express requirements
 * and relative passengers numbers in the queues 
 * @author louis
 * @version 1.1
 */
public class FloorStatisticsPanel extends JPanel{
    private Floor floor;
    private JButton upButton;
    private JButton downButton;
    private JButton upButtonExpress;
    private JButton downButtonExpress;
    /**
     * 
     */
    public JLabel numberOfWaittingUp;
    public JLabel numberOfWaittingUpExpress;

    /**
     *
     */
    public JLabel numberOfWaittingDown;
    public JLabel numberOfWaittingDownExpress;
    
    /**
     * 
     * @param floor a floor
     * @param isExpress if the floor is express floor
     */
    public FloorStatisticsPanel (Floor floor, boolean isExpress){
        this.floor = floor;
        upButton = new JButton("U");
        downButton = new JButton("D");
        upButtonExpress = new JButton("UE");
        downButtonExpress = new JButton("DE");
        numberOfWaittingUp = new JLabel("0");
        numberOfWaittingDown = new JLabel("0");
        numberOfWaittingUpExpress = new JLabel("0");
        numberOfWaittingDownExpress = new JLabel("0");
        addEventToButton();
        //g.add(g1);
        //g.add(g2);
        setLayout(new GridLayout(1,3));
        createAndAddUpDownPanel();
        //createLablePanel();
        createAndAddStatisticsPanel();
        if(isExpress){
            createAndAddUpDownPanelExpress();
            createAndAddStatisticsPanelExpress();
            
        }
        
        
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        //TitledBorder title = BorderFactory.createTitledBorder(
         //          loweredetched, "Floor: "+this.floor.getFloorNumber());
        //title.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(loweredetched);
        this.setMaximumSize(new Dimension(200,50));
        this.setPreferredSize(new Dimension(200,50));
    }
    
    private void createAndAddUpDownPanelExpress(){
        JPanel upDownPanel = new JPanel(new GridLayout(2,3));
        upDownPanel.setSize(1000, 100);
        upDownPanel.add(upButtonExpress);
        upDownPanel.add(downButtonExpress);
        add(upDownPanel);
    }
    private void createAndAddStatisticsPanelExpress(){
        JPanel sPanel = new JPanel(new GridLayout(2,1));
        sPanel.add(numberOfWaittingUpExpress);
        sPanel.add(numberOfWaittingDownExpress);
        add(sPanel);
    }
    private void addEventToButton(){
        upButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                Passenger p = new Passenger(100001);

                p.setHomeFloorNumber(floor.getFloorNumber());
                p.setDestFloorNumber(0);
                while(floor.isIterating){
                }
                //floor.summonLiftUp(p);
                DetialedLift.p = p;
                DetialedLift.floor = floor;
            }
            
        });
        
        downButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                Passenger p = new Passenger(100001);

                p.setHomeFloorNumber(floor.getFloorNumber());
                p.setDestFloorNumber(0);
                DetialedLift.p = p;
                DetialedLift.floor = floor;
                //floor.summonLiftDown(p);
                     
            }
        
        });
        upButtonExpress.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                
                Passenger p = new Passenger(100001);

                p.setHomeFloorNumber(floor.getFloorNumber());
                p.setDestFloorNumber(0);
                
                //p.isExpress=true;
                //floor.summonExpressLiftUp(p);
                DetialedLift.p = p;
                DetialedLift.floor = floor;
            }
            
        });
        
        downButtonExpress.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                Passenger p = new Passenger(100001);

                p.setHomeFloorNumber(floor.getFloorNumber());
                p.setDestFloorNumber(0);
                DetialedLift.p = p;
                DetialedLift.floor = floor;
                
                //p.isExpress=true;
                //floor.summonExpressLiftDown(p);
                     
            }
        
        });
    }
    
    /**
     *
     * @return JButton
     */
    public JButton getUpButton() {
        return upButton;
    }

    /**
     *
     * @return JButton
     */
    public JButton getDownButton() {
        return downButton;
    }
    
    private void createLablePanel(){
        GridLayout gl = new GridLayout(2,1);
        JPanel lPanel  = new JPanel(gl);
        
        lPanel.add(new JLabel("Up Queue:"));
        lPanel.add(new JLabel("Down Queue:"));
        add(lPanel);
    }
    
    private void createAndAddStatisticsPanel(){
        JPanel sPanel = new JPanel(new GridLayout(2,1));
        sPanel.add(numberOfWaittingUp);
        sPanel.add(numberOfWaittingDown);
        add(sPanel);
    }
    
    private void createAndAddUpDownPanel(){
        JPanel upDownPanel = new JPanel(new GridLayout(2,3));
        upDownPanel.setSize(1000, 100);
        upDownPanel.add(upButton);
        upDownPanel.add(downButton);
        add(upDownPanel);
    }

    public JButton getUpButtonExpress() {
        return upButtonExpress;
    }

    public JButton getDownButtonExpress() {
        return downButtonExpress;
    }




}
