package LiftModel;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * Read building and lift parameters from project dir/config/config.txt
 * The class will return parameters in form of BuildingParameters and LiftParameters
 * 
 * @author luis_chen
 * @version 1.1
 */
public class ConfigFileParser {
    private BufferedReader br;
    private BuildingParameters bp;
    private LiftParameters lp;
    
    /**
     *default constructor
     */
    public ConfigFileParser(){
        bp = new BuildingParameters();
        lp = new LiftParameters();
    }
    
    /**
     * read the configuration file
     * @param fileName file name and path
     */
    public void readConfigFile(String fileName){
        br = null;
        try {
            
            br = new BufferedReader(new FileReader(fileName));

            String line;
            int counter = 0;
            while ((line = br.readLine()) != null) {
                if(counter == 0){
                    //building config
                    String [] ps = line.split(",");
                    bp.numberOfFloor = Integer.parseInt(ps[0]);
                    bp.numberOfLift = Integer.parseInt(ps[1]);
                }else if(counter == 1){
                    //lift configure
                    String [] ps = line.split(",");
                    lp.idleTime = Integer.parseInt(ps[0]);
                    lp.a = Integer.parseInt(ps[1]);
                    lp.doorCloseOpenTime = Integer.parseInt(ps[2]);
                    lp.maxSpeed = Integer.parseInt(ps[3]);
                    lp.homeFloor = Integer.parseInt(ps[4]);
                    lp.capacity = Integer.parseInt(ps[5]);
                    lp.enterInTimeB = Integer.parseInt(ps[6]);
                    lp.enterInTimeA = Integer.parseInt(ps[7]);
                    lp.liftId = Integer.parseInt(ps[8]);
                    lp.expressType = Integer.parseInt(ps[9]);
                    //System.out.println(ps[0]+ps[1]+ps[2]+ps[3]+ps[4]);
                }
                counter++;
                
                
            }

        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    
    }
    
    /**
     * get building parameters
     * @return BuildingParameters
     */
    public BuildingParameters getBuildingPara(){
        return this.bp;
    }
    
    /**
     * get lift parameters
     * @return LiftParameters
     */
    public LiftParameters getLiftPara(){
        return this.lp;
    }
    
    /**
     * for testing purpose only
     * @param args args
     */
    public static void main(String [] args){
        ConfigFileParser cfp = new ConfigFileParser();
        cfp.readConfigFile("config/config.txt");
    }
}
