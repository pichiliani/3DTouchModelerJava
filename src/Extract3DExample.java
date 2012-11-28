import javax.vecmath.*;

import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.swing.JFrame;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;
import java.util.Hashtable;

import java.io.File;
import java.io.IOException;

import java.net.*;
import java.io.*;
import java.util.*;

/**
* An example for loading a geometric object which is not used completely
* in the scene. Only a part of the object is selected and included in the scene.
*
* @author Frank Klawonn
* Last change 07.07.2005
*/
public class Extract3DExample extends JFrame
{

  //The canvas to be drawn upon.
  public Canvas3D myCanvas3D;


  public Extract3DExample()
  {

    //Mechanism for closing the window and ending the program.
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    //Default settings for the viewer parameters.
    myCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());


    //Construct the SimpleUniverse:
    //First generate it using the Canvas.
    SimpleUniverse simpUniv = new SimpleUniverse(myCanvas3D);


    //Default position of the viewer.
    simpUniv.getViewingPlatform().setNominalViewingTransform();


    //The scene is generated in this method.
    createSceneGraph(simpUniv);


    //Add some light to the scene.
    addLight(simpUniv);


    //The following three lines enable navigation through the scene using the mouse.
    OrbitBehavior ob = new OrbitBehavior(myCanvas3D);
    ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE));
    simpUniv.getViewingPlatform().setViewPlatformBehavior(ob);


    //Show the canvas/window.
    setTitle("A part of an object loaded from a file");
    setSize(700,700);
    getContentPane().add("Center", myCanvas3D);
    setVisible(true);


  }




  public static void main(String[] args)
  {
     Extract3DExample e3de = new Extract3DExample();
  }



  //In this method, the objects for the scene are generated and added to 
  //the SimpleUniverse.
  public void createSceneGraph(SimpleUniverse su)
  {



    //Load an obj-file.
    ObjectFile f = new ObjectFile(ObjectFile.RESIZE);
    Scene s = null;

    try
    {
      s = f.load("C:\\Meus Documentos\\pos\\Tese\\Artigos2012\\UIST_2012\\Java3d\\java3dexamples\\java3dexamples\\schiff.obj");
    }
    catch (Exception e)
    {
      System.out.println("File loading failed:" + e);
    }



    //Choose a part of the object (here: schiffrumpf).
    Hashtable namedObjects = s.getNamedObjects();
    Shape3D partOfTheObject = (Shape3D) namedObjects.get("schiffsrumpf");

    //Generate a copy of the chosen part.
    Shape3D extractedObject = (Shape3D) partOfTheObject.cloneTree();
    
    extractedObject.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
            
    //Paint the part in blue.
    
    
    Appearance lightBlueApp = new Appearance();
    
    
    lightBlueApp.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
    setToMyDefaultAppearance(lightBlueApp,new Color3f(0.0f,0.1f,0.3f));
    extractedObject.setAppearance(lightBlueApp);
    
    
    Appearance app = new Appearance();
    PolygonAttributes polyAttribs = new PolygonAttributes( PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE, 0 );
    app.setPolygonAttributes(polyAttribs );
    extractedObject.setAppearance(app);
    
    /*
	// Variável estática para a janela de captura do BCI
    ThreadManipulaModelo t  = new ThreadManipulaModelo();
    t.s = extractedObject;
    t.start();
    */

    //Assign the chosen part to a transformation group.
    Transform3D tfObject = new Transform3D();
    tfObject.rotZ(0.4*Math.PI);
    Transform3D xRotation = new Transform3D();
    xRotation.rotY(0.4*Math.PI);
    tfObject.mul(xRotation);
    TransformGroup tgObject = new TransformGroup(tfObject);

    tgObject.addChild(extractedObject);

    BranchGroup theScene = new BranchGroup();

    //Add the transformation group with the blue part to the scene.
    theScene.addChild(tgObject);


    //The following four lines generate a white background.
    Background bg = new Background(new Color3f(1.0f,1.0f,1.0f));
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);
    bg.setApplicationBounds(bounds);
    theScene.addChild(bg);



    theScene.compile();

    //Add the scene to the universe.
    su.addBranchGraph(theScene);
  }


  /**
  * Generates a default surface (Appearance) in a specified colour.
  *
  * @param app      The Appearance for the surface.
  * @param col      The colour.
  */
  public static void setToMyDefaultAppearance(Appearance app, Color3f col)
  {
    app.setMaterial(new Material(col,col,col,col,120.0f));
  }



  //Some light is added to the scene here.
  public void addLight(SimpleUniverse su)
  {

    BranchGroup bgLight = new BranchGroup();

    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
    Color3f lightColour1 = new Color3f(1.0f,1.0f,1.0f);
    Vector3f lightDir1  = new Vector3f(-1.0f,0.0f,-0.5f);
    DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
    light1.setInfluencingBounds(bounds);

    bgLight.addChild(light1);
    su.addBranchGraph(bgLight);
  }



}

/*
class ThreadManipulaModelo extends Thread 
{
	public Shape3D s;
	
	public void run() 
	{
		
		try {
			Thread.sleep(5000);	
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	    
		Appearance lightBlueApp = new Appearance();
		lightBlueApp.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
	    setToMyDefaultAppearance(lightBlueApp,new Color3f(0.2f,0.5f,0.2f));
	    s.setAppearance(lightBlueApp);

	}
	
	public static void setToMyDefaultAppearance(Appearance app, Color3f col)
	  {
	    app.setMaterial(new Material(col,col,col,col,120.0f));
	  }
}
	
*/