package starter;


import LiftModel.*;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import specification.reader.SpecificationFileReader;
import view2D.*;
import view3D.AnimationPanel3D;
import view3D.FloorControllPanel3D;



/**
 * User to start the simulator 
 * @author louis
 * @version 1.1
 */
public class SimulationStarter {
    
    /**
     *
     * @param args not used
     * @throws InterruptedException timer inside
     */
    public static void main(String [] args) throws InterruptedException{
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            
            public void run() {
                
                boolean notConfig = true;
                //ConfigFileParser cfp = new ConfigFileParser();
                SpecificationFileReader sfr = new SpecificationFileReader();
                try{
                    
                    //cfp.readConfigFile("config/config.txt");
                    sfr.readSpecificationFromFile("config/config2.txt");
                    notConfig = false;
                    
                    
                } catch (IOException ex) {
                    Logger.getLogger(SimulationStarter.class.getName()).log(Level.SEVERE, null, ex);
                }catch(NullPointerException e){
                    //notConfig
                    //System.out.println("aaa");
                }
                
                
                
                if(!notConfig){
                    //System.out.println("1");
                    SimulationMainFrame smf = new SimulationMainFrame();
                     //System.out.println("2");
                    BuildingParameters bp = sfr.getBp();
                    ArrayList<LiftParameters> lpList = sfr.getlList();
                    Building building = new Building(bp,lpList);
                    
                    DetialedLift dl = new DetialedLift();
                   
                    building.centralController.dl=dl;
                    FloorControllPanel3D.building = building;
                    AnimationPanel3D.building = building;
                    SimulationMainFrame.building = building;
                    smf.setDl(dl);
                    Clock clock = new Clock();
                    clock.speedFactor =1;
                    SimulationTimer st = new SimulationTimer();
                    st.speedFactor = 1;
                    StatisticsCollector collector = new StatisticsCollector();
                    
                    Lift.setSc(clock);
                    Lift.st = st;
                    Lift.collector = collector;
                    Floor.st = st;
                    Passenger.st = st;
                    ToolKitPanel.st = st;
                    ToolKitPanel.clock = clock;
                    ToolKitPanel tkp = new ToolKitPanel();
                    tkp.building = building;
                    SimulationMainFrame.c = clock;
                    SimulationMainFrame.st = st;
                    SpeedControllPanel scp = new SpeedControllPanel();
                    smf.setScp(scp);
                    //JFrame frame = new JFrame();


                    //JPanel container = new JPanel(new GridLayout(1,3));
                    //JPanel stisPanel = new JPanel(new GridLayout(Building.MAX_FLOORS,1));

                    AnimationPanel ap = new AnimationPanel(building);
                    smf.setAp(ap);
                    //container.add(ap);
                    //container.add(stisPanel);

                    //container.add(tkp);
                    //container.add(st);
                    ArrayList<FloorStatisticsPanel> fspList= new ArrayList<FloorStatisticsPanel>();
                    for(int i = Building.MAX_FLOORS ; i > 0; i--){
                        fspList.add(building.getFloor(i).getFsp());
                        //stisPanel.add(building.getFloor(i).getFsp());
                    }

                    PassengerAssigner.building = building;
                    StatisticsCalculator.building = building;
                    PassengerAssigner pa = new PassengerAssigner();
                    PassengerAssigner.c = clock;
                    SimulationMainFrame.pa = pa;
                    StatisticsCalculator sc = new StatisticsCalculator();
                    sc.setSc(collector);
                    SimulationMainFrame.sc = sc;
                    Floor.pa = pa;
                    SpeedControllPanel.building = building;
                    smf.setFspList(fspList);
                    new Thread(ap).start();
                    
                    AnimationPanel3D.liftsModel = building.getLifts();
                    AnimationPanel3D.floorsModel = building.getCentralController().getFloors();
                    AnimationPanel3D ap3d = new AnimationPanel3D();
                    PassengerAssigner.ap3d = ap3d;
                    //new MainFrame(ap3d,400,400);
                    
                    SimulationMainFrame.ap3d = ap3d;
                    DetialedLift.building =building;
                    new Thread(ap3d).start();
                    smf.setupFrame();
                    smf.setSize(new Dimension(1350,920));
                    smf.setVisible(true);
                    
                    
                }else{
                    //System.out.println("aaaa");
                    SimulationMainFrame smf = new SimulationMainFrame();
                    Building building = new Building();
                    
                    SimulationMainFrame.building = building;
                    DetialedLift dl = new DetialedLift();
                    building.centralController.dl=dl;
                    FloorControllPanel3D.building = building;
                    AnimationPanel3D.building = building;
                    smf.setDl(dl);
                    //SimulatorClock sc = new SimulatorClock();
                    Clock clock = new Clock();
                    clock.speedFactor =1;

                    SimulationTimer st = new SimulationTimer();
                    st.speedFactor = 1;
                    StatisticsCollector collector = new StatisticsCollector();


                    Lift.setSc(clock);
                    Lift.st = st;
                    Lift.collector = collector;
                    Floor.st = st;
                    Passenger.st = st;
                    ToolKitPanel.st = st;
                    ToolKitPanel.clock = clock;
                    ToolKitPanel tkp = new ToolKitPanel();
                    tkp.building = building;
                    SimulationMainFrame.c = clock;
                    SimulationMainFrame.st = st;
                    SpeedControllPanel scp = new SpeedControllPanel();
                    smf.setScp(scp);
                    //JFrame frame = new JFrame();


                    //JPanel container = new JPanel(new GridLayout(1,3));
                    //JPanel stisPanel = new JPanel(new GridLayout(Building.MAX_FLOORS,1));

                    AnimationPanel ap = new AnimationPanel(building);
                    smf.setAp(ap);
                    //container.add(ap);
                    //container.add(stisPanel);

                    //container.add(tkp);
                    //container.add(st);
                    ArrayList<FloorStatisticsPanel> fspList= new ArrayList<FloorStatisticsPanel>();
                    for(int i = Building.MAX_FLOORS ; i > 0; i--){
                        fspList.add(building.getFloor(i).getFsp());
                        //stisPanel.add(building.getFloor(i).getFsp());
                    }
                    
                    PassengerAssigner.building = building;
                    StatisticsCalculator.building = building;
                    PassengerAssigner pa = new PassengerAssigner();
                    PassengerAssigner.c = clock;
                    SimulationMainFrame.pa = pa;
                    StatisticsCalculator sc = new StatisticsCalculator();
                    sc.setSc(collector);
                    SimulationMainFrame.sc = sc;
                    Floor.pa = pa;
                    SpeedControllPanel.building = building;
                    smf.setFspList(fspList);
                    DetialedLift.building =building;
                    AnimationPanel3D.liftsModel = building.getLifts();
                    AnimationPanel3D.floorsModel = building.getCentralController().getFloors();
                    AnimationPanel3D ap3d = new AnimationPanel3D();
                    PassengerAssigner.ap3d = ap3d;
                    
                    SimulationMainFrame.ap3d = ap3d;
                    //new MainFrame(ap3d,400,400);
                    new Thread(ap).start();
                    new Thread(ap3d).start();
                    smf.setupFrame();
                    smf.setSize(new Dimension(1350,920));
                    smf.setVisible(true);
                }
                //System.out.println("aaa");
                
                
                
                //frame.add(container);
                //frame.setSize(new Dimension(800,600));
                //frame.setVisible(true);
                //clock.speedFactor = 4;
                //st.speedFactor = 4;
                
                /*
                Person.setBuilding(building);
                for(int i = 0; i < 1; i++){
                    Person p = new Person(i+1);
                    p.speedFactor = 20;
                    //people.add(p);
                    p.start();

                }
                */
            }
        });
        
        
    }
}
