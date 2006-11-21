/*
 * Created on Nov 18, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package sim.portrayal3d.network;

import java.awt.Color;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;

/**
 * @author Gabriel Balan
 *
 */
public class CylinderEdgePortrayal3D extends GenericEdgePortrayal3D
{
	static class MyPickableCylinder extends Cylinder
	{
		public MyPickableCylinder(){super();}
		
		public MyPickableCylinder(float radius)
		{
			super(radius, 2);
		}
		
		public MyPickableCylinder(float radius, Appearance ap)
		{
			super(radius, 2, ap);
		}
		public MyPickableCylinder(Appearance ap)
		{
			super(1, 2, ap);
		}

		public void setUserData(java.lang.Object userData)
		{
			super.setUserData(userData);
			setup(getShape(Cylinder.BODY), userData);
			setup(getShape(Cylinder.TOP), userData);
			setup(getShape(Cylinder.BOTTOM), userData);


		}
		
		private void setup(Shape3D shape,java.lang.Object userData)
		{
			shape.setUserData(userData);
			setPickableFlags(shape);
		}
		
	}

	public CylinderEdgePortrayal3D()
	{
		super(new MyPickableCylinder());
	}
	
	public CylinderEdgePortrayal3D(float cylinderRadius)
	{
		super(new MyPickableCylinder(cylinderRadius));
	}
	
	public CylinderEdgePortrayal3D(float cylinderRadius, Appearance ap)
	{
		super(new MyPickableCylinder(cylinderRadius, ap));
	}	


	public CylinderEdgePortrayal3D(Color labelColor)
	{
		super(new MyPickableCylinder(), labelColor);
	}

	public CylinderEdgePortrayal3D(Appearance edgeAppearance, Color labelColor)
	{
		super(new MyPickableCylinder(edgeAppearance), labelColor);
	}
}
