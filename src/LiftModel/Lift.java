package LiftModel;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import view2D.DetialedLift;
import view2D.DetialedLiftView;
import view2D.LiftDebugInfPanel;



/**
 * Model of the lift
 * @author louis
 * @version 1.1
 */
public class Lift implements Runnable{

    /**
     * lift moving
     */
    public static final int MOVING = 1;

    /**
     * lift stop
     */
    public static final int STOPPED = 0;

    /**
     * moving up
     */
    public static final int MOVING_UP = 1;

    /**
     * moving down
     */
    public static final int MOVING_DOWN = -1;

    /**
     * no direction
     */
    public static final int NO_DIRECTION = 0;
    
    /**
     * width of lift
     */
    public float liftWidth;

    /**
     * width of door
     */
    public float doorWidth;
    
    private static final long INACTIVE_TIME = 1000 * 2;

    /**
     * door open
     */
    public static final int DOOR_OPEN = 1;

    /**
     * door closing/opening
     */
    public static final int DOOR_CLOSING_OPENNING =2;
    public int homeFloor;
    /**
     * door closed
     */
    public static final int DOOR_CLOSED = 0;
    private static final long FLOOR_WAIT_TIME = 500;
    
    
    
    private static int MAX_OCCUPANCY = 20;
    private int id;
    private boolean keepRunning;
    private int doorState;
    private boolean requestDoorOpen;
    
    
    private long idleTime;
    private float accelation;
    private float doorOpenCloseTime;
    private int maxSpeed;
    private float currentSpeed;
    private boolean speedChange = false;
    private float enterInTimeB;
    private float enterInTimeH;
    
    private long ridleTime;
    private float raccelation;
    private float rdoorOpenCloseTime;
    private float renterInTimeB;
    private float renterInTimeH;
    
    private boolean isInterrupted = false;
    
    private int rmaxSpeed;
    private float rcurrentSpeed;
    
    private float currentFloorNumber;
    
    
    
    private int motionState;

    /**
     * passengers inside
     */
    public ArrayList<Passenger> passengers;
    //public CopyOnWriteArrayList<Passenger> passengerList;

    /**
     * inside passenger's destination
     */
        public boolean[] destinationList = new boolean[Building.MAX_FLOORS];

    /**
     *call up destination
     */
    public boolean[] callUp = new boolean[Building.MAX_FLOORS];

    /**
     * call down destination
     */
    public boolean[] callDown = new boolean[Building.MAX_FLOORS];
    private int motionDirection;
    

    //private LiftController controller;
    private static CentralController centralController; 

    /**
     * lift thread
     */
    public Thread activeLift;
    private LiftState state;
    //private ArrayList<Integer> specialSpeeds;
    private float[] specialSpeeds = new float[Building.MAX_FLOORS];
    
    private static Clock sc;

    /**
     * time lapse
     */
    public static SimulationTimer st;

    /**
     * statistics collector
     */
    public static StatisticsCollector collector;
    
    /**
     * speed factor
     */
    public static float speedFactor;
    private volatile boolean pause = false;
    private final Object pauseLock = new Object();
    /**
     * express type
     * 0:normal
     * 1:hight express
     * 4:low expressliftNumber
     */
    public int expressType=0; //high 1,2,3, low 4, normal 0
    private LiftDebugInfPanel ldip;

    /**
     * total number of trip
     */
    public int tripNumber=0;

    /**
     * set clock
     * @param sc system clock
     */
    public static void setSc(Clock sc) {
        Lift.sc = sc;
        
    }

    /**
     * set lift debug information panel
     * @param ldip lift debug information panel
     */
    public void setLdip(LiftDebugInfPanel ldip) {
        this.ldip = ldip;
    }

    /**
     * return lift debug information panel
     * @return lift debug information panel
     */
    public LiftDebugInfPanel getLdip() {
        return ldip;
    }

    /**
     * set door status 
     * @param requestDoorOpen boolean
     */
    public void setRequestDoorOpen(boolean requestDoorOpen) {
        this.requestDoorOpen = requestDoorOpen;
    }

    /**
     * 
     * @param ridleTime ridleTime
     */
    public void setRidleTime(long ridleTime) {
        this.ridleTime = ridleTime;
    }

    /**
     *
     * @param rdoorOpenCloseTime rdoorOpenCloseTime
     */
    public void setRdoorOpenCloseTime(float rdoorOpenCloseTime) {
        this.rdoorOpenCloseTime = rdoorOpenCloseTime;
    }

    /**
     *
     * @param rmaxSpeed rmaxSpeed
     */
    public void setRmaxSpeed(int rmaxSpeed) {
        this.rmaxSpeed = rmaxSpeed;
    }

    /**
     * 
     * @param renterInTimeB renterInTimeB
     */
    public void setRenterInTimeB(float renterInTimeB) {
        this.renterInTimeB = renterInTimeB;
    }

    /**
     *
     * @param renterInTimeH renterInTimeH
     */
    public void setRenterInTimeH(float renterInTimeH) {
        this.renterInTimeH = renterInTimeH;
    }

    /**
     *
     * @param MAX_OCCUPANCY MAX_OCCUPANCY
     */
    public static void setMAX_OCCUPANCY(int MAX_OCCUPANCY) {
        Lift.MAX_OCCUPANCY = MAX_OCCUPANCY;
    }
    
    /**
     *
     * @param f speed factor
     */
    public void setSpeedFactor(float f){
        
        speedFactor = f;
        maxSpeed = (int) (f*rmaxSpeed);
        accelation = (maxSpeed/raccelation)*f;
        idleTime = (long) (ridleTime/f);
        //maxSpeed = (int) (f*rmaxSpeed);
        //currentSpeed = currentSpeed*f;
        doorOpenCloseTime = rdoorOpenCloseTime/f;
        enterInTimeB = renterInTimeB/f;
        enterInTimeH = renterInTimeH/f;
        setSpecialSpeeds();
        speedChange=true;
        currentSpeed=0;
        this.currentFloorNumber = Math.round(currentFloorNumber);
    }

    /**
     *
     * @param currentFloorNumber currentFloorNumber
     */
    public void setCurrentFloorNumber(float currentFloorNumber) {
        this.currentFloorNumber = currentFloorNumber;
    }

    /**
     *
     * @param raccelation raccelation
     */
    public void setRaccelation(float raccelation) {
        this.raccelation = raccelation;
    }
    
    /**
     * Default constructor need lift id
     * @param id lift id
     */
    public Lift(int id){
        this.id = id;
        rmaxSpeed = 2;
        raccelation = rmaxSpeed/1;
        speedFactor = 1;
        currentSpeed = 0;
        rmaxSpeed = 2;
        ridleTime = 1000;
        idleTime = 1000;
        rdoorOpenCloseTime = 1000;
        setSpeedFactor(speedFactor);
        currentFloorNumber = 1;
        motionState = STOPPED;
        doorState = DOOR_CLOSED;
        
        liftWidth = 100;
        passengers = new ArrayList<Passenger>();
        state = new LiftState();
        for(int i = 0; i < destinationList.length; i++){
            destinationList[i] = false;
        }
        for(int i = 0; i < callUp.length; i++){
            callUp[i] = false;
        }
        for(int i = 0; i < callDown.length; i++){
            callDown[i] = false;
        }
    }

    /**
     * calculate lift special speed for travel time calculation
     */
    public void setSpecialSpeeds(){
        for(int i=0;i<Building.MAX_FLOORS;i++){
            float v = (float) (accelation*Math.sqrt(((i+1)/accelation)));
            if(v<maxSpeed){
                specialSpeeds[i] = v;
            }else{
                v= maxSpeed;
                specialSpeeds[i] = v;
            }
            //System.out.println(v);
        }
        ///System.out.println("...........");
    }

    /**
     *
     * @param controller controller
     */
    public static void setCentralController(CentralController controller){
       centralController = controller;
    }

    /**
     * stop
     */
    public void setStopRunning(){
       keepRunning = false;
    }

    /**
     *
     * @return getCurrentFloorNumber
     */
    public float getCurrentFloorNumber(){
        return currentFloorNumber;
    }

    /**
     * stop
     */
    public void stop(){
        if(activeLift!=null){
            try {
                activeLift.sleep(1000000000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        
    }

    /**
     * start
     */
    public void start() {
      //if(Simulator.debug) log = new Logger("Elevator" + elevatorID) ;
       keepRunning = true;
        if(activeLift == null){
          activeLift = new Thread(this);
          //activeElevator.setDaemon(true);
          //activeLift.setPriority(10 );
          activeLift.start();
           //System.out.println("11111");
       }
    } // end start 

    /**
     *
     * @param floorNumber floor number
     * @throws LiftMovingException moving exception
     */
    public synchronized void summonDestination(int floorNumber) throws LiftMovingException {
       if(getCurrentFloorNumber() != floorNumber || passengers.isEmpty()){
          destinationList[floorNumber - 1] = true;
          //activeLift.interrupt();
       }else{
          throw new LiftMovingException();
       }
    } 

    /**
     * pause
     */
    public void pause()  
    {  
        this.pause = true;  
    } 
    
    /**
     * continue
     */
    public void continueWork(){
        this.pause = false;
        synchronized(pauseLock)  
        {  
            pauseLock.notify();  
        }
    }
    
    @Override
    public void run() {
       //if(Simulator.debug) log.write("Starting elevator " + elevatorID);
        while(keepRunning){
            synchronized(this.pauseLock){
                while (pause){
                     
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
          
            }
            switch(motionState){
                        case STOPPED:
                            //
                           //if(Simulator.debug) log.write("Elevator " + elevatorID + " is stopped");
                           if(passengers.isEmpty() && !isDestination()){
                              motionDirection = NO_DIRECTION;
                              //if(Simulator.debug) log.write("Elevator " + elevatorID + " is empty and has no destination");
                              //System.out.println("!!!!!!!");
                              //System.out.println("2222");
                              action((long) (idleTime));
                              //this.destinationList[0]=true;
                           }else if(isArrived()){
                              //if(Simulator.debug) log.write("Elevator " + elevatorID + " has arrived on " + currentFloorNumber);
                              //System.out.println("!!!!!!!");
                              ///System.out.println("33333"); 
                              tripNumber++; 
                              removeDestination();
                              openDoor();
                              
                              closingDoor();
                              /*
                              if(!this.passengers.isEmpty()){
                                  motionState =1;
                              }
                              */

                           }else{
                              //System.out.println("***####");
                              //if(Simulator.debug) log.write("Elevator " + elevatorID + " is continuing to travel");
                              //System.out.println("....");
                              travel();
                           }
                           break;
                        case MOVING:
                            //
                           // System.out.println("4444"); 
                           if(isArrived()){
                               //System.out.println("++++++");
                                       String moveDire = "";
                            /*
                            if(motionDirection == MOVING_UP){
                                moveDire="Moving up";
                            }else if(motionDirection == MOVING_DOWN){
                                moveDire="Moving down";
                            }
                                       */
                            String string = st.timeEclpse+" Lift "+id+" arrived at  "+currentFloorNumber+" with "+passengers.size()+" passengers";
                            ldip.addADebugInf(string);
                            stopLift();
                           }else{
                              travel();
              
                           }
                           break;
                    }               

           //if(Simulator.debug) log.write(getState().toString());
        }
        //System.out.println("error");
    }
    /**
     * is lift have destination
    */
    private boolean isDestination(){
       //System.out.println("2222");
       boolean returnValue = false;
       //System.out.println("aaaa");
       for(int i = 0; i < destinationList.length; i++){
           //System.out.println(" "+destinationList[i]+callUp[i]+callDown[i]+",");
          if(destinationList[i]||callUp[i]||callDown[i]){
             
            returnValue = true;
             
             //System.out.println("dest"+(i+1));
          }
       }
       //System.out.println("#############"+returnValue);
       //System.out.println(returnValue);
       return returnValue;
    }  
   private void action(long time){
      try{
         activeLift.sleep(time);
      }catch(InterruptedException ix){
         //intentionally left empty
      }
   }

   private synchronized boolean isArrived(){
      boolean returnValue = false;
      //System.out.println(isInterrupted);
      double approxFloor = currentFloorNumber;//Math.round(currentFloorNumber);
      //System.out.println("arrive floor"+currentFloorNumber);
      //System.out.println("arrive floor after round"+currentFloorNumber);
      if(this.motionDirection==0&&isInterrupted==false){
        if(destinationList[(int)approxFloor - 1]||callUp[(int)approxFloor - 1]||callDown[(int)approxFloor - 1]){
            returnValue = true;
            //System.out.println("1");
            //System.out.println(this.motionDirection);
            motionState = STOPPED;
        }
        if(!returnValue){
            //System.out.println("1");
        }
      }else if (this.motionDirection==1&&isInterrupted==false){
        
        if(destinationList[(int)approxFloor - 1]||callUp[(int)approxFloor - 1]){
            returnValue = true;
            //System.out.println("2");
            //System.out.println(this.motionDirection);
            motionState = STOPPED;
        }else if(passengers.isEmpty()){
            if(callDown[(int)approxFloor-1]){
                returnValue = true;
                //System.out.println("3");
            }
        }
        if(!returnValue){
            //System.out.println("2");
        }
      }else if (this.motionDirection==-1&&isInterrupted==false){
          //System.out.println(approxFloor);
        if(destinationList[(int)approxFloor - 1]||callDown[(int)approxFloor - 1]){
            
            returnValue = true;
            //System.out.println("4");
            //System.out.println(this.motionDirection);
            motionState = STOPPED;
        }else if(passengers.isEmpty()){
            if(callUp[(int)approxFloor - 1]){
                returnValue = true;
                //System.out.println("5");
            }
        }
        if(!returnValue){
            //System.out.println("3");
        }
      }else{
          //System.out.println("do not know");
      }
      if(!returnValue&&this.passengers.isEmpty()){
          //System.out.println("aaaaaaaa");
      }
      //System.out.println("Arrived "+returnValue+"id"+id);
      //System.out.println(returnValue);
      if(isInterrupted==true){
          isInterrupted = false;
      }
      return returnValue;
   }
   
    /**
     * get lift status
     * @return lift status data structure
     */
    public synchronized LiftState getState(){
        state.elevatorID = id;
        state.currentFloorNumber = currentFloorNumber;
        state.motionState = motionState;
        state.motionDirection = motionDirection;
        state.numberPassengers = passengers.size();
        state.doorState = doorState;
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < destinationList.length; i++){
        sb.append(destinationList[i] ? "1" : "0");
        }
        state.destinations = sb.toString();
        return state;
    }
    
    private  void openDoor(){
       if(doorState == DOOR_CLOSED && motionState == STOPPED){
          doorState = 2; //openning
          doorOpenning();
          //action((long)doorOpenCloseTime);
          doorState = DOOR_OPEN;
          
          
          //action((long) (500/speedFactor));
          //notifyRiders();
          //remove passengers
          leaveLift();
          //System.out.println("1111");
          action((long) (idleTime));//give engouh time for riders to get off
          notifyController();
          //System.out.println("2222");
          action((long) (idleTime));//give engouh time for riders to get on
          //shoudl be floor wait time + peole move in time if possible
          //System.out.println("door open");
          
          
       }
    }
    
    private void doorOpenning(){
        float timeInt = this.doorOpenCloseTime/100;
        int counter =0;
        doorWidth = 0;
        //System.out.println("open");
        while(counter<100){
            synchronized(this.pauseLock){
                while (pause){
                     
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
          
            }
            counter++;
            try {
                doorWidth+= liftWidth/100;
                activeLift.sleep((long)timeInt);
            } catch (InterruptedException ex) {
                
            }
        }
    
    
    }
    private  void closingDoor(){
       
        doorState = 2; //openning
        doorClosing();
        requestDoorOpen = false;
        doorState = DOOR_CLOSED;

        //notifyRiders();
        //notifyController();
        //action((long)doorOpenCloseTime);//give people a change to request door open
       
        
    }
    
    private void doorClosing(){
        //System.out.println("close");
        float timeInt = this.doorOpenCloseTime/100;
        int counter =0;
        doorWidth = liftWidth;
        while(counter<100){
            synchronized(this.pauseLock){
                while (pause){
                     
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
          
            }
            counter++;
            try {
                doorWidth-= liftWidth/100;
                activeLift.sleep((long)timeInt);
            } catch (InterruptedException ex) {
                System.out.println("callUp excetpion");
            }
        }
    
    
    }
    /*
    private synchronized void notifyRiders(){
        synchronized(passengers){
            
            for(int i = 0; i < passengers.size(); i++){
                //passengers.get(i).leaveLift();
                
                passengers.get(i).attention();
                
                
                
            }
        }

    }
    */
    private synchronized void notifyController(){
       float approxFloor = Math.round(currentFloorNumber);
       //System.out.println("notify user "+ approxFloor);
       centralController.liftArrived((int)approxFloor, this);
    }

    private synchronized void resetDoorRequest(){
       requestDoorOpen = false;
    }
    private synchronized boolean isRequestDoorOpen(){
       return requestDoorOpen;
    }

    /**
     * call down a lift
     * @param floorNumber floor number
     */
    public synchronized void callDown(int floorNumber){
        //Thread.interrupted();
        synchronized(callDown){
            callDown[floorNumber] = true;
        }
        
        
    }

    /**
     * call up a lift
     * @param floorNumber floor number
     */
    public synchronized void callUp(int floorNumber){
        //Thread.interrupted();
        
        synchronized(callUp){
            callUp[floorNumber] = true;
            
        }
        
    }
    private synchronized void removeDestination(){
       float approxFloor = Math.round(currentFloorNumber);
       
       //potential problem here
       if(this.motionDirection==1){
           destinationList[(int)approxFloor - 1] = false;
           callUp[(int)approxFloor - 1] = false;
           //System.out.println("remove "+((int)approxFloor));
           if((int)currentFloorNumber == Building.MAX_FLOORS){
               callDown[(int)approxFloor - 1]=false;
           }
           if(passengers.isEmpty()){ //being called to this dest
               callDown[(int)approxFloor - 1]=false;
               //System.out.println("remove "+((int)approxFloor ));
               //System.out.println("aaaa");
           }
       }else if(this.motionDirection==-1){
           destinationList[(int)approxFloor - 1] = false;
           callDown[(int)approxFloor - 1] = false;
           //System.out.println("remove "+((int)approxFloor ));
           if((int)currentFloorNumber == 1){
               callUp[(int)approxFloor - 1]=false;
           }
           if(passengers.isEmpty()){ //being called to this dest
               callUp[(int)approxFloor - 1]=false;
              // System.out.println("remove "+((int)approxFloor ));
               //System.out.println("bbbbb");
           }
           
       }else if(this.motionState == 0){
           
           if(!this.passengers.isEmpty()&&currentFloorNumber==homeFloor){
            destinationList[(int)approxFloor - 1] = false;
            callUp[(int)approxFloor - 1] = false;
            callDown[(int)approxFloor - 1] = false;
            //System.out.println("remove "+((int)approxFloor ));
           }else{
               callUp[(int)approxFloor - 1] = false;
               callDown[(int)approxFloor - 1] = false;
               //System.out.println("remove "+((int)approxFloor ));
           }
           
           //destinationList[(int)approxFloor - 1] = false;
           //callUp[(int)approxFloor - 1] = false;
           //callDown[(int)approxFloor - 1] = false;
       }
       
       //System.out.println(" "+destinationList[(int)approxFloor - 1]+callDown[(int)approxFloor - 1]+callUp[(int)approxFloor - 1]);
       //System.out.println("remove destination"+approxFloor+",id:"+id);
       //destinationList[(int)approxFloor - 1] = false;
    }
    
    //important
    
    
    private void travel(){
      //System.out.println("call travel");  
      if(isDestination()){//need know where is nextDestination
         //if(Simulator.debug) log.write("Elevator has a destination");
         
         motionState = MOVING;
         if(motionDirection == MOVING_UP){
            //if(Simulator.debug) log.write("Moving up");
                     String moveDire = "";
                    if(motionDirection == MOVING_UP){
                        moveDire="Moving up";
                    }else if(motionDirection == MOVING_DOWN){
                        moveDire="Moving down";
                    }
                    String string = st.timeEclpse+" Lift "+id+" Door close "+moveDire+" with "+passengers.size()+" passengers";
                    ldip.addADebugInf(string);
            moveUp();
            //System.out.println("travel");
         }else if(motionDirection == MOVING_DOWN){
            //if(Simulator.debug) log.write("Moving Down");
                    String moveDire = "";
                    if(motionDirection == MOVING_UP){
                        moveDire="Moving up";
                    }else if(motionDirection == MOVING_DOWN){
                        moveDire="Moving down";
                    }
                    String string = st.timeEclpse+" Lift "+id+" Door close "+moveDire+" with "+passengers.size()+" passengers";
                    ldip.addADebugInf(string);
            moveDown();
         }else if(isDestinationAbove()){
            //if(Simulator.debug) log.write("Setting direction up");
            //System.out.println();
            
            motionDirection = MOVING_UP;
            moveUp();
         }else if(isDestinationBelow()){
             
            //if(Simulator.debug) log.write("Setting direction down");
            motionDirection = MOVING_DOWN;
            moveDown();
         }else{ //someone wants us where we are
             
            //if(Simulator.debug) log.write("someone wants on this floor " + currentFloorNumber);
            //System.out.println("@@@@@1");
            stopLift();
         }
      }else{ // no destination don't move;
         //if(Simulator.debug) log.write("There is no destination");
        //System.out.println("aaaaaa");
         //System.out.println("@@@@@2");
         motionDirection = NO_DIRECTION;
         motionState = STOPPED;
         action(INACTIVE_TIME);
      }
   }
   //good point

    /**
     * check if lift have enough distance to accept the calling
     * @param summonFloor calling floor number
     * @return ture yes false no
     */
       public boolean isSpeedSlowerEnoguh(int summonFloor){
       boolean result = false;
       
       int size = specialSpeeds.length+1;
       float [] speedArr = new float[size];
       for(int i=0;i<specialSpeeds.length;i++){
           speedArr[i] = specialSpeeds[i];
       }
       speedArr[size-1] = currentSpeed;
       Arrays.sort(speedArr);
       int locOfCurrentSpeed = Arrays.asList(speedArr).indexOf(currentSpeed);
       int minSpeedDownFloor = locOfCurrentSpeed+1+1;
       float distanceToSummonFloor = Math.abs((float)currentFloorNumber-(float)summonFloor);
       //distanceToSummonFloor
       //System.out.println("sumFloor:"+summonFloor);
       //System.out.println("distance:"+distanceToSummonFloor+","+minSpeedDownFloor);
       if (distanceToSummonFloor>minSpeedDownFloor){
           result =true;
       }
       if(motionState==0){
           result =true;
       }
       //System.out.println("resutl"+result);
       return result;
   } 

   
   private synchronized void stopLift(){
      //System.out.println("stoped");
      motionState = STOPPED;
   }
   
   private synchronized TravelSchedule moveScheduling(float v,float distance){

       if(currentSpeed<0){
           currentSpeed=0;
       }
       if(currentSpeed>v){
           currentSpeed = v;
       }
       //System.out.println("distance ini:"+distance);
       
       float t1 = (v-currentSpeed)/accelation;
       //System.out.println(t1);
       float t2 = v/accelation;
       float s1 = currentSpeed*t1+accelation*t1*t1/2;
       //System.out.println("s1:"+s1);
       float s2 = v*t2-accelation*t2*t2/2;
      // System.out.println("s2:"+s2);
       float t3 = 0;
       if((s1+s2)<distance){
           //System.out.println("s3:"+(distance - s1 - s2));
           t3 = (distance - s1 - s2 )/v;
       }
       TravelSchedule ts = new TravelSchedule();
       //System.out.println("distance cal:"+(s1+s2+(distance - s1 - s2)));
       ts.setSpeedUpTime(t1);
       ts.setSpeedDownTime(t2);
       ts.setMaxSpeedTime(t3);

       
       return ts;
   }
   
   
   private void moveUpAction(TravelSchedule ts, int nextFloor) throws InterruptedException{
       float t1 = ts.getSpeedUpTime();
       float tempT = 0;
       isInterrupted=false;
       while (tempT<t1){
           synchronized(this.pauseLock){
                while (pause){
                     
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
          
            }
           //if dest change through new request exception
           int newNextFloor = this.getNextUpFloorNumber();
           //System.out.println(newNextFloor+","+newNextFloor);
           if(newNextFloor!=-1){
               if(newNextFloor!=nextFloor){
                  
                    //throw new InterruptedException();
                   isInterrupted = true;
                   return;
                }
            }
            if(speedChange){
                speedChange = false;
                return;
            }
           tempT+=t1/(250);
           currentSpeed = currentSpeed+accelation*t1/(250);
           currentFloorNumber += currentSpeed*t1/(250) + accelation*(t1/(250))*(t1/(250))/2;
           //System.out.println(currentFloorNumber);
           //need to calcuate current location
           if(currentSpeed>maxSpeed){
               currentSpeed = maxSpeed;
           }
           activeLift.sleep((long) (t1*1000/(250)));
            //System.out.println("Current location:"+currentFloorNumber);
       }
       tempT = 0;
       float t2 = ts.getMaxSpeedTime();
       while (tempT<t2){
           synchronized(this.pauseLock){
                while (pause){
                     
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
          
            }
           if(speedChange){
                speedChange = false;
                return;
            }
           //if dest change through new request exception
           int newNextFloor = this.getNextUpFloorNumber();
           if(newNextFloor!=-1){
               if(newNextFloor!=nextFloor){
                   //return;
                    //throw new InterruptedException();
                   isInterrupted = true;
                   return;
                }
            }
           tempT+=t2/(250);
           //need to calcuate current location
           currentFloorNumber += currentSpeed*t2/(250);
           activeLift.sleep((long) (t2*1000/(250)));
           //System.out.println("Current location:"+currentFloorNumber);
       }
       
       tempT = 0;
       float t3 = ts.getSpeedDownTime();
       while(tempT<t3){
           synchronized(this.pauseLock){
                while (pause){
                     
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
          
            }
           if(speedChange){
                speedChange = false;
                return;
            }
           //if dest change through new request exception
           int newNextFloor = this.getNextUpFloorNumber();
           if(newNextFloor!=-1){
               if(newNextFloor!=nextFloor){
                   //return;
                    //throw new InterruptedException();
                   //isInterrupted = true;
                   //break;
                }
            }
           tempT+=t3/(250);
           currentSpeed = currentSpeed-accelation*t3/(250);
           currentFloorNumber += currentSpeed*t3/(250) - accelation*(t3/(250))*(t3/(250))/2;
           activeLift.sleep((long) (t3*1000/(250)));
           //System.out.println("Current location:"+currentFloorNumber);
       }
       //System.out.println("before round "+currentFloorNumber);
       currentFloorNumber = (float) Math.round(currentFloorNumber);
       if(currentFloorNumber>Building.MAX_FLOORS){
           currentFloorNumber = Building.MAX_FLOORS;
       }
       //System.out.println("after round" +currentFloorNumber);
   }
   private void moveDownAction(TravelSchedule ts, int nextStopFloor) throws InterruptedException{
       float t1 = ts.getSpeedUpTime();
       float tempT = 0;
       boolean isBreak = false;
       isInterrupted=false;
       while (tempT<t1){
           synchronized(this.pauseLock){
                while (pause){
                     
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
          
            }
           if(speedChange){
                speedChange = false;
                return;
            }
            int newNextFloor = this.getNextMoveDownFloor();
            //System.out.println(newNextFloor+","+newNextFloor);
            if(newNextFloor!=-1){
                if(newNextFloor!=nextStopFloor){
                     //throw new InterruptedException();
                    System.out.println("1");
                    isInterrupted = true;
                    return;
                    //break;
                 }
             }
            tempT+=t1/(250f);
            currentSpeed = currentSpeed+accelation*t1/(250f);
            currentFloorNumber -= (currentSpeed*t1/(250f) + accelation*(t1/(250f))*(t1/(250f))/2);
            //need to calcuate current location
        
            if(currentSpeed>maxSpeed){
                currentSpeed = maxSpeed;
            }
            
            activeLift.sleep((long) (t1*1000/(250)));
       }
       tempT = 0;
       float t2 = ts.getMaxSpeedTime();
       while (tempT<t2){
           synchronized(this.pauseLock){
                while (pause){
                     
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
          
            }
           if(speedChange){
                speedChange = false;
                return;
            }
           int newNextFloor = this.getNextMoveDownFloor();
           //System.out.println(newNextFloor+","+newNextFloor);
           if(newNextFloor!=-1){
               if(newNextFloor!=nextStopFloor){
                    //throw new InterruptedException();
                   isInterrupted = true;
                   System.out.println("2");
                    return;
                   //break;
                }
            }
           tempT+=t2/(250);
           //need to calcuate current location
          
           currentFloorNumber -= currentSpeed*t2/(250);
         
           activeLift.sleep((long) (t2*1000/(250)));
           
       }
       
       tempT = 0;
       float t3 = ts.getSpeedDownTime();
       while(tempT<t3){
           synchronized(this.pauseLock){
                while (pause){
                     
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
          
            }
           if(speedChange){
                speedChange = false;
                return;
            }
           int newNextFloor = this.getNextMoveDownFloor();
           //System.out.println(newNextFloor+","+newNextFloor);
           if(newNextFloor!=-1){
               if((float)newNextFloor!=nextStopFloor){
                    //throw new InterruptedException();
                   //isInterrupted = true;
                   //System.out.println("3");
                    //return;
                   //break;
                }
            }
           tempT+=t3/(250f);
           currentSpeed = currentSpeed-accelation*t3/(250f);
           
           currentFloorNumber -= (currentSpeed*t3/(250f) - accelation*(t3/(250f))*(t3/(250f))/2f);
           if(currentFloorNumber<1){
               break;
           }
           activeLift.sleep((long) (t3*1000/(250)));
            
       }
       //System.out.println("after round"+currentFloorNumber);
        if(!isBreak){
            currentFloorNumber = (float) Math.round(currentFloorNumber);//
            if(currentFloorNumber<1){
                currentFloorNumber = 1;
            }
        
        }else{
            currentFloorNumber = (float) Math.round(currentFloorNumber);//
            //currentFloorNumber+=1;
            if(currentFloorNumber<1){
                currentFloorNumber = 1;
            }
        }
        

        

       //System.out.println("after round" +currentFloorNumber);
   }
   private synchronized int getNextUpFloorNumber(){
       int nextStopFloor = -1;
 
       float apprxFloor = Math.round(getCurrentFloorNumber());
       //System.out.println(apprxFloor);
       if(apprxFloor>Building.MAX_FLOORS){
            apprxFloor = Building.MAX_FLOORS;
        }
        if(apprxFloor<1){
            apprxFloor = 1;
        }
        for(int i = (int)apprxFloor-1; i < destinationList.length; i++){
            if(this.passengers.size()<this.MAX_OCCUPANCY){
                if(destinationList[i]||callUp[i]){
                    nextStopFloor = i+1;//logical error once
                    break;
                }
            }else{
                if(destinationList[i]){
                    nextStopFloor = i+1;//logical error once
                    break;
                }
            }
            
        }
        if(nextStopFloor==-1){
            for(int i = (int)apprxFloor-1; i < destinationList.length; i++){
                if(callDown[i]){
                    nextStopFloor = i+1;//logical error once
                   // break;
                }
            }            
        }
        return nextStopFloor;
   }
   
   
   private  void moveUp(){
     
      if(isDestinationAbove()){
         if(currentFloorNumber != Building.MAX_FLOORS){
            int nextStopFloor = getNextUpFloorNumber();
            //System.out.println(nextStopFloor);
            if(nextStopFloor!=-1){
                float distance = nextStopFloor - currentFloorNumber;
                //System.out.println("dis cal:"+distance);
                float maxSpeedThisRun;
                float appxD;
                if(distance<=1){
                    maxSpeedThisRun = specialSpeeds[0];
                }else{
                    appxD = Math.round(distance);
                    maxSpeedThisRun = specialSpeeds[(int)appxD-1];
                }

                TravelSchedule ts = moveScheduling(maxSpeedThisRun,distance);
                 try {

                     moveUpAction(ts,nextStopFloor);


                 } catch (InterruptedException ex) {
                     System.out.println("callUp excetpion");
                 }            
            }

         }
      }else if(isDestinationBelow()){
          
         
         //if(Simulator.debug) log.write("moving up is changing direction");
         //System.out.println("error1"); 
         motionDirection = MOVING_DOWN; //  someone missed floor change direction
      }else{
         //if(Simulator.debug) log.write("move up is stopping");
        //System.out.println("travel");
         //System.out.println("error1");
         stopLift(); // only destination is this floor
         
      }
   }
   
   private int getNextMoveDownFloor(){
        int nextStopFloor  = -1;

        double approxCurrentFloor = Math.round(getCurrentFloorNumber());
        //could be - 2
        for(int i = (int)approxCurrentFloor-1; i >=1; i--){

            if(i>Building.MAX_FLOORS){
                i = Building.MAX_FLOORS;
            }
            if(passengers.size()<MAX_OCCUPANCY){
                if(destinationList[i-1]||callDown[i-1]){
                
                //System.out.println("current fllor"+approxCurrentFloor+"next down"+i);
                    nextStopFloor = i;
                    break;
                }
            }else{
                if(destinationList[i-1]){
                
                //System.out.println("current fllor"+approxCurrentFloor+"next down"+i);
                    nextStopFloor = i;
                    break;
                }
            }
            
        }
        if(nextStopFloor==-1){
            for(int i = (int)approxCurrentFloor-1; i >= 0; i--){

                if(i>Building.MAX_FLOORS){
                    i = Building.MAX_FLOORS;
                }
                if(callUp[i]){

                    //System.out.println("current fllor"+approxCurrentFloor+"next down"+i);
                    nextStopFloor = i+1;
                    
                    
                }
            }
        }
        return nextStopFloor;
   }
   private  void moveDown(){
      if(isDestinationBelow()){
         if(currentFloorNumber != 1){
            //if(Simulator.debug) log.write("move down moves down");
            int nextStopFloor = getNextMoveDownFloor();
            
            if(nextStopFloor != -1){
                //System.out.println("MoveDown current floor"+currentFloorNumber+"dest floor"+nextStopFloor);
                //System.out.println("current floor"+currentFloorNumber);
                float distance = currentFloorNumber - (float)nextStopFloor;
                //System.out.println("distance"+distance);
                //System.out.println("next down distance:"+distance);
                //System.out.println(distance);
               // distance=distance+1;
                float maxSpeedThisRun;
                float appxD;
                if(distance<=1.0){
                    maxSpeedThisRun = specialSpeeds[0];
                }else{
                    appxD = Math.round(distance);
                    maxSpeedThisRun = specialSpeeds[(int)appxD-1];
                }
                /*
                int specificalSpeedPos=0;
                for(int i=0;i<Building.MAX_FLOORS;i++){
                    if(distance<=i){
                        specificalSpeedPos = i;
                        break;
                    }
                }
                */
                //maxSpeedThisRun = specialSpeeds[specificalSpeedPos];
                //System.out.println("in moveDown"+distance);
                TravelSchedule ts = moveScheduling(maxSpeedThisRun,distance);
                //System.out.println(" "+ts.getMaxSpeedTime()+ts.getSpeedDownTime()+ts.getSpeedUpTime());
                 try {
                     //System.out.println("Move Down TO "+nextStopFloor);
                     moveDownAction(ts,nextStopFloor);

                 } catch (InterruptedException ex) {
                     //Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                 }            
            }

         }
      }else if(isDestinationAbove()){
         //if(Simulator.debug) log.write("move down is changing direction");
        // System.out.println("error2");
          motionDirection = MOVING_UP;  // someone missed floor change direction
      }else{
         //if(Simulator.debug) log.write("move down is stopping");
          //System.out.println("error2");
         stopLift(); // only destination is this flooor
      }
   }
   private  boolean isDestinationAbove(){
      boolean returnValue = false;
      //above problem
      double approxCurrentFloor = Math.ceil(getCurrentFloorNumber());
      
      for(int i = (int)approxCurrentFloor; i < destinationList.length; i++){

         if(destinationList[i]||callUp[i]){
            //System.out.println(i+","+destinationList[i]+callUp[i]);
            returnValue = true;
            break;
         }
      }
      if(returnValue == false){
        for(int i = (int)approxCurrentFloor; i < destinationList.length; i++){

           if(callDown[i]){
              //System.out.println(i+","+destinationList[i]+callUp[i]);
              returnValue = true;
              break;
           }
        }
      }
      //System.out.println("above"+returnValue);
      if(!returnValue){
        //System.out.println(" up false");
      }
      return returnValue;
   }
   private boolean isDestinationBelow(){
      boolean returnValue = false;    
      double approxCurrentFloor = Math.ceil(getCurrentFloorNumber());
      for(int i = (int)approxCurrentFloor - 2; i >= 0; i--){
         if(i>Building.MAX_FLOORS){
             i = Building.MAX_FLOORS;
         }
         
         if(destinationList[i]||callDown[i]){
             
            returnValue = true;
            break;
         }
      }
      if(returnValue == false){
        for(int i = (int)approxCurrentFloor - 2; i >= 0; i--){
           if(i>Building.MAX_FLOORS){
               i = Building.MAX_FLOORS;
           }

           if(callUp[i]){

              returnValue = true;
              break;
           }
        }
      }
      if(!returnValue){
        //System.out.println(" below false");
      }
      //System.out.println("below"+returnValue);
      return returnValue;
   }

    /**
     *
     * @return lift id
     */
    public int getId() {
        return id;
    }
    
    /**
     * passenger leave lift
     */
    public synchronized void leaveLift(){
        synchronized(passengers){
            if(doorState == DOOR_OPEN){
                Iterator<Passenger> it = this.passengers.iterator();
                while(it.hasNext()){
                    Passenger p = it.next();
                    if(p.getDestFloorNumber() == currentFloorNumber){
                        p.getTimeRec().id = p.personID;
                        p.getTimeRec().getOffTime = st.timeEclpse;
                        collector.addRec(p.getTimeRec());
                        it.remove();

                        
                        if(p.personID==100001){
                            DetialedLiftView.lift =null;
                            DetialedLift.lift = null;
                        }
                    }
                } 
            }        
        }

        
    }
    /*
    public synchronized  void leaveElevator(Person person) throws DoorClosedException{
       if(doorState == DOOR_OPEN){
          passengers.remove(person);
          person.timeRec.getOffTime = st.timeEclpse;
          //add the
          LiftTakenRec ltr = new LiftTakenRec();
          ltr.enterLiftTime = person.timeRec.enterLiftTime;
          ltr.getOffTime = person.timeRec.getOffTime;
          ltr.startWaitTime = person.timeRec.startWaitTime;
          collector.addRec(ltr);
          //put the ltr to Statistic collector
       }else{
          //System.out.println("******");
          //if(Simulator.debug) log.write("Elevator " + elevatorID + " door is closed can not leave.");
          throw new DoorClosedException();
       }
    }
    */

    /**
     * passenger enter the lift
     * @param person passenger
     */
    
    public synchronized void enterLift(Passenger person){
        if(doorState == DOOR_OPEN){
            synchronized(passengers){
                passengers.add(person);
                //System.out.println("aaaa");
                /*
                while(person.getDestFloorNumber()==0){
                    
                    try {
                        this.activeLift.sleep(500);
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                */
                this.destinationList[person.getDestFloorNumber()-1] = true;
               
                person.timeRec.enterLiftTime = st.timeEclpse;
            }
            

       }

    }
   /*
   public synchronized void enterElevator(Person person)throws LiftFullException, DoorClosedException{
      if(doorState == DOOR_OPEN){
         if(passengers.size() <= MAX_OCCUPANCY){
            passengers.add(person);
            person.timeRec.enterLiftTime = st.timeEclpse;
         }else{
           // if(Simulator.debug) log.write("Elevator " + elevatorID + " is full");
            throw new LiftFullException();
         }
      }else{
         //if(Simulator.debug) log.write("Elevator " + elevatorID + " door is closed can not enter.");
         //System.out.println("@@@@@@@@");
          throw new DoorClosedException();
         
      }
   }
   */    

    /**
     * passenger set destination
     * @param floorNumber floor number
     */
        
   public synchronized void setDestination(int floorNumber) {
       //Thread.interrupted();
      
        //System.out.println("set dest !!");
        if(floorNumber>0){
            destinationList[floorNumber - 1] = true;
        }
        
        //System.out.println("current floor"+currentFloorNumber+","+floorNumber+"is Dest");
        //Thread.interrupted();
       
         
   }

    /**
     *
     * @throws LiftMovingException moving exception
     */
    public synchronized void requestOpenDoor() throws LiftMovingException {
       /*
      if(motionState == STOPPED)
         //requestDoorOpen = true;
      else
         throw new LiftMovingException();
               */
   }

    /**
     *
     * @return MAX_OCCUPANCY
     */
    public static int getMAX_OCCUPANCY() {
        return MAX_OCCUPANCY;
    }

    /**
     * get lift passengers
     * @return passengers in a list
     */
    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }
    
    /**
     * 
     * @return description of lift
     */
    public String toString(){
        String s = new String();
        s+="<table border=1>";
        s+="<tr><td>ID:</td><td>"+id+"</td></tr>";
        s += "<tr><td>Capacity:</td><td>"+MAX_OCCUPANCY+"</td></tr>";
        s += "<tr><td>Accelation:</td><td>"+raccelation+"</td></tr>";
        s += "<tr><td>Max Speed:</td><td>"+rmaxSpeed+"</td></tr>";
        s += "<tr><td>Idle Time:</td><td>"+ridleTime+"</td></tr>";
        s += "<tr><td>Door Close/Open Time:</td><td>"+rdoorOpenCloseTime+"</td></tr>";
        if(this.expressType == 0){
            s+= "<tr><td>No Express</td></tr>";
        }else if(this.expressType == 1){
            s+= "<tr><td>High Express</td></tr>";
        }else if(this.expressType == 4){
            s+="<tr><td>Low Express</td></tr>";
        }
        s+="</table>";
        return s;
    
    }
   
}
