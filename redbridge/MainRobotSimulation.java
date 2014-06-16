import sim.engine.*;
import sim.util.*;
import sim.field.continuous.*;
import ec.util.*;
import java.awt.geom.*;
import java.awt.*;
import sim.portrayal.*;

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


//R&B SINGER CODE

/** Rotates (using orientation), translates, and scales the shape as requested, then returns the
  modified shape as an Area. */
  public static Area getLocatedArea(double translateX, double translateY, double scaleX, double scaleY)
  {
     //i have no idea what the orientation conventions are for this thing, yolo
     double orientation = 0.0;

	  AffineTransform transform = new AffineTransform();
	  transform.translate(translateX, translateY);
	  transform.rotate(orientation);
	  if (scaleX != 1.0 && scaleY != 1.0) 
		transform.scale(scaleX, scaleY);

//make a shape that the area will be shaped in. rectangle with size 4,4??
     Shape shape = new Rectangle(4, 4);

	  Area area = new Area(shape);

	  area.transform(transform);

	  return area;
  }

 /** Rotates (using orientation), and translates the shape as requested, then returns
     the modified shape as an Area.  Translation is done by centering the shape at the
     Element's location in the forageArea. */
 public static Area getLocatedArea(MainRobotSimulation simulation, SimplePortrayal2D element)
 {
     Double2D loc = simulation.forageArea.getObjectLocation(element);
     return getLocatedArea(loc.x, loc.y, 1.0, 1.0);
 }

 //returns whether or not this simpleportrayal2d is colliding with another simpleportrayal2d

 public static boolean isCollidingWith (MainRobotSimulation simulation, SimplePortrayal2D element1, SimplePortrayal2D element2)
 {
	Double2D d = (simulation.forageArea.getObjectLocation(element1));
	double width = simulation.forageArea.width;
	double height = simulation.forageArea.height;
	Area elementloc = getLocatedArea(simulation, element2);
		     
	// the obvious one
	Area a = getLocatedArea(simulation, element1);
	a.add(elementloc);
	if (a.isSingular()) return true;
		     
	// check wrap-around situations
	AffineTransform transform = new AffineTransform();
	transform.translate(
		 d.x < width / 2 ? width : // I'm on the left, gotta check on the right
		 0 - width, 0); // I'm on the right, gotta check on the left
	a.transform(transform);
	a.add(elementloc);
	if (a.isSingular()) return true;
		     
	// get another area
	a = getLocatedArea(simulation, element1);
	AffineTransform transform2 = new AffineTransform();
	transform2.translate(0, 
		 d.y < height / 2 ? height : // I'm on the top, gotta check on the bottom
		 0 - height ); // I'm on the bottom, gotta check on the top
	a.transform(transform2);
	a.add(elementloc);
	if (a.isSingular()) return true;
		     
	return false;  

 }

}
