import java.util.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;


/**
* This class initiates alternatingly two different movements
* when a collision occurs.
*
* @author Frank Klawonn
* Last change 07.07.2005
* @see CollisionExample
*/
public class CollisionBehaviour2 extends Behavior
{
   //The movement is started when a collision occurs.
   public WakeupOnCollisionEntry hit;

   //This boolean variable keeps track which of the two movements should be
   //carried out next.
   public boolean toRight;
   //The index (0 or 1) of the movement.
   int whichAlpha;

   public Primitive collidingShape;

   //The Alphas associated with the movements.
   public Alpha[] movement;


   //Constructor
   public CollisionBehaviour2(Primitive theShape, Alpha[] theAlphas,
                              Bounds theBounds)
   {
     collidingShape = theShape;
     movement = theAlphas;

     setSchedulingBounds(theBounds);

     //At the very first collision, movement[0] should be carried out.
     whichAlpha = 0;
     toRight = true;
   }


  public void initialize()
  {
    hit = new WakeupOnCollisionEntry(collidingShape);
    wakeupOn(hit);
  }




  public void processStimulus(Enumeration criteria)
  {
    while (criteria.hasMoreElements())
    {
      WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();
      if (theCriterion instanceof WakeupOnCollisionEntry)
      {
        //Select the correct movement.
        if (toRight) whichAlpha = 0; else whichAlpha = 1;

        //Set the starting time to "now".
        movement[whichAlpha].setStartTime(System.currentTimeMillis()
                                          -movement[whichAlpha].getTriggerTime());
        //Next time, the other movement should be carried out.
        toRight = !toRight;
      }
      wakeupOn(hit);
    }
  }

}
