package view2D;


import LiftModel.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;


/**
 * Use to calculate the statistics and show them in a table
 * @author luis
 * @version 1.1
 */
public class StatisticsCalculator extends JPanel implements Runnable{
    
    /**
     * Building
     */
    public static Building building;
    private StatisticsCollector sc;
    private long time = 1000;
    private long rTime = 1000;
    private boolean isRunning;
    private Thread t;
    private int totalNumberOfEmployee;
    private int totoalNumberOfTrips;
    private float averageWaitTime;
    private int maximumTripTime;
    private int minimumTripTime;
    private float averageTripTime;
    
    private JLabel tnoe;
    private JLabel tnot;
    private JLabel awt;
    private JLabel mxwt;
    private JLabel mxtt;
    private JLabel mitt;
    private JLabel att;
    private JLabel specialPassengerTime;
    
    private JLabel ait;
    private JLabel mxit;
    
    //GUI

    /**
     * default constructor
     */
        public StatisticsCalculator(){
    
        tnoe = new JLabel("0");
        tnot = new JLabel("0");
        awt = new JLabel("0");
        mxwt = new JLabel("0");
        mxtt = new JLabel("0");
        mitt = new JLabel("0");
        att = new JLabel("0");
        
        ait = new JLabel("0");
        mxit = new JLabel("0");
        specialPassengerTime = new JLabel("0");
        Font displayFont = tnoe.getFont().deriveFont(20.0f);
        tnoe.setFont(displayFont);

        tnot.setFont(displayFont);
        
   
        awt.setFont(displayFont);
        
 
        mxtt.setFont(displayFont);
        

        mitt.setFont(displayFont);
        

        att.setFont(displayFont);
        specialPassengerTime.setFont(displayFont);
        JPanel panel = new JPanel(new GridLayout(0,1));

        /*
        panel.add(new JLabel("Total Number Of Employee in Building:"));
        panel.add(tnoe);
        panel.add(new JLabel("Total Number of Trips:"));
        panel.add(tnot);
        panel.add(new JLabel("Average Waitting Time:"));
        panel.add(awt);
        panel.add(new JLabel("Max Trip Time:"));
        panel.add(mxtt);
        panel.add(new JLabel("Minimum Trip Time:"));
        panel.add(mitt);
        panel.add(new JLabel("Average Trip Time:"));
        panel.add(att);
        panel.add(new JLabel("Special Passenger last trip time:"));
        panel.add(specialPassengerTime);
        */
        AbstractTableModel model = new StatisticsTableModel(this);
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF );
        

        JScrollPane jsp = new JScrollPane(table);
        jsp.setPreferredSize(new Dimension(245,200));
        panel.add(jsp);
        
        add(panel);
        
        
        
        //add(new JScrollPane(table));
        this.setMinimumSize(new Dimension(170,20));
        //this.setPreferredSize(new Dimension(250,200));
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(
                       loweredetched, "Simulation Statistics");
        title.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(title);
    
    }
    
    /**
     * Thread start
     */
    public void start(){
        isRunning = true;
        if(t==null){
            t = new Thread(this);
            t.start();
        }
    }
    @Override
    public void run() {
        while(isRunning){
            totalNumberOfEmployee();
            totoalNumberOfTrips();
            averageTimeWaiting();
            maximumTripTime();
            averageTripTime();
            minimumTripTime();
            specialPeopleTripTime();
            averageInsideTime();
            maximumInsideTime();
            maximumTimeWaiting();
            
            this.repaint();
            try {
                t.sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(StatisticsCalculator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }
    private void specialPeopleTripTime(){
        ArrayList<LiftTakenRec> recList;
        recList = sc.getRecords();

        for(LiftTakenRec sc:recList){
            if(sc.id == 100001){
                specialPassengerTime.setText((sc.getOffTime-sc.startWaitTime)+"");
                
            }
        }
        
    }

    public JLabel getMxwt() {
        return mxwt;
    }

    public JLabel getSpecialPassengerTime() {
        return specialPassengerTime;
    }

    public JLabel getAit() {
        return ait;
    }

    public JLabel getMxit() {
        return mxit;
    }
    
    /**
     *
     * @return minimum trip time
     */
    public JLabel getMitt() {
        return mitt;
    }

    /**
     *
     * @return average trip time
     */
    public JLabel getAtt() {
        return att;
    }

    /**
     *
     * @return total number of employee
     */
    public JLabel getTnoe() {
        return tnoe;
    }

    /**
     *
     * @return total number of trips
     */
    public JLabel getTnot() {
        return tnot;
    }

    /**
     *
     * @return maximum trip time
     */
    public JLabel getMxtt() {
        return mxtt;
    }

    /**
     *
     * @return average waiting time
     */
    public JLabel getAwt() {
        return awt;
    }
    
    private synchronized void averageInsideTime(){
        ArrayList<LiftTakenRec> recList;
        recList = sc.getRecords();
        int it = 0;
        
        for(LiftTakenRec sc:recList){
            it += (sc.getOffTime-sc.enterLiftTime);
        }
        if(it!=0){
            this.averageTripTime = (float)it/(float)recList.size();
            String result = String.format("%.2f", averageTripTime);
            this.ait.setText(result+"");
        }
    } 
    
    private synchronized void maximumInsideTime(){
        ArrayList<LiftTakenRec> recList;
        recList = sc.getRecords();
        long mxit = 0;
        
        for(LiftTakenRec sc:recList){
            long it = (sc.getOffTime-sc.enterLiftTime);
            if(mxit<it){
                mxit = it;
            }
        }
        this.mxit.setText(""+mxit);
    } 
    
    private synchronized void averageTripTime(){
        ArrayList<LiftTakenRec> recList;
        recList = sc.getRecords();
        int totalTripTime = 0;
        
        for(LiftTakenRec sc:recList){
            totalTripTime += (sc.getOffTime-sc.startWaitTime);
        }
        if(totalTripTime!=0){
            this.averageTripTime = (float)totalTripTime/(float)recList.size();
            String result = String.format("%.2f", averageTripTime);
            this.att.setText(result+"");
        }
        
    }
    
    private synchronized void minimumTripTime(){
        ArrayList<LiftTakenRec> recList;
        recList = sc.getRecords();
        minimumTripTime = 100000;
        for(LiftTakenRec sc:recList){
            int time = (int) (sc.getOffTime-sc.startWaitTime);
            if(minimumTripTime>time){
                minimumTripTime = time;
            }
        }
        if(minimumTripTime!=100000){
            this.mitt.setText(minimumTripTime+"");
        }
        
    }
    private synchronized void maximumTripTime(){
        ArrayList<LiftTakenRec> recList;
        recList = sc.getRecords();
        maximumTripTime = 0;
        for(LiftTakenRec sc:recList){
            int time = (int) (sc.getOffTime-sc.startWaitTime);
            if(maximumTripTime<time){
                maximumTripTime = time;
            }
        }
        this.mxtt.setText(maximumTripTime+"");
        //System.out.println("max time:"+maximumTripTime);
    }
    private synchronized void  averageTimeWaiting(){
        ArrayList<LiftTakenRec> recList;
        recList = sc.getRecords();
        int waitTime = 0;
        for(LiftTakenRec sc:recList){
            waitTime+=(sc.enterLiftTime-sc.startWaitTime);
        }
        averageWaitTime = (float)waitTime/(float)Building.employees.size();
        String result = String.format("%.2f", averageWaitTime);
        this.awt.setText(result+"");
        //System.out.println(averageWaitTime);
    
    }
    private void  maximumTimeWaiting(){
        ArrayList<LiftTakenRec> recList;
        recList = sc.getRecords();
        long maxWait = 0;
        for(LiftTakenRec sc:recList){
            long w =(sc.enterLiftTime-sc.startWaitTime);
            if(w>maxWait){
                maxWait = w;
            }
        }
        
        this.mxwt.setText(maxWait+"");
        //System.out.println(averageWaitTime);
    
    }
    
    private void totoalNumberOfTrips(){
        ArrayList<Lift> lList =  building.getLifts();
        totoalNumberOfTrips = 0;
        for(Lift l:lList){
            totoalNumberOfTrips+=l.tripNumber;
        }
        this.tnot.setText(totoalNumberOfTrips+"");
        //System.out.println("trip:"+totoalNumberOfTrips);
    }
    
    private void totalNumberOfEmployee(){
        totalNumberOfEmployee = Building.employees.size();
        this.tnoe.setText(totalNumberOfEmployee+"");
        //System.out.println("employee:"+totalNumberOfEmployee);
    }

    /**
     *
     * @param num speed factor
     */
    public void setSpeed(float num){
        time = (long) (rTime/num);
    }
    
    /**
     *
     * @param sc statistics panel
     */
    public void setSc(StatisticsCollector sc) {
        this.sc = sc;
    }
    
}
