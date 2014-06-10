import sim.engine.*;
import sim.field.continuous.*;
import sim.util.*;

//implementing Steppable means this object can be called by the Scheduler of a simulation
public class Student implements Steppable
{
 
 public void step(SimState state)
 {
  //apparently implementing steppable means you have a dependency on this object's calling context class variables
  Students students = (Students) state;
  Continuous2D yard = students.yard;
  
  //current location of this Student object
  Double2D me = students.yard.getObjectLocation(this);

  //mutable 2D location class (used to store the result of applying forces to this agent??)
  MutableDouble2D sumForces = new MutableDouble2D();


  sumForces.addIn(new Double2D((yard.width * 0.5 - me.x) * students.forceToSchoolMultiplier,
				 (yard.height * 0.5 - me.y) * students.forceToSchoolMultiplier));

  sumForces.addIn(new Double2D(students.randomMultiplier * (students.random.nextDouble() * 1.0 - 0.5),
  students.randomMultiplier * (students.random.nextDouble() * 1.0 - 0.5)));

  sumForces.addIn(me);

  students.yard.setObjectLocation(this, new Double2D(sumForces));

 }

}
