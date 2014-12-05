/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package specification.reader;

import LiftModel.BuildingParameters;
import LiftModel.LiftParameters;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * New configuration file reader
 * It read configurations from property file
 * @author luis_chen
 * @version 1.1
 */


public class SpecificationFileReader {
    private BuildingParameters bp;
    private ArrayList<LiftParameters> lList;
    private Properties prop;
    private InputStream input;
    
    /**
     * Constructor
     */
    public SpecificationFileReader(){
        prop = new Properties();
        bp = new BuildingParameters();
        lList = new ArrayList<LiftParameters>();
    
    }
    
    /**
     * Read a configuration file
     * @param file the file path and name want to read
     * @throws IOException  IO exception
     */
    public void readSpecificationFromFile(String file) throws IOException{
        try {
            input = new FileInputStream(file);
            prop.load(input);
            bp.numberOfFloor = Integer.parseInt(prop.getProperty("buildingLevel"));
            bp.numberOfLift = Integer.parseInt(prop.getProperty("buildingLiftNumber"));
            int hen = Integer.parseInt(prop.getProperty("highExpressNumber"));
            int len = Integer.parseInt(prop.getProperty("lowExpressNumber"));
            if(bp.numberOfLift>=hen+len){
                for(int i=0;i<bp.numberOfLift;i++){
                    LiftParameters lp = new LiftParameters();

                    if(hen>0){
                        hen--;
                        lp.expressType = 1;
                    }else if(len>0){
                        len--;
                        lp.expressType =4;
                    }else{
                        lp.expressType=0;
                    }
                    lp.liftId = i;
                    lp.capacity = Integer.parseInt(prop.getProperty("liftCapacity"));
                    lp.doorCloseOpenTime = Integer.parseInt(prop.getProperty("liftDoorOpenCloseTime"));
                    lp.enterInTimeA = Integer.parseInt(prop.getProperty("liftLoadingTime1"));
                    lp.enterInTimeB = Integer.parseInt(prop.getProperty("liftLoadingTime2"));
                    lp.idleTime = Integer.parseInt(prop.getProperty("liftIdleTime"));
                    lp.a = Integer.parseInt(prop.getProperty("liftAcceleration"));
                    lp.homeFloor = Integer.parseInt(prop.getProperty("liftHomeFloor"));
                    lp.maxSpeed = Integer.parseInt(prop.getProperty("liftMaxSpeed"));
                    lList.add(lp);
                }
            
            }else{
                throw new IOException();
            }
            

            
            
        } catch (IOException ex) {
            throw new IOException();
        }finally{
            if(input != null){
                try{
                    input.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * get the building parameter 
     * @return BuildingParameters
     */
    public BuildingParameters getBp() {
        return bp;
    }

    /**
     * get the lifts parameters
     * @return ArrayList of lifts parameters 
     */
    public ArrayList<LiftParameters> getlList() {
        return lList;
    }
    
    
    /**
     * testing purpose only
     * @param args args
     */
    public static void main(String [] args){
        SpecificationFileReader sfr = new SpecificationFileReader();
        try {
            sfr.readSpecificationFromFile("config/config2.txt");
        } catch (IOException ex) {
            Logger.getLogger(SpecificationFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(sfr.getBp().numberOfFloor);
        System.out.println(sfr.getBp().numberOfLift);
        for(LiftParameters l:sfr.getlList()){
            System.out.println(l.a);
            System.out.println(l.capacity);
            System.out.println(l.doorCloseOpenTime);
            System.out.println(l.enterInTimeA);
            System.out.println(l.enterInTimeB);
            System.out.println(l.expressType);
            System.out.println(l.homeFloor);
            System.out.println(l.idleTime);
            System.out.println(l.liftId);
            System.out.println(l.maxSpeed);
        }
    
    }
}
