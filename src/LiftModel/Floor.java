package LiftModel;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import view2D.FloorStatisticsPanel;


/**
 * model a floor.
 * a floor has passengers queues for both going up/down
 * it also in charge of send request to central controller
 * 
 * @author louis
 * @version 1.1
 */
public class Floor {
    
    private int floorNumber;
    private volatile boolean summonUp;
    private volatile boolean summonDown;
    
    private ArrayList<Passenger> upWaiting = new ArrayList<Passenger>(); // of type Person
    private ArrayList<Passenger> downWaiting = new ArrayList<Passenger>();
    
    
    private ArrayList<Passenger> upExpressWaiting = new ArrayList<Passenger>();
    private ArrayList<Passenger> downExpressWaiting = new ArrayList<Passenger>();
    
    private ArrayList<Passenger> upLowExpressWaiting = new ArrayList<Passenger>();
    private ArrayList<Passenger> downLowExpressWaiting = new ArrayList<Passenger>();
    
    private ArrayList<Passenger> upHighExpressWaiting = new ArrayList<Passenger>();
    private ArrayList<Passenger> downHighExpressWaiting = new ArrayList<Passenger>();
    
    
    private ArrayList<Passenger> rupWaiting = new ArrayList<Passenger>();
    private ArrayList<Passenger> rdownWaiting = new ArrayList<Passenger>(); 
    
    
    
    
    
    private static CentralController centralController;
    //private Logger log;    
    private FloorStatisticsPanel fsp;

    /**
     * time reference
     */
    public static SimulationTimer st;

    /**
     * Passenger assigner used in the simulator
     */
    public static PassengerAssigner pa;
    
    /**
     * for testing purpose only
     */
    public boolean isIterating = false;

    /**
     * for testing purpose only
     */
    public boolean isDownWaitingIterating = false;
    
    /**
     *
     * Constructor
     * 
     * @param level number of floors, start from 1
     */
    public Floor(int level){
        floorNumber = level;
        if(floorNumber==1||floorNumber==2||floorNumber==Building.MAX_FLOORS/2+1||floorNumber==Building.MAX_FLOORS){
            fsp = new FloorStatisticsPanel(this,true);
        }else{
            fsp = new FloorStatisticsPanel(this,false);
        }
        
    }

    /**
     *
     * @return the statistics panel for this floor
     */
    public FloorStatisticsPanel getFsp() {
        return fsp;
    }

    /**
     *
     * @return the floor number for this floor
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * 
     * @return up waiting queue
     */
    public ArrayList<Passenger> getUpWaiting() {
        return upWaiting;
    }

    /**
     *
     * @return down waiting queue
     */
    public ArrayList<Passenger> getDownWaiting() {
        return downWaiting;
    }
    
    /**
     *
     * @param controller the controller in the simulator 
     */
    public static void setCentralController(CentralController controller){
       centralController = controller;
    }    

    /**
     * 
     * @return number of passengers in both high and low up express queue 
     */
    public int getNumberWaitingUpExpress(){
        return this.upExpressWaiting.size()+this.upHighExpressWaiting.size()+this.upLowExpressWaiting.size();
    }
    /**
     * 
     * @return number of passengers in both high and low down express queue
     */
    public int getNumberWaitingDownExpress(){
        return this.downExpressWaiting.size()+this.downHighExpressWaiting.size()+this.downLowExpressWaiting.size();
    }
    
    /**
     * 
     * @return number of passengers in high up express queue
     */
    public int getNumberWaitingUpHighExpress(){
        return this.upHighExpressWaiting.size();
    }
    
    /**
     * 
     * @return number of passengers in high down express queue
     */
    public int getNumberWaitingDownHighExpress(){
        return this.downHighExpressWaiting.size();
    }
    
    /**
     * 
     * @return number of passengers in low up express queue
     */
    public int getNumberWaitingUpLowExpress(){
        return this.upLowExpressWaiting.size();
    }
    
    /**
     * 
     * @return number of passengers in low down express queue
     */
    public int getNumberWaitingDownLowExpress(){
        return this.downLowExpressWaiting.size();
    }
    
    /**
     *
     * @return number of passengers in normal up queue
     */
    public int getNumberWaitingUp(){
       return upWaiting.size();
    }

    /**
     *
     * @return number of passengers in normal down queue
     */
    public int getNumberWaitingDown(){
       return downWaiting.size();
    }

    /**
     * summon a lift for going up
     * @param person the passenger want to go up
     */
    public synchronized void summonLiftUp(Passenger person) {
       //if(Simulator.debug) log.write("Summon up for person " + person.getPersonNumber());
       upWaiting.add(person);
       Building.addEmployee(person);
       //start record time, if it is reset
       if(person.timeRec.startWaitTime==0){
           person.timeRec.startWaitTime = st.timeEclpse;
       }
       fsp.numberOfWaittingUp.setText(upWaiting.size()+"");
       fsp.repaint();
       //if(!summonUp){//if already summoned no need to do it again
          //if(Simulator.debug) log.write("Light off summon UP for person " + person.getPersonNumber());
          //System.out.println("sum flooar:"+floorNumber);
          centralController.summonLiftUp(floorNumber, person,0);
          summonUp = true;
          fsp.getUpButton().setBackground(Color.red);
          fsp.getUpButton().setForeground(Color.red);
       //}
    }
    /**
     * summon a express up lift
     * @param person the passenger want to go up via express lift
     */
    public synchronized void summonExpressLiftUp(Passenger person) {
       //if(Simulator.debug) log.write("Summon up for person " + person.getPersonNumber());
       this.upExpressWaiting.add(person);
       Building.addEmployee(person);
       //start record time, if it is reset
       if(person.timeRec.startWaitTime==0){
           person.timeRec.startWaitTime = st.timeEclpse;
       }
       fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
       //numberOfExpressWaittingUp
       fsp.repaint();
       //if(!summonUp){//if already summoned no need to do it again
          //if(Simulator.debug) log.write("Light off summon UP for person " + person.getPersonNumber());
          //System.out.println("sum flooar:"+floorNumber);
          centralController.summonLiftUp(floorNumber, person,0);
          summonUp = true;
          fsp.getUpButtonExpress().setBackground(Color.red);
          fsp.getUpButtonExpress().setForeground(Color.red);
       //}
    }
    /**
     * summon a low express lift for going up
     * @param person a passenger
     */
    public synchronized void summonLowExpressLiftUp(Passenger person) {
       //if(Simulator.debug) log.write("Summon up for person " + person.getPersonNumber());
       this.upLowExpressWaiting.add(person);
       Building.addEmployee(person);
       //start record time, if it is reset
       if(person.timeRec.startWaitTime==0){
           person.timeRec.startWaitTime = st.timeEclpse;
       }
       fsp.numberOfWaittingUpExpress.setText(this.getNumberWaitingUpExpress()+"");
       //numberOfExpressWaittingUp
       fsp.repaint();
       //if(!summonUp){//if already summoned no need to do it again
          //if(Simulator.debug) log.write("Light off summon UP for person " + person.getPersonNumber());
          //System.out.println("sum flooar:"+floorNumber);
          centralController.summonLiftUp(floorNumber, person,4);
          summonUp = true;
          fsp.getUpButtonExpress().setBackground(Color.red);
          fsp.getUpButtonExpress().setForeground(Color.red);
       //}
    }
    
    /**
     * summon a high express lift for going up
     * @param person a passenger
     */
    public synchronized void summonHighExpressLiftUp(Passenger person) {
       //if(Simulator.debug) log.write("Summon up for person " + person.getPersonNumber());
       this.upHighExpressWaiting.add(person);
       Building.addEmployee(person);
       //start record time, if it is reset
       if(person.timeRec.startWaitTime==0){
           person.timeRec.startWaitTime = st.timeEclpse;
       }
       fsp.numberOfWaittingUpExpress.setText(this.getNumberWaitingUpExpress()+"");
       //numberOfExpressWaittingUp
       fsp.repaint();
       //if(!summonUp){//if already summoned no need to do it again
          //if(Simulator.debug) log.write("Light off summon UP for person " + person.getPersonNumber());
          //System.out.println("sum flooar:"+floorNumber);
          centralController.summonLiftUp(floorNumber, person,1);
          summonUp = true;
          fsp.getUpButtonExpress().setBackground(Color.red);
          fsp.getUpButtonExpress().setForeground(Color.red);
       //}
    }
    
    /**
     * summon a lift for going down
     * @param person a passenger
     */
    public void summonLiftDown(Passenger person) {
      downWaiting.add(person);
      Building.addEmployee(person);
      if(person.timeRec.startWaitTime==0){
           person.timeRec.startWaitTime = st.timeEclpse;
       }
      fsp.numberOfWaittingDown.setText(downWaiting.size()+"");
      fsp.repaint();
      //if(!summonDown){ // id already summoned no need to do it again
         //if(Simulator.debug) log.write("Light off summon DOWN for person " + person.getPersonNumber());
         centralController.summonLiftDown(floorNumber, person,0);
         summonDown = true;
         fsp.getDownButton().setBackground(Color.red);
         fsp.getDownButton().setForeground(Color.red);
      //}
    }  
    
    /**
     * summon a express lift for going down
     * @param person a passenger
     */
    public void summonExpressLiftDown(Passenger person) {
      this.downExpressWaiting.add(person);
      //System.out.println(downExpressWaiting.size());
      Building.addEmployee(person);
      if(person.timeRec.startWaitTime==0){
           person.timeRec.startWaitTime = st.timeEclpse;
       }
      fsp.numberOfWaittingDownExpress.setText(downExpressWaiting.size()+"");
      fsp.repaint();
      //if(!summonDown){ // id already summoned no need to do it again
         //if(Simulator.debug) log.write("Light off summon DOWN for person " + person.getPersonNumber());
         //centralController.summonLiftDown(floorNumber, person);
         //summonDown = true;
         centralController.summonLiftDown(floorNumber, person,0);
         fsp.getDownButtonExpress().setBackground(Color.red);
         fsp.getDownButtonExpress().setForeground(Color.red);
      //}
    }
    
    /**
     * summon a low express lift for going down
     * @param person a passenger
     */
    public void summonLowExpressLiftDown(Passenger person) {
      this.downLowExpressWaiting.add(person);
      //System.out.println(downExpressWaiting.size());
      Building.addEmployee(person);
      if(person.timeRec.startWaitTime==0){
           person.timeRec.startWaitTime = st.timeEclpse;
       }
      fsp.numberOfWaittingDownExpress.setText(this.getNumberWaitingDownExpress()+"");
      //fsp.repaint();
      //if(!summonDown){ // id already summoned no need to do it again
         //if(Simulator.debug) log.write("Light off summon DOWN for person " + person.getPersonNumber());
         centralController.summonLiftDown(floorNumber, person,4);
         //summonDown = true;

         fsp.getDownButtonExpress().setBackground(Color.red);
         fsp.getDownButtonExpress().setForeground(Color.red);
      //}
    } 
    
    /**
     * Summon a high express lift for going down
     * @param person a passenger
     */
    public void summonHighExpressLiftDown(Passenger person) {
      this.downHighExpressWaiting.add(person);
      //System.out.println(downExpressWaiting.size());
      Building.addEmployee(person);
      if(person.timeRec.startWaitTime==0){
           person.timeRec.startWaitTime = st.timeEclpse;
       }
      fsp.numberOfWaittingDownExpress.setText(this.getNumberWaitingDownExpress()+"");
      //fsp.repaint();
      //if(!summonDown){ // id already summoned no need to do it again
         //if(Simulator.debug) log.write("Light off summon DOWN for person " + person.getPersonNumber());
         centralController.summonLiftDown(floorNumber, person,1);
         //summonDown = true;
         fsp.getDownButtonExpress().setBackground(Color.red);
         fsp.getDownButtonExpress().setForeground(Color.red);
      //}
    }
    /**
     *
     * @return true if the floor is summon up
     */
    public boolean isSummonUp(){
       return summonUp;
    }

    /**
     *
     * @return true if the floor is summon down
     */
    public boolean isSummonDown(){
       return summonDown;
    }
    //return true if shoud
    /**
     * 
     * @param l the arrived lift
     * @param p the passenger
     * @return  true if the passenger's destination match the lift express type
     */
    private boolean shouldEnter(Lift l, Passenger p){
        boolean result = false;
        if(l.expressType==0){
            result = true;
            //System.out.println("1111");
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
        }
        return result;
    }

    
    /**
     *
     * @param lift the lift arrived 
     */
    public void liftArrivedUp(Lift lift) {
       //System.out.println("3333");
       Passenger p = null;
       summonUp = false;
    
       if(lift.expressType==1){
           
           
            synchronized(upHighExpressWaiting){
                Iterator<Passenger> pI = upHighExpressWaiting.iterator();
               
                while(pI.hasNext()){
                    this.isIterating = true;
                    //enter Lift
                    
                    p=pI.next();
                    //System.out.println("4444");
                    //p.enterLift(lift);

                    //remove it from queue
                    if(lift.getPassengers().size()<Lift.getMAX_OCCUPANCY()){
                       
                        if(lift.getState().doorState==1){
                            
                            if(p.getDestFloorNumber()!=0){

                                         lift.enterLift(p);
                                         pI.remove();
                                         fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                                     
                               

                            }else{

                                 pI.remove();
                                 fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                                 pa.addPersonToReRequest(p);
                            }


                        }else{

                             pI.remove();
                             fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                             pa.addPersonToReRequest(p);
                             //this.summonLiftUp(p);
                        }

                        //System.out.println("3333");

                    }else{

                        //System.out.println("6666");
                        rupWaiting.add(p);
                        pI.remove();
                        fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                        pa.addPersonToReRequest(p);

                        //this.summonLiftUp(p);

                        //wait until lift go awasy and re send wait
                    }

                 }
                 if(!rupWaiting.isEmpty()){
                     //reRequestUp();
                 }
                 this.isIterating = false;
                 setFloorState();

            }       
       }else if(lift.expressType==4){
            synchronized(upLowExpressWaiting){
                Iterator<Passenger> pI = upLowExpressWaiting.iterator();
                while(pI.hasNext()){
                    this.isIterating = true;
                    //enter Lift
                    p=pI.next();
                    //System.out.println("4444");
                    //p.enterLift(lift);

                    //remove it from queue
                    if(lift.getPassengers().size()<Lift.getMAX_OCCUPANCY()){
                        //System.out.println("5555");
                        if(lift.getState().doorState==1){
                            if(p.getDestFloorNumber()!=0){
                                //express check
                                 if(lift.expressType==4){
                                     
                                         lift.enterLift(p);
                                         pI.remove();
                                         fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                                     
                                 }else{
                                     lift.enterLift(p);
                                     pI.remove();
                                     fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                                 }

                            }else{

                                 pI.remove();
                                 fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                                 pa.addPersonToReRequest(p);
                            }


                        }else{

                             pI.remove();
                             fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                             pa.addPersonToReRequest(p);
                             //this.summonLiftUp(p);
                        }

                        //System.out.println("3333");

                    }else{

                        //System.out.println("6666");
                        rupWaiting.add(p);
                        pI.remove();
                        fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                        pa.addPersonToReRequest(p);

                        //this.summonLiftUp(p);

                        //wait until lift go awasy and re send wait
                    }

                 }
                 if(!rupWaiting.isEmpty()){
                     //reRequestUp();
                 }
                 this.isIterating = false;
                 setFloorState();

            }       
       }else{

            synchronized(upWaiting){
                //System.out.println(upWaiting.size());
                Iterator<Passenger> pI = upWaiting.iterator();
                while(pI.hasNext()){
                    this.isIterating = true;
                    p=pI.next();
                    if(lift.getPassengers().size()<Lift.getMAX_OCCUPANCY()){
                        //System.out.println("enter2");
                        if(lift.getState().doorState==1){
                            if(p.getDestFloorNumber()!=0){
                 
                                     //System.out.println("enter");
                                     lift.enterLift(p);
                                     pI.remove();
                                     fsp.numberOfWaittingUp.setText(upWaiting.size()+"");
                                 

                            }else{
                                //System.out.println("enter2");
                                 pI.remove();
                                 fsp.numberOfWaittingUp.setText(upWaiting.size()+"");
                                 pa.addPersonToReRequest(p);
                            }


                        }else{
                            //System.out.println("enter3");
                             pI.remove();
                             fsp.numberOfWaittingUp.setText(upWaiting.size()+"");
                             pa.addPersonToReRequest(p);
                             //this.summonLiftUp(p);
                        }

                        //System.out.println("3333");

                    }else{
                        //System.out.println("enter4");
                        //System.out.println("6666");
                        rupWaiting.add(p);
                        pI.remove();
                        fsp.numberOfWaittingUp.setText(upWaiting.size()+"");
                        pa.addPersonToReRequest(p);

                        //this.summonLiftUp(p);

                        //wait until lift go awasy and re send wait
                    }

                 }
                 if(!rupWaiting.isEmpty()){
                     //reRequestUp();
                 }
                 this.isIterating = false;
                 setFloorState();
                pI = upExpressWaiting.iterator();
                while(pI.hasNext()){
                    this.isIterating = true;
                    //enter Lift
                    p=pI.next();
                    //System.out.println("4444");
                    //p.enterLift(lift);

                    //remove it from queue
                    if(lift.getPassengers().size()<Lift.getMAX_OCCUPANCY()){
                        //System.out.println("5555");
                        if(lift.getState().doorState==1){
                            if(p.getDestFloorNumber()!=0){
                                //express check
                                 if(lift.expressType>=1){
                                     if(this.shouldEnter(lift, p)){
                                         lift.enterLift(p);
                                         pI.remove();
                                         fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                                     }
                                 }else{
                                     lift.enterLift(p);
                                     pI.remove();
                                     fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                                 }

                            }else{

                                 pI.remove();
                                 fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                                 pa.addPersonToReRequest(p);
                            }


                        }else{

                             pI.remove();
                             fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                             pa.addPersonToReRequest(p);
                             //this.summonLiftUp(p);
                        }

                        //System.out.println("3333");

                    }else{

                        //System.out.println("6666");
                        rupWaiting.add(p);
                        pI.remove();
                        fsp.numberOfWaittingUpExpress.setText(upExpressWaiting.size()+"");
                        pa.addPersonToReRequest(p);

                        //this.summonLiftUp(p);

                        //wait until lift go awasy and re send wait
                    }

                 }
                 if(!rupWaiting.isEmpty()){
                     //reRequestUp();
                 }
                 this.isIterating = false;
                 setFloorState();


            }       
       }

       //if(Simulator.debug) log.write("Elevator " + elevator.getElevatorNumber() + " has arrived UP on " + getFloorNumber());
    } // end elevatorArrived
    
    /**
     * re-request
     */
    private void reRequestUp(){
      
        Iterator<Passenger> i = rupWaiting.iterator();
        while(i.hasNext()){
            Passenger p = i.next();
            this.summonLiftUp(p);
            i.remove();
        }

        
    }

    /**
     *
     * @param lift the lift arrived for going down
     */
    public void liftArrivedDown(Lift lift){
        //System.out.println("3333");
        Passenger p = null;
        summonDown = false;
        if(lift.expressType==1){
            
            synchronized(downHighExpressWaiting){
                Iterator<Passenger> pI = downHighExpressWaiting.iterator();
                while(pI.hasNext()){
                    //enter Lift
                    this.isIterating = true;
                    p=pI.next();
                    //p.enterLift(lift);

                    //remove it from queue
                    if(lift.getPassengers().size()<Lift.getMAX_OCCUPANCY()){
                        if(lift.getState().doorState==1){
                            if(p.getDestFloorNumber()!=0){
                                    
                                        lift.enterLift(p);
                                        pI.remove();
                                        fsp.numberOfWaittingDownExpress.setText(this.getNumberWaitingDownExpress()+"");
                                    
                                


                            }else{

                                pI.remove();
                                fsp.numberOfWaittingDownExpress.setText(this.getNumberWaitingDownExpress()+"");
                                pa.addPersonToReRequest(p);
                            }


                        }else{

                            pI.remove();
                            fsp.numberOfWaittingDownExpress.setText(this.getNumberWaitingDownExpress()+"");
                            //this.summonLiftDown(p);
                            //wait until lift go awasy and re send wait
                            pa.addPersonToReRequest(p);
                            //this.summonLiftDown(p);
                        }

                    }else{
                        rdownWaiting.add(p);
                        pI.remove();
                        fsp.numberOfWaittingDownExpress.setText(this.getNumberWaitingDownExpress()+"");
                        pa.addPersonToReRequest(p);

                    }

                }
                if(!downWaiting.isEmpty()){
                    //reRequestDown();
                }
                this.isIterating = false;
                setFloorState();
            }        
        }else if(lift.expressType==4){
            synchronized(downLowExpressWaiting){
                Iterator<Passenger> pI = downLowExpressWaiting.iterator();
                while(pI.hasNext()){
                    //enter Lift
                    this.isIterating = true;
                    p=pI.next();
                    //p.enterLift(lift);

                    //remove it from queue
                    if(lift.getPassengers().size()<Lift.getMAX_OCCUPANCY()){
                        if(lift.getState().doorState==1){
                            if(p.getDestFloorNumber()!=0){
                                if(lift.expressType==4){
                                   
                                        lift.enterLift(p);
                                        pI.remove();
                                        fsp.numberOfWaittingDownExpress.setText(this.getNumberWaitingDownExpress()+"");
                                    
                                }else{
                                    lift.enterLift(p);
                                    pI.remove();
                                    fsp.numberOfWaittingDownExpress.setText(this.getNumberWaitingDownExpress()+"");
                                }


                            }else{

                                pI.remove();
                                fsp.numberOfWaittingDownExpress.setText(this.getNumberWaitingDownExpress()+"");
                                pa.addPersonToReRequest(p);
                            }


                        }else{

                            pI.remove();
                            fsp.numberOfWaittingDownExpress.setText(this.getNumberWaitingDownExpress()+"");
                            //this.summonLiftDown(p);
                            //wait until lift go awasy and re send wait
                            pa.addPersonToReRequest(p);
                            //this.summonLiftDown(p);
                        }

                    }else{
                        rdownWaiting.add(p);
                        pI.remove();
                        fsp.numberOfWaittingDownExpress.setText(this.getNumberWaitingDownExpress()+"");
                        pa.addPersonToReRequest(p);

                    }

                }
                if(!downWaiting.isEmpty()){
                    //reRequestDown();
                }
                this.isIterating = false;
                setFloorState();
            }        
        }else{
            synchronized(downWaiting){
                Iterator<Passenger> pI = downWaiting.iterator();
                while(pI.hasNext()){
                    //enter Lift
                    this.isIterating = true;
                    p=pI.next();
                    //p.enterLift(lift);

                    //remove it from queue
                    if(lift.getPassengers().size()<Lift.getMAX_OCCUPANCY()){
                        if(lift.getState().doorState==1){
                            if(p.getDestFloorNumber()!=0){
                                if(lift.expressType>=1){
                                    if(this.shouldEnter(lift, p)){
                                        lift.enterLift(p);
                                        pI.remove();
                                        fsp.numberOfWaittingDown.setText(downWaiting.size()+"");
                                    }
                                }else{
                                    lift.enterLift(p);
                                    pI.remove();
                                    fsp.numberOfWaittingDown.setText(downWaiting.size()+"");
                                }


                            }else{

                                pI.remove();
                                fsp.numberOfWaittingDown.setText(downWaiting.size()+"");
                                pa.addPersonToReRequest(p);
                            }


                        }else{

                            pI.remove();
                            fsp.numberOfWaittingDown.setText(downWaiting.size()+"");
                            //this.summonLiftDown(p);
                            //wait until lift go awasy and re send wait
                            pa.addPersonToReRequest(p);
                            //this.summonLiftDown(p);
                        }

                    }else{
                        rdownWaiting.add(p);
                        pI.remove();
                        fsp.numberOfWaittingDown.setText(downWaiting.size()+"");
                        pa.addPersonToReRequest(p);

                    }

                }
                if(!downWaiting.isEmpty()){
                    //reRequestDown();
                }
                this.isIterating = false;
                setFloorState();
            }        
        }

       //if(Simulator.debug) log.write("Elevator " + elevator.getElevatorNumber() + " has arrived DOWN on " + getFloorNumber());
    }
    
    /**
     * re request going down
     */
    private void reRequestDown(){
        
        Iterator<Passenger> i = downWaiting.iterator();
        while(i.hasNext()){
            Passenger p = i.next();
            this.summonLiftDown(p);
            i.remove();
        }
        
        
    }
    /**
     * set the floor status
     */
    private void setFloorState(){
        if(upWaiting.isEmpty()){
            this.summonUp = false;
            fsp.getUpButton().setBackground(null);
            fsp.getUpButton().setForeground(null);

        }else{
            
        }
        if(downWaiting.isEmpty()){
            this.summonDown = false;
            fsp.getDownButton().setBackground(null);
            fsp.getDownButton().setForeground(null);
        }else{
        
        }
        if(upExpressWaiting.isEmpty()){
            this.summonUp = false;
            fsp.getUpButtonExpress().setBackground(null);
            fsp.getUpButtonExpress().setForeground(null);
        }
        if(downExpressWaiting.isEmpty()){
            this.summonDown = false;
            fsp.getDownButtonExpress().setBackground(null);
            fsp.getDownButtonExpress().setForeground(null);
        }
    
    }

    /**
     *
     * @param person the person who want stop waits
     */
    public synchronized void stopWaiting(Passenger person) {
      //if(Simulator.debug) log.write("Person " + person.getPersonNumber() + "  has stopped waiting on " + getFloorNumber());
      
        synchronized(upWaiting){
             if(upWaiting.remove(person)){
               fsp.numberOfWaittingUp.setText(upWaiting.size()+"");
               
               //fsp.repaint();
               //System.out.println("removed up"+this.getFloorNumber());
             }else{
                 
             }      
        }      
      
      
        synchronized(downWaiting){
             if(downWaiting.remove(person)){
                 fsp.numberOfWaittingDown.setText(downWaiting.size()+"");
                 if(downWaiting.isEmpty()){
                     this.summonDown = false;
                     fsp.getDownButton().setBackground(null);
                 }
                 
             }else{
                
             }        

        }     
        fsp.repaint();

   } 
}
