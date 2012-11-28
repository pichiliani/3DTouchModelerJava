import java.util.*;
import javax.media.j3d.*;



/**
* This class implements the change between two states (Switches)
* when a collision occurs.
*
* @author Frank Klawonn
* Last change 07.07.2005
* @see CollisionExample
*/
public class CollisionBehaviour1 extends Behavior
{

   //Collision entry and exit must be taken into account.
   public WakeupCriterion[] theCriteria;
   public WakeupOr oredCriteria;
   public Switch collidingShape;


   public CollisionBehaviour1(Switch theShape, Bounds theBounds)
   {
     collidingShape = theShape;
     setSchedulingBounds(theBounds);
   }


  public void initialize()
  {
    theCriteria = new WakeupCriterion[2];
    theCriteria[0] = new WakeupOnCollisionEntry(collidingShape);
    theCriteria[1] = new WakeupOnCollisionExit(collidingShape);
    oredCriteria = new WakeupOr(theCriteria);
    wakeupOn(oredCriteria);
  }



  public void processStimulus(Enumeration criteria)
  {
    //Here we define what happens when a collision occurs.
    while (criteria.hasMoreElements())
    {
      WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();
      if (theCriterion instanceof WakeupOnCollisionEntry) //-> switch to the red sphere
      {
        collidingShape.setWhichChild(1);
      }
      else
      {
        if (theCriterion instanceof WakeupOnCollisionExit) //-> switch to the green sphere
        {
          collidingShape.setWhichChild(0);
        }
      }
      wakeupOn(oredCriteria);
    }
  }

}
