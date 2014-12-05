package LiftModel;



/**
 * Used to represent a passenger
 * 
 * @author luis_chen
 * @version 1.1
 */
public class Passenger {
    private int homeFloorNumber;
    private int destFloorNumber;

    /**
     * Person ID
     */
    public int personID;

    /**
     * Clock
     */
    public static Clock clock;

    /**
     * time lapse
     */
    public static SimulationTimer st;
    private Lift lift;
    private Floor floor;
    LiftTakenRec timeRec;
    public boolean isExpress=false;

    /**
     *
     * @param personID ID of the person
     */
    public Passenger(int personID){
        this.personID = personID;
        timeRec = new LiftTakenRec();

    }
    /*
    public void enterLift(Lift lift){
        this.lift = lift;
        try{
           //System.out.println("enter lift"+lift.getId());
         lift.enterLift(this);

         //we don't get here unless we entered the elevator
         lift.setDestination(destFloorNumber);
         //

         //System.out.println("set dest"+destination);

         //floor.stopWaiting(this);


         floor = null;

        }catch(LiftFullException fx){
           //System.out.println("....1");
           resetWaitForLift();
        }catch(DoorClosedException cx){
            /*
          try{
              lift.requestOpenDoor();//use loop so not reenterant method
           }catch(LiftMovingException mx){
              //System.out.println("....2");
              resetWaitForLift(); // assume the elevator has moved on
           }
                    
        }
    }*/
    


    /**
     *
     * @return home floor
     */
    public int getHomeFloorNumber() {
        return homeFloorNumber;
    }

    /**
     *
     * @param homeFloorNumber home floor
     */
    public void setHomeFloorNumber(int homeFloorNumber) {
        this.homeFloorNumber = homeFloorNumber;
    }

    /**
     *
     * @return destination floor
     */
    public int getDestFloorNumber() {
        return destFloorNumber;
    }

    /**
     *
     * @param destFloorNumber destination floor
     */
    public void setDestFloorNumber(int destFloorNumber) {
        this.destFloorNumber = destFloorNumber;
    }

    /**
     *
     * @return statistics data object
     */
    public LiftTakenRec getTimeRec() {
        return timeRec;
    }

    /**
     *
     * @param timeRec statistics data object
     */
    public void setTimeRec(LiftTakenRec timeRec) {
        this.timeRec = timeRec;
    }
    
    
}
