package view3D;

/*

 * To change this license header, choose License Headers in Project Properties.

 * To change this template file, choose Tools | Templates

 * and open the template in the editor.

 */

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.util.Enumeration;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;




/**

 * Used to create button shapes (a box with customized size)

 * @author luis
 * @version 1.1

 */

public class Button3D {

    private SimpleUniverse buttonUniverse;

    //floors and lifts should with in this group to ensure zoom in out and so on

    private TransformGroup buttonTG;

    private BranchGroup buttonBG;  
    
    private Appearance app;

    public Button3D(float w, String role){

        //buttonUniverse = new SimpleUniverse();
        app = new Appearance();
        
        buttonBG = new BranchGroup();
        buttonBG.setUserData(role);

        buttonTG = new TransformGroup();

        buttonTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        buttonTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        createButtonSharp(w);
        
        
        //enablePicking(buttonBG);

    }
    /**
     * get appearance of the button
     * @return the appearance
     */
    public Appearance getApp() {
        return app;
    }
    
    /**
     * get transform group of the button
     * @return the TransformGroup
     */
    public TransformGroup getButtonTG() {
        return buttonTG;
    }

    /**
     * get the branch group of the button
     * @return  the BranchGroup
     */
    public BranchGroup getButtonBG() {
        return buttonBG;
    }

    /*
    public void createUpArrow(){
        
    }
    
    public void createDownArrow(){
    
    }
    */
    private void createButtonSharp(float w){

        Material mat = new Material();
        mat.setCapability(Material.ALLOW_COMPONENT_READ );
        mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
        mat.setAmbientColor(new Color3f(1f,0.0f,0.0f));

        mat.setDiffuseColor(new Color3f(0.7f,0.7f,0.7f));

        mat.setSpecularColor(new Color3f(0.7f,0.7f,0.7f));

        app.setMaterial(mat); 
        
        

        QuadArray polygon1 = new QuadArray (20,QuadArray.COORDINATES);

//front

        polygon1.setCoordinate(0, new Point3f (0f,0f,0f));
        //polygon1.setNormal(0, new Vector3f(0f,0f,1f));

        polygon1.setCoordinate(1, new Point3f (w,0f,0f));
        //polygon1.setNormal(1, new Vector3f(0f,0f,1f));

        polygon1.setCoordinate(2, new Point3f (w,w,0f));
        //polygon1.setNormal(2, new Vector3f(0f,0f,1f));

        polygon1.setCoordinate(3, new Point3f (0f,w,0f));
       // polygon1.setNormal(3, new Vector3f(0f,0f,1f));
         /*     
        //bottom
        //1
        polygon1.setCoordinate(4, new Point3f (0f,0f,0f));
        //polygon1.setNormal(4, new Vector3f(0f,-1f,0f));
        //2
        polygon1.setCoordinate(5, new Point3f (w,0f,0f));
        //polygon1.setNormal(5, new Vector3f(0f,-1f,0f));
        //3
        polygon1.setCoordinate(6, new Point3f (w,0f,-w));
        //polygon1.setNormal(6, new Vector3f(0f,-1f,-0f));
        //4
        polygon1.setCoordinate(7, new Point3f (0f,0f,-w));
        //polygon1.setNormal(7, new Vector3f(0f,-1f,0f));
        */
        //1
        polygon1.setCoordinate(4, new Point3f (0f,0f,0f));
        //4
        polygon1.setCoordinate(5, new Point3f (0f,0f,-w));
        //3
        polygon1.setCoordinate(6, new Point3f (w,0f,-w));
        //2
        polygon1.setCoordinate(7, new Point3f (w,0f,0f));
        

                //top

        polygon1.setCoordinate(8, new Point3f (0f,w,0f));
        //polygon1.setNormal(8, new Vector3f(0f,1,0f));

        polygon1.setCoordinate(9, new Point3f (w,w,0f));
        //polygon1.setNormal(9, new Vector3f(0,1,0f));

        polygon1.setCoordinate(10, new Point3f (w,w,-w));
        //polygon1.setNormal(10, new Vector3f(0,1,0));

        polygon1.setCoordinate(11, new Point3f (0f,w,-w));
        //polygon1.setNormal(11, new Vector3f(0,1,0));

        /*
        //left
        //1
        polygon1.setCoordinate(12, new Point3f (0f,0f,0f));
        //polygon1.setNormal(12, new Vector3f(-1f,0f,0f));
        //2
        polygon1.setCoordinate(13, new Point3f (0f,0f,-w));
        //polygon1.setNormal(13, new Vector3f(-1f,0f,w));
        //3
        polygon1.setCoordinate(14, new Point3f (0f,w,-w));
        //polygon1.setNormal(14, new Vector3f(-1f,w,-w));
        //4
        polygon1.setCoordinate(15, new Point3f (0f,w,0f));
        //polygon1.setNormal(15, new Vector3f(-1f,w,0f));
        */
        
        //1
        polygon1.setCoordinate(12, new Point3f (0f,0f,0f));
        //4
        polygon1.setCoordinate(13, new Point3f (0f,w,0f));
        //3
        polygon1.setCoordinate(14, new Point3f (0f,w,-w));
        //2
        polygon1.setCoordinate(15, new Point3f (0f,0f,-w));
        
        //right
        polygon1.setCoordinate(16, new Point3f (w,0f,0f));
        //polygon1.setNormal(16, new Vector3f(1,0f,0f));
        polygon1.setCoordinate(17, new Point3f (w,0f,-w));
        //polygon1.setNormal(17, new Vector3f(1,0f,0));
        polygon1.setCoordinate(18, new Point3f (w,w,-w));
        //polygon1.setNormal(18, new Vector3f(1,0,0));
        polygon1.setCoordinate(19, new Point3f (w,w,0));
        //polygon1.setNormal(19, new Vector3f(w,w,0));
        
        PolygonAttributes pa = new PolygonAttributes();
        pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        app.setPolygonAttributes(pa);
        GeometryInfo geometryInfo = new GeometryInfo(polygon1);
        NormalGenerator ng = new NormalGenerator();
	ng.generateNormals(geometryInfo);
        GeometryArray result = geometryInfo.getGeometryArray();
        
        Shape3D s = new Shape3D(result,app);

        buttonTG.addChild(s);
        /*

        Color3f lightColor = new Color3f(1f,1f,1f);
        AmbientLight ambientLightNode1 = new AmbientLight (lightColor);
        BoundingSphere bounds1 = new BoundingSphere(new Point3d(0.2f,0.4f,5f), 100.0f);
        ambientLightNode1.setInfluencingBounds (bounds1);

        MouseRotate myMouseRotate = new MouseRotate();

        myMouseRotate.setTransformGroup(buttonTG);

        myMouseRotate.setSchedulingBounds(new BoundingSphere());

        buttonTG.addChild(myMouseRotate);

        

        MouseTranslate myMouseTranslate = new MouseTranslate();

        myMouseTranslate.setTransformGroup(this.buttonTG);  // ---------------(5)

        myMouseTranslate.setSchedulingBounds(new BoundingSphere());  // ---------------(6)

        this.buttonTG.addChild(myMouseTranslate);

        

        

        MouseZoom myMouseZoom = new MouseZoom();

        myMouseZoom.setTransformGroup(buttonTG);  // ---------------(7)

        myMouseZoom.setSchedulingBounds(new BoundingSphere());  // ---------------(8)

        this.buttonTG.addChild(myMouseZoom);

        buttonBG.addChild(ambientLightNode1);
                */

        buttonBG.addChild(buttonTG);
        
        //buttonUniverse.addBranchGraph(buttonBG);

        

        

    }

    /**
     * enable all shapes clicking functionalities
     * @param node the node
     */
    public void enablePicking(Node node) { 

        node.setPickable(true); 

        node.setCapability(Node.ENABLE_PICK_REPORTING); 

        try { 

           Group group = (Group) node; 

           for (Enumeration e = group.getAllChildren(); e.hasMoreElements();) { 

              enablePicking((Node)e.nextElement()); 

           } 

        } 

        catch(ClassCastException e) { 

            // if not a group node, there are no children so ignore exception 

        } 

        try { 

              Shape3D shape = (Shape3D) node; 

              PickTool.setCapabilities(node, PickTool.INTERSECT_FULL); 

              for (Enumeration e = shape.getAllGeometries(); e.hasMoreElements();) { 

                 Geometry g = (Geometry)e.nextElement(); 

                 g.setCapability(g.ALLOW_INTERSECT); 

              } 

           } 

        catch(ClassCastException e) { 

           // not a Shape3D node ignore exception 

        } 

    } 
    /**
     * testing only
     * @param args args
     */
    public static void main(String [] args){

        //Button3D b = new Button3D(5f);

    }

}
