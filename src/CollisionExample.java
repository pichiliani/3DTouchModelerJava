import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.picking.behaviors.*;
import com.sun.j3d.utils.behaviors.keyboard.*;
import javax.swing.JFrame;


/**
* A blue cube can be moved through the scene by clicking it with the right mouse button 
* and moving the mouse. The scene also contains two additional objects:
* A green sphere that changes its colour to red when the cube is touching it.
* A golden cylinder that starts to move when the cube is touching it.
*
* In addition, navigation through the scene is possible using the keyboard.
*
* @author Frank Klawonn
* Last change 07.07.2005
* @see CollisionBehaviour1
* @see CollisionBehaviour2
*/
public class CollisionExample extends JFrame
{

  //The canvas to be drawn upon.
  public Canvas3D myCanvas3D;


  public CollisionExample()
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

  

    //Show the canvas/window.
    setTitle("Move the cube with the right mouse button.");
    setSize(700,700);
    getContentPane().add("Center", myCanvas3D);
    setVisible(true);

  }




  public static void main(String[] args)
  {
     CollisionExample colliexam = new CollisionExample();
  }





  //In this method, the objects for the scene are generated and added to 
  //the SimpleUniverse.
  public void createSceneGraph(SimpleUniverse su)
  {

    //A blue Appearance for the cube.
    Color3f ambientColourBox = new Color3f(0.0f,0.0f,0.7f);
    Color3f emissiveColourBox = new Color3f(0.0f,0.0f,0.0f);
    Color3f diffuseColourBox = new Color3f(0.0f,0.0f,0.7f);
    Color3f specularColourBox = new Color3f(0.0f,0.0f,0.8f);
    float shininessBox = 128.0f;

    Appearance boxApp = new Appearance();

    boxApp.setMaterial(new Material(ambientColourBox,emissiveColourBox,
                           diffuseColourBox,specularColourBox,shininessBox));


    //Generate the cube.
    float cubeHalfLength = 0.1f;
    Box moveBox = new Box(cubeHalfLength,cubeHalfLength,cubeHalfLength,boxApp);


    //The CollisionBounds for the cube.
    moveBox.setCollisionBounds(new BoundingBox(new Point3d(0.0,0.0,0.0),
                                               new Point3d(cubeHalfLength,
                                                           cubeHalfLength,
                                                           cubeHalfLength)));

    moveBox.setCollidable(true);

    //Position the cube and assign it to a transformation group.
    Transform3D tfBox = new Transform3D();
    tfBox.rotY(Math.PI/6);
    Transform3D rotationX = new Transform3D();
    rotationX.rotX(-Math.PI/5);
    tfBox.mul(rotationX);
    TransformGroup tgBox = new TransformGroup(tfBox);
    tgBox.addChild(moveBox);


    //These properties are needed to allow the cube to moved around the scene.
    tgBox.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    tgBox.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    tgBox.setCapability(TransformGroup.ENABLE_PICK_REPORTING);



    //Generate a golden Appearance for the cylinder.
    Color3f ambientColourCylinder = new Color3f(0.0f,0.0f,0.0f);
    Color3f emissiveColourCylinder = new Color3f(0.0f,0.0f,0.0f);
    Color3f diffuseColourCylinder = new Color3f(0.8f,0.4f,0.0f);
    Color3f specularColourCylinder = new Color3f(0.8f,0.8f,0.0f);
    float shininessCylinder = 100.0f;

    Appearance yellowCylinderApp = new Appearance();

    yellowCylinderApp.setMaterial(new Material(ambientColourCylinder,
                                               emissiveColourCylinder,
                                               diffuseColourCylinder,
                                               specularColourCylinder,
                                               shininessCylinder));


    //Generate the cylinder.
    Cylinder cyli = new Cylinder(0.1f,0.3f,yellowCylinderApp);

    //The CollisionBounds for the cylinder.
    cyli.setCollisionBounds(new BoundingBox(new Point3d(0.0,-0.15,0.0),
                                            new Point3d(0.1,0.15,0.1)));
    cyli.setCollidable(true);

    //Position the cylinder and assign it to a transformation group.
    Transform3D tfCylinder = new Transform3D();
    tfCylinder.mul(rotationX);
    Transform3D positionCyl = new Transform3D();
    positionCyl.setTranslation(new Vector3f(-0.7f,0.0f,0.0f));
    tfCylinder.mul(positionCyl);

    TransformGroup tgCylinder = new TransformGroup(tfCylinder);
    tgCylinder.addChild(cyli);

    //This transformation group is needed for the movement of the cylinder.
    TransformGroup tgmCyl = new TransformGroup();
    tgmCyl.addChild(tgCylinder);

    //The movement from left to right.
    Transform3D escape = new Transform3D();
    Alpha cylAlphaR = new Alpha(1,2000);

    //The starting time is first postponed until "infinity".
    cylAlphaR.setStartTime(Long.MAX_VALUE);

    //The interpolator for the movement.
    float maxRight = 0.5f;
    PositionInterpolator cylMoveR = new PositionInterpolator(cylAlphaR,tgmCyl,
                                                             escape,0.0f,maxRight);

    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);
    cylMoveR.setSchedulingBounds(bounds);


    //The same for the movement from right to left.
    Alpha cylAlphaL = new Alpha(1,2000);
    cylAlphaL.setStartTime(Long.MAX_VALUE);

    PositionInterpolator cylMoveL = new PositionInterpolator(cylAlphaL,tgmCyl,
                                                             escape,maxRight,0.0f);

    cylMoveL.setSchedulingBounds(bounds);


    //Add the movements to the transformation group.
    tgmCyl.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    tgmCyl.addChild(cylMoveR);
    tgmCyl.addChild(cylMoveL);


    //A Switch for the green and the red sphere.
    Switch colourSwitch = new Switch();
    colourSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

    //An Appearance for the green sphere.
    Color3f ambientColourGSphere = new Color3f(0.0f,0.8f,0.0f);
    Color3f emissiveColourGSphere = new Color3f(0.0f,0.0f,0.0f);
    Color3f diffuseColourGSphere = new Color3f(0.0f,0.8f,0.0f);
    Color3f specularColourGSphere = new Color3f(0.0f,0.8f,0.0f);
    float shininessGSphere = 1.0f;

    Appearance greenSphereApp = new Appearance();
    greenSphereApp.setMaterial(new Material(ambientColourGSphere,
                                            emissiveColourGSphere,
                                            diffuseColourGSphere,
                                            specularColourGSphere,
                                            shininessGSphere));

    //Generate the green sphere.
    float radius = 0.1f;
    Sphere greenSphere = new Sphere(radius,greenSphereApp);



    //The same for the red sphere.
    Color3f ambientColourRSphere = new Color3f(0.6f,0.0f,0.0f);
    Color3f emissiveColourRSphere = new Color3f(0.0f,0.0f,0.0f);
    Color3f diffuseColourRSphere = new Color3f(0.6f,0.0f,0.0f);
    Color3f specularColourRSphere = new Color3f(0.8f,0.0f,0.0f);
    float shininessRSphere = 20.0f;

    Appearance redSphereApp = new Appearance();

    redSphereApp.setMaterial(new Material(ambientColourRSphere,emissiveColourRSphere,
                             diffuseColourRSphere,specularColourRSphere,shininessRSphere));

    Sphere redSphere = new Sphere(radius,redSphereApp);



    //Add the two spheres to the Switch.
    colourSwitch.addChild(greenSphere);
    colourSwitch.addChild(redSphere);

    //The CollisionBounds for the two spheres.
    colourSwitch.setCollisionBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0),radius));

    //Enable the test for collision.
    colourSwitch.setCollidable(true);

    //The green sphere should be visible in the beginning.
    colourSwitch.setWhichChild(0);

    //A transformation group for the Switch (the spheres).
    Transform3D tfSphere = new Transform3D();
    tfSphere.setTranslation(new Vector3f(0.7f,0.0f,0.0f));
    TransformGroup tgSphere = new TransformGroup(tfSphere);
    tgSphere.addChild(colourSwitch);



    //Generate the scenegraph.
    BranchGroup theScene = new BranchGroup();


    //In order to allow navigation through the scene with the keyboard,
    //everything must be collected in a separate transformation group to which 
    //the KeyNavigatorBehavior is applied.
    TransformGroup tgAll = new TransformGroup();
    tgAll.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    tgAll.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    tgAll.addChild(tgBox);
    tgAll.addChild(tgSphere);
    tgAll.addChild(tgmCyl);
    KeyNavigatorBehavior knb = new KeyNavigatorBehavior(tgAll);
    knb.setSchedulingBounds(bounds);
    tgAll.addChild(knb);
    theScene.addChild(tgAll);

    //The PickTranslateBehavior for moving the blue cube.
    PickTranslateBehavior pickTrans = new PickTranslateBehavior(theScene,myCanvas3D,bounds);
    theScene.addChild(pickTrans);



    //This class takes care of changing the colour of the sphere when the cube touches it.
    CollisionBehaviour1 scb1 = new CollisionBehaviour1(colourSwitch,bounds);
    theScene.addChild(scb1);


    //A corresponding class for the movement(s) of the cylinder.
    Alpha[] cylAlphas= new Alpha[2];
    cylAlphas[0] = cylAlphaR;
    cylAlphas[1] = cylAlphaL;

    CollisionBehaviour2 scb2 = new CollisionBehaviour2(cyli,cylAlphas,bounds);
    theScene.addChild(scb2);



    theScene.compile();


    //Add everything to the universe.
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


    Vector3f lightDir2  = new Vector3f(1.0f,-1.0f,0.5f);
    DirectionalLight light2 = new DirectionalLight(lightColour1, lightDir2);
    light2.setInfluencingBounds(bounds);

    bgLight.addChild(light2);
    su.addBranchGraph(bgLight);

  }



}
