package view2D;


import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Show the critical events of a lift
 * @author luis
 * @version 1.1
 */
public class LiftDebugInfPanel extends JPanel implements Runnable {

    /**
     * text area to show debug information
     */
    public JTextArea jta;
    
    /**
     * default constructor
     */
    public LiftDebugInfPanel(){
        jta = new JTextArea();
        jta.setWrapStyleWord(true);
        //jta.setMinimumSize(new Dimension(300,300));
        JScrollPane jScrollPane1 = new JScrollPane(jta);

        jScrollPane1.setMinimumSize(new Dimension(290,145));
        jScrollPane1.setPreferredSize(new Dimension(290,300));
        //this.setMinimumSize(new Dimension(300,300));
        add(jScrollPane1);
    }
    
    /**
     * add a debug information to text area
     * @param debugInf debug information
     */
    public void addADebugInf(String debugInf){
        jta.append(debugInf+"\n");
        this.repaint();
    
    }
    
    /**
     * testing only
     * @param args testing only
     */
    public static void main(String [] args){
        JFrame frame = new JFrame();
        LiftDebugInfPanel ldif = new LiftDebugInfPanel();
        frame.add(ldif);
        frame.setSize(new Dimension(400,400));
        frame.setVisible(true);
        new Thread(ldif).start();
    }
    
    
    @Override
    public void run() {

    }
    
}
