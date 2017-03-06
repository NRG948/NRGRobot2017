package org.usfirst.frc.team948.utilities;

public class Point2D {
	public double x, y;
	
	public Point2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}
