package view2D;


import LiftModel.Lift;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * the animation panel for tracking a lift 
 * @author luis_chen
 * @version 1.1
 */
public class DetialedLiftView extends JPanel implements Runnable {
    
    /**
     * lift for display
     */
    public static Lift lift;
    
    /**
     * default constructor
     */
    public DetialedLiftView(){
        this.setSize(200,200);
        this.setMaximumSize(new Dimension(200,200));
        this.setPreferredSize(new Dimension(200,200));
        this.setMinimumSize(new Dimension(200,200));
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        Graphics2D g2 = (Graphics2D)g;
        paintLift(g2);
        
        
    }
    
    private void paintLift(Graphics2D g2){

        int liftWidth = this.getWidth();
        int floorHeight = this.getHeight();
        //System.out.println(liftWidth);
        //System.out.println(floorHeight);
        Point2D.Float start = new Point2D.Float();
        Rectangle2D.Float rec = new Rectangle2D.Float();
        Rectangle2D.Float rec2 = new Rectangle2D.Float();
        Rectangle2D.Float rec3 = new Rectangle2D.Float();
        Color c = new Color((float)0.5,(float)0.5,(float)0.5,(float)0.7);
        if(lift!=null){
            if(lift.getState().doorState==2){                    
                start.x = 0;
                start.y = 0;
                rec.x = start.x;
                rec.y = start.y;
                rec.width = liftWidth;
                rec.height = floorHeight;
                ///System.out.println("current passenger num:"+building.getCentralController().getLifts().get(i).getPassengers().size());
                if(lift.getPassengers().isEmpty()){
                    g2.setPaint(Color.GREEN);
                    g2.fill(rec);
                }else if(lift.getPassengers().size()>=1 && lift.getPassengers().size() < Lift.getMAX_OCCUPANCY()){
                    g2.setPaint(Color.YELLOW);
                    g2.fill(rec);
                }else if(lift.getPassengers().size()==Lift.getMAX_OCCUPANCY()){
                    g2.setPaint(Color.RED);
                    g2.fill(rec);
                }                    

                start.x = 0;
                start.y = 0;
                rec2.x = start.x;
                rec2.y = start.y;
                rec2.width = (float) (liftWidth/2 - lift.doorWidth*0.01*liftWidth/2);
                rec2.height = floorHeight;
                ///System.out.println("current passenger num:"+building.getCentralController().getLifts().get(i).getPassengers().size());
                g2.setPaint(c);
                g2.fill(rec2);

                //right door
                start.x = (float) (liftWidth/2+lift.doorWidth*0.01*liftWidth/2);
                start.y = 0;
                rec3.x = start.x;
                rec3.y = start.y;
                rec3.width = (float) (liftWidth/2 - lift.doorWidth*0.01*liftWidth/2);
                rec3.height = floorHeight;
                ///System.out.println("current passenger num:"+building.getCentralController().getLifts().get(i).getPassengers().size());
                g2.setPaint(c);
                g2.fill(rec3);            
            }else{
                start.x = 0;
                start.y = 0;
                rec.x = start.x;
                rec.y = start.y;
                rec.width = liftWidth;
                rec.height = floorHeight;
                
                g2.setPaint(c);
                g2.fill(rec);
             
            }
        }else{
            start.x = 0;
            start.y = 0;
            rec.x = start.x;
            rec.y = start.y;
            rec.width = liftWidth;
            rec.height = floorHeight;

            g2.setPaint(c);
            g2.fill(rec);
        }
    }

    @Override
    public void run() {
        while(true){
            this.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                //Logger.getLogger(AnimationPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    
    }

    /**
     * for testing purpose only
     * @param args testing only
     */
    public static void main(String [] args){
        JFrame frame = new JFrame("");
        frame.add(new DetialedLiftView());
        frame.setSize(100,100);
        frame.setVisible(true);
        
        
    }
    
    
}
