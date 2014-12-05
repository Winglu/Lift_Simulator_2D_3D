package view3D;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
/**
 * Lift 3D view
 * @author luis
 */
public class Lift3DView {
    
    
    private SimpleUniverse liftUniverse;//test prupose only
    private TransformGroup liftTG;
    private TransformGroup leftTG;
    private TransformGroup rightTG;
    private BranchGroup liftBG;
    
    public Shape3D leftDoor;
    public Shape3D rightDoor;
    
    public Font3D f3d;
    public Text3D t3d;
    
    /**
     * Constructor
     * @param x lift position
     */
    public Lift3DView(float x){
        //liftUniverse= new SimpleUniverse();
        liftTG = new TransformGroup();
        liftBG = new BranchGroup();
        leftTG = new TransformGroup();
        rightTG = new TransformGroup();
        f3d = new Font3D(new Font("TestFont", Font.PLAIN,1),new FontExtrusion());
        t3d = new Text3D(f3d, "",new Point3f(0.2f,0.45f,0.07f));
        t3d.setCapability(Text3D.ALLOW_STRING_READ);
        t3d.setCapability(Text3D.ALLOW_STRING_WRITE);
        //Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f blue = new Color3f(Color.BLUE);
        Appearance a = new Appearance();
 
        Material m = new Material(blue, blue, blue, blue, 80.0f);
        m.setLightingEnable(true);
        a.setMaterial(m);
        Shape3D sh = new Shape3D();
        sh.setAppearance(a);
        Transform3D tf = new Transform3D();
        tf.setTranslation(new Vector3f(0.14f,0.38f,0.07f));
        
        tf.setScale(0.2);
        TransformGroup tg = new TransformGroup();
        
        sh.setGeometry(t3d);
        tg.addChild(sh);
        
        //add light to numbers
        AmbientLight aLgt = new AmbientLight(blue);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.2f,0.45f,0.07f), 100.0f);
        aLgt.setInfluencingBounds(bounds);
        tg.setTransform(tf);
        liftTG.addChild(tg);
        //t3d.setPosition(new Point3f(0.2f,0.45f,0.07f));
        //t3d.setString("Test");
        
        generateTop(0);
        generateRight(0);
        generateLeft(0);
        generateBottom(0);
        generateBack(0);
        
        generateLeftDorr();
        generateRightDoor();
        
        generateBorder();
        liftBG.addChild(liftTG);  
        liftTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        liftTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        leftTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        leftTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        rightTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        rightTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        /*
        Color3f lightColor = new Color3f(1f,1f,1f);
        AmbientLight ambientLightNode = new AmbientLight (lightColor);
        BoundingSphere bounds1 = new BoundingSphere(new Point3d(0.2f,0.4f,5f), 100.0f);
        ambientLightNode.setInfluencingBounds (bounds1);
        liftBG.addChild(ambientLightNode);
        
        */
        /*
      
        //
        
         
        
        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(liftTG);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        
        liftBG.addChild(myMouseRotate);
        
        MouseTranslate myMouseTranslate = new MouseTranslate();
        myMouseTranslate.setTransformGroup(this.liftTG);  // ---------------(5)
        myMouseTranslate.setSchedulingBounds(new BoundingSphere());  // ---------------(6)
        this.liftBG.addChild(myMouseTranslate);

        MouseZoom myMouseZoom = new MouseZoom();
        myMouseZoom.setTransformGroup(liftTG);  // ---------------(7)
        myMouseZoom.setSchedulingBounds(new BoundingSphere());
        this.liftBG.addChild(myMouseZoom);
        
     
        Color3f lightColor = new Color3f(1f,1f,1f);
        AmbientLight ambientLightNode1 = new AmbientLight (lightColor);
        BoundingSphere bounds1 = new BoundingSphere(new Point3d(0.2f,0.4f,5f), 100.0f);
        ambientLightNode1.setInfluencingBounds (bounds1);

        AmbientLight ambientLightNode2 = new AmbientLight (lightColor);
        BoundingSphere bounds2 = new BoundingSphere(new Point3d(0.2f,0.4f,-5f), 100.0f);
        ambientLightNode2.setInfluencingBounds (bounds2);
        
        AmbientLight ambientLightNode3 = new AmbientLight (lightColor);
        BoundingSphere bounds3 = new BoundingSphere(new Point3d(-5.0f,0.4f,0.0f), 100.0f);
        ambientLightNode3.setInfluencingBounds (bounds3);
        
        AmbientLight ambientLightNode4 = new AmbientLight (lightColor);
        BoundingSphere bounds4 = new BoundingSphere(new Point3d(10.0f,0.4f,0.0f), 100.0f);
        ambientLightNode4.setInfluencingBounds (bounds4);
        
        AmbientLight ambientLightNode5 = new AmbientLight (lightColor);
        BoundingSphere bounds5 = new BoundingSphere(new Point3d(0.2f,0.4f,-0.2f), 100.0f);
        ambientLightNode5.setInfluencingBounds (bounds5);
        
        AmbientLight ambientLightNode6 = new AmbientLight (lightColor);
        BoundingSphere bounds6 = new BoundingSphere(new Point3d(0.2f,-5f,0.0), 100.0f);
        ambientLightNode6.setInfluencingBounds (bounds6);
        
        
        
        liftBG.addChild(ambientLightNode1);
        liftBG.addChild(ambientLightNode2);
        liftBG.addChild(ambientLightNode3);
        liftBG.addChild(ambientLightNode4);
        liftBG.addChild(ambientLightNode5);
    
        TransformGroup tg = liftUniverse.getViewingPlatform().getViewPlatformTransform();

	Transform3D t3d = new Transform3D();
	t3d.setTranslation(new Vector3f(0f,0f,10f));
	tg.setTransform(t3d);
        
        liftUniverse.addBranchGraph(liftBG);
                */
    }
    
    private void generateBorder(){
        //top
        Shape3D rec1 = generateRec(
              //OUTSIDE
              new Point3f(0f,0.85f,0f),
              new Point3f(0.45f,0.85f,0f),
              new Point3f(0.45f,0.90f,0f),
              new Point3f(0,0.90f,0f),
              //INSIDE 1,4,3,2
              new Point3f(0f,0.85f,0f),
              new Point3f(0.45f,0.85f,0f),
              new Point3f(0.45f,0.90f,0f),
              new Point3f(0,0.90f,0f)
        );
        liftTG.addChild(rec1);
        
        //left
        Shape3D rec2 = generateRec(
              //OUTSIDE
              new Point3f(0f,0f,0f),
              new Point3f(0.05f,0.0f,0f),
              new Point3f(0.05f,0.9f,0.0f),
              new Point3f(0,0.9f,0.0f),
              //INSIDE 1,4,3,2
              new Point3f(0f,0f,0f),
              new Point3f(0.05f,0.0f,0f),
              new Point3f(0.05f,0.9f,0.0f),
              new Point3f(0,0.9f,0.0f)
        );
        liftTG.addChild(rec2);
        //right
        Shape3D rec3 = generateRec(
              //OUTSIDE
              new Point3f(0.40f,0f,0f),
              new Point3f(0.45f,0f,0f),
              new Point3f(0.45f,0.9f,0f),
              new Point3f(0.40f,0.9f,0f),
              //INSIDE 1,4,3,2
              new Point3f(0.40f,0f,0f),
              new Point3f(0.45f,0f,0f),
              new Point3f(0.45f,0.9f,0f),
              new Point3f(0.40f,0.9f,0f)
        );
        liftTG.addChild(rec3);
        //
        Shape3D rec4 = generateRec(
              //OUTSIDE
              new Point3f(0.0f,0f,0f),
              new Point3f(0.45f,0f,0f),
              new Point3f(0.45f,0.05f,0f),
              new Point3f(0.0f,0.05f,0f),
              //INSIDE 1,4,3,2
              new Point3f(0.0f,0f,0f),
              new Point3f(0.45f,0f,0f),
              new Point3f(0.45f,0.05f,0f),
              new Point3f(0.0f,0.05f,0f)
        );
        liftTG.addChild(rec4);
        
    }
    
    /**
     * Used to animate door close and open
     * @param doorWidth door width
     */
    public void changeDoorSize(float doorWidth){
        float space = 0.45f*doorWidth*0.01f;
        space = space/2f;
        Transform3D transform = new Transform3D();
        Vector3f vector = new Vector3f(0f-space,0f,0.0f);
        transform.setTranslation(vector);
        leftTG.setTransform(transform);
        
        transform = new Transform3D();
        vector = new Vector3f(0.026f+space-0.02f,0f,0.0f);
        transform.setTranslation(vector);
        rightTG.setTransform(transform);
        //5
        //liftTG.removeChild(leftDoor);
        /*
        leftDoor = generateRec(
                new Point3f(0f,0f,0f),
                new Point3f(0.224f-space,0f,0f),
                new Point3f(0.224f-space,0.9f,0f),
                new Point3f(0,0.9f,0f),
                new Point3f(0f,0f,0.05f),
                new Point3f(0.224f-space,0f,0.05f),
                new Point3f(0.224f-space,0.9f,0.05f),
                new Point3f(0,0.9f,0.05f)
        );
        liftTG.addChild(leftDoor);
        //6
        //liftTG.removeChild(rightDoor);
        rightDoor = generateRec(
                new Point3f(0.226f+space,0f,0f),
                new Point3f(0.45f,0f,0f),
                new Point3f(0.45f,0.9f,0f),
                new Point3f(0.226f+space,0.9f,0f),
                new Point3f(0.226f,0f,0.05f),
                new Point3f(0.45f,0f,0.05f),
                new Point3f(0.45f,0.9f,0.05f),
                new Point3f(0.226f+space,0.9f,0.05f)
        );
        liftTG.addChild(rightDoor);
        */
    }
    
    private void generateLeftDorr(){
        leftDoor = generateRec(
                
                //FRONT 1,2,3,4
                new Point3f(0f,0f,0f),
                new Point3f(0.224f,0f,0f),
                new Point3f(0.224f,0.9f,0f),
                new Point3f(0,0.9f,0f),
                //BACK 1,4,3,2
                new Point3f(0f,0f,0.05f),
                new Point3f(0,0.9f,0.05f),
                new Point3f(0.224f,0.9f,0.05f),
                new Point3f(0.224f,0f,0.05f)
               
                
        );
        //border right left
        Shape3D rl = generateRec(
                
                //FRONT 1,2,3,4
                new Point3f(0f,0f,0f),
                new Point3f(0f,0f,0.05f),
                new Point3f(0,0.9f,0.05f),
                new Point3f(0,0.9f,0f),
                //BACK 1,4,3,2
                new Point3f(0.224f,0f,0.0f),
                new Point3f(0.224f,0.9f,0.0f),
                new Point3f(0.224f,0.9f,0.05f),
                new Point3f(0.224f,0f,0.05f)
        );
        //border top down
        
        leftTG.addChild(rl);
        leftTG.addChild(leftDoor);
        liftTG.addChild(leftTG);
    }
    
    private void generateRightDoor(){
        rightDoor = generateRec(
                
                //FRONT 1,2,3,4
                new Point3f(0.226f,0f,0f),
                new Point3f(0.45f,0f,0f),
                new Point3f(0.45f,0.9f,0f),
                new Point3f(0.226f,0.9f,0f),
                //BACK
                //1,4,3,2 
                new Point3f(0.226f,0f,0.05f),
                new Point3f(0.226f,0.9f,0.05f),
                new Point3f(0.45f,0.9f,0.05f),
                new Point3f(0.45f,0f,0.05f)
               
                
        );
        Shape3D rl = generateRec(
                
                //FRONT 1,2,3,4
                new Point3f(0.226f,0f,0f),
                new Point3f(0.226f,0f,0.05f),
                new Point3f(0.226f,0.9f,0.05f),
                new Point3f(0.226f,0.9f,0f),
                //BACK 1,4,3,2
                new Point3f(0.45f,0f,0.0f),
                new Point3f(0.45f,0.9f,0.0f),
                new Point3f(0.45f,0.9f,0.05f),
                new Point3f(0.45f,0f,0.05f)
        );
        rightTG.addChild(rl);
        rightTG.addChild(rightDoor);
        liftTG.addChild(rightTG);
    }
    
    private Shape3D generateRec(Point3f a,Point3f b,Point3f c,Point3f d,Point3f a2,Point3f b2,Point3f c2,Point3f d2){
        Shape3D newRec;
        Appearance ra = new Appearance();
        Material mat = new Material();
	mat.setAmbientColor(new Color3f(0.0f,0.0f,1.0f));
	mat.setDiffuseColor(new Color3f(0.7f,0.7f,0.7f));
	mat.setSpecularColor(new Color3f(0.7f,0.7f,0.7f));
        mat.setCapability(Material.ALLOW_COMPONENT_READ);
        mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
	ra.setMaterial(mat); 
        QuadArray p = new QuadArray (8, (QuadArray.COORDINATES));

    	p.setCoordinate (0, a);//right
    	p.setCoordinate (1, b);//left
    	p.setCoordinate (2, c);//left
    	p.setCoordinate (3, d);//gright
        p.setCoordinate (4, a2);//right
    	p.setCoordinate (5, b2);//left
    	p.setCoordinate (6, c2);//left
    	p.setCoordinate (7, d2);//right
        /*
        p.setNormal(0, new Vector3f(a));
        p.setNormal(1,  new Vector3f(b));
        p.setNormal(2,  new Vector3f(c));
        p.setNormal(3,  new Vector3f(d));
        p.setNormal(4,  new Vector3f(a2));
        p.setNormal(5,  new Vector3f(b2));
        p.setNormal(6,  new Vector3f(c2));
        p.setNormal(7,  new Vector3f(d2));
        */
        GeometryInfo geometryInfo = new GeometryInfo(p);
        NormalGenerator ng = new NormalGenerator();
	ng.generateNormals(geometryInfo);
        GeometryArray result = geometryInfo.getGeometryArray();
        newRec = new Shape3D(result,ra);
        return newRec;
    }
    
    public void setLiftColor(Color3f c){
        
        Enumeration e = liftTG.getAllChildren();
        while(e.hasMoreElements()){
            Object s = e.nextElement();
            if(s instanceof Shape3D){
                ((Shape3D)s).getAppearance().getMaterial().setDiffuseColor(c);
            }
            
        }
        e = rightTG.getAllChildren();
        while (e.hasMoreElements()){
            Object s = e.nextElement();
            if(s instanceof Shape3D){
                ((Shape3D)s).getAppearance().getMaterial().setDiffuseColor(c);
            }
        }
        
        e = leftTG.getAllChildren();
        while (e.hasMoreElements()){
            Object s = e.nextElement();
            if(s instanceof Shape3D){
                ((Shape3D)s).getAppearance().getMaterial().setDiffuseColor(c);
            }
        }
        
    
    }
    
    private void generateTop(float x){

        Shape3D rec1 = generateRec(
                //OUTSIDE
                new Point3f(x,0.9f,0f),
                new Point3f(x+0.45f,0.9f,0f),
                new Point3f(x+0.45f,0.9f,-0.45f),
                new Point3f(x,0.9f,-0.45f),
                //INSIDE 1,4,3,2
                new Point3f(x+0.05f,0.85f,0f),
                new Point3f(x+0.05f,0.85f,-0.4f),
                new Point3f(x+0.40f,0.85f,-0.4f),
                new Point3f(x+0.40f,0.85f,0f)
                
                
                
        );
        liftTG.addChild(rec1);
 
        //inside
        //Shape3D rec2 = generateRec(new Point3f(x+0.5f,0.8f,0f),new Point3f(x+0.4f,0.8f,0f),new Point3f(x+0.4f,0.8f,-0.4f),new Point3f(x+0.5f,0.8f,-0.4f));
        //liftTG.addChild(rec2);
    }
    private void generateRight(float x){
        Shape3D rec1 = generateRec(
                //OUTSIDE 1,2,3,4
                new Point3f(x+0.45f,0.0f,0.0f),
                new Point3f(x+0.45f,0.0f,-0.45f),
                new Point3f(x+0.45f,0.9f,-0.45f),
                new Point3f(x+0.45f,0.9f,0.0f),
                //INSIDE 1,4,3,2
                new Point3f(x+0.4f,0.05f,0.0f),
                new Point3f(x+0.4f,0.85f,-0.0f),
                new Point3f(x+0.4f,0.85f,-0.4f),
                new Point3f(x+0.4f,0.05f,-0.4f)
                
        );
        liftTG.addChild(rec1);
    }
    private void generateLeft(float x){
        Shape3D rec1 = generateRec(
                //OUTSIDE 1,4,3,2
                
                new Point3f(x,0.0f,0.0f),
                new Point3f(x,0.9f,0.0f),
                new Point3f(x,0.9f,-0.45f),
                new Point3f(x,0.0f,-0.45f),
              
                
                
                //INSIDE
                new Point3f(x+0.05f,0.05f,0.0f),
                new Point3f(x+0.05f,0.05f,-0.40f),
                new Point3f(x+0.05f,0.85f,-0.40f),
                new Point3f(x+0.05f,0.85f,0.0f)
        );
        liftTG.addChild(rec1);
    }
    private void generateBottom(float x){
        Shape3D rec1 = generateRec(
                
                //OUTSIDE 1,4,3,2
                new Point3f(x,0.0f,0.0f),
                new Point3f(x,0.0f,-0.45f),
                new Point3f(x+0.45f,0.0f,-0.45f),
                new Point3f(x+0.45f,0.0f,0.0f),
                
                
                //INSIDE
                new Point3f(x+0.05f,0.05f,0.0f),
                new Point3f(x+0.4f,0.05f,0.0f),
                new Point3f(x+0.4f,0.05f,-0.40f),
                new Point3f(x+0.05f,0.05f,-0.40f)
        );
        liftTG.addChild(rec1);
    }
    private void generateBack(float x){
        Shape3D rec1 = generateRec(
                /*
                new Point3f(x,0.0f,0f),
                new Point3f(x+0.45f,0.0f,0f),
                new Point3f(x+0.45f,0.9f,0f),
                new Point3f(x,0.9f,0f),
                new Point3f(x+0.05f,0.05f,-0.05f),
                new Point3f(x+0.40f,0.05f,-0.05f),
                new Point3f(x+0.40f,0.85f,-0.05f),
                new Point3f(x+0.05f,0.85f,-0.05f)
        */
                //1,4,3,2
                new Point3f(x,0.0f,-0.45f),
                new Point3f(x,0.9f,-0.45f),
                new Point3f(x+0.45f,0.9f,-0.45f),
                new Point3f(x+0.45f,0.0f,-0.45f),
                
                
                
                new Point3f(x+0.05f,0.05f,-0.4f),
                new Point3f(x+0.40f,0.05f,-0.4f),
                new Point3f(x+0.40f,0.85f,-0.4f),
                new Point3f(x+0.05f,0.85f,-0.4f)
        );
        liftTG.addChild(rec1);
    }
    
    /**
     * used to animate lift up and down
     */
    public void moveLift(){
        float i=-2f;
        while(i<2){
            i+=0.1;
            Transform3D transform = new Transform3D();
            Vector3f vector = new Vector3f( .0f, i, .0f);
            transform.setTranslation(vector);
            liftTG.setTransform(transform);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Lift3DView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }

    /**
     * 
     * @return Shape3D
     */
    public Shape3D getLeftDoor() {
        return leftDoor;
    }
    
    /**
     * 
     * @return Shape3D
     */
    public Shape3D getRightDoor() {
        return rightDoor;
    }
    
    /**
     * testing only
     * @param args args
     */
    public static void main(String [] args){
        Lift3DView lv = new Lift3DView(0);
  
    }

    /**
     * 
     * @return TransformGroup
     */
    public TransformGroup getLiftTG() {
        return liftTG;
    }

    /**
     * 
     * @return BranchGroup
     */
    public BranchGroup getLiftBG() {
        return liftBG;
    }
    
    
}
