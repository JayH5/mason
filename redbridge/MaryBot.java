//defines a marybot
import java.util.*;

import sim.engine.*;
import sim.field.continuous.*;
import sim.util.*;

public class MaryBot implements RedbridgeBot
{
 
 public MaryBot ()
 {

 }

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

  //just some dumb shit to get it to move
  newPosition.addIn(new Double2D (1.0,1.0));
  
  newPosition.addIn(myPosition);

  //move to this position
  forageArea.setObjectLocation(this, new Double2D(newPosition));
    


 }

 public void crossover (Gene partner)
 {

 }

 public void mutate ()
 {

 }
 
 public ArrayList<Object> input (Object...arguments)
 {
  return null;
 }

}
