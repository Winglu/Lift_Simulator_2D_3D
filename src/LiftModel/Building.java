package LiftModel;


import java.util.ArrayList;
import view2D.LiftDebugInfPanel;

/**
 * Model the building for the simulation system
 * A building contains many floors, lifts and a lift controller
 * @author louis
 * @version 1.1
 */


public class Building {

    /**
     * default max floors
     */
    public static int MAX_FLOORS = 10;

    /**
     * default max lifts
     */
    public static int MAX_LIFTS = 2;

    /**
     * lift controller
     */
    public CentralController centralController;


    /**
     * Clock for the simulation system`
     */
        public static Clock clock;

    /**
     * Timer for the simulation system
     */
    public static SimulationTimer st;

    /**
     * Lifts in the system
     */
    public ArrayList<Lift> lifts;
    
    /**
     * Employee in the building 
     */
    public static ArrayList<Passenger> employees;
    
    
    public Building(BuildingParameters bp, ArrayList<LiftParameters> lpList){
        MAX_FLOORS = bp.numberOfFloor;
        MAX_LIFTS = bp.numberOfLift;
        employees = new ArrayList<Passenger>();
        
        ArrayList<Floor> floors = new ArrayList<Floor>();
        for(int i = 0; i < MAX_FLOORS; i++){
           Floor f = new Floor(i + 1);
           floors.add(f);
        }
        lifts = new ArrayList<Lift>();
        int j=0;
        for(int i = 0; i < MAX_LIFTS; i++){
            LiftParameters lp = lpList.get(i);
            LiftDebugInfPanel ldip = new LiftDebugInfPanel();
            Lift l = new Lift(i + 1);
            l.setRenterInTimeB(lp.enterInTimeB);
            l.setRenterInTimeH(lp.enterInTimeA);
            Lift.setMAX_OCCUPANCY(lp.capacity);
            l.setRidleTime(lp.idleTime);
            l.setRaccelation(lp.a);
            l.setRdoorOpenCloseTime(lp.doorCloseOpenTime);
            l.setRmaxSpeed(lp.maxSpeed);
            l.setCurrentFloorNumber(lp.homeFloor);
            l.setSpeedFactor((float) 1);
            l.setSpecialSpeeds();
            l.setLdip(ldip);
            l.expressType = lp.expressType;
            l.homeFloor = lp.homeFloor;
            lifts.add(l);
        }
        centralController = new CentralController(floors, lifts);
        Floor.setCentralController(centralController);
        Lift.setCentralController(centralController);        
        
    }
    
    /**
     * constructor for accepting parameters via data object. The data object might
     * read parameters via configuration file
     * @param bp BuildingParameters
     * @param lp LiftParameters
     */
    public Building(BuildingParameters bp, LiftParameters lp){
        MAX_FLOORS = bp.numberOfFloor;
        MAX_LIFTS = bp.numberOfLift;
        employees = new ArrayList<Passenger>();
        ArrayList<Floor> floors = new ArrayList<Floor>();
        for(int i = 0; i < MAX_FLOORS; i++){
           Floor f = new Floor(i + 1);
           floors.add(f);
        }
        lifts = new ArrayList<Lift>();
        int j=0;
        for(int i = 0; i < MAX_LIFTS; i++){
            LiftDebugInfPanel ldip = new LiftDebugInfPanel();
            Lift l = new Lift(i + 1);
            l.setRenterInTimeB(lp.enterInTimeB);
            l.setRenterInTimeH(lp.enterInTimeA);
            Lift.setMAX_OCCUPANCY(lp.capacity);
            l.setRidleTime(lp.idleTime);
            l.setRaccelation(lp.a);
            l.setRdoorOpenCloseTime(lp.doorCloseOpenTime);
            l.setRmaxSpeed(lp.maxSpeed);
            l.setCurrentFloorNumber(lp.homeFloor);
            l.setSpeedFactor((float) 1);
            l.setSpecialSpeeds();
            l.setLdip(ldip);
            if(j<lp.liftId){
                l.expressType = lp.expressType;
                //System.out.println("aaa");
                
            }
            j++;
            l.homeFloor = lp.homeFloor;
            lifts.add(l);
        }
        centralController = new CentralController(floors, lifts);
        Floor.setCentralController(centralController);
        Lift.setCentralController(centralController);
    }
    
    /**
     * Simple constructor creating object by providing floor number and lift number
     * @param numOfFloor floor number
     * @param numOfLift lift number
     */
    public Building(int numOfFloor, int numOfLift){
        employees = new ArrayList<Passenger>();
        ArrayList<Floor> floors = new ArrayList<Floor>();
        MAX_FLOORS = numOfFloor;
        MAX_LIFTS = numOfLift;
        for(int i = 0; i < MAX_FLOORS; i++){
           Floor f = new Floor(i + 1);
           floors.add(f);
        }
        lifts = new ArrayList<Lift>();
        for(int i = 0; i < MAX_LIFTS; i++){
            LiftDebugInfPanel ldip = new LiftDebugInfPanel();
            Lift l = new Lift(i + 1);
            l.setSpeedFactor((float) 1);
            l.setSpecialSpeeds();
            l.setLdip(ldip);
            lifts.add(l);
        }
        centralController = new CentralController(floors, lifts);
        Floor.setCentralController(centralController);
        Lift.setCentralController(centralController);
    }
    
    /**
     * Default constructor 2 lifts and 10 floors
     */
    public Building(){
        employees = new ArrayList<Passenger>();
        ArrayList<Floor> floors = new ArrayList<Floor>();
        for(int i = 0; i < MAX_FLOORS; i++){
           Floor f = new Floor(i + 1);
           floors.add(f);
        }
        lifts = new ArrayList<Lift>();
        for(int i = 0; i < MAX_LIFTS; i++){
            LiftDebugInfPanel ldip = new LiftDebugInfPanel();
            Lift l = new Lift(i + 1);
            l.setSpeedFactor((float) 1);
            l.setSpecialSpeeds();
            l.setLdip(ldip);
            lifts.add(l);
        }
        centralController = new CentralController(floors, lifts);
        Floor.setCentralController(centralController);
        Lift.setCentralController(centralController);
        //Lift.st = st;
        //Lift.setSc(clock);

      //centralController.startLift();
    }

    /**
     * Get lifts in the building
     * @return ArrayList of lifts
     */
    public ArrayList<Lift> getLifts() {
        return lifts;
    }

    /**
     * Method for add employee to the building. If will check if a employee is re-enter.
     * @param p an employee
     */
    public static void addEmployee(Passenger p){

        employees.add(p);
        
        
    }

    /**
     * method to get the central controller of the building
     * @return the controller
     */
    public CentralController getCentralController() {
        return centralController;
    }
    
    /**
     * get a lift by lift number
     * @param liftNumber lift number
     * @return the lift state
     */
    public LiftState getLiftState(int liftNumber){
      return centralController.getLiftState(liftNumber);
    }

    /**
     * get the floor up waiting people number by floor number 
     * @param floorNumber floor number
     * @return number waiting up
     */
    public int getNumberWaitingUp(int floorNumber){
      return centralController.getNumberWaitingUp(floorNumber);
    }

    /**
     * get the floor down waiting people number by floor number 
     * @param floorNumber floor number
     * @return number waiting down
     */
    public int getNumberWaitingDown(int floorNumber){
      return centralController.getNumberWaitingDown(floorNumber);
    }

    /**
     * get a floor by floor number
     * @param floorNumber floor number
     * @return floor
     */
    public Floor getFloor(int floorNumber) {
      return centralController.getFloor(floorNumber);
    }


    /**
     * stop all lifts
     */
    public void stopLifts(){
     centralController.stopLifts();
    }
    /**
     *  used for show the simulation parameters in GUI
     * @return description
    */
    public String toString(){
        String s = new String();
        s +="<P>Floors:"+MAX_FLOORS+"</p>";
        s +="<p>Lifts:"+MAX_LIFTS+"</p>";
        return s;
    
    }
}
