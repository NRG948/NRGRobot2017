package org.usfirst.frc.team948.utilities;

public class VisionField {
	/**
	 * Theta is the angle which the plane of the wall of the air-ship and
	 * the plane of the of robot's front intersect.
	 * 
	 * V is the shortest distance to the plane of the camera from the center of the tape.
	 * 
	 * Zeta how to the left or right the object is of the robot measured from -1 (all the way to the left)
	 * to +1 (all the way to the right)
	 * 
	 * Omega is the distance from the camera to the point on the plane of the robot that is
	 * used for the calculation of v.
	 * 
	 * Gamma is the change in heading (in radians) that is needed to face the center of
	 * the object.
	 * 
	 * All distances are measured in inches.
	 */
	public double distanceToTarget;
	public double theta;
	public double v;
	public double zeta;
	public double omega;
	public double gamma;
	public boolean isTape;
}