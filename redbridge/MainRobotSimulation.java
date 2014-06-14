import sim.engine.*;
import sim.util.*;
import sim.field.continuous.*;

public class MainRobotSimulation extends SimState
{

 //2D simulation environment
 public Continuous2D forageArea = new Continuous2D(1.0,100,100);
 
 //number of robot agents
 public int numRobots = 50;
 
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
  for (int i = 0; i < numRobots; i++)
  { 

   //gotta change this to ya own bots
   MaryBot bot = new MaryBot();

   //place the bot randomly in forage area
   forageArea.setObjectLocation(bot, new Double2D(forageArea.getWidth() * 0.5  + random.nextDouble() - 0.5,
   											      forageArea.getHeight() * 0.5 + random.nextDouble() - 0.5));
   
   //schedule this bot to step forever
   schedule.scheduleRepeating(bot);

  }


 }

 public static void main (String[] args)
 {
  doLoop(MainRobotSimulation.class, args);
  
  System.exit(0);
 }

}
