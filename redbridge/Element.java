//defines an element (agent or forage object) within the simulation framework.
//the element has an associated portrayal from it subclassing SimplePortrayal2D;
//it is up to the individual object to define how it is drawn.

import sim.portrayal.*;
import sim.engine.*;
import sim.util.*;

import java.awt.geom.*;
import java.awt.*;

public class Element extends SimplePortrayal2D implements Steppable
{

 //doesn't do too much
 public void step(SimState state)
 {
  

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

//END R&B SINGER CODE


}
