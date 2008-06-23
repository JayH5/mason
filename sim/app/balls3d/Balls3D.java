/*
  Copyright 2006 by Sean Luke and George Mason University
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

package sim.app.balls3d;
import sim.engine.*;
import sim.field.continuous.*;
import sim.field.network.*;
import sim.util.*;


public class Balls3D extends SimState
    {
    public Continuous3D balls;
    public Network bands;

    public int numBalls = 50;
    public int numBands = 60;
    
    public double gridWidth = 100; 
    public double gridHeight = 100; 
    public double gridLength = 100; 

    public final static double maxMass = 10.0;
    public final static double minMass = 1.0;
    public final static double minLaxBandDistance = 10.0;
    public final static double maxLaxBandDistance = 50.0;
    public final static double minBandStrength = 5.0;
    public final static double maxBandStrength = 10.0;
    public final static double collisionDistance = 5.0;
    
    public int getNumBalls() { return numBalls; }
    public void setNumBalls(int val) { if (val >= 2 ) numBalls = val; }
    public int getNumBands() { return numBands; }
    public void setNumBands(int val) { if (val >= 0 ) numBands = val; }

    public Balls3D(long seed)
        {
        super(seed);
        }

    public void start()
        {
        super.start();

        balls = new Continuous3D(collisionDistance,gridWidth, gridHeight, gridLength); 
        bands = new Network();
        
        Steppable[] s = new Steppable[numBalls];
        
        // make the balls
        for(int i=0; i<numBalls;i++)
            {
            // must be final to be used in the anonymous class below
            final Ball ball = new Ball(0,0,0,random.nextDouble() * (maxMass-minMass) + minMass);
            balls.setObjectLocation(ball,
                                    new Double3D(random.nextDouble() * gridWidth,
                                                 random.nextDouble() * gridHeight, 
                                                 random.nextDouble() * gridLength));
            bands.addNode(ball);
            schedule.scheduleRepeating(ball);
     
            // schedule the balls to compute their force after everyone's moved
            s[i] = new Steppable()
                {
                public void step(SimState state) { ball.computeForce(state); }
                // see Tutorial 3 for why this is helpful
                static final long serialVersionUID = -4269174171145445918L;
                };
            }
        
        // add the sequence
        schedule.scheduleRepeating(Schedule.EPOCH,1,new Sequence(s),1);
        
        // make the bands
        Bag ballObjs = balls.getAllObjects();
        for(int i=0;i<numBands;i++)
            {
            Band band = new Band(random.nextDouble() * 
                                 (maxLaxBandDistance - minLaxBandDistance) + minLaxBandDistance,
                                 random.nextDouble() *
                                 (maxBandStrength - minBandStrength) + minBandStrength);
            Ball from;
            from = (Ball)(ballObjs.objs[random.nextInt(ballObjs.numObjs)]);

            Ball to = from;
            while(to == from)
                to = (Ball)(ballObjs.objs[random.nextInt(ballObjs.numObjs)]);
            bands.addEdge(from,to,band);
            } 
        
//        schedule.scheduleRepeating(Schedule.EPOCH,2,new Steppable()
//        {
//              public void step(SimState state)
//              {
//                      Bag ballObjs = balls.getAllObjects();
//                      Ball from;
//                      int x,y=-1;
//                from = (Ball)(ballObjs.objs[x=random.nextInt(ballObjs.numObjs)]);
//
//                Ball to = from;
//                while(to == from)
//                    to = (Ball)(ballObjs.objs[y=random.nextInt(ballObjs.numObjs)]);
//                //I got a random pair of nodes.
//                //If there's a band between them, I delete it
//                //It there isn't, I add one.
//                Bag edgesFromFrom = bands.getEdgesOut(from);
//                for(int i=0;i<edgesFromFrom.numObjs;i++)
//                {
//                      Edge e = (Edge)edgesFromFrom.objs[i];
//                      if(e.to()==to)
//                      {
//                              System.out.println("Removing edge ("+x+"->"+y+").");
//                              bands.removeEdge(e);
//                              return;
//                      }
//                }
//                Bag edgesToFrom = bands.getEdgesIn(from);
//                for(int i=0;i<edgesToFrom.numObjs;i++)
//                {
//                      Edge e = (Edge)edgesToFrom.objs[i];
//                      if(e.from()==to)
//                      {
//                              System.out.println("Removing edge ("+x+"<-"+y+").");
//                              bands.removeEdge(e);
//                              return;
//                      }
//                }
//
//                //I'm still here, that means I found no edge. I'm adding one
//                      System.out.println("Adding edge "+x+"->"+y);
//                Band band = new Band(random.nextDouble() * 
//                        (maxLaxBandDistance - minLaxBandDistance) + minLaxBandDistance,
//                        random.nextDouble() *
//                        (maxBandStrength - minBandStrength) + minBandStrength);
//                bands.addEdge(from,to,band);
//              }
//        },1);
        
        }

    public static void main(String[] args)
        {
        doLoop(Balls3D.class, args);
        System.exit(0);
        }    
        
    // see Tutorial 3 for why this is helpful
    static final long serialVersionUID = -7164072518609011190L;
    }
