package LiftModel;


import java.util.ArrayList;


/**
 * Used to store all passengers critical time
 * used for calculate statistics
 * @author luis_chen
 * @version 1.1
 */
public class StatisticsCollector {
    private ArrayList<LiftTakenRec> records;
    
    /**
     * default constructor
     */
    public StatisticsCollector(){
        records = new ArrayList<LiftTakenRec>();
    }
    
    /**
     *
     * @param rec statistics data object
     */
    public synchronized void addRec(LiftTakenRec rec){
        //System.out.println("add a rec");
        records.add(rec);
    }

    /**
     *
     * @return total number of record
     */
    public int numberOfTrips(){
        return records.size();
    }

    /**
     *
     * @return all records
     */
    public ArrayList<LiftTakenRec> getRecords() {
        return records;
    }
    
}
