package LiftModel;



import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import view3D.AnimationPanel3D;


/**
 * Used to assign passengers in to the building
 * It will assign passengers to the right queue according to
 * configuration of the lift and the passengers' home and dest floors
 * @author luis
 * @version 1.1
 */
public class PassengerAssigner implements Runnable{
    public static Clock c;
    private List<Passenger> pList;
    private List<Passenger> morningList;
    private List<Passenger> dayList;
    private List<Passenger> lanuchGoOutList;
    private List<Passenger> lanuchGoInList;
    
    private static long morningDelay;
    private static long lanuchDelay;
    private static long dailyDelay;
    
    private static long rmorningDelay;
    private static long rlanuchDelay;
    private static long rdailyDelay;
    
    
    private Thread t;
    private boolean isRunning = false;
    private volatile boolean pauseWorking = false;
    private final Object pauseLock = new Object();

    /**
     * animation panel 3D used by the simulator
     */
    public static AnimationPanel3D ap3d;
    /**
     * building used by the simulator
     */
    public static Building building;

    /**
     * speed factor
     */
    public static float speedFactor;

    /**
     * testing use only
     */
    public volatile boolean isReListIterating = false;

    /**
     * testing use only
     */
    public boolean isTestScene = false;

    /**
     * testing use only
     */
    public boolean isMorningScene = false;
    
    private CopyOnWriteArrayList<Passenger> reRequestList;

    
    private CopyOnWriteArrayList<Passenger> bufferReRequestList;
    
    /**
     *  default constructor
     * 
     */
    public PassengerAssigner(){
        pList = new CopyOnWriteArrayList<Passenger>(); 
        morningList = new CopyOnWriteArrayList<Passenger>();
        lanuchGoOutList = new CopyOnWriteArrayList<Passenger>();
        lanuchGoInList = new CopyOnWriteArrayList<Passenger>();
        dayList = new CopyOnWriteArrayList<Passenger>();
        
        reRequestList = new CopyOnWriteArrayList<Passenger>(); 
        bufferReRequestList = new CopyOnWriteArrayList<Passenger>(); 
        speedFactor = 1;
        morningDelay = 3600; //ms
        rmorningDelay = 3600;
        
        dailyDelay = 22500;
        rdailyDelay = 22500;
        
        lanuchDelay = 5400;
        rlanuchDelay = 5400;
        
        //generatePassengers(500);
        generateMorningPassengers(1000);
        generateNormalDistributedPassengers(500);
        
        generateLanuchList();
        generateDailyList();
        
    }
    
    /**
     * set speed
     * @param speed speed
     */
    public static void setSpeed(long speed){
        morningDelay = rmorningDelay/speed;     
        dailyDelay = rdailyDelay/speed;     
        lanuchDelay = rlanuchDelay/speed;

    }
    
    /**
     * generate passengers during lunch time
     */
    public void generateLanuchList(){
        //go out
        int counter1 = 0;
        while(counter1 < 500){
            counter1++;
            Passenger p = new Passenger(counter1);
            int floor = this.setDestination();
            while(floor<=2){
                floor = this.setDestination();
            }
            p.setHomeFloorNumber(floor);
            p.setDestFloorNumber(2);
            this.lanuchGoOutList.add(p);
            
        }
        //go in
        int counter2 = 0;
        while(counter2 < 500){
            counter2++;
            Passenger p = new Passenger(counter1);
            p.setHomeFloorNumber(2);
            int floor = this.setDestination();
            while(floor<=1){
                floor = this.setDestination();
            }
            p.setDestFloorNumber(floor);
            this.lanuchGoInList.add(p);
        }
    }
    
    /**
     * generate passenger for daily scene
     */
    public void generateDailyList(){
        int counter = 0;
        while(counter < 1200){
            counter++;
            Passenger p = new Passenger(counter);
            int home = this.setDestination();
            int dest = this.setDestination();
            while (home <=2){
                home = this.setDestination();
            }
            while (dest==home||dest<=2){
                dest = this.setDestination();
            }

            p.setDestFloorNumber(dest);
            p.setHomeFloorNumber(home);
            this.dayList.add(p);
        }
    }
    
    /**
     * generate morning passengers
     * @param num number want to generated
     */
    public void generateMorningPassengers(int num){
        Random randomGenerator = new Random();
        while(num>0){
            num--;
            int rNum = randomGenerator.nextInt(10);
            rNum+=1;
            if(rNum==9||rNum==10){
                //home floor 1
                Passenger p = new Passenger(num);
                //int home = setDestination();
                p.setHomeFloorNumber(1);
                int dest = setDestination();
                while(dest<=1){
                    dest = setDestination();
                } 
                p.setDestFloorNumber(dest);
                morningList.add(p);
            }else{
                //home floor 2
                Passenger p = new Passenger(num);
                //int home = setDestination();
                p.setHomeFloorNumber(2);
                int dest = setDestination();
                while(dest<=2){
                    dest = setDestination();
                } 
                p.setDestFloorNumber(dest);
                morningList.add(p);
            }
        }
    }
    
    /**
     *
     * @param p add passenger to re request
     */
    public synchronized void addPersonToReRequest(Passenger p){
        reRequestList.add(p);
    
    }
    
    /**
     * generate normal distributed passengers
     * @param num number want to generate
     */
    private void generateNormalDistributedPassengers(int num){
        while(num>0){
            Passenger p = new Passenger(num);
            int home = setDestination();
            p.setHomeFloorNumber(home);
            int dest = setDestination();
            while (home==dest){
                dest = setDestination();
            }
            p.setDestFloorNumber(dest);
            pList.add(p);
            num--;
        }
    }
    
    
    /**
     * generate passengers
     * @param num number want to generate
     */
    private void generatePassengers(int num){
        while(num>0){
            
            Passenger p = new Passenger(num);
            p.setHomeFloorNumber(1);
            int dest = setDestination();
            while(dest<=1){
                dest = setDestination();
               
            } 
             p.setDestFloorNumber(dest);
            pList.add(p);
            num--;
        }
    }
    /**
     * random generate home floor and destination floor
     * @return generated floor number
     */
    private int setDestination(){
        return ((int) (1 + ((Math.random() * 10000) % (Building.MAX_FLOORS ))));
    
    }
    
    /**
     * start
     */
    public void start(){
        isRunning = true;
        if(t==null){
            t = new Thread(this);
            t.start();
        }
    
    }
    
    /**
     * stop
     */
    public void pause(){  
        this.pauseWorking = true;  
    } 
    
    /**
     * continue
     */
    public void continueWork(){  
        this.pauseWorking = false;  
  
       synchronized(pauseLock)  
       {  
            pauseLock.notify();  
        }  
    } 
    
    //true is the person can go express
    /**
     * Match passenger with express lift type
     * @param p the passenger
     * @return type matched express lift
     */
    private int isGoExpress(Passenger p){
        int result = 0;
        if(p.getHomeFloorNumber()==1||
                p.getHomeFloorNumber()==2||
                p.getHomeFloorNumber()==Building.MAX_FLOORS/2+1){
            if(p.getDestFloorNumber()>Building.MAX_FLOORS/2+1){
                result = 1; //high express up
                return result;
            }
        }
        if(p.getHomeFloorNumber()==1||
                p.getHomeFloorNumber()==2){
            if(p.getDestFloorNumber()<=Building.MAX_FLOORS/2+1){
                result = 4; //low express up
                return result;
            }
        }
        if(p.getHomeFloorNumber()==Building.MAX_FLOORS){
            if(p.getDestFloorNumber()>=Building.MAX_FLOORS/2+1){
                result = 1; //high express down
                 return result;
            }
            if((p.getDestFloorNumber()==1||p.getDestFloorNumber()==2)&&p.isExpress){
                result =1;
                return result;
                
            }
        }
        if(p.getHomeFloorNumber()==Building.MAX_FLOORS/2+1){
            if(p.getHomeFloorNumber()<Building.MAX_FLOORS/2+1){
                result = 4; //low express down
                 return result;
            }
        }
        /*
        if(building.centralController.isHavingExpressLift()){
            ArrayList<Lift> lifts = building.centralController.getLifts();
            for(Lift l:lifts){
                if(l.expressType==0){
                    result = false;
                }else if(l.expressType==1){
                    //0,1,nf/2-
                    if(p.getHomeFloorNumber()==1||p.getHomeFloorNumber()==2||p.getHomeFloorNumber()==Building.MAX_FLOORS/2+1){
                        if(p.getDestFloorNumber() == Building.MAX_FLOORS){
                            result = true;
                            break;
                        }
                    }
        
                }else if(l.expressType == 4){
                    //System.out.println("!!!!!!");
                    if(p.getHomeFloorNumber()==1 && p.getDestFloorNumber()<=Building.MAX_FLOORS/2+1){
                        result = true;
                        break;

                    }
                }
            }
            
            
        }
        */
        
        return result;
    } 
    
    /**
     * 
     * Send a passenger to the right queue
     * @param list passenger list
     * @param frequency sleep time after send the passenger
     */
    private void assignPassengers(List<Passenger> list, long frequency){
        if(!list.isEmpty()){
            Passenger p = list.get(0);
            if(p.getHomeFloorNumber()<p.getDestFloorNumber()){
                //express or not 
                if(this.isGoExpress(p)==1&&building.centralController.isHasHELift()){
                    //System.out.println("aaaaa");

                    p.isExpress = true;
                    building.getFloor(p.getHomeFloorNumber()).summonHighExpressLiftUp(p);
                }else if(this.isGoExpress(p)==4&&building.centralController.isHasLELift()){
                    //System.out.println("wrong");
                    p.isExpress = true;
                    building.getFloor(p.getHomeFloorNumber()).summonLowExpressLiftUp(p);
                }else{
                    building.getFloor(p.getHomeFloorNumber()).summonLiftUp(p);
                }

                list.remove(p);

            }else{
                if(this.isGoExpress(p)==1&&building.centralController.isHasHELift()){
                    p.isExpress = true;
                    building.getFloor(p.getHomeFloorNumber()).summonHighExpressLiftDown(p);
                }else if(this.isGoExpress(p)==4&&building.centralController.isHasLELift()){
                    p.isExpress = true;
                    building.getFloor(p.getHomeFloorNumber()).summonLowExpressLiftDown(p);
                }else{
                    building.getFloor(p.getHomeFloorNumber()).summonLiftDown(p);
                }

                list.remove(p);
            }
        
        }
        
        
        try {
            Thread.sleep((long) (frequency/speedFactor));
        } catch (InterruptedException ex) {
            Logger.getLogger(PassengerAssigner.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    @Override
    public void run() {
        while(isRunning){
            
            synchronized(pauseLock){
                while (pauseWorking){  
                    try {
                        pauseLock.wait(); //pause scheduler till told to continue  
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SimulationTimer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
            }
            int hour = c.getClok().getHours().getValue();
            int minute = c.getClok().getMinutes().getValue();
            int second = c.getClok().getSeconds().getValue();
           
            if(hour>=8 && hour<=9){
                //morning
                if(building.centralController.getLifts().size()>=4){
                    building.centralController.getLifts().get(0).expressType=1;
                    building.centralController.getLifts().get(1).expressType=4;
                    //ap3d.reconfigFloorText(true);
                }
                assignPassengers(morningList,morningDelay);
            }
            if(hour >= 9 && hour < 17){
                if(building.centralController.getLifts().size()>=4){
                    building.centralController.getLifts().get(0).expressType=0;
                    building.centralController.getLifts().get(1).expressType=0;
                    //ap3d.reconfigFloorText(false);
                }
                assignPassengers(dayList,dailyDelay);
            }
            if(hour >=12 && hour <=14){
                if(building.centralController.getLifts().size()>=4){
                    building.centralController.getLifts().get(0).expressType=1;
                    building.centralController.getLifts().get(1).expressType=4;
                    //ap3d.reconfigFloorText(true);
                }
                if(hour == 13 && minute < 30){
                    assignPassengers(lanuchGoOutList,lanuchDelay);
                }else if (hour>=12&&hour<=13){
                    
                    assignPassengers(lanuchGoOutList,lanuchDelay);
                }
                if(hour == 12 && minute>=20){
                    assignPassengers(lanuchGoInList,lanuchDelay);
                }else if(hour==13 && minute<=30){
                    assignPassengers(lanuchGoInList,lanuchDelay);
                }
            }
                
            /*
            if(isTestScene){
                testScene();
            }
            if(isMorningScene){
                morningScene();
            }
            */
        }
        /*
        try {
            //System.out.println("finish");
            Thread.sleep((long) (1000));
        } catch (InterruptedException ex) {
            Logger.getLogger(PassengerAssigner.class.getName()).log(Level.SEVERE, null, ex);
        }
                */
    }
    /*
    private void morningScene(){
        
        Iterator<Passenger> i = pList.iterator();
        while(i.hasNext()){
            synchronized(pauseLock){
                while (pauseWorking){  
                    try {
                        pauseLock.wait(); //pause scheduler till told to continue  
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SimulationTimer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
                
            }
            
            if(!reRequestList.isEmpty()){
                Iterator<Passenger> ir = reRequestList.iterator();
                //System.out.println(reRequestList.size());
                while(ir.hasNext()){

                    //System.out.println("bbbb");
                    isReListIterating = true;
                    Passenger p = ir.next();
                    if(p.getHomeFloorNumber()<p.getDestFloorNumber()){

                            building.getFloor(p.getHomeFloorNumber()).summonLiftUp(p);
                            reRequestList.remove(p);

                    }else{
                            building.getFloor(p.getHomeFloorNumber()).summonLiftDown(p);
                            reRequestList.remove(p);
                    }
                }
                try {
                    Thread.sleep((long) (3200/speedFactor));
                } catch (InterruptedException ex) {
                    Logger.getLogger(PassengerAssigner.class.getName()).log(Level.SEVERE, null, ex);
                }
                isReListIterating = false;
            }
            isReListIterating = false;
            try {
                        
                Passenger p = i.next();
                if(p.getHomeFloorNumber()<p.getDestFloorNumber()){

                    building.getFloor(p.getHomeFloorNumber()).summonLiftUp(p);
                    pList.remove(p);
                }else{

                    building.getFloor(p.getHomeFloorNumber()).summonLiftDown(p);
                    pList.remove(p);
                }
                Thread.sleep((long) (3200/speedFactor));
            } catch (InterruptedException ex) {
                Logger.getLogger(PassengerAssigner.class.getName()).log(Level.SEVERE, null, ex);
            }    
        }
    }
    private void testScene(){
                    //real work
            Iterator<Passenger> i = pList.iterator();
            while(i.hasNext()){
                synchronized(pauseLock){
                    while (pauseWorking){  
                        try {
                            pauseLock.wait(); //pause scheduler till told to continue  
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SimulationTimer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } 
                }
                int counter = 0;
                while(counter<10){
                    synchronized(pauseLock){
                        while (pauseWorking){  
                            try {
                                pauseLock.wait(); //pause scheduler till told to continue  
                            } catch (InterruptedException ex) {
                                Logger.getLogger(SimulationTimer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } 
                    }
                    
                    if(!reRequestList.isEmpty()){

                        Iterator<Passenger> ir = reRequestList.iterator();
                        //System.out.println(reRequestList.size());
                        while(ir.hasNext()){
                            
                            //System.out.println("bbbb");
                            isReListIterating = true;
                            Passenger p = ir.next();
                            if(p.getHomeFloorNumber()<p.getDestFloorNumber()){
               
                                    building.getFloor(p.getHomeFloorNumber()).summonLiftUp(p);
                                    reRequestList.remove(p);
                                
                            }else{
                                    building.getFloor(p.getHomeFloorNumber()).summonLiftDown(p);
                                    reRequestList.remove(p);
                            }
                        }
                        isReListIterating = false;
                    }
                    isReListIterating = false;
                            
                    try {
                        counter++;
                        Passenger p = i.next();
                        if(p.getHomeFloorNumber()<p.getDestFloorNumber()){
             
                            building.getFloor(p.getHomeFloorNumber()).summonLiftUp(p);
                            pList.remove(p);
                        }else{
                       
                            building.getFloor(p.getHomeFloorNumber()).summonLiftDown(p);
                            pList.remove(p);
                        }
                        Thread.sleep((long) (1000/speedFactor));
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PassengerAssigner.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                counter = 0;
                
                if(!reRequestList.isEmpty()){   
                    Iterator<Passenger> ir = reRequestList.iterator();
                    while(ir.hasNext()){
                        isReListIterating = true;
                        //System.out.println("bbbbbb");
                        Passenger p = ir.next();
                        if(p.getHomeFloorNumber()<p.getDestFloorNumber()){                            
                            building.getFloor(p.getHomeFloorNumber()).summonLiftUp(p);
                            reRequestList.remove(p);
                        }else{                     
                            building.getFloor(p.getHomeFloorNumber()).summonLiftDown(p);
                            reRequestList.remove(p);                             
                        }
                    }
                    isReListIterating = false;
                }
                
                try {
                    Thread.sleep((long) (1000/speedFactor));
                } catch (InterruptedException ex) {
                    Logger.getLogger(PassengerAssigner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    * */
    
}
