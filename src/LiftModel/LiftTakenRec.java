package LiftModel;



/**
 * data structure used to record the critical time for each passengers
 * those data are used to calculate required statistics 
 * @author Louis
 * @version 1.1
 */
public class LiftTakenRec {

    /**
     *
     */
    public long startWaitTime;

    /**
     *
     */
    public long enterLiftTime;

    /**
     *
     */
    public long getOffTime;
    
    public int id;
    
    /**
     *
     */
    public LiftTakenRec(){
        startWaitTime = 0;
        enterLiftTime = 0;
        getOffTime = 0;
    }
}
