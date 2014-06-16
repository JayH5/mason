//defines a marybot
import java.util.*;
import java.awt.Color;

import sim.engine.*;
import sim.field.continuous.*;
import sim.util.*;
import sim.portrayal.simple.*;
import sim.portrayal.*;

//this class extends CircledPortrayal2D bundled with agent behaviour.
//it handles both model and view.

public class MaryBot extends CircledPortrayal2D implements Steppable
{

 public int xDirection = 1;
 public int yDirection = 1;
 
 //constructor
 public MaryBot ()
 {
 
  //set the default "child" of the circle to an oval thing
  super(new OvalPortrayal2D(new Color(1,106,128,200), 2.0, true), 4.0, 2.0,
  							new Color(41,41,41), false);
  
 }

 //executed each time when this object is called on the schedule
 public void step (SimState state)
 {
  
  //get the simulation context in which this bot is stepping
  MainRobotSimulation simulation = (MainRobotSimulation) state;
  
  //get the forageArea object from this context
  Continuous2D forageArea = simulation.forageArea;

  //current position of this marybot
  Double2D myPosition = forageArea.getObjectLocation(this);
  
  //TODO: work with phenotype shit
  //get sum inputs into this bot's phenotype and get sum outputs out of it
  //in order to determine whar it goes next
  MutableDouble2D newPosition = new MutableDouble2D();

  //just some dumb random shit to get it to move
  newPosition.addIn(new Double2D ((simulation.random.nextDouble() * 1.0 - 0.5) * simulation.randomMultiplier,
  								  (simulation.random.nextDouble() * 1.0 - 0.5) * simulation.randomMultiplier));
  
  newPosition.addIn(myPosition);

  //move to this position
  forageArea.setObjectLocation(this, new Double2D(newPosition));
    
  //test and handle collisions
//  testCollision(simulation);

 }

 //tests for, and handles, collision behaviour. at the moment
 //this thing is just so that the bots don't overlap with each other
 //and the forage objects - obv the actual controller will ultimately
 //be the thing that dictates this behaviour when everything is set up
 //to not suck.

 public void testCollision (MainRobotSimulation simulation)
 {

  //naive check for collision with all other objects in field
  Bag allObjects = simulation.forageArea.getAllObjects();
 
  for (int i = 0; i < allObjects.numObjs; i++)
  {
   
   SimplePortrayal2D current = (SimplePortrayal2D) (allObjects.objs[i]);

   if (current instanceof SimplePortrayal2D)
   {

    if (MainRobotSimulation.isCollidingWith(simulation, this, current));
	{
     //move in other direction if collision is detected (this is not any sophisticaed
	 //angle of incidence shit, we're just inverting both x and y directions

//	 xDirection *= -1;
//     yDirection *= -1;

     System.out.println("Collision Detected.");

	}

   }

  }


 }

 public void crossover (Gene partner)
 {

 }

 public void mutate ()
 {

 }

 //input to the neural network?
 public ArrayList<Object> input (Object...arguments)
 {
  return null;
 }

}


