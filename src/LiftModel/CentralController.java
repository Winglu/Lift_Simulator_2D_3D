package LiftModel;

import java.util.ArrayList;
import view2D.DetialedLift;

/**
 * Central controller will accept lift call and select the most proper lift for
 * the calling. It will also notify the relative floor when a lift is arrived.
 *
 * @author louis
 * @version 1.1
 */
public class CentralController {

    private ArrayList<Lift> lifts;
    private ArrayList<Floor> floors;
    public DetialedLift dl;
    /**
     *
     * Constructor
     *
     * @param floors ArrayLift of Floors
     * @param lifts ArrayList of lifts
     */
    public CentralController(ArrayList<Floor> floors, ArrayList<Lift> lifts) {
        this.lifts = lifts;
        this.floors = floors;
    }
    
    /**
     * 
     * Method to find if the building has express lift (high express/low express or all)
     * @return  true if building has express lift(s)
     */
    
    public boolean isHavingExpressLift(){
        boolean result = false;
        for(Lift l:lifts){
            if(l.expressType>=1){
                result = true;
                break;
            }
        }
        return result;
    }
    /**
     * Start all lifts managed by the controller
     */
    public void startLift() {
        for (int i = 0; i < lifts.size(); i++) {
            lifts.get(i).start();
        }
    }

    /**
     * get a lift state by the lift id
     *
     * @param liftNumber id of the lift
     * @return LiftState the lift state data structure
     */
    public LiftState getLiftState(int liftNumber) {
        return lifts.get(liftNumber - 1).getState();
    }

    /**
     * get up waiting people for a floor by a floor number
     *
     * @param floorNumber floor number
     * @return number of people waiting going up in the floor
     */
    public int getNumberWaitingUp(int floorNumber) {
        return getFloor(floorNumber).getNumberWaitingUp();
    }

    /**
     * get down waiting people for a floor by a floor number
     *
     * @param floorNumber floor number
     * @return number of people waiting going down in the floor
     */
    public int getNumberWaitingDown(int floorNumber) {
        return getFloor(floorNumber).getNumberWaitingDown();
    }

    /**
     * get a floor by floor number
     *
     * @param floorNumber floor number
     * @return the floor number
     */
    public Floor getFloor(int floorNumber) {
        return floors.get(floorNumber - 1);
    }

    /**
     * terminate all lift
     */
    public void stopLifts() {
        for (int i = 0; i < lifts.size(); i++) {
            lifts.get(i).setStopRunning();
        }
        for (int i = 0; i < floors.size(); i++) {
            //((Floor)floors.get(i)).closeLog();
        }
        //if(log != null)log.close();
    }

    /**
     * get all lifts
     *
     * @return all lifts
     */
    public ArrayList<Lift> getLifts() {
        return lifts;
    }

    /**
     * call a lift for up request
     *
     * @param floorNumber floor number
     * @param person a passenger
     * @param expressType 0 normal 1 hight express 4 low express
     */
    public void summonLiftUp(int floorNumber, Passenger person, int expressType) {
        //System.out.println("sum up");
        Lift l = null;
        int counter = 0;
        while (l == null) { //&& person.getKeepRunning()){  // need this to stop processes stuck in this loop when program stops
            //if(Simulator.debug) log.write("" + ++counter + " tries to summon up elevator to floor " + floorNumber); 
            
            //assign express lift firstly
            if(person.isExpress){
                //System.out.println("aa");
                l = this.getExpressLift(person,expressType);
            }
            if(l!=null){
                l.callUp(floorNumber - 1);
                if (person.personID == 100001) {
                    //DetialedLiftView.lift = l;
                    //DetialedLift.lift = l;
                    //dl.enableButtonAccordingToLiftType(l.expressType);
                }
            }else{
                l = getSameFloorLift(floorNumber,person);
                if(l!=null&&l.expressType>0&&!person.isExpress){
                    l = null;
                }
                if (l != null) {
                    
                //if(Simulator.debug) log.write("Setting up destination for elevator " + e.getElevatorNumber() + " same floor " + floorNumber);
                    //l.activeLift.interrupt();

                    l.callUp(floorNumber - 1);
                    if (person.personID == 100001) {
                        //DetialedLiftView.lift = l;
                        //DetialedLift.lift = l;
                        //dl.enableButtonAccordingToLiftType(l.expressType);
                    }

                } else {
                    if (floorNumber > 1) {
                        l = getFreeLift(floorNumber,person);
                    }
                    if(l!=null&&l.expressType>0&&!person.isExpress){
                        l = null;
                    }
                    if (l != null) {
                    //l.activeLift.interrupt();

                        l.callUp(floorNumber - 1);
                        if (person.personID == 100001) {
                            //DetialedLiftView.lift = l;
                            //DetialedLift.lift = l;
                            //dl.enableButtonAccordingToLiftType(l.expressType);

                        }
                    } else {
                        if (floorNumber > 1) { // there won't be any below floor 1
                            l = getNearestLift(floorNumber, Lift.MOVING_UP,person);
                        }
                        if (l != null) {
                       //if(Simulator.debug) log.write("Setting destination for elevator " + e.getElevatorNumber() + " from  below floor " + floorNumber);
                            //l.activeLift.interrupt();

                            l.callUp(floorNumber - 1);
                            if (person.personID == 100001) {
                                //DetialedLiftView.lift = l;
                                //DetialedLift.lift = l;
                                //dl.enableButtonAccordingToLiftType(l.expressType);
                            }

                        } else {
                            //if(Simulator.debug) log.write("Looking for closest stopped elevator for up floor " + floorNumber);
                            l = getNearestLift(floorNumber, Lift.NO_DIRECTION,person);
                            if (l != null) {
                           //if(Simulator.debug) log.write("Setting destination for stopped elevator " + e.getElevatorNumber() + " for up floor " + floorNumber);
                                //l.activeLift.interrupt();

                                l.callUp(floorNumber - 1);
                                if (person.personID == 100001) {
                                    //DetialedLiftView.lift = l;
                                    //DetialedLift.lift = l;
                                    //dl.enableButtonAccordingToLiftType(l.expressType);
                                }

                            } else {
                                //if(Simulator.debug) log.write("Looking for elevator coming down " + floorNumber);
                                l = getLift(floorNumber, Lift.MOVING_DOWN,person);
                                if (l != null) {
                                    //l.activeLift.interrupt();

                                    l.callUp(floorNumber - 1);
                                    if (person.personID == 100001) {
                                        //DetialedLiftView.lift = l;
                                        //DetialedLift.lift = l;
                                        //dl.enableButtonAccordingToLiftType(l.expressType);
                                    }

                                }// end null any
                            }// end null nearest stopped
                        } // end null nearest moving up
                    }
                }
            }// end null same floor
            if (l == null) {
                try {
               //System.out.println("!!!!!!");
                    //System.out.println("22222");
                    Thread.currentThread().sleep(1000); //wait a second and try again
                } catch (InterruptedException ix) {
            //intentionally left empty
                    //System.out.println("callUp excetpion");
                }
            }//end if null for sleep
        }// end while
    }
    
    //return ture if this lift can be used
    /**
     * 
     * Check if the passenger home floor and destination floor math the express lift type
     * @param l a lift
     * @param p a passenger
     * @return true is the passenger home floor and destination floor math the express lift type 
     */
    private boolean liftExpressCheck(Lift l, Passenger p) {
        
        boolean result = false;
      
        if(l.expressType==0){
            result = true;
            
        }else if(l.expressType==1){
            //0,1,nf/2-
            if(p.getHomeFloorNumber()==1||p.getHomeFloorNumber()==2||p.getHomeFloorNumber()==Building.MAX_FLOORS/2+1){
                
               result = true;
                
            }
            
            if(p.getHomeFloorNumber()==Building.MAX_FLOORS){
                result = true;
            }
            
            
        }else if(l.expressType == 4){
            //System.out.println("!!!!!!");
            if(p.getHomeFloorNumber()==1 && p.getDestFloorNumber()<=Building.MAX_FLOORS/2+1){
                result = true;
                
            }
            if(p.getHomeFloorNumber()==2 && p.getDestFloorNumber()<=Building.MAX_FLOORS/2+1){
                result = true;
                
            }
            if(p.getHomeFloorNumber()==Building.MAX_FLOORS/2+1){
                result = true;
            }
        }
        return result;
    }

    /**
     * 
     * Get a express lift according to requriement
     * @param p a passenger
     * @param expressType required express type
     * @return a lift match the requirement 
     */
    public synchronized Lift getExpressLift(Passenger p,int expressType){
        Lift lift = null;
        //System.out.println(expressType);
        for (Lift lift1 : lifts) {
            //express check
            if(lift1.expressType==expressType){
                if(this.liftExpressCheck(lift1, p)){
                        
                        lift = lift1;
                        break;
                   
                    
                }
                 
            }
            
        }

        return lift;
    }
    
    /**
     * get a free lift
     *
     * @param floorNumber floor number
     * @param p a passenger
     * @return a lift
     */
    public synchronized Lift getFreeLift(int floorNumber,Passenger p) {
        Lift lift = null;
        //System.out.println("aaaa");
        for (Lift lift1 : lifts) {
            //express check
            if(this.liftExpressCheck(lift1, p)){
                
                if (lift1.getState().numberPassengers == 0 && lift1.getState().motionState == 0) {
                    
                    lift = lift1;
                    
                    break;
                } else {
                    lift = null;
                }
                
            }
            
        }

        return lift;
    }

    /**
     * call a lift for moving down request
     *
     * @param floorNumber floor number
     * @param person a passenger
     * @param expressType 0 1 or 4
     */
    public void summonLiftDown(int floorNumber, Passenger person, int expressType) {
        //System.out.println("sum down"); 
        Lift l = null;
        int counter = 0;
        while (l == null) { //&& person.getKeepRunning()){  // need this to stop processes stuck in this loop when program stops
            //if(Simulator.debug) log.write("" + ++counter + " tries to summon down elevator to floor " + floorNumber); 
            if(person.isExpress){
                l = this.getExpressLift(person,expressType);
                //System.out.println("++++");
            }
            
            if(l!=null){
                l.callUp(floorNumber - 1);
                if (person.personID == 100001) {
                    //DetialedLiftView.lift = l;
                    //DetialedLift.lift = l;
                    //dl.enableButtonAccordingToLiftType(l.expressType);
                }
            }else{
            //assign express lift firstly
            l = getSameFloorLift(floorNumber,person);
            if(l!=null&&l.expressType>0&&!person.isExpress){
                l = null;
            }
            if (l != null) {
                //if(Simulator.debug) log.write("Setting down destination for elevator " + e.getElevatorNumber() + " same floor " + floorNumber);
                //l.activeLift.interrupt();

                l.callDown(floorNumber - 1);
                if (person.personID == 100001) {
                    //DetialedLiftView.lift = l;
                    //DetialedLift.lift = l;
                    //dl.enableButtonAccordingToLiftType(l.expressType);
                }

            } else {
                if (floorNumber != Building.MAX_FLOORS) {
                    l = getFreeLift(floorNumber,person);

                }
                if(l!=null&&l.expressType>0&&!person.isExpress){
                    l = null;
                }
                if (l != null) {
                    //l.activeLift.interrupt();

                    l.callDown(floorNumber - 1);
                    if (person.personID == 100001) {
                        //DetialedLiftView.lift = l;
                        //DetialedLift.lift = l;
                        //dl.enableButtonAccordingToLiftType(l.expressType);
                    }

                } else {
                    if (floorNumber != Building.MAX_FLOORS) {  // there won't be any above the top floor
                        //if(Simulator.debug) log.write("looking for one moving down from above to floor " + floorNumber);
                        l = getNearestLift(floorNumber, Lift.MOVING_DOWN,person);

                    }
                    if (l != null) {
                       //if(Simulator.debug) log.write("Setting destination for elevator " + e.getElevatorNumber() + "from  above floor " + floorNumber);
                        //l.activeLift.interrupt();

                        l.callDown(floorNumber - 1);
                        if (person.personID == 100001) {
                            //DetialedLiftView.lift = l;
                            //DetialedLift.lift = l;
                            //dl.enableButtonAccordingToLiftType(l.expressType);
                        }

                    } else {
                   //if(Simulator.debug) log.write("Looking for closest stopped elevator for down floor " + floorNumber);

                        l = getNearestLift(floorNumber, Lift.NO_DIRECTION,person);

                        if (l != null) {
                           //if(Simulator.debug) log.write("Setting destination for stopped elevator " + e.getElevatorNumber() + " for down floor " + floorNumber);
                            //l.activeLift.interrupt();

                            l.callDown(floorNumber - 1);
                            if (person.personID == 100001) {
                                //DetialedLiftView.lift = l;
                                //DetialedLift.lift = l;
                                //dl.enableButtonAccordingToLiftType(l.expressType);
                            }

                        } else {
                       //if(Simulator.debug) log.write("Looking for elevator coming up " + floorNumber);

                            l = getLift(floorNumber, Lift.MOVING_UP,person);

                            if (l != null) {
                              //if(Simulator.debug) log.write("Setting destination for moving up elevator " + e.getElevatorNumber() + " for floor " + floorNumber);
                                //l.activeLift.interrupt();

                                l.callDown(floorNumber - 1);
                                if (person.personID == 100001) {
                                    ///DetialedLiftView.lift = l;
                                    //DetialedLift.lift = l;
                                    //dl.enableButtonAccordingToLiftType(l.expressType);
                                }

                            }// end null any
                        }// end null stopped
                    }// end null moving down

                }
            } //end null same floor
            }
            if (l == null) {
                try {
                    //System.out.println("11111");
                    Thread.currentThread().sleep(1000); //wait a second and try again
                } catch (InterruptedException ix) {
             //intentionally left empty
                    //System.out.println("callUp excetpion");
                }
            } //end if null for sleep
        }// end while
    }

    /**
     * get a lift
     *
     * @param floorNumber floor number
     * @param direction moving up or down
     * @return Lift a lift
     */
    private synchronized Lift getLift(int floorNumber, int direction, Passenger p) {
        Lift closestLift = null;
        //System.out.println("Get Lift!");
        float closestFloor = 0;
        float closest = Building.MAX_FLOORS + 1;
        Lift currentLift = null;
        float currentFloorNumber = 0;
        for (int i = 0; i < lifts.size(); i++) {
            currentLift = lifts.get(i);

            //express
            if(currentLift.expressType>0&&!p.isExpress){
                       
            }else{
                currentFloorNumber = currentLift.getCurrentFloorNumber();
                if (direction == Lift.MOVING_UP) { // summon up
                    if (currentFloorNumber > closestFloor && currentFloorNumber < floorNumber) {
                        if (currentLift.isSpeedSlowerEnoguh(floorNumber)) {
                            closestLift = currentLift;
                            closestFloor = currentFloorNumber;
                        }
                    }
                } else if (direction == Lift.MOVING_DOWN) { // summon down
                    if (currentFloorNumber < closestFloor && currentFloorNumber > floorNumber) {
                        if (currentFloorNumber > closestFloor && currentFloorNumber < floorNumber) {
                            if (currentLift.isSpeedSlowerEnoguh(floorNumber)) {
                                closestLift = currentLift;
                                closestFloor = currentFloorNumber;
                            }
                        }
                    }
                }    
            }
            
 
        }
        return closestLift;
    }

    /**
     * get nearest lift from the request level
     *
     * @param floorNumber floor number
     * @param direction moving up or moving down
     * @param  Passenger a passenger
     * @return
     */
    private synchronized Lift getNearestLift(int floorNumber, int direction, Passenger p) {
        //System.out.println("Nearest!");
        Lift closestLift = null;
        float closestFloor = 0;
        float closest = Building.MAX_FLOORS + 1;
        Lift currentLift = null;
        float currentFloorNumber = 0;
        for (int i = 0; i < lifts.size(); i++) {
            currentLift = lifts.get(i);
            //if lift is express and passenger dest not in the express floor do not do any thing
            if(currentLift.expressType>0&&!p.isExpress){
            }else{
                       
            
                currentFloorNumber = currentLift.getCurrentFloorNumber();
                if (direction == Lift.MOVING_UP) { // summon up
                    if (currentFloorNumber > closestFloor && currentFloorNumber < floorNumber) {
                        if (currentFloorNumber > closestFloor && currentFloorNumber < floorNumber) {
                            if (currentLift.isSpeedSlowerEnoguh(floorNumber)) {
                                if(currentLift.getState().doorState!=2){
                                    closestLift = currentLift;
                                    closestFloor = currentFloorNumber;
                                }
                            }
                        }
                    }
                } else if (direction == Lift.MOVING_DOWN) { // summon down
                    if (currentFloorNumber < closestFloor && currentFloorNumber > floorNumber) {
                        if (currentLift.isSpeedSlowerEnoguh(floorNumber)) {
                            if(currentLift.getState().doorState!=2){
                                closestLift = currentLift;
                                closestFloor = currentFloorNumber;
                            }
                            
                        }
                    }
                } else if (direction == Lift.NO_DIRECTION) { // get one close but not on the same floor
                    if (currentFloorNumber != floorNumber && Math.abs(currentFloorNumber - floorNumber) < closest) {
                        if (currentLift.isSpeedSlowerEnoguh(floorNumber)) {
                            if(currentLift.getState().doorState!=2){
                                closestLift = currentLift;
                                closest = Math.abs(currentFloorNumber - floorNumber);
                            }
                            
                        }
                    }
                }
            
            
            }
        }
        return closestLift;
    }

    //THIS PART CAN BE IMPORVED

    /**
     * get the lift the same floor as the request
     *
     * @param floorNumber
     * @return
     */
    private synchronized Lift getSameFloorLift(int floorNumber,Passenger p) {
        Lift l = null;
        LiftState state = null;
       //System.out.println("Get Some!");
        //can be optimized if there are free lift, use free lift firstly
        for (int i = 0; i < lifts.size(); i++) {
            l = lifts.get(i);

            //express
            if(l.expressType>0&&!p.isExpress){
                    
            
                
            }else{
                state = l.getState();
                if (l.getCurrentFloorNumber() == floorNumber && state.motionState == Lift.STOPPED && state.numberPassengers == 0) {
                    if(l.getState().doorState!=2){
                        break;
                    }else{
                        l = null;
                    }
                    

                } else {
                    l = null;
                }
            
            }
            

        }
        return l;
    }
    
    /**
     * Get all floors
     * @return floors in the building 
     */
    public ArrayList<Floor> getFloors(){
        return this.floors;
    }

    /**
     * notify the floor that a lift is arrived
     *
     * @param floorNumber floor number
     * @param lift a lift
     */
    public void liftArrived(int floorNumber, Lift lift) {
        //System.out.println("333333333333");
        int direction = lift.getState().motionDirection;
        Floor floor = getFloor(floorNumber);
        if (direction == Lift.MOVING_UP && floor.isSummonUp()) {
            floor.liftArrivedUp(lift);
            if (lift.passengers.isEmpty()) {
                floor.liftArrivedDown(lift);
            }
        } else if (direction == Lift.MOVING_DOWN && floor.isSummonDown()) {
            floor.liftArrivedDown(lift);
            //new code
            if (lift.passengers.isEmpty()) {
                floor.liftArrivedUp(lift);
            }
        } else if (floor.isSummonUp() && direction == 0) {

            floor.liftArrivedUp(lift);
        } else if (floor.isSummonDown() && direction == 0) {

            floor.liftArrivedDown(lift);
        } else {
            if (lift.passengers.isEmpty()) {
                if (floor.isSummonUp()) {
                    floor.liftArrivedUp(lift);

                } else if (floor.isSummonDown()) {
                    floor.liftArrivedDown(lift);

                } else if (!floor.getUpWaiting().isEmpty()||floor.getNumberWaitingUpExpress()>0) {
                    floor.liftArrivedUp(lift);
                } else if (!floor.getDownWaiting().isEmpty()||floor.getNumberWaitingDownExpress()>0) {
                    floor.liftArrivedDown(lift);
                } else if (floor.getUpWaiting().size() > floor.getDownWaiting().size()) {
                    floor.liftArrivedUp(lift);
                } else {
                    
                    floor.liftArrivedDown(lift);
                }
            }else{
                if (floor.isSummonUp()) {
                    floor.liftArrivedUp(lift);

                } else if (floor.isSummonDown()) {
                    floor.liftArrivedDown(lift);

                } else if (!floor.getUpWaiting().isEmpty()||floor.getNumberWaitingUpExpress()>0) {
                    floor.liftArrivedUp(lift);
                } else if (!floor.getDownWaiting().isEmpty()||floor.getNumberWaitingDownExpress()>0) {
                    floor.liftArrivedDown(lift);
                } else if (floor.getUpWaiting().size() > floor.getDownWaiting().size()) {
                    floor.liftArrivedUp(lift);
                } else {
                    
                    floor.liftArrivedDown(lift);
                }
                
            }

        }
    }
    
    public boolean isHasHELift(){
        boolean result = false;
        for(Lift l:lifts){
            if(l.expressType==1){
                result = true;
                break;
            }
        }
        
        return result;
    }
    public boolean isHasLELift(){
        boolean result = false;
        for(Lift l:lifts){
            if(l.expressType==4){
                result = true;
                break;
            }
        }
        
        return result;
    }
    public boolean isHasELift(){
        boolean result = false;
        for(Lift l:lifts){
            if(l.expressType==4||l.expressType==1){
                result = true;
                break;
            }
        }
        return result;
    }
}
