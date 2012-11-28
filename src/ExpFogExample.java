import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.swing.JFrame;


/**
* An example for the use of exponential fog and the class Link.
*
* @author Frank Klawonn
* Last change 07.07.2005
*/
public class ExpFogExample extends JFrame
{

  //The canvas to be drawn upon.
  public Canvas3D myCanvas3D;



  public ExpFogExample()
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
    setTitle("Exponential fog");
    setSize(700,700);
    getContentPane().add("Center", myCanvas3D);
    setVisible(true);

  }




  public static void main(String[] args)
  {
     ExpFogExample efe = new ExpFogExample();
  }





  //In this method, the objects for the scene are generated and added to 
  //the SimpleUniverse.
  public void createSceneGraph(SimpleUniverse su)
  {

    BranchGroup theScene = new BranchGroup();

    //Generate a sphere.
    Color3f ambientColourBSphere = new Color3f(0.0f,0.0f,0.0f);
    Color3f emissiveColourBSphere = new Color3f(0.0f,0.0f,0.0f);
    Color3f diffuseColourBSphere = new Color3f(0.9f,0.9f,0.9f);
    Color3f specularColourBSphere = new Color3f(0.9f,0.9f,0.9f);
    float shininessBSphere = 128.0f;

    Appearance bSphereApp = new Appearance();

    bSphereApp.setMaterial(new Material(ambientColourBSphere,
                                        emissiveColourBSphere,
                                        diffuseColourBSphere,
                                        specularColourBSphere,
                                        shininessBSphere));

    Sphere bSphere = new Sphere(0.1f,bSphereApp);


    //Assign the sphere to a SharedGroup to use it multiple
    //times in the scene.
    SharedGroup sgSphere = new SharedGroup();
    sgSphere.addChild(bSphere);



    //Generate a two-dimensional array of rows*columns spheres.
    //rowStep and columnStep define the distance between the spheres.
    int rows = 40;
    float rowStep = 0.25f;
    int columns = 20;
    float columnStep = 0.3f;

    TransformGroup[][] tgArray = new TransformGroup[rows][columns];
    Transform3D[][] tfArray = new Transform3D[rows][columns];

    for (int i=0; i<rows; i++)
    {
      for (int j=0; j<columns; j++)
      {
        tfArray[i][j] = new Transform3D();
        tfArray[i][j].setTranslation(new Vector3f(i*columnStep-2.0f,j*0.1f-0.5f,-j*rowStep));
        tgArray[i][j] = new TransformGroup(tfArray[i][j]);
        tgArray[i][j].addChild(new Link(sgSphere));
        theScene.addChild(tgArray[i][j]);
      }
    }




    //Generate a background in the colour of the fog.
    Color3f fogColour = new Color3f(0.5f,0.5f,0.5f);
    Background bg = new Background(fogColour);
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);
    bg.setApplicationBounds(bounds);
    theScene.addChild(bg);


    //Generate exponential fog.
    ExponentialFog fog = new ExponentialFog(fogColour,6.0f);
    fog.setInfluencingBounds(bounds);

    theScene.addChild(fog);

    theScene.compile();

    //Add the scene to the universe.
    su.addBranchGraph(theScene);

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
