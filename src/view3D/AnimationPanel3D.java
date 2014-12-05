package view3D;

import LiftModel.Building;
import LiftModel.Floor;
import LiftModel.Lift;
import LiftModel.Passenger;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.*;
import javax.vecmath.*;
import view2D.DetialedLift;


/**
 * The 3D GUI 
 * It contains all 3D shapes and action listeners
 * @author luis_chen
 * @version 1.1
 */
public class AnimationPanel3D implements Runnable {
    
    public static ArrayList<Lift> liftsModel;
    public static ArrayList<Floor> floorsModel;
    public static Building building;
    
    private SimpleUniverse buildingUniverse;
    //floors and lifts should with in this group to ensure zoom in out and so on
    private TransformGroup buildingTG;
    private BranchGroup buildingBG;
    
    private ArrayList<Lift3DView> lifts;
    
    private ArrayList<FloorControllPanel3D> floors;
    
    private PickCanvas pickCanvas;
    Canvas3D canvas;

    /**
     * Default constructor
     */
    public AnimationPanel3D() {
        
        //this.setSize(800, 600);
        //this.setMinimumSize(new Dimension(800,600));
        //this.setPreferredSize(new Dimension(800,600));
        lifts = new ArrayList<Lift3DView>();
        floors = new ArrayList<FloorControllPanel3D>();
        //setLayout(new BorderLayout());

        GraphicsConfiguration config =

        SimpleUniverse.getPreferredConfiguration();

        canvas = new Canvas3D(config);
        canvas.setPreferredSize(new Dimension(800,600));
        //canvas.setMinimumSize(new Dimension(800,600));
        
        //add("North",new Label("This is the top"));

        //add("Center", canvas);

        //add("South",new Label("This is the bottom"));
        
        
        buildingUniverse = new SimpleUniverse(canvas);
        buildingBG = new BranchGroup();
        buildingTG = new TransformGroup();
        addFloorsToUniverse();
        addLifts();
        addFloorsPanel();
        buildingTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        buildingTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.buildingBG.addChild(buildingTG);     
        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(buildingTG);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        
        buildingBG.addChild(myMouseRotate);
        
        MouseTranslate myMouseTranslate = new MouseTranslate();
        myMouseTranslate.setTransformGroup(this.buildingTG);  // ---------------(5)
        myMouseTranslate.setSchedulingBounds(new BoundingSphere());  // ---------------(6)
        this.buildingBG.addChild(myMouseTranslate);

        MouseZoom myMouseZoom = new MouseZoom();
        myMouseZoom.setTransformGroup(buildingTG);  // ---------------(7)
        myMouseZoom.setSchedulingBounds(new BoundingSphere());  // ---------------(8)
        this.buildingBG.addChild(myMouseZoom);
        
        generateLightAccordingToFloor();    
        
        Transform3D moveTo = new Transform3D();
        Vector3f vector = new Vector3f(-4f,-(((float)Building.MAX_FLOORS+1)/2f),-15f);
        moveTo.setTranslation(vector);
        buildingTG.setTransform(moveTo);
       // Transform3D move = lookTowardsOriginFrom(new Point3d(5, 2, -20));
        //buildingUniverse.getViewingPlatform().getViewPlatformTransform().setTransform(move);
        
        this.buildingUniverse.addBranchGraph(buildingBG);
        pickCanvas = new PickCanvas(canvas, buildingBG);
        pickCanvas.setMode(PickCanvas.GEOMETRY);
        canvas.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                pickCanvas.setShapeLocation(e);

                PickResult result = pickCanvas.pickClosest();

                if (result == null) {

                   

                } else {
                   Shape3D s = (Shape3D)result.getNode(PickResult.SHAPE3D);
                   if(s.getParent().getParent().getUserData()!=null){
                       String action = s.getParent().getParent().getUserData().toString();
                       //System.out.println(action);
                       if(s.getParent().getParent().getParent().getParent().getUserData()!=null){
                           int floorNumber = Integer.parseInt(s.getParent().getParent().getParent().getParent().getUserData().toString());
                           //System.out.println(floorNumber);
                           if(action.equalsIgnoreCase("upe")){
                                if(floorNumber==Building.MAX_FLOORS){
                                }else{
                                    Floor floor = floorsModel.get(floorNumber-1);
                                    Passenger p = new Passenger(100001);
                                    p.isExpress = true;
                                    p.setHomeFloorNumber(floor.getFloorNumber());
                                    p.setDestFloorNumber(0);
                                    //floor.summonExpressLiftUp(p);
                                    DetialedLift.floor=floor;
                                    DetialedLift.p = p;
                                
                                }
                                
                           }else if(action.equalsIgnoreCase("downe")){
                                Floor floor = floorsModel.get(floorNumber-1);
                                Passenger p = new Passenger(100001);
                                p.isExpress = true;
                                p.setHomeFloorNumber(floor.getFloorNumber());
                                p.setDestFloorNumber(0);
                                //floor.summonExpressLiftDown(p);
                                DetialedLift.floor=floor;
                                DetialedLift.p = p;
                              
                                
                                
                               
                           }else if(action.equalsIgnoreCase("up")){
                               
                               Floor floor = floorsModel.get(floorNumber-1);
                               Passenger p = new Passenger(100001);

                               p.setHomeFloorNumber(floor.getFloorNumber());
                               p.setDestFloorNumber(0);

                               //floor.summonLiftUp(p);
                               DetialedLift.floor=floor;
                               DetialedLift.p = p;
                           
                           }else if(action.equalsIgnoreCase("down")){
                               Floor floor = floorsModel.get(floorNumber-1);
                               Passenger p = new Passenger(100001);

                               p.setHomeFloorNumber(floor.getFloorNumber());
                               p.setDestFloorNumber(0);

                               DetialedLift.floor=floor;
                               DetialedLift.p = p;
                           }
                       }
                   
                   }



                }

            }
            
        });

    }

    /**
     * reconfigure queue text
     * @param isExpress is express or not
     */
    public void reconfigFloorText(boolean isExpress){
        for(FloorControllPanel3D f:floors){
            f.configText(isExpress);
        }
    
    }
    
    /**
     * get the canvas3D
     * @return the canvas 3D
     */
    public Canvas3D getCanvas() {
        return canvas;
    }
    
    
    
    private void addFloorsPanel(){
        for(int i=1;i<Building.MAX_FLOORS+1;i++){
            FloorControllPanel3D f = null;
            if(i==1||i==2||i==Building.MAX_FLOORS/2+1||i==Building.MAX_FLOORS){
                f = new FloorControllPanel3D(floorsModel.get(i-1),true);
            }else{
                f = new FloorControllPanel3D(floorsModel.get(i-1),false);
            }
            
            Transform3D transform = new Transform3D();
            Vector3f vector = new Vector3f(Building.MAX_LIFTS*1.4f+0.225f,i, 0f);
            transform.setTranslation(vector);
            f.getFloorTG().setTransform(transform);
            buildingTG.addChild(f.getFloorBG());
            floors.add(f);
            
        }
    }
    private void addLifts(){
        for(int i=0;i<Building.MAX_LIFTS;i++){
            Lift3DView lift = new Lift3DView(0);
            Transform3D transform = new Transform3D();
            Vector3f vector = new Vector3f((float)i*1.4f,1.0f, 0f);
            transform.setTranslation(vector);
            lift.getLiftTG().setTransform(transform);
            buildingTG.addChild(lift.getLiftBG());
            lifts.add(lift);
            
        }
    }
    
    /**
     * reset look at
     * @param point the point want look at
     * @return Transform3D for universe transformation
     */
    public Transform3D lookTowardsOriginFrom(Point3d point){
        Transform3D move = new Transform3D();
        Vector3d up = new Vector3d(point.x, point.y + 1, point.z);
        move.lookAt(point, new Point3d(0.0d, 0.0d, 0.0d), up);

        return move;
    }
    private void generateLightAccordingToFloor(){
        for(int i =1; i <= Building.MAX_FLOORS+1; i++){
            Color3f lightColor = new Color3f(0.5f,0.5f,0.5f);
            BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,(float)i+0.5f,20f), 100.0f);
            Vector3f lightDirection1 = new Vector3f(30f, (float)i+0.5f, 10f);
            Vector3f lightDirection2 = new Vector3f(30f, (float)i+0.5f, -10f);
            Vector3f lightDirection3 = new Vector3f(-30f, (float)i+0.5f, 10f);
            Vector3f lightDirection4 = new Vector3f(-30f, (float)i+0.5f, -10f);
            Vector3f lightDirection5 = new Vector3f((Building.MAX_LIFTS*1.4f+0.225f)/2f, -(float)i+0.1f, 1f);
            Vector3f lightDirection6 = new Vector3f((Building.MAX_LIFTS*1.4f+0.225f)/2f, -(float)i+0.1f, -1f);
            DirectionalLight light1 = new DirectionalLight(lightColor, lightDirection1);
            DirectionalLight light2 = new DirectionalLight(lightColor, lightDirection2);
            DirectionalLight light3 = new DirectionalLight(lightColor, lightDirection3);
            DirectionalLight light4 = new DirectionalLight(lightColor, lightDirection4);
            DirectionalLight light5 = new DirectionalLight(lightColor, lightDirection5);
            DirectionalLight light6 = new DirectionalLight(lightColor, lightDirection6);
            light1.setInfluencingBounds(bounds);
            light2.setInfluencingBounds(bounds);
            light3.setInfluencingBounds(bounds);
            light4.setInfluencingBounds(bounds);
            light5.setInfluencingBounds(bounds);
            light6.setInfluencingBounds(bounds);
            buildingBG.addChild(light1);    
            buildingBG.addChild(light2);  
            buildingBG.addChild(light3);    
            buildingBG.addChild(light4);
            buildingBG.addChild(light5);    
            buildingBG.addChild(light6);
            
        }
    }
    
    private void drawAFloor(float floor){
        //LineArray landGeom = new LineArray(88,GeometryArray.COORDINATES | GeometryArray.COLOR_3|GeometryArray.NORMALS);
        QuadArray qa = new QuadArray(8,QuadArray.COORDINATES);
        qa.setCoordinate(0, new Point3f(0f-0.225f,floor+0.025f,1));
        qa.setCoordinate(1, new Point3f(Building.MAX_LIFTS*1.4f+0.225f,floor+0.025f,1));
        qa.setCoordinate(2, new Point3f(Building.MAX_LIFTS*1.4f+0.225f,floor+0.025f,-1));
        qa.setCoordinate(3, new Point3f(0f-0.225f,floor+0.025f,-1));
        
        qa.setCoordinate(4, new Point3f(0f-0.225f,floor-0.025f,1));
        qa.setCoordinate(5, new Point3f(0f-0.225f,floor-0.025f,-1));
        qa.setCoordinate(6, new Point3f(Building.MAX_LIFTS*1.4f+0.225f,floor-0.025f,-1));
        qa.setCoordinate(7, new Point3f(Building.MAX_LIFTS*1.4f+0.225f,floor-0.025f,1));
        
        
        /*
        float l = -5f;
        for (int c = 0; c < 88; c+=4)
        {
          landGeom.setCoordinate( c+0, new Point3f( -5f, floor,  l ));
          landGeom.setCoordinate( c+1, new Point3f(  5f, floor,  l ));
          landGeom.setCoordinate( c+2, new Point3f(   l   , floor, -5f ));
          landGeom.setCoordinate( c+3, new Point3f(   l   , floor,  5f ));
          l += 1;
        }
        Color3f c = new Color3f(1.0f,0.1f,0.1f);
        for (int i = 0; i < 44; i++){
            landGeom.setColor(i,c);
        }
        */
        Appearance app = new Appearance();
        
        //add floor number
        if(floor>1){
            drawFloorNumber(floor-1);
        }
        
        
        buildingTG.addChild(new Shape3D(qa,app));
    }
    private void drawFloorNumber(float f){
        Font3D f3d = new Font3D(new Font("TestFont", Font.PLAIN,1),new FontExtrusion());
        Text3D t3d = new Text3D(f3d, (int)f-1+"",new Point3f(0.2f,0.45f,0.07f));
        t3d.setCapability(Text3D.ALLOW_STRING_READ);
        t3d.setCapability(Text3D.ALLOW_STRING_WRITE);
        //Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f blue = new Color3f(.2f, 0.2f, 5f);
        Appearance a = new Appearance();
        Material m = new Material(blue, blue, blue, blue, 80.0f);
        m.setLightingEnable(true);
        a.setMaterial(m);
        Shape3D sh = new Shape3D();
        sh.setAppearance(a);
        Transform3D tf = new Transform3D();
        tf.setTranslation(new Vector3f(-0.5f,f+0.5f,0f));
        
        tf.setScale(0.2);
        sh.setGeometry(t3d);
        TransformGroup tg = new TransformGroup();
        tg.addChild(sh);
        tg.setTransform(tf);
        buildingTG.addChild(tg);
        //
        
        
        //buildingTG.addChild(sh);
    }
    private void addFloorsToUniverse(){
        for(int i=1;i<=Building.MAX_FLOORS+1;i++){
            drawAFloor(i);
            
        }
    }

    /**
     * testing only
     * @param args args
     */
    public static void main( String[] args ) {

       AnimationPanel3D demo = new AnimationPanel3D();
       //new MainFrame(demo,400,400);

    }

    @Override
    public void run() {
        while(true){
            
            for(int i=0 ;i<Building.MAX_LIFTS; i++){
                Lift3DView view = this.lifts.get(i);
                Lift l = liftsModel.get(i);
                Transform3D transform = new Transform3D();
                Vector3f vector = new Vector3f((float)i*1.4f,l.getCurrentFloorNumber(), 0f);
                transform.setTranslation(vector);
                view.getLiftTG().setTransform(transform);
                view.t3d.setString(""+l.passengers.size());
                if(l.getPassengers().isEmpty()){
                    view.setLiftColor(new Color3f(Color.GREEN));
                }else if(l.getPassengers().size()<Lift.getMAX_OCCUPANCY()){
                    //yellow
                    view.setLiftColor(new Color3f(Color.YELLOW));
                }else if(l.getPassengers().size()==Lift.getMAX_OCCUPANCY()){
                    //red
                    view.setLiftColor(new Color3f(Color.RED));
                }
                if(l.getState().doorState==2){
                    //door open and close
                    //right door 1,2,5,6
                    view.changeDoorSize(l.doorWidth);
                }
                
            }
            
            for(int i = 0;i<Building.MAX_FLOORS;i++){
                FloorControllPanel3D view = this.floors.get(i);
                Floor f = floorsModel.get(i);
              
                view.upQueue.setString("U: "+f.getNumberWaitingUp()+"");
                view.downQueue.setString("D: "+f.getNumberWaitingDown()+"");
                if(i==0||i==1||i==Building.MAX_FLOORS/2||i==Building.MAX_FLOORS-1){
                    //express
                    ///HE
                    if(building.centralController.isHasHELift()){
                        view.upHEQueue.setString("HU: "+f.getNumberWaitingUpHighExpress()+"");
                        view.downHEQueue.setString("HD: "+f.getNumberWaitingDownHighExpress()+"");
                    }else{
                        view.upHEQueue.setString("");
                         view.downHEQueue.setString("");
                    }
                        
                    //}
                    
                    ///LE
                    if(building.centralController.isHasLELift()){
                        view.upLEQueue.setString("LU: "+f.getNumberWaitingUpLowExpress()+"");
                        view.downLEQueue.setString("LD: "+f.getNumberWaitingDownLowExpress()+"");
                    }else{
                        view.upLEQueue.setString("");
                        view.downLEQueue.setString("");
                    }
                    if(f.getNumberWaitingDownExpress()>0){
                        view.getDownEButton().getApp().getMaterial().setDiffuseColor(new Color3f(10f,0f,0f));
                        view.getDownEButton().getApp().getMaterial().setSpecularColor(new Color3f(10f,0f,0f));
                        view.getDownEButton().getApp().getMaterial().setShininess(128f);
                    //mat.setAmbientColor(new Color3f(1f,0.0f,0.0f));mat.setDiffuseColor(new Color3f(0.7f,0.7f,0.7f));mat.setSpecularColor(new Color3f(0.7f,0.7f,0.7f));
                    }else{
                        if(view.getDownEButton()!=null){
                            view.getDownEButton().getApp().getMaterial().setDiffuseColor(new Color3f(0.5f,0.5f,0.5f));
                            view.getDownEButton().getApp().getMaterial().setSpecularColor(new Color3f(0.5f,0.5f,0.5f));
                            view.getDownEButton().getApp().getMaterial().setShininess(10f);
                        }
                        
                    }
                    
                    if(f.getNumberWaitingUpExpress()>0){
                        view.getUpEButton().getApp().getMaterial().setDiffuseColor(new Color3f(10f,0f,0f));
                        view.getUpEButton().getApp().getMaterial().setSpecularColor(new Color3f(10f,0f,0f));
                        view.getUpEButton().getApp().getMaterial().setShininess(128f);
                    }else{
                        view.getUpEButton().getApp().getMaterial().setDiffuseColor(new Color3f(0.5f,0.5f,0.5f));
                        view.getUpEButton().getApp().getMaterial().setSpecularColor(new Color3f(0.5f,0.5f,0.5f));
                        view.getUpEButton().getApp().getMaterial().setShininess(10f);
                    }
                    
                    
                }
                if(f.getNumberWaitingDown()>0){

                    view.getDownButton().getApp().getMaterial().setDiffuseColor(new Color3f(10f,0f,0f));
                    view.getDownButton().getApp().getMaterial().setSpecularColor(new Color3f(10f,0f,0f));
                    view.getDownButton().getApp().getMaterial().setShininess(128f);
                    //mat.setAmbientColor(new Color3f(1f,0.0f,0.0f));mat.setDiffuseColor(new Color3f(0.7f,0.7f,0.7f));mat.setSpecularColor(new Color3f(0.7f,0.7f,0.7f));
                }else{
                    view.getDownButton().getApp().getMaterial().setDiffuseColor(new Color3f(0.5f,0.5f,0.5f));
                    view.getDownButton().getApp().getMaterial().setSpecularColor(new Color3f(0.5f,0.5f,0.5f));
                    view.getDownButton().getApp().getMaterial().setShininess(10f);
                }
                if(f.getNumberWaitingUp()>0){
                    view.getUpButton().getApp().getMaterial().setDiffuseColor(new Color3f(10f,0f,0f));
                    view.getUpButton().getApp().getMaterial().setSpecularColor(new Color3f(10f,0f,0f));
                    view.getUpButton().getApp().getMaterial().setShininess(128f);
                }else{
                    view.getUpButton().getApp().getMaterial().setDiffuseColor(new Color3f(0.5f,0.5f,0.5f));
                    view.getUpButton().getApp().getMaterial().setSpecularColor(new Color3f(0.5f,0.5f,0.5f));
                    view.getUpButton().getApp().getMaterial().setShininess(10f);
                }
                
            
            }
            
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(AnimationPanel3D.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
    }
    
    
}