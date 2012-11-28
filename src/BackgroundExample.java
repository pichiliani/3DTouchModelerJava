import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.swing.JFrame;


/**
* An example for using an image loaded from a file as a background.
* This program needs the file darkclouds.jpg.
*
* @author Frank Klawonn
* Last change 17.07.2005
*/
public class BackgroundExample extends JFrame
{


  //The canvas to be drawn upon.
  public Canvas3D myCanvas3D;


  public BackgroundExample()
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
    setTitle("An image as a background");
    setSize(1000,700);
    getContentPane().add("Center", myCanvas3D);
    setVisible(true);


  }




  public static void main(String[] args)
  {
     BackgroundExample be = new BackgroundExample();
  }





  //In this method, the objects for the scene are generated and added to 
  //the SimpleUniverse.
  public void createSceneGraph(SimpleUniverse su)
  {

//*** A cube to be included in the scene including its transformation group.

    //The colours for the Appearance.
    Color3f ambientColourFBox = new Color3f(0.4f,0.0f,0.0f);
    Color3f emissiveColourFBox = new Color3f(0.0f,0.0f,0.0f);
    Color3f diffuseColourFBox = new Color3f(0.6f,0.0f,0.0f);
    Color3f specularColourFBox = new Color3f(0.8f,0.0f,0.0f);
    float shininessFBox = 10.0f;


    //The Appearance for the cube.
    Appearance fBoxApp = new Appearance();
    fBoxApp.setMaterial(new Material(ambientColourFBox,emissiveColourFBox,
                          diffuseColourFBox,specularColourFBox,shininessFBox));

    //The cube.
    Box fBox = new Box(0.2f,0.2f,0.2f,fBoxApp);

    //The transformation group of the cube.
    Transform3D tfFBox = new Transform3D();
    tfFBox.rotY(Math.PI/6);
    Transform3D rotationX = new Transform3D();
    rotationX.rotX(-Math.PI/5);
    tfFBox.mul(rotationX);
    TransformGroup tgFBox = new TransformGroup(tfFBox);
    tgFBox.addChild(fBox);


//*** And a sphere with its transformation group.

    //The colours for the Appearance.
    Color3f ambientColourBSphere = new Color3f(0.0f,0.4f,0.0f);
    Color3f emissiveColourBSphere = new Color3f(0.0f,0.0f,0.0f);
    Color3f diffuseColourBSphere = new Color3f(0.0f,0.0f,0.0f);
    Color3f specularColourBSphere = new Color3f(0.0f,0.8f,0.0f);
    float shininessBSphere = 128.0f;

    //The Appearance for the sphere.
    Appearance bSphereApp = new Appearance();
    bSphereApp.setMaterial(new Material(ambientColourBSphere,emissiveColourBSphere,
                          diffuseColourBSphere,specularColourBSphere,shininessBSphere));

    //The sphere.
    Sphere bSphere = new Sphere(0.4f,bSphereApp);

    //The transformation group for the sphere.
    Transform3D tfBSphere = new Transform3D();
    tfBSphere.setTranslation(new Vector3f(0.0f,2.7f,-10.5f));
    TransformGroup tgBSphere = new TransformGroup(tfBSphere);
    tgBSphere.addChild(bSphere);


    //Add everything to the scene.
    BranchGroup theScene = new BranchGroup();
    theScene.addChild(tgFBox);
    theScene.addChild(tgBSphere);



    //The bounding region for the background.
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);
    //Load the background image.
    TextureLoader textureLoad = new TextureLoader("C:\\Meus Documentos\\pos\\Tese\\Artigos2012\\UIST_2012\\Java3d\\java3dexamples\\java3dexamples\\darkclouds.jpg",null);
    //Define the image as the background and add it to the scene.
    Background bgImage = new Background(textureLoad.getImage());
    bgImage.setApplicationBounds(bounds);
    theScene.addChild(bgImage);


    theScene.compile();

    //Add the scene to the universe.
    su.addBranchGraph(theScene);

  }






  //Some light is added to the scene here.
  public void addLight(SimpleUniverse su)
  {

    BranchGroup bgLight = new BranchGroup();

    //Directional light.
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), Double.MAX_VALUE);
    Color3f lightColour1 = new Color3f(1.0f,1.0f,1.0f);
    Vector3f lightDir1  = new Vector3f(-1.0f,0.0f,-0.1f);
    DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
    light1.setInfluencingBounds(bounds);

    bgLight.addChild(light1);


    //Ambient light.
    Color3f ambientLightColour = new Color3f(0.5f, 0.5f, 0.5f);
    AmbientLight ambLight = new AmbientLight(ambientLightColour);
    ambLight.setInfluencingBounds(bounds);
    bgLight.addChild(ambLight);



    su.addBranchGraph(bgLight);

  }



}
