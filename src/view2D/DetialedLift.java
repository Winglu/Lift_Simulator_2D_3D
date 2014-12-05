package view2D;


import LiftModel.Building;
import LiftModel.Floor;
import LiftModel.Lift;
import LiftModel.Passenger;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


/**
 *
 * Panel of a single lift tracker
 * Once user click floor button to require a lift, this panel is activated to show
 * the assigned lift; it will show the lift current floor number and door status
 * it also provide buttons for user to select destination
 * @author luis_chen
 * @version 1.1
 */
public class DetialedLift extends JPanel implements Runnable{
    private JLabel levelDisplay;
    private JLabel liftInf;
    public static JLabel tripTime;
    private Thread activity;
    //public static Building building;
    
    private JButton enterButton;
    private JButton leaveButton;
    
    /**
     * the lift for tracking
     */
    public static Floor floor;
    public static Lift lift;
    public static Building building;
    private ArrayList<JButton> buttonList;
    private DetialedLiftView dlv;
   

    /**
     * the passenger for tracking
     */
    public static Passenger p;
    
    private boolean isRunning;

    /**
     * default constructor.
     * It will create all components in the panel
     */
    public DetialedLift(){
        levelDisplay = new JLabel("Please call a lift");
        tripTime = new JLabel("Last trip time");
        Font displayFont = levelDisplay.getFont().deriveFont(15.0f);
        levelDisplay.setFont(displayFont);
        liftInf = new JLabel("Please call a lift");
        enterButton = new JButton("Enter");
        leaveButton = new JButton("Leave");
        
        
        buttonList = new ArrayList<JButton>();
        //dlv = new DetialedLiftView();
        //new Thread(dlv).start();
        //this.setSize(new Dimension(400,400));
        //this.setPreferredSize(new Dimension(400,400));
        for(int i=0; i<Building.MAX_FLOORS; i++){
            JButton button = new JButton((i)+"");
            button.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    int destLevel = Integer.parseInt(e.getActionCommand());
                    if(p!=null&&floor!=null){
                        //p.lift = lift;
                        p.setDestFloorNumber(destLevel+1);
                        sendPassengerToTheRightQueue(p);
                        //System.out.println(p.floor+"!!!!!!");
                        //p.activity = 1;
                        //p.start();
                    }
                }
            
            });
            buttonList.add(button);
        }
        
        setELButtonAction();
        BorderLayout bl = new BorderLayout();
        bl.setHgap(10);
        bl.setVgap(10);
        
        setLayout(bl);
        
        /*****************
         * 
         * 1 1 4 1 1 1
         * 1 2 2 2 3 3
         * 1 2 2 2 3 3
         * 1 2 2 2 3 3
         * 1 1 5 1 1 1
         * 
         * 5*6
         */
        JPanel j = new JPanel();
        j.add(levelDisplay);
        add(j,BorderLayout.NORTH);
        
        

        //add(dlv,BorderLayout.CENTER);
        
        
        JPanel infPanel = new JPanel();
        //infPanel.setSize(100, 100);
        infPanel.add(liftInf);
        //infPanel.add(tripTime);
        //infPanel.add(enterButton);
        //infPanel.add(leaveButton);
        add(infPanel,BorderLayout.SOUTH);
        
        this.setMinimumSize(new Dimension(330,200));
        this.setPreferredSize(new Dimension(330,200));
        add(this.createButtonPanel(),BorderLayout.CENTER);
        
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(
                       loweredetched, "Lift Tracker");
        title.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(title);
                
        
    }
    
    private void sendPassengerToTheRightQueue(Passenger p){
        if(p.getHomeFloorNumber()<p.getDestFloorNumber()){
            //express or not 
            if(this.isGoExpress(p)==1&&building.centralController.isHasHELift()){
                //System.out.println("aaaaa");
                
                p.isExpress = true;
                building.getFloor(p.getHomeFloorNumber()).summonHighExpressLiftUp(p);
            }else if(this.isGoExpress(p)==4&&building.centralController.isHasLELift()){
                //System.out.println("wrong");
                p.isExpress = true;
                building.getFloor(p.getHomeFloorNumber()).summonLowExpressLiftUp(p);
            }else{
                building.getFloor(p.getHomeFloorNumber()).summonLiftUp(p);
            }
            
            

        }else{
            if(this.isGoExpress(p)==1&&building.centralController.isHasHELift()){
                p.isExpress = true;
                building.getFloor(p.getHomeFloorNumber()).summonHighExpressLiftDown(p);
            }else if(this.isGoExpress(p)==4&&building.centralController.isHasLELift()){
                p.isExpress = true;
                building.getFloor(p.getHomeFloorNumber()).summonLowExpressLiftDown(p);
            }else{
                building.getFloor(p.getHomeFloorNumber()).summonLiftDown(p);
            }
            
            
        }
    }
    private int isGoExpress(Passenger p){
        int result = 0;
        if(p.getHomeFloorNumber()==1||
                p.getHomeFloorNumber()==2||
                p.getHomeFloorNumber()==Building.MAX_FLOORS/2+1){
            if(p.getDestFloorNumber()>Building.MAX_FLOORS/2+1&&p.isExpress){
                result = 1; //high express up
                return result;
            }
        }
        if(p.getHomeFloorNumber()==1||
                p.getHomeFloorNumber()==2){
            if(p.getDestFloorNumber()<=Building.MAX_FLOORS/2+1&&p.isExpress){
                result = 4; //low express up
                return result;
            }
        }
        if(p.getHomeFloorNumber()==Building.MAX_FLOORS){
            if(p.getDestFloorNumber()>=Building.MAX_FLOORS/2+1&&p.isExpress){
                result = 1; //high express down
                 return result;
            }
            if((p.getDestFloorNumber()==1||p.getDestFloorNumber()==2)&&p.isExpress){
                result =1;
                return result;
                
            }
        }
        if(p.getHomeFloorNumber()==Building.MAX_FLOORS/2+1){
            if(p.getDestFloorNumber()<Building.MAX_FLOORS/2+1&&p.isExpress){
                result = 4; //low express down
                 return result;
            }
        }
        /*
        if(building.centralController.isHavingExpressLift()){
            ArrayList<Lift> lifts = building.centralController.getLifts();
            for(Lift l:lifts){
                if(l.expressType==0){
                    result = false;
                }else if(l.expressType==1){
                    //0,1,nf/2-
                    if(p.getHomeFloorNumber()==1||p.getHomeFloorNumber()==2||p.getHomeFloorNumber()==Building.MAX_FLOORS/2+1){
                        if(p.getDestFloorNumber() == Building.MAX_FLOORS){
                            result = true;
                            break;
                        }
                    }
        
                }else if(l.expressType == 4){
                    //System.out.println("!!!!!!");
                    if(p.getHomeFloorNumber()==1 && p.getDestFloorNumber()<=Building.MAX_FLOORS/2+1){
                        result = true;
                        break;

                    }
                }
            }
            
            
        }
        */
        
        return result;
    } 
    /*
    public void enableButtonAccordingToLiftType(int type){
        
        if(type == 0){
            for(int i =0;i<=Building.MAX_FLOORS-1;i++){
                buttonList.get(i).setEnabled(true);
            }
        }else if(type == 1){
            for(int i=0;i<=Building.MAX_FLOORS-1;i++){
              
                buttonList.get(i).setEnabled(false);
            }
            buttonList.get(0).setEnabled(true);
            buttonList.get(1).setEnabled(true);
            buttonList.get(Building.MAX_FLOORS/2).setEnabled(true);
            buttonList.get(Building.MAX_FLOORS-1).setEnabled(true);

        
        }else if(type == 2){
            buttonList.get(0).setEnabled(false);
        }else if(type == 3){
            for(int i =0;i<=Building.MAX_FLOORS/2;i++){
                buttonList.get(i).setEnabled(false);
            }
        
        }else if(type == 4){
            for(int i=Building.MAX_FLOORS/2+1;i<=Building.MAX_FLOORS-1;i++){
                
                buttonList.get(i).setEnabled(false);
            }
        }
        
    }
    */
    /**
     * stop animation in the panel
     */
    public void stop(){
        if(activity!=null){
            try {
                activity.sleep(1000000000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DetialedLift.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    /**
     * start the animation
     */
    public void start(){
        
        isRunning =true;
        if(activity == null){
          activity = new Thread(this);
          //activeElevator.setDaemon(true);
          //activeLift.setPriority(10 );
          activity.start();
       }else{
            activity.interrupt();
        }
    }
    private void setELButtonAction(){
        enterButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                //p.lift = lift;
                //if(lift.getState().doorState==1&&(int)lift.getCurrentFloorNumber()==p.location){
                    //p.enterLift();
                    
                //}
                
            }
        
        });
        leaveButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(lift.getState().doorState==1){
                   // p.leaveLift();
                }
            }
        
        });
    }
    private JScrollPane createButtonPanel(){
       
        GridLayout gl = new GridLayout(0,2);
        gl.setHgap(5);
        gl.setVgap(5);
        JPanel buttonPanel = new JPanel(gl);
         
        //buttonPanel.setSize(100, 100);
        for(int i = buttonList.size()-1; i>0; i--){
            i=i-1;
            buttonPanel.add(buttonList.get(i));
            buttonPanel.add(buttonList.get(i+1));
            
        }
        JScrollPane sp = new JScrollPane(buttonPanel);
        return sp;
    }
    
    /**
     * for testing purpose only
     * @param args testing only
     */
    public static void main(String [] args){
        DetialedLift df = new DetialedLift();
        JFrame f = new JFrame();
        new Thread(df.dlv).start();
        f.add(df);
        f.setSize(400, 400);
        f.setVisible(true);
    }

    @Override
    public void run() {
        while(isRunning){
            if(lift!=null){
                
                levelDisplay.setText((int)(lift.getState().currentFloorNumber-1)+"");
                liftInf.setText("Lift "+lift.getId()+" is for you");
            }else{
                levelDisplay.setText("Please call a lift");
                liftInf.setText("Please call a lift");
            }
               
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                //Logger.getLogger(DetialedLift.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
