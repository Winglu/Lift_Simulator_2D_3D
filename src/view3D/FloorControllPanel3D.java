package view3D;


import LiftModel.Building;
import LiftModel.Floor;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.Color;
import java.awt.Font;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
/**
 * 3D view of a floor control panel
 * Contains the same components of 2D version
 * @author luis
 * @version 1.1
 */
public class FloorControllPanel3D {
    private Floor floor;
    private Button3D upButton;
    private Button3D downButton;
    private Button3D upEButton;
    private Button3D downEButton;
    private SimpleUniverse floorUniverse;
    //floors and lifts should with in this group to ensure zoom in out and so on
    private TransformGroup floorTG;
    private BranchGroup floorBG;
    public Font3D f3d;
    public Text3D upQueue;
    public Text3D downQueue;
    public Text3D upHEQueue;
    public Text3D downHEQueue;
    public Text3D upLEQueue;
    public Text3D downLEQueue;
    
    
    public TransformGroup up;
    public TransformGroup down;
    public TransformGroup upe;
    public TransformGroup downe;
    
    public static Building building;
    
    public FloorControllPanel3D(Floor floor,boolean isExpress){
        f3d = new Font3D(new Font("TestFont", Font.PLAIN,1),new FontExtrusion());
        floorTG = new TransformGroup();
        floorTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        floorTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        
        
     
        
        
        this.floor = floor;
        configText(isExpress);
        upButton = new Button3D(0.25f,"up");
        downButton = new Button3D(0.25f,"down");
        
        if(isExpress){
            
            upEButton = new Button3D(0.25f,"upe");
            downEButton = new Button3D(0.25f,"downe");
            
            
            
            

        }
        addButtonsToWall(isExpress);
        
        
        
        floorBG = new BranchGroup();
        //floorUniverse = new SimpleUniverse();
        
        floorTG.addChild(paintWall());
        
        floorBG.addChild(floorTG);
        floorBG.setUserData(floor.getFloorNumber());
        
        /*
        Color3f lightColor = new Color3f(1f,1f,1f);
        AmbientLight ambientLightNode1 = new AmbientLight (lightColor);
        BoundingSphere bounds1 = new BoundingSphere(new Point3d(0f,0f,0f), 100.0f);
        ambientLightNode1.setInfluencingBounds (bounds1);
        
        floorBG.addChild(ambientLightNode1);

        MouseRotate myMouseRotate = new MouseRotate();

        myMouseRotate.setTransformGroup(floorTG);

        myMouseRotate.setSchedulingBounds(new BoundingSphere());

        floorTG.addChild(myMouseRotate);

        MouseTranslate myMouseTranslate = new MouseTranslate();

        myMouseTranslate.setTransformGroup(this.floorTG);  // ---------------(5)

        myMouseTranslate.setSchedulingBounds(new BoundingSphere());  // ---------------(6)

        this.floorTG.addChild(myMouseTranslate);

        MouseZoom myMouseZoom = new MouseZoom();

        myMouseZoom.setTransformGroup(floorTG);  // ---------------(7)

        myMouseZoom.setSchedulingBounds(new BoundingSphere());  // ---------------(8)

        this.floorTG.addChild(myMouseZoom);

        

        floorUniverse.addBranchGraph(floorBG);
         */
        
    }
    /**
     * get down express button
     * @return Button3D
     */
    public Button3D getDownEButton() {
        return downEButton;
    }
    /**
     * get up express button
     * @return Button3D
     */
    public Button3D getUpEButton() {
        return upEButton;
    }
    
    /**
     * Setup the text to show number of passengers in each queue
     * @param isExpress if the floor is express floor
     */
    public void configText(boolean isExpress){
            upQueue = new Text3D(f3d, "U:0",new Point3f(0.2f,0.45f,0.07f));
            downQueue = new Text3D(f3d, "D:0",new Point3f(0.2f,0.45f,0.07f));
            upHEQueue = new Text3D(f3d, "HU:0",new Point3f(0.2f,0.45f,0.07f));
            downHEQueue = new Text3D(f3d, "HD:0",new Point3f(0.2f,0.45f,0.07f));
            upLEQueue = new Text3D(f3d, "LU:0",new Point3f(0.2f,0.45f,0.07f));
            downLEQueue = new Text3D(f3d, "LD:0",new Point3f(0.2f,0.45f,0.07f));
            //
            upQueue.setCapability(Text3D.ALLOW_STRING_READ);
            upQueue.setCapability(Text3D.ALLOW_STRING_WRITE);
            //Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
            Color3f blue = new Color3f(Color.pink);
            Appearance a = new Appearance();
            Material m = new Material(blue, blue, blue, blue, 80.0f);
            m.setLightingEnable(true);
            a.setMaterial(m);
            Shape3D sh = new Shape3D();
            sh.setAppearance(a);
            Transform3D tf = new Transform3D();
            tf.setTranslation(new Vector3f(0.65f,0.57f,0.07f));

            tf.setScale(0.2);
            TransformGroup tg = new TransformGroup();

            sh.setGeometry(upQueue);
            tg.addChild(sh);
            tg.setTransform(tf);
            floorTG.addChild(tg);
            
            //
            downQueue.setCapability(Text3D.ALLOW_STRING_READ);
            downQueue.setCapability(Text3D.ALLOW_STRING_WRITE);
            //white = new Color3f(1.0f, 1.0f, 1.0f);
            blue = new Color3f(Color.pink);
            a = new Appearance();
            m = new Material(blue, blue, blue, blue, 80.0f);
            m.setLightingEnable(true);
            a.setMaterial(m);
            sh = new Shape3D();
            sh.setAppearance(a);
            tf = new Transform3D();
            tf.setTranslation(new Vector3f(0.65f,0.16f,0.07f));

            tf.setScale(0.2);
            tg = new TransformGroup();

            sh.setGeometry(downQueue);
            tg.addChild(sh);
            tg.setTransform(tf);
            floorTG.addChild(tg);
            
            //
            if(isExpress){
                
                //if(building.centralController.isHasHELift()){
                    upHEQueue.setCapability(Text3D.ALLOW_STRING_READ);
                    upHEQueue.setCapability(Text3D.ALLOW_STRING_WRITE);
                    //white = new Color3f(1.0f, 1.0f, 1.0f);
                    blue = new Color3f(Color.pink);
                    a = new Appearance();
                    m = new Material(blue, blue, blue, blue, 80.0f);
                    m.setLightingEnable(true);
                    a.setMaterial(m);
                    sh = new Shape3D();
                    sh.setAppearance(a);
                    tf = new Transform3D();
                    tf.setTranslation(new Vector3f(1.65f,0.57f,0.07f));

                    tf.setScale(0.2);
                    tg = new TransformGroup();

                    sh.setGeometry(upHEQueue);
                    tg.addChild(sh);
                    tg.setTransform(tf);
                    floorTG.addChild(tg);

                    //
                    downHEQueue.setCapability(Text3D.ALLOW_STRING_READ);
                    downHEQueue.setCapability(Text3D.ALLOW_STRING_WRITE);
                    //white = new Color3f(1.0f, 1.0f, 1.0f);
                    blue = new Color3f(Color.pink);
                    a = new Appearance();
                    m = new Material(blue, blue, blue, blue, 80.0f);
                    m.setLightingEnable(true);
                    a.setMaterial(m);
                    sh = new Shape3D();
                    sh.setAppearance(a);
                    tf = new Transform3D();
                    tf.setTranslation(new Vector3f(1.65f,0.16f,0.07f));

                    tf.setScale(0.2);
                    tg = new TransformGroup();

                    sh.setGeometry(downHEQueue);
                    tg.addChild(sh);
                    tg.setTransform(tf);
                    floorTG.addChild(tg);
                //}

                //if(building.centralController.isHasLELift()){
                    upLEQueue.setCapability(Text3D.ALLOW_STRING_READ);
                    upLEQueue.setCapability(Text3D.ALLOW_STRING_WRITE);
                    //white = new Color3f(1.0f, 1.0f, 1.0f);
                    blue = new Color3f(Color.pink);
                    a = new Appearance();
                    m = new Material(blue, blue, blue, blue, 80.0f);
                    m.setLightingEnable(true);
                    a.setMaterial(m);
                    sh = new Shape3D();
                    sh.setAppearance(a);
                    tf = new Transform3D();
                    tf.setTranslation(new Vector3f(2.65f,0.57f,0.07f));

                    tf.setScale(0.2);
                    tg = new TransformGroup();

                    sh.setGeometry(upLEQueue);
                    tg.addChild(sh);
                    tg.setTransform(tf);
                    floorTG.addChild(tg);

                    //
                    downLEQueue.setCapability(Text3D.ALLOW_STRING_READ);
                    downLEQueue.setCapability(Text3D.ALLOW_STRING_WRITE);
                    //white = new Color3f(1.0f, 1.0f, 1.0f);
                    blue = new Color3f(Color.pink);
                    a = new Appearance();
                    m = new Material(blue, blue, blue, blue, 80.0f);
                    m.setLightingEnable(true);
                    a.setMaterial(m);
                    sh = new Shape3D();
                    sh.setAppearance(a);
                    tf = new Transform3D();
                    tf.setTranslation(new Vector3f(2.65f,0.16f,0.07f));

                    tf.setScale(0.2);
                    tg = new TransformGroup();

                    sh.setGeometry(downLEQueue);
                    tg.addChild(sh);
                    tg.setTransform(tf);
                    floorTG.addChild(tg);                
                //}
                //////lE

                
                
                

            }
          
    
    }
    /**
     * set model for this view
     * @param floor a floor
     */
    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    /**
     * get transform group 
     * @return TransformGroup
     */
    public TransformGroup getFloorTG() {
        return floorTG;
    }

    /**
     * get branch group 
     * @return BranchGroup
     */
    public BranchGroup getFloorBG() {
        return floorBG;
    }
    
    private void addButtonsToWall(boolean isExpress){
        Transform3D transform = new Transform3D();
        Vector3f vector = new Vector3f(0.375f,0.57f,0.25f);
        transform.setTranslation(vector);
        upButton.getButtonTG().setTransform(transform);
        
        transform = new Transform3D();
        vector = new Vector3f(0.375f,0.16f,0.25f);
        transform.setTranslation(vector);
        downButton.getButtonTG().setTransform(transform);
        
        
        if(isExpress){
            //UPE
            
            transform = new Transform3D();
            vector = new Vector3f(1.375f,0.57f,0.25f);
            transform.setTranslation(vector);
            upEButton.getButtonTG().setTransform(transform);
            //DOWNE
            transform = new Transform3D();
            vector = new Vector3f(1.375f,0.16f,0.25f);
            transform.setTranslation(vector);
            downEButton.getButtonTG().setTransform(transform);

            floorTG.addChild(upEButton.getButtonBG());
            floorTG.addChild(downEButton.getButtonBG());
            
            
            
        
        
        }
        
        
        floorTG.addChild(upButton.getButtonBG());
        floorTG.addChild(downButton.getButtonBG());
    }
    
    private Shape3D paintWall(){
        //height 1
        Material mat = new Material();
        mat.setCapability(Material.ALLOW_COMPONENT_READ );
         mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
        mat.setAmbientColor(new Color3f(1f,0.0f,0.0f));

        mat.setDiffuseColor(new Color3f(0.7f,0.7f,0.7f));

        mat.setSpecularColor(new Color3f(0.7f,0.7f,0.7f));
        
        Appearance app = new Appearance();
        app.setMaterial(mat);
        QuadArray rec = new QuadArray (8, QuadArray.COORDINATES);
        rec.setCoordinate(0, new Point3f (0f,0f,0f));
        rec.setCoordinate(1, new Point3f (4f,0f,0f));
        rec.setCoordinate(2, new Point3f (4f,1f,0f));
        rec.setCoordinate(3, new Point3f (0f,1f,0f));
        
        rec.setCoordinate(4, new Point3f (0f,0f,0f));
        rec.setCoordinate(5, new Point3f (0f,1f,0f));
        rec.setCoordinate(6, new Point3f (4f,1f,0f));
        rec.setCoordinate(7, new Point3f (4f,0f,0f));
        
        
        
        GeometryInfo geometryInfo = new GeometryInfo(rec);
        NormalGenerator ng = new NormalGenerator();
	ng.generateNormals(geometryInfo);
        GeometryArray result = geometryInfo.getGeometryArray();
        Shape3D s = new Shape3D(result,app);
        return s; 
    }
    /**
     * 
     * @return Button3D
     */
    public Button3D getUpButton() {
        return upButton;
    }
    /**
     * 
     * @return Button3D
     */
    public Button3D getDownButton() {
        return downButton;
    }
    
    /**
     * testing only
     * @param args args
     */
    public static void main(String [] args){
       // FloorControllPanel3D fcp3D = new FloorControllPanel3D();
    }
}
