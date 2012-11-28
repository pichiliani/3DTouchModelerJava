import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.swing.JFrame;

/**
* Use of interpolated and screen-door transparency.
* Two transparent cubes, one rendered with interploated, the other with
* screen door transparency. A sphere is placed behind each cube.
*
* @author Frank Klawonn
* Last change 20.07.2005
*/
public class TransparencyExample extends JFrame
{

  //The canvas to be drawn upon.
  public Canvas3D myCanvas3D;


  public TransparencyExample()
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
    setTitle("Interpolated and screen door transparency");
    setSize(700,700);
    getContentPane().add("Center", myCanvas3D);
    setVisible(true);

  }




  public static void main(String[] args)
  {
     TransparencyExample te = new TransparencyExample();
  }


  //In this method, the objects for the scene are generated and added to 
  //the SimpleUniverse.
  public void createSceneGraph(SimpleUniverse su)
  {

    float cubeEdge = 0.2f;//Half edge length of the transparent cube.
    float radius = 0.4f;  //Radius of the nontransparent sphere.
    float xShift = 0.6f;  //Shift between the cubes.
    float xShiftSphere = -0.4f; //Shift of the spheres (left/right).
    float sphereLift = 0.5f; //Shift of the spheres (up/down).
    float sphereBackShift = -2.5f;//Shift of the spheres (forward/backward).
    float transparencyCoefficient = 0.8f;//Transparency coefficient.


    //The colours for the Appearance of the two cubes.
    Color3f ambientColourFBox = new Color3f(0.3f,0.0f,0.0f);
    Color3f emissiveColourFBox = new Color3f(0.0f,0.0f,0.0f);
    Color3f diffuseColourFBox = new Color3f(0.6f,0.0f,0.0f);
    Color3f specularColourFBox = new Color3f(0.8f,0.0f,0.0f);
    float shininessFBox = 128.0f;


    //The Appearance for the left cube.
    Appearance fBoxApp1 = new Appearance();

    fBoxApp1.setMaterial(new Material(ambientColourFBox,emissiveColourFBox,
                          diffuseColourFBox,specularColourFBox,shininessFBox));

    //Generate interpolated transparency.
    TransparencyAttributes ta1 = new TransparencyAttributes();
    ta1.setTransparencyMode(TransparencyAttributes.BLENDED);
    ta1.setTransparency(transparencyCoefficient);

    fBoxApp1.setTransparencyAttributes(ta1);


    //Generate a cube with interpolated transparency.
    Box fBox1 = new Box(cubeEdge,cubeEdge,cubeEdge,fBoxApp1);


    //Position the cube.
    Transform3D tfFBox1 = new Transform3D();
    Transform3D rotationX = new Transform3D();
    rotationX.rotX(-Math.PI/5);
    tfFBox1.mul(rotationX);

    //The transformation group for the cube.
    TransformGroup tgFBox1 = new TransformGroup(tfFBox1);
    tgFBox1.addChild(fBox1);



    //Generate a nontransparent Appearance for the spheres.
    Color3f ambientColourBSphere = new Color3f(0.0f,0.7f,0.0f);
    Color3f emissiveColourBSphere = new Color3f(0.0f,0.0f,0.0f);
    Color3f diffuseColourBSphere = new Color3f(0.0f,0.7f,0.0f);
    Color3f specularColourBSphere = new Color3f(0.0f,0.9f,0.0f);
    float shininessBSphere = 120.0f;

    Appearance bSphereApp = new Appearance();

    bSphereApp.setMaterial(new Material(ambientColourBSphere,emissiveColourBSphere,
                          diffuseColourBSphere,specularColourBSphere,shininessBSphere));

    //The left sphere and its transformation group.
    Sphere bSphere1 = new Sphere(radius,bSphereApp);

    Transform3D tfBSphere1 = new Transform3D();
    tfBSphere1.setTranslation(new Vector3f(xShiftSphere,sphereLift,sphereBackShift));
    TransformGroup tgBSphere1 = new TransformGroup(tfBSphere1);
    tgBSphere1.addChild(bSphere1);


    //The left cube and the left sphere are combined in one transformation group
    //in order to position them jointly.
    Transform3D tf1 = new Transform3D();
    tf1.setTranslation(new Vector3f(-xShift,0.0f,0.0f));
    TransformGroup tg1 = new TransformGroup(tf1);
    tg1.addChild(tgFBox1);
    tg1.addChild(tgBSphere1);


    //The right cube with sreen door transparency.
    Appearance fBoxApp2 = new Appearance();
    fBoxApp2.setMaterial(new Material(ambientColourFBox,emissiveColourFBox,
                          diffuseColourFBox,specularColourFBox,shininessFBox));

    //Generate screen door transparency.
    TransparencyAttributes ta2 = new TransparencyAttributes();
    ta2.setTransparencyMode(TransparencyAttributes.SCREEN_DOOR);
    ta2.setTransparency(transparencyCoefficient);

    fBoxApp2.setTransparencyAttributes(ta2);

    Box fBox2 = new Box(cubeEdge,cubeEdge,cubeEdge,fBoxApp2);


    //Position the cube.
    Transform3D tfFBox2 = new Transform3D();
    tfFBox2.mul(rotationX);
    TransformGroup tgFBox2 = new TransformGroup(tfFBox2);

    //The transformation group for the right cube.
    tgFBox2.addChild(fBox2);


    //The right sphere and its transformation group.
    Sphere bSphere2 = new Sphere(radius,bSphereApp);
    Transform3D tfBSphere2 = new Transform3D();
    tfBSphere2.setTranslation(new Vector3f(-xShiftSphere,sphereLift,sphereBackShift));
    TransformGroup tgBSphere2 = new TransformGroup(tfBSphere2);
    tgBSphere2.addChild(bSphere2);



    //The right cube and the right sphere are combined in one transformation group
    //in order to position them jointly.
    Transform3D tf2 = new Transform3D();
    tf2.setTranslation(new Vector3f(xShift,0.0f,0.0f));
    TransformGroup tg2 = new TransformGroup(tf2);
    tg2.addChild(tgFBox2);
    tg2.addChild(tgBSphere2);




    //Add everything to the scene.
    BranchGroup theScene = new BranchGroup();;
    theScene.addChild(tg1);
    theScene.addChild(tg2);


    //Generate a white background.
    Background bg = new Background(new Color3f(1.0f,1.0f,1.0f));
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);
    bg.setApplicationBounds(bounds);
    theScene.addChild(bg);


    theScene.compile();

    //Add the scene to the universe.
    su.addBranchGraph(theScene);

  }






  //Some light is added to the scene here.
  public void addLight(SimpleUniverse su)
  {

    BranchGroup bgLight = new BranchGroup();

    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);

    //Directional light.
    Color3f lightColour1 = new Color3f(1.0f,1.0f,1.0f);
    Vector3f lightDir1  = new Vector3f(0.0f,0.0f,-0.1f);
    DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
    light1.setInfluencingBounds(bounds);

    bgLight.addChild(light1);


    //Ambient light.
    Color3f lightColour2 = new Color3f(0.5f, 0.5f, 0.5f);
    AmbientLight light2 = new AmbientLight(lightColour2);
    light2.setInfluencingBounds(bounds);
    bgLight.addChild(light2);


    su.addBranchGraph(bgLight);

  }


}
