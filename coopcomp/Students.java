import sim.engine.*;
import sim.util.*;
import sim.field.continuous.*;

public class Students extends SimState
{
 
 public Continuous2D yard = new Continuous2D(1.0,100,100);
 public int numStudents = 50;

 public double forceToSchoolMulitplier = 0.01;
 public double randomMultiplier = 0.1;

 public Students (long seed)
 {
  super(seed);
 } 

 public void start ()
 {

  super.start();

  yard.clear();

  buddies.clear();

  for (int i = 0; i < numStudents; i++)
  {
   Student student = new Student();
   
   yard.setObjectLocation(student, new Double2D (yard.getWidth() * 0.5 + random.nextDouble() - 0.5,
   												 yard.getHeight() * 0.5 + random.nextDouble() -0.5);

   schedule.scheduleRepeating(student); 

  }

 }

 public static void main (String[] args)
 {
  
  doLoop(Students.class, args);
  System.exit(0);

 }


}
