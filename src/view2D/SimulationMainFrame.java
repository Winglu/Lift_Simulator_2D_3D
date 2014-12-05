package view2D;


import LiftModel.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import view3D.AnimationPanel3D;


/**
 * Used to contains all GUI panel to the JFrame
 * @author luis_chen
 * @version 1.1
 */
public class SimulationMainFrame extends JFrame {
    //public AnimationPanel3D ap3D;
    /**
     * Building
     */
    public static Building building;

    /**
     * Clock
     */
    public static Clock c;

    /**
     * Time lapse
     */
    public static SimulationTimer st;

    /**
     * Passenger assigner
     */
    public static PassengerAssigner pa;

    /**
     * Statistic calculator
     */
    public static StatisticsCalculator sc;
    
    private AnimationPanel ap;
    JPanel animationFloorStPanel;
    JScrollPane aniscrollPane;
    private int apDefaultW = 500;
    private int apDefaultH = 450;
    //private FloorStatisticsPanel fsp;
    private ToolKitPanel tp;
    private SpeedControllPanel scp;
    private DetialedLift dl;
    private ArrayList<FloorStatisticsPanel> fspList;
    public static AnimationPanel3D ap3d;
    
    /**
     * initialize the frame
     */
    public void setupFrame(){
        BorderLayout bl = new BorderLayout();
        bl.setHgap(10);
        bl.setVgap(10);
        this.setLayout(new BorderLayout());
        this.setJMenuBar(setupMenu());
        
        setupSimulationTitle();
        
        setupAnimaitonPanel();
        
        setupLeftStatisticsPanel();
        
        setupRightPanel();
        
        setupBottomPanel();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    private void setupBottomPanel(){
        JPanel bottomPanel = new JPanel(new MigLayout());
        
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton endButton = new JButton("End");
        startButton.setMaximumSize(new Dimension(100,30));
        stopButton.setMaximumSize(new Dimension(100,30));
        endButton.setMaximumSize(new Dimension(100,30));
        startButton.setMinimumSize(new Dimension(100,30));
        stopButton.setMinimumSize(new Dimension(100,30));
        endButton.setMinimumSize(new Dimension(100,30));
        bottomPanel.add(startButton,"dock center,align center");
        bottomPanel.add(stopButton,"dock center,align center");
        bottomPanel.add(endButton,"dock center,align center");
        
        startButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Lift> liftList = building.getLifts();
                //start lift
                for(Lift l:liftList){
                    l.start();
                }
                //run dl
                c.getClok().setTime(8, 0, 0);
                dl.start();
                //start timer
                c.start();
                st.start();
                pa.start();
                sc.start();
                
                //ap.start();
                //start clock
            }
        });
        
        endButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                //append simulation results to a file
                pauseSimulation();
                int n = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want quit the simulatior?",
                    "Quit",
                    JOptionPane.YES_NO_OPTION);
                if (n==0) {
                    //System.out.print(1);
                    //close
                    pauseSimulation();
                    File resultFile = new File("result/result.txt");
                    if (!resultFile.exists()) {
                        try {
                            resultFile.createNewFile();
                        } catch (IOException ex) {
                            Logger.getLogger(SimulationMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    try {
                        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(resultFile, true)));
                        //out.println("the text");

                        String content = "";
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                        content+=timeStamp+"\n";
                        content+="Average Wait Time:"+sc.getAwt().getText()+"\n";
                        content+="Total Number of Employee in Building:"+sc.getTnoe().getText()+"\n";
                        content+="Totoal Number of Trips:"+sc.getTnot().getText()+"\n";
                        content+="Max travel time:"+sc.getMxtt().getText()+"\n";
                        content+="Minimum travel time:"+sc.getMitt().getText()+"\n";
                        content+="Average trip time:"+sc.getAtt().getText()+"\n"+"\n";
                        out.println(content);
                        out.close();

                    } catch (IOException ex) {
                        Logger.getLogger(SimulationMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.exit(-1);
                }else{
                    //System.out.print(2);
                    //continue
                    continueWork();
                }
                /*
                
                */
                
            }
        });
        
        stopButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton)e.getSource();
                if(button.getText().equals("Stop")){
                    pauseSimulation();
                    button.setText("Continue");
                }else{
                    continueWork();
                    button.setText("Stop");
        
        
                }
                
            }
        });
        
        this.add(bottomPanel,BorderLayout.SOUTH);
        
    }
    
    
    private void setupRightPanel(){
        
        //JPanel rightPanel = new JPanel(new MigLayout("", "0[]0[]0[]50", "[]"));
        JTabbedPane mTabbedPane = new JTabbedPane();
        mTabbedPane.addTab("SpeedControl", scp);
        mTabbedPane.addTab("Destinations", dl);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        ArrayList<Lift> lifts = building.getLifts();
        for(Lift l:lifts){
            tabbedPane.addTab("Lift"+l.getId(), l.getLdip());
        }
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(
                   loweredetched, "Lift Debug Information");
        title.setTitleJustification(TitledBorder.CENTER);
        tabbedPane.setBorder(title);
        mTabbedPane.add("Debug Information",tabbedPane);
        //JPanel rightPanel = new JPanel(new MigLayout());
        //rightPanel.setPreferredSize(new Dimension(200,800));
//add speed controll panel
        //rightPanel.add(scp,"wrap");
        //add track panel
        //rightPanel.add(dl,"wrap");
        
        //create JTabPanel
        
        
        
        
        
        JScrollPane scrollPane = new JScrollPane(mTabbedPane);
        Border loweredetched3 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title3 = BorderFactory.createTitledBorder(
                   loweredetched, "ControlPanel");
        title3.setTitleJustification(TitledBorder.CENTER);
        scrollPane.setBorder(title3);
        
        
        this.add(scrollPane,BorderLayout.EAST);
        
    }
    
    private void setupLeftStatisticsPanel(){
        JPanel timePanel = new JPanel(new MigLayout());
        timePanel.add(c,"wrap");
        timePanel.add(st,"wrap");
        //create table and add it to here
        
        timePanel.add(sc,"wrap");
        
        //add zoom in and zoom out
        JPanel zoomInOutPanel = new JPanel(new GridLayout(1,2));
        zoomInOutPanel.add(createZoomControllPanel());
        Border loweredetched1 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title1 = BorderFactory.createTitledBorder(
                   loweredetched1, "Zoom Toolkit");
        
        title1.setTitleJustification(TitledBorder.CENTER);
        zoomInOutPanel.setBorder(title1);
        timePanel.add(zoomInOutPanel);
        //other statistics
        JScrollPane scrollPane = new JScrollPane(timePanel);
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(
                   loweredetched, "Data Panel");
        title.setTitleJustification(TitledBorder.CENTER);
        scrollPane.setBorder(title);
        this.add(scrollPane,BorderLayout.WEST);
        
    }
    
    private void setupAnimaitonPanel(){
        if(this.ap!=null){
            
            GridLayout gl= new GridLayout(1,2);
            gl.setHgap(10);
            gl.setVgap(10);
            //JPanel animationFloorStPanel = new JPanel(gl);
            MigLayout ml = new MigLayout();
            //JPanel animationFloorStPanel = new JPanel(new MigLayout());
            animationFloorStPanel = new JPanel(new BorderLayout());
            animationFloorStPanel.setPreferredSize(new Dimension(50*Building.MAX_FLOORS,450));
            aniscrollPane = new JScrollPane(animationFloorStPanel);
            //animationFloorStPanel.setMinimumSize(new Dimension(400,200));
            //animationFloorStPanel.setSize(new Dimension(400,500));
            animationFloorStPanel.add(ap,BorderLayout.CENTER);
            gl = new GridLayout(Building.MAX_FLOORS,1);
            gl.setHgap(10);
            gl.setVgap(10);
            JPanel stisPanel = new JPanel(gl);
            for(int i = 0; i < Building.MAX_FLOORS; i++){
                stisPanel.add(fspList.get(i));
            }

            animationFloorStPanel.add(stisPanel,BorderLayout.EAST);
            //animationFloorStPanel.setMaximumSize(new Dimension(800,500));
            Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
            TitledBorder title = BorderFactory.createTitledBorder(
                       loweredetched, "Simulation Panel");
            title.setTitleJustification(TitledBorder.CENTER);
            aniscrollPane.setBorder(title);
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.add("3D",ap3d.getCanvas());
            tabbedPane.addTab("2D",aniscrollPane);
           
            this.add(tabbedPane,BorderLayout.CENTER);
            
            
                    
            MouseAdapter mouseAdapter = new MouseAdapter() {
                private final Cursor defCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
                private final Cursor hndCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
                private final Point pp = new Point();
                public void mouseDragged(MouseEvent e) {
                    JViewport vport = (JViewport)e.getSource();
                    Point cp = e.getPoint();
                    Point vp = vport.getViewPosition();
                    vp.translate(pp.x-cp.x, pp.y-cp.y);
                    animationFloorStPanel.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
                    pp.setLocation(cp);
                }
                @Override
                public void mousePressed(MouseEvent e)
                {
                    animationFloorStPanel.setCursor(hndCursor);
                    pp.setLocation(e.getPoint());
                }

                public void mouseReleased(MouseEvent e)
                {
                    animationFloorStPanel.setCursor(defCursor);
                    animationFloorStPanel.repaint();
                    animationFloorStPanel.revalidate();
                    aniscrollPane.revalidate();
                }
            };
            aniscrollPane.getViewport().addMouseListener(mouseAdapter);
            aniscrollPane.getViewport().addMouseMotionListener(mouseAdapter);
        }
    }
    private void setupSimulationTitle(){
        JLabel title = new JLabel("Advanced Java--Lift Simulatior");
        JPanel titlePanel = new JPanel();
        titlePanel.add(title);
        this.add(titlePanel,BorderLayout.NORTH);
        
    }
    
    private JMenuBar setupMenu(){
        JMenuItem menuItem;
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menu1 = new JMenu("Application");
        menuItem = new JMenuItem("Close");
        menuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                File resultFile = new File("result/result.txt");
                if (!resultFile.exists()) {
                    try {
                        resultFile.createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger(SimulationMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
  
                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(resultFile, true)));
                    //out.println("the text");
                    
                    String content = "";
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                    content+=timeStamp+"\n";
                    content+="Average Wait Time:"+sc.getAwt().getText()+"\n";
                    content+="Total Number of Employee in Building:"+sc.getTnoe().getText()+"\n";
                    content+="Totoal Number of Trips:"+sc.getTnot().getText()+"\n";
                    content+="Max travel time:"+sc.getMxtt().getText()+"\n";
                    content+="Minimum travel time:"+sc.getMitt().getText()+"\n";
                    content+="Average trip time:"+sc.getAtt().getText()+"\n"+"\n";
                    out.println(content);
                    out.close();
                    
                } catch (IOException ex) {
                    Logger.getLogger(SimulationMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(-1);
            }
        });
        menu1.add(menuItem);
        menuBar.add(menu1);
        
        JMenu menu2 = new JMenu("Simulation");
        menuItem = new JMenuItem("Simulation Specifications");
        menuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String buildingDes = building.toString();
                String liftDes = new String();
                ArrayList<Lift> lifts = building.getLifts();
                for(Lift l:lifts){
                    liftDes+=l.toString();
                }
                        
                JOptionPane.showMessageDialog(null, "<html><body width='200'><h1>Building</h1>"+buildingDes+"<h1>Lifts</h1>"+liftDes+"</body></html>");
            }
        
        });
        
        menu2.add(menuItem);
        menuBar.add(menu2);
        
        //change scen
        JMenu menu4 = new JMenu("Scene Select");
        menuItem = new JMenuItem("Morning");
        menuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                c.getClok().setTime(8, 0, 0);
            }
            
        });
        menu4.add(menuItem);
        
        menuItem = new JMenuItem("Daily");
        menuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                c.getClok().setTime(9, 0, 0);
            }
            
        });
        menu4.add(menuItem);
        
        
        menuItem = new JMenuItem("Lanuch");
        menuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                c.getClok().setTime(12, 0, 0);
            }
        
        });
        
        
        menu4.add(menuItem);
        
        menuBar.add(menu4);
        
        
        JMenu menu3 = new JMenu("Help");
        menuItem = new JMenuItem("About");
        menuItem.addActionListener(new ActionListener(){
            String message =
                    "<html><body><h1>Lift Simulator About</h1>" +
                    "<p>Lift Simulator v0.3 beta</p>" +
                    "<p>Author: Lu Chen</p>" +
                    "<p>ID: 2091739</p></body></html>";
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, message,"About",JOptionPane.PLAIN_MESSAGE);
            }
        });
        menu3.add(menuItem);
        menuBar.add(menu3);
        
        
        
        
        return menuBar;
    
    }
    
    
    /*
    *setters
    */

    /**
     *
     * @param ap animation panel
     */
    
    public void setAp(AnimationPanel ap) {
        this.ap = ap;
    }

    /**
     *
     * @param fspList floor statistic panel list
     */
    public void setFspList(ArrayList<FloorStatisticsPanel> fspList) {
        this.fspList = fspList;
    }

    /**
     *
     * @param dl detailed lift
     */
    public void setDl(DetialedLift dl) {
        this.dl = dl;
    }

    /**
     *
     * @param scp speed control panel
     */
    public void setScp(SpeedControllPanel scp) {
        this.scp = scp;
        
    }
    
    /**
     * pause simulation
     */
    public void pauseSimulation(){
        ArrayList<Lift> liftList = building.getLifts();
                    //start lift
        for(Lift l:liftList){
            l.pause();
        }
        //run dl
        //dl.pause();
        //start timer
        c.pause();
        st.pause();
        pa.pause();
    }

    /**
     * continue the simulation
     */
    public void continueWork(){
        ArrayList<Lift> liftList = building.getLifts();
        //start lift
        for(Lift l:liftList){
            l.continueWork();
        }
        //run dl
        //dl.pause();
        //start timer
        c.continueWork();
        st.continueWork();
        pa.continueWork();
                  
    }
    private JPanel createZoomControllPanel(){
        GridLayout gl = new GridLayout(1,1);
        
        JPanel scPanel = new JPanel(gl);
        
        JSlider js = new JSlider(JSlider.HORIZONTAL, 1,100,1);
        
        js.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            int jsValue = ((JSlider)e.getSource()).getValue();
            int newW = (int)(apDefaultW*(1+0.1*jsValue));
            int newH = (int)(apDefaultH*(1+0.1*jsValue));
            
            animationFloorStPanel.setPreferredSize(new Dimension(newW,newH));
            animationFloorStPanel.setMinimumSize(new Dimension(newW,newH));
            animationFloorStPanel.setSize(new Dimension(newW,newH));
            //animationFloorStPanel.repaint();
            aniscrollPane.repaint();
            aniscrollPane.validate();
            aniscrollPane.revalidate();
            //animationFloorStPanel.setSize(new Dimension(newW,newH));
            
            
         }
        });
        scPanel.add(js);
        scPanel.setMinimumSize(new Dimension(255,50));
        
        return scPanel;
        
    }
}
