package view2D;


import LiftModel.Building;
import LiftModel.Lift;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * This class is used to reflect the motivation of the lift
 * It will paint the static floor according to configuration file and
 * lifts according to their positions 
 * @author louis
 * @version 1.1
 */
public class AnimationPanel extends JPanel implements Runnable{
    private Building building;
    private Thread t;
    private boolean isRunning =false;

    /**
     *
     * @param building the building for the simulation
     */
    public AnimationPanel(Building building){
        this.building = building;
        this.setMinimumSize(new Dimension(400,400));
        //this.setMaximumSize(new Dimension(500,600));
        this.setBackground(Color.WHITE);


    }
 
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        paintFloors(g2);
        paintLifts(g2);
    }
   /**
    * paint floors according to floor number defined in Building class 
    */
    private void paintFloors(Graphics2D g2){
        int floorNumber = Building.MAX_FLOORS;
        int floorHeight = this.getHeight()/floorNumber;
        Line2D.Float line = new Line2D.Float();
        Point2D.Float start = new Point2D.Float();
        Point2D.Float end = new Point2D.Float();
        g2.drawString(floorNumber-1+"", start.x+5,floorHeight/3);
        for(int i=1; i<floorNumber+1; i++){
            start.x = 0;
            start.y = i*floorHeight;
            end.x = this.getWidth();
            end.y = i*floorHeight;
            line.setLine(start, end);
            g2.setColor(Color.blue);
            g2.draw(line);
            g2.setPaint(Color.BLACK);
            
            g2.drawString(floorNumber-1-i+"", start.x+5, start.y+floorHeight/3);
            
            
                
        }
    }
    
   /**
    *  Dynamically draw lifts according to their position
    *  Door open and close is also represented in this method
    */
    private void paintLifts(Graphics2D g2){
        int liftNumber = Building.MAX_LIFTS;
        int div = (this.getWidth()-50)/liftNumber;
        //int liftWidth = (this.getWidth()-50)/liftNumber-10;
        
        int floorHeight = this.getHeight()/Building.MAX_FLOORS;
        int liftWidth = floorHeight;
        if(liftWidth>=80){
            //liftWidth =80;
        }
        int liftHeight = floorHeight-10;
        Point2D.Float start = new Point2D.Float();
        Rectangle2D.Float rec = new Rectangle2D.Float();
        Rectangle2D.Float rec2 = new Rectangle2D.Float();
        Rectangle2D.Float rec3 = new Rectangle2D.Float();
        Lift l = null;
        Color c = new Color((float)0.5,(float)0.5,(float)0.5,(float)0.7);
        for(int i=0 ;i<liftNumber; i++){
            l = building.getCentralController().getLifts().get(i);
            //l.liftWidth = (float)liftWidth;
            if(l.getState().motionState==1){
                start.x = i*div+15;
                start.y = this.getHeight() - l.getCurrentFloorNumber()*(float)floorHeight;
                rec.x = start.x;
                rec.y = start.y;
                rec.width = liftWidth;
                rec.height = liftHeight;
                ///System.out.println("current passenger num:"+building.getCentralController().getLifts().get(i).getPassengers().size());
                if(l.getPassengers().isEmpty()){
                    g2.setPaint(Color.GREEN);
                    g2.fill(rec);
                }else if(l.getPassengers().size()>=1 && l.getPassengers().size() < Lift.getMAX_OCCUPANCY()){
                    g2.setPaint(Color.YELLOW);
                    g2.fill(rec);
                }else if(l.getPassengers().size()==Lift.getMAX_OCCUPANCY()){
                    g2.setPaint(Color.RED);
                    g2.fill(rec);
                }
                g2.setPaint(c);
                g2.fill(rec);
                
                g2.setPaint(Color.BLACK);
                g2.drawString(l.passengers.size()+"", start.x+liftWidth/2-5, start.y+floorHeight/2);
            }else{
                if(l.getState().doorState==2 ||l.getState().doorState==1){
                    
                    start.x = i*div+15;
                    start.y = this.getHeight() - l.getCurrentFloorNumber()*floorHeight;
                    rec.x = start.x;
                    rec.y = start.y;
                    rec.width = liftWidth;
                    rec.height = liftHeight;
                    ///System.out.println("current passenger num:"+building.getCentralController().getLifts().get(i).getPassengers().size());
                    if(l.getPassengers().isEmpty()){
                        g2.setPaint(Color.GREEN);
                        g2.fill(rec);
                    }else if(l.getPassengers().size()>=1 && l.getPassengers().size() < Lift.getMAX_OCCUPANCY()){
                        g2.setPaint(Color.YELLOW);
                        g2.fill(rec);
                    }else if(l.getPassengers().size()==Lift.getMAX_OCCUPANCY()){
                        g2.setPaint(Color.RED);
                        g2.fill(rec);
                    }        
                    g2.setPaint(Color.BLACK);
                    g2.drawString(l.passengers.size()+"", start.x+liftWidth/2-5, start.y+floorHeight/2);
                    
                    start.x = i*div+15;
                    start.y = this.getHeight() - l.getCurrentFloorNumber()*floorHeight;
                    rec2.x = start.x;
                    rec2.y = start.y;
                    rec2.width = (float) (liftWidth/2 - l.doorWidth*0.01*liftWidth/2);
                    rec2.height = liftHeight;
                    ///System.out.println("current passenger num:"+building.getCentralController().getLifts().get(i).getPassengers().size());
                    g2.setPaint(c);
                    g2.fill(rec2);
                    
                    //right door
                    start.x = (float) (i*div+15+liftWidth/2+l.doorWidth*0.01*liftWidth/2)+2;
                    start.y = this.getHeight() - l.getCurrentFloorNumber()*floorHeight;
                    rec3.x = start.x;
                    rec3.y = start.y;
                    rec3.width = (float) (liftWidth/2 - l.doorWidth*0.01*liftWidth/2);
                    rec3.height = liftHeight;
                    ///System.out.println("current passenger num:"+building.getCentralController().getLifts().get(i).getPassengers().size());
                    g2.setPaint(c);
                    g2.fill(rec3);
                    //wait
                    
                    //close
                    
                    
                    //System.out.println("door open and close");
                }else{
                    //System.out.println("**********");
                    start.x = i*div+15;
                    start.y = this.getHeight() - l.getCurrentFloorNumber()*floorHeight;
                    rec.x = start.x;
                    rec.y = start.y;
                    rec.width = liftWidth;
                    rec.height = liftHeight;
                    ///System.out.println("current passenger num:"+building.getCentralController().getLifts().get(i).getPassengers().size());
                    if(l.getPassengers().isEmpty()){
                        g2.setPaint(Color.GREEN);
                        g2.fill(rec);
                    }else if(l.getPassengers().size()>=1 && l.getPassengers().size() < Lift.getMAX_OCCUPANCY()){
                        g2.setPaint(Color.YELLOW);
                        g2.fill(rec);
                    }else if(l.getPassengers().size()==Lift.getMAX_OCCUPANCY()){
                        g2.setPaint(Color.RED);
                        g2.fill(rec);
                    }       
                    g2.setPaint(c);
                    g2.fill(rec);
                    
                    g2.setPaint(Color.BLACK);
                    g2.drawString(l.passengers.size()+"", start.x+liftWidth/2-5, start.y+floorHeight/2);
                }
            }
        }
    }
    
    /**
     * Method for put the instance of this class in a Thread 
     */
    public void start(){
        isRunning =true;
        if(t == null){
        
            t= new Thread(this);
            t.run();
        }
    
    }
    
    /**
     * repaint this panel every 10ms to ensure the smooth motivation of lifts
     */
    
    @Override
    public void run() {
        while(true){
            
            this.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(AnimationPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
