//a new visualisation and UI for the robots simulation

import javax.swing.*;
import java.awt.Color;

import sim.portrayal.continuous.*;
import sim.engine.*;
import sim.display.*;
import sim.portrayal.simple.*;

public class RobotSimulationUI extends GUIState
{

 public Display2D display;
 public JFrame displayFrame;

 public ContinuousPortrayal2D forageAreaPortrayal = new ContinuousPortrayal2D();

 public static void main (String[] args)
 {
  
  RobotSimulationUI video = new RobotSimulationUI();
  
  //new console which displays this simulation
  Console c = new Console(video);
  c.setVisible(true);

 }

 public RobotSimulationUI()
 {
  super (new MainRobotSimulation(System.currentTimeMillis()));
 }

 public RobotSimulationUI(SimState state)
 {
  super(state);
 }

 public static String getName()
 {
  return "Redbridge Mining Crew WHADDUP";
 }

 public void start()
 {
  super.start();
  setupPortrayals();
 }

 public void load(SimState state)
 {
  super.load(state);
  setupPortrayals();
 }

 //set up the portrayal stuff
 public void setupPortrayals()
 {
  
  MainRobotSimulation simulation = (MainRobotSimulation) state;

  //tell portrayals what/how to portray shit

  //set the portrayal field as the forageArea
  forageAreaPortrayal.setField(simulation.forageArea);

  //display all shit as ovals
  //forageAreaPortrayal.setPortrayalForAll(new OvalPortrayal2D(1.0, true));

  //display all MaryBots as ovals with a circle around it
  //forageAreaPortrayal.setPortrayalForAll(new CircledPortrayal2D(new OvalPortrayal2D(2.0, true)));

  //forageAreaPortrayal.setPortrayalForObject(new MaryBot(), (new OvalPortrayal2D(5.0, true)));

  display.reset();
  display.setBackdrop(Color.white);

  //redraw display
  display.repaint();
  
 }

 public void init (Controller c)
 {
  super.init(c);

  display = new Display2D(600,600,this);

  display.setClipping(false);

  displayFrame = display.createFrame();
  displayFrame.setTitle("Redbridge Bot Display");

  c.registerFrame(displayFrame);

  displayFrame.setVisible(true);
  display.attach(forageAreaPortrayal, "Forage Area");

 }

 //clean up yo shit
 public void quit()
 {
  super.quit();

  if (displayFrame != null)
   displayFrame.dispose();

  displayFrame = null;

  display = null;

 }

}
