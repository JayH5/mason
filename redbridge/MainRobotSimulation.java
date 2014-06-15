import sim.engine.*;
import sim.util.*;
import sim.field.continuous.*;

public class MainRobotSimulation extends SimState
{

 //2D simulation environment
 public Continuous2D forageArea = new Continuous2D(1.0,100,100);
 
 //number of robot agents
 public int numRobots = 50;

 //number of large objects
 public int numLargeObjects = 10;

 //number of small objects
 public int numSmallObjects = 10;
 
 //random number multiplier
 public double randomMultiplier = 0.1;

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
   forageArea.setObjectLocation(bot, new Double2D(forageArea.getWidth() * random.nextDouble(),
   											      forageArea.getHeight() *  random.nextDouble()));
   
   //schedule this bot to step forever
   schedule.scheduleRepeating(bot);

  }

  //add the large objects to the forage area
  for (int i = 0; i < numLargeObjects; i++)
  {
   
   LargeObject obj = new LargeObject();

   forageArea.setObjectLocation(obj, new Double2D(forageArea.getWidth() * random.nextDouble(),
   											      forageArea.getHeight() *  random.nextDouble()));
   
   //schedule this forage object to step forever
   schedule.scheduleRepeating(obj);
  }


  //add the small objects to the forage area
  for (int i = 0; i < numSmallObjects; i++)
  {
   
   SmallObject obj = new SmallObject();

   forageArea.setObjectLocation(obj, new Double2D(forageArea.getWidth() * random.nextDouble(),
   											      forageArea.getHeight() *  random.nextDouble()));
   
   //schedule this forage object to step forever
   schedule.scheduleRepeating(obj);
  }

 }

 public static void main (String[] args)
 {
  doLoop(MainRobotSimulation.class, args);
  
  System.exit(0);
 }

}
