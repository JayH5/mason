//defines a large forage object that a robot should push n shit

import sim.engine.*;
import sim.util.*;
import sim.field.continuous.*;
import sim.portrayal.simple.*;

import java.awt.Color;

//TODO: gotta be able to step this while it stands alone, unpicked-up.
//then need to specify the block behaviour when attached to one or more agents.
//oh god.

public class LargeObject extends RectanglePortrayal2D implements Steppable
{

 
 public LargeObject ()
 {
  super(new Color(255, 235, 82), 5.0, true);
 }

 public void step(SimState state)
 {
   //get simulation context
   MainRobotSimulation simulation = (MainRobotSimulation) state;
   
   //get the forage environment
   Continuous2D forageArea = simulation.forageArea;

   Double2D currentPosition = forageArea.getObjectLocation(this);



 }

}
