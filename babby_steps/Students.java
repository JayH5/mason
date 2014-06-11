import sim.engine.*;
import sim.util.*;
import sim.field.continuous.*;
import sim.field.network.*;

public class Students extends SimState
{
 
 public Continuous2D yard = new Continuous2D(1.0,100,100);
 public int numStudents = 50;

 public double forceToSchoolMultiplier = 0.01;
 public double randomMultiplier = 0.1;
 public Network buddies = new Network (false);

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
   												 yard.getHeight() * 0.5 + random.nextDouble() -0.5));
   
   //add students to the network
   buddies.addNode(student);
   schedule.scheduleRepeating(student); 

  }

  //define like/dislike relationships to scatter students away/bring them close

  Bag students = buddies.getAllNodes();

  for (int i = 0; i < students.size(); i++)
  {
   Object student = students.get(i);

   //define who this student likes
   Object studentB = null;

   double buddiness = random.nextDouble();
   
   do
   {
    studentB = students.get(random.nextInt(students.numObjs));
    buddies.addEdge(student, studentB, new Double(buddiness));
   }
   while (student == studentB);


   //define who this student dislikes
   do
   {
    studentB = students.get(random.nextInt(students.numObjs));
    buddiness = random.nextDouble();
    buddies.addEdge(student, studentB, new Double(-buddiness));
   }
   while (student == studentB);
  }

 }

 public static void main (String[] args)
 {
  
  doLoop(Students.class, args);
  System.exit(0);

 }


}
