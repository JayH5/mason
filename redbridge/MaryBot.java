//defines a marybot

public class MaryBot implements RedbridgeBot
{
 
 public MaryBot ()
 {

 }

 public void step (SimState state)
 {
  
  //get the forageArea context in which this bot is stepping
  MainRobotSimulation simulation = (MainRobotSimulation) state;
  
  //get the forageArea object from this context
  Continuous2D forageArea = simulation.forageArea;

  

   
 }

}
