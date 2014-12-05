package LiftModel;


import javax.swing.table.AbstractTableModel;
import view2D.StatisticsCalculator;


/**
 * Used to for mapping statics objects to table
 * @author luis
 * @version 1.1
 */
public class StatisticsTableModel extends AbstractTableModel{
    
    private String []columnNames; 
    
    private String [] rowLabel;
    
    private StatisticsCalculator sc;
    

    /**
     * 
     * @param sc the statistics calculator (statistics data)
     */
    public StatisticsTableModel(StatisticsCalculator sc){
        super();
        columnNames = new String[3];
        rowLabel = new String[4];
        generateColumnName();
        generateRowLabel();
        this.sc = sc;
    }
    
    /**
     * labels
     */
    private void generateRowLabel(){
        rowLabel[0]="Waiting time";
        rowLabel[1]="Inside time";
        rowLabel[2]="Trip time";
        rowLabel[3]="Totoal number of trip";
    }
    /**
     * column names
     */
    private void generateColumnName(){
        columnNames[0]="Title";
        columnNames[1]="Average";
        columnNames[2]="Maximum";
        
    }
    
    @Override
    public String getColumnName(int col){
        
        return columnNames[col];
    }
    @Override
    public int getRowCount() {
        return rowLabel.length;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object o = null;
        if(rowIndex==0&&columnIndex==0){
            o = rowLabel[0];
        }else if(rowIndex==0&&columnIndex==1){
            o = sc.getAwt().getText();
            
        }else if(rowIndex==0&&columnIndex==2){
            o = sc.getMxwt().getText();
            
        }else if(rowIndex==1&&columnIndex==0){
            o = rowLabel[1];
        }else if(rowIndex==1&&columnIndex==1){
            o = sc.getAit().getText();
        }else if(rowIndex==1&&columnIndex==2){
            o = sc.getMxit().getText();
        }else if(rowIndex==2&&columnIndex==0){
            o = rowLabel[2];
        }else if(rowIndex==2&&columnIndex==1){
            o = sc.getAtt().getText();
        }else if(rowIndex==2&&columnIndex==2){
            o = sc.getMxtt().getText();
        }else if(rowIndex==3&&columnIndex==0){
            o = rowLabel[3];
        }else if(rowIndex==3&&columnIndex==1){
            o = sc.getTnot().getText();
        }else if(rowIndex==3&&columnIndex==2){
            o = sc.getTnot().getText();
        }
        return (String)o;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

}
