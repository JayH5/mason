/*
  Copyright 2006 by Sean Luke and George Mason University
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

package sim.portrayal.continuous;
import sim.portrayal.*;
import sim.portrayal.simple.*;
import sim.field.continuous.*;
import sim.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import sim.portrayal.inspector.*;
import sim.display.*;

/**
   Portrays Continuous2D fields.  When asked to portray objects, this field computes the buckets
   covered by the requested region, then includes an additional boundary of two buckets in each
   direction just in case objects leak over the boundary region.
   
   The 'location' passed
   into the DrawInfo2D handed to the SimplePortryal2D is a Double2D.
*/

public class ContinuousPortrayal2D extends FieldPortrayal2D
    {
    // a grey oval.  You should provide your own protrayals...
    SimplePortrayal2D defaultPortrayal = new OvalPortrayal2D();

    Paint frame = null;
    /** If you provide a Paint, a thin frame of this paint will be drawn around the (0,0,width,height) space of
        the field.  This is mostly useful for seeing the frame of the field when clipping is turned off and you're zoomed out. */ 
    public void setFrame(Paint p)
        {
        frame = p;
        }
                
    public Paint getFame()
        {
        return frame;
        }

    Paint axes = null;
    /** If you provide a Paint, a thin frame of this paint will be drawn around the (0,0,width,height) space of
        the field.  This is mostly useful for seeing the frame of the field when clipping is turned off and you're zoomed out. */ 
    public void setAxes(Paint p)
        {
        axes = p;
        }
                
    public Paint getAxes()
        {
        return axes;
        }
        
    boolean drawsFrameAndAxesInFront = true;
    public boolean getDrawsFrameAndAxesInFront() { return drawsFrameAndAxesInFront; }
    public void setDrawsFrameAndAxesInFront(boolean val) { drawsFrameAndAxesInFront = val; }
    

    public void setField(Object field)
        {
        if (field instanceof Continuous2D) super.setField(field);
        else throw new RuntimeException("Invalid field for ContinuousPortrayal2D: " + field);
        }
        
    public Portrayal getDefaultPortrayal()
        {
        return defaultPortrayal;
        }
        
    public Point2D.Double getRelativeObjectPosition(Object location, Object otherObjectLocation, DrawInfo2D otherObjectInfo)
        {
        final Continuous2D field = (Continuous2D)this.field;
        if (field==null) return null;

        Double2D loc = (Double2D) location;
        Double2D oloc = (Double2D) otherObjectLocation;
        double dx = loc.x - oloc.x;
        double dy = loc.y - oloc.y;
        double xScale = otherObjectInfo.draw.width;
        double yScale = otherObjectInfo.draw.height;
        return new Point2D.Double(dx * xScale + otherObjectInfo.draw.x, dy * yScale + otherObjectInfo.draw.y);
        }
        
    public Double2D getScale(DrawInfo2D info)
        {
        synchronized(info.gui.state.schedule)
            {
            final Continuous2D field = (Continuous2D)this.field;
            if (field==null) return null;
                
            final double xScale = info.draw.width / field.width;
            final double yScale = info.draw.height / field.height;
            return new Double2D(xScale, yScale);
            }
        }
                
    public Object getPositionLocation(Point2D.Double position, DrawInfo2D fieldPortrayalInfo)
        {
        Double2D scale = getScale(fieldPortrayalInfo);
        double xScale = scale.x;
        double yScale = scale.y;
                
        final double x = (position.getX() - fieldPortrayalInfo.draw.x) / xScale;  // notice not (int) like elsewhere.
        final double y = (position.getY() - fieldPortrayalInfo.draw.y) / yScale;
        return new Double2D(x,y);
        }

    public void setObjectPosition(Object object, Point2D.Double position, DrawInfo2D fieldPortrayalInfo)
        {
        synchronized(fieldPortrayalInfo.gui.state.schedule)
            {
            final Continuous2D field = (Continuous2D)this.field;
            if (field==null) return;
            if (field.getObjectLocation(object) == null) return;
            Double2D location = (Double2D)(getPositionLocation(position, fieldPortrayalInfo));
            if (location != null)
                {
                if (object instanceof Fixed2D && (!((Fixed2D)object).maySetLocation(field, location)))
                    return;  // this is deprecated and will be deleted
                //if (object instanceof Constrained)
                //      location = (Double2D)((Constrained)object).constrainLocation(field, location);
                if (location != null)
                    field.setObjectLocation(object, location);
                }
            }
        }

    public Object getObjectLocation(Object object, GUIState gui)
        {
        synchronized(gui.state.schedule)
            {
            final Continuous2D field = (Continuous2D)this.field;
            if (field==null) return null;
            return field.getObjectLocation(object);
            }
        }

    public Point2D.Double getLocationPosition(Object location, DrawInfo2D fieldPortrayalInfo)
        {
        synchronized(fieldPortrayalInfo.gui.state.schedule)
            {
            final Continuous2D field = (Continuous2D)this.field;
            if (field==null) return null;
                
            final double xScale = fieldPortrayalInfo.draw.width / field.width;
            final double yScale = fieldPortrayalInfo.draw.height / field.height;
            DrawInfo2D newinfo = new DrawInfo2D(fieldPortrayalInfo.gui, fieldPortrayalInfo.fieldPortrayal, new Rectangle2D.Double(0,0, xScale, yScale), fieldPortrayalInfo.clip);  // we don't do further clipping 

            Double2D loc = (Double2D) location;
            if (loc == null) return null;

            newinfo.draw.x = (fieldPortrayalInfo.draw.x + (xScale) * loc.x);
            newinfo.draw.y = (fieldPortrayalInfo.draw.y + (yScale) * loc.y);

            return new Point2D.Double(newinfo.draw.x, newinfo.draw.y);
            }
        }

    // values to multiply width or height by to add to a location to shift for toroidal drawing
    static final int[] toroidalX = new int[] { 0, 1, -1, 0, 1, -1, 0, 1, -1 };
    static final int[] toroidalY = new int[] { 0, 0, 0, 1, 1, 1, -1, -1, -1 };

    boolean displayingToroidally = false;
        
    /** Set this to TRUE to cause the portrayal to display objects multiply (in a toroidal fashion)
        if they overlap on the edges of the field.  Note that this incurs a slight constant overhead.
        By default this setting is FALSE. */
    public void setDisplayingToroidally(boolean val) { displayingToroidally = val; }

    /** Returns TRUE if the portrayal is displaying objects multiply (in a toroidal fashion)
        if they overlap on the edges of the field. */
    public boolean isDisplayingToroidally() { return displayingToroidally; }
        
    protected void hitOrDraw(Graphics2D graphics, DrawInfo2D info, Bag putInHere)
        {
        final Continuous2D field = (Continuous2D)this.field;
        if (field==null) return;
                
        if (!drawsFrameAndAxesInFront) drawFrameAndAxes(graphics, info);
                
        boolean objectSelected = !selectedWrappers.isEmpty();
                
//        Rectangle2D.Double cliprect = (Rectangle2D.Double)(info.draw.createIntersection(info.clip));

        final double xScale = info.draw.width / field.width;
        final double yScale = info.draw.height / field.height;
        final int startx = (int)Math.floor((info.clip.x - info.draw.x) / xScale);
        final int starty = (int)Math.floor((info.clip.y - info.draw.y) / yScale);
        int endx = /*startx +*/ (int)Math.floor((info.clip.x - info.draw.x + info.clip.width) / xScale) + /*2*/ 1;  // with rounding, width be as much as 1 off
        int endy = /*starty +*/ (int)Math.floor((info.clip.y - info.draw.y + info.clip.height) / yScale) + /*2*/ 1;  // with rounding, height be as much as 1 off

//        final Rectangle clip = (graphics==null ? null : graphics.getClipBounds());

        DrawInfo2D newinfo = new DrawInfo2D(info.gui, info.fieldPortrayal, new Rectangle2D.Double(0,0, xScale, yScale),
            info.clip);  // we don't do further clipping 
        newinfo.fieldPortrayal = this;

        // hit/draw the objects one by one -- perhaps for large numbers of objects it would
        // be smarter to grab the objects out of the buckets that specifically are inside
        // our range...
        Bag objects = field.getAllObjects();
        final double discretizationOverlap = field.discretization;
        for(int x=0;x<objects.numObjs;x++)
            {
            Object object = (objects.objs[x]);
            Double2D objectLoc = field.getObjectLocation(object);
                        
            if (displayingToroidally)
                objectLoc = new Double2D(field.tx(objectLoc.x), field.tx(objectLoc.y));
                                                
            for(int i = 0; i < toroidalX.length; i++) 
                {
                Double2D loc = null;
                if (i == 0)
                    loc = objectLoc;
                else if (displayingToroidally)  // and i > 0
                    loc = new Double2D(objectLoc.x + field.width * toroidalX[i],
                        objectLoc.y + field.height * toroidalY[i]);
                else
                    break; // no toroidal function
                                
                // here we only hit/draw the object if it's within our range.  However objects
                // might leak over to other places, so I dunno...  I give them the benefit
                // of the doubt that they might be three times the size they oughta be, hence the -2 and +2's
                                
                if (loc.x >= startx - discretizationOverlap && loc.x < endx + discretizationOverlap &&
                    loc.y >= starty - discretizationOverlap && loc.y < endy + discretizationOverlap)
                    {
                    Portrayal p = getPortrayalForObject(object);
                    if (!(p instanceof SimplePortrayal2D))
                        throw new RuntimeException("Unexpected Portrayal " + p + " for object " + 
                            objects.objs[x] + " -- expected a SimplePortrayal2D");
                    SimplePortrayal2D portrayal = (SimplePortrayal2D) p;
                                        
                    newinfo.draw.x = (info.draw.x + (xScale) * loc.x);
                    newinfo.draw.y = (info.draw.y + (yScale) * loc.y);

                    newinfo.location = loc;

                    final Object portrayedObject = object;
                    if (graphics == null)
                        {
                        if (portrayal.hitObject(portrayedObject, newinfo))
                            putInHere.add(getWrapper(portrayedObject));
                        }
                    else
                        {
                        // MacOS X 10.3 Panther has a bug which resets the clip, YUCK
                        //                    graphics.setClip(clip);
                        newinfo.selected = (objectSelected &&  // there's something there
                            selectedWrappers.get(portrayedObject) != null); 
                        /* {
                           LocationWrapper wrapper = (LocationWrapper)(selectedWrappers.get(portrayedObject));
                           portrayal.setSelected(wrapper,true);
                           portrayal.draw(portrayedObject, graphics, newinfo);
                           portrayal.setSelected(wrapper,false);
                           }
                           else */ portrayal.draw(portrayedObject, graphics, newinfo);
                        }
                    }
                }
            }
            
        if (drawsFrameAndAxesInFront) drawFrameAndAxes(graphics, info);
        }


    void drawFrameAndAxes(Graphics2D graphics, DrawInfo2D info)
        {
        if (frame != null && graphics != null)
            {
            graphics.setPaint(frame);
            graphics.draw(new Rectangle2D.Double(info.draw.x - 1, info.draw.y - 1, info.draw.width + 1, info.draw.height + 1));
            }

        if (axes != null && graphics != null)
            {
            graphics.setPaint(axes);
            
            // Bugs in OS X's graphics handling prevent any line extending beyond
            // (Integer.MAX_VALUE - 2, Integer.MAX_VALUE - 52),
            // even when the line being drawn is real-valued.  Also, if the line endpoints are
            // well outside the clip region it's clipped entirely even if the line intersects
            // with the clip region.  Grrrrrr......  
            graphics.draw(new Line2D.Double(info.clip.x, info.draw.y + (info.draw.height) / 2.0, 
                                            info.clip.x + info.clip.width, info.draw.y + (info.draw.height) / 2.0));
            graphics.draw(new Line2D.Double(info.draw.x + (info.draw.width) / 2.0, info.clip.y, 
                                            info.draw.x + (info.draw.width) / 2.0, info.clip.y + info.clip.height));
            }
        }

    public LocationWrapper getWrapper(final Object obj)
        {
        final Continuous2D field = (Continuous2D)this.field;
        final StableDouble2D w = new StableDouble2D(field, obj);
        return new LocationWrapper( obj, null , this)  // don't care about location
            {
            public Object getLocation()
                {
                return w;
                }
                
            public String getLocationName()
                {
                return w.toString();
                }
            };
        }
                
                        
    HashMap selectedWrappers = new HashMap();
    public boolean setSelected(LocationWrapper wrapper, boolean selected)
        {
        if (wrapper == null) return true;
        if (wrapper.getFieldPortrayal() != this) return true;

        Object obj = wrapper.getObject();
        boolean b = getPortrayalForObject(obj).setSelected(wrapper,selected);
        if (selected)
            {
            if (b==false) return false;
            else selectedWrappers.put(obj, wrapper);
            }
        else
            {
            selectedWrappers.remove(obj);
            }
        return true;
        }
    }
    
    
