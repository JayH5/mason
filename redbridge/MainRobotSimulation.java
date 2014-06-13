import sim.engine.*;
import sim.util.*;
import sim.field.continuous.*;

public class MainRobotSimulation extends SimState
{

 //2D simulation environment
 public Continuous2D forageArea = new Continuous2D(1.0,100,100);
 
 //number of agents
 public int numStudents = 50;
 
 public MainRobotSimulation (long seed)
 {
  super (seed);
 }

 public void start()
 {
  super.start();

  //clear the forage area
  forageArea.clear();

  //add robots to the forage area
  for (int i = 0; i < numStudents; i++)
  {
   

  }


 }

 public static void main (String[] args)
 {
  doLoop(MainRobotSimulation.class, args);
  
  System.exit(0);
 }

}
