import sim.engine.*;
import sim.field.continuous.*;
import sim.util.*;
import sim.field.network.*;

//implementing Steppable means this object can be called by the Scheduler of a simulation
public class Student implements Steppable
{

 public static final double MAX_FORCE = 3.0;

 public void step(SimState state)
 {
  //apparently implementing steppable means you have a dependency on this object's calling context class variables
  Students students = (Students) state;
  Continuous2D yard = students.yard;
  
  //current location of this Student object
  Double2D me = students.yard.getObjectLocation(this);

  //mutable 2D location class (used to store the result of applying forces to this agent??)
  MutableDouble2D sumForces = new MutableDouble2D();
 
  //Go through buddies and determine forces of attraction/repulsion

  MutableDouble2D forceVector = new MutableDouble2D();

  Bag out = students.buddies.getEdges(this, null);

  int len = out.size();

  //go through all the outgoing nodes and do some shit upon it
  for (int buddy = 0; buddy < len; buddy++)
  {
   Edge e = (Edge) (out.get(buddy));
   double buddiness = ((Double) (e.info)).doubleValue();

   //getotherNode() gets the 'buddy' student on the opposite end of this one

   Double2D other = students.yard.getObjectLocation(e.getOtherNode(this));

   //get closer to buddy if not close enough already
   if (buddiness  >= 0 )
   {
    forceVector.setTo((other.x - me.x) * buddiness, (other.y - me.y) * buddiness);

    //temper dat max force
	if (forceVector.length() > MAX_FORCE)
     forceVector.resize(MAX_FORCE);

   }
   //get further away from buddy if too close
   else
   {
    forceVector.setTo((other.x - me.x) * buddiness, (other.y - me.y) * buddiness);

	if (forceVector.length() > MAX_FORCE)
     forceVector.resize(0.0);  
    else if (forceVector.length() > 0)
	 forceVector.resize(MAX_FORCE - forceVector.length()); //invert distance
   }

   sumForces.addIn(forceVector);

  }

  sumForces.addIn(new Double2D((yard.width * 0.5 - me.x) * students.forceToSchoolMultiplier,
				 (yard.height * 0.5 - me.y) * students.forceToSchoolMultiplier));

  sumForces.addIn(new Double2D(students.randomMultiplier * (students.random.nextDouble() * 1.0 - 0.5),
  students.randomMultiplier * (students.random.nextDouble() * 1.0 - 0.5)));

  sumForces.addIn(me);

  students.yard.setObjectLocation(this, new Double2D(sumForces));

 }

}
