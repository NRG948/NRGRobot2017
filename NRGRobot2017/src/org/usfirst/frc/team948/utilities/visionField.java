package org.usfirst.frc.team948.utilities;

public class visionField {
	/**
	 * Theta is the angle wich the plane of the wall of the air-ship and
	 * the plane of the of robot's front intersect.
	 * 
	 * V is the shortest distance to the plane of the camera from the center of the tape.
	 * 
	 * Zeta has is -1 when the object is to the left of the robot, 0 when the object is aproximately directly
	 * infront of the robot, and 1 when the object is the the right of the robot
	 * 
	 * Omega is the distance from the camera to the point on the plane of the robot that is used for the calculation of v.
	 * 
	 * Gamma is the change in heading (in radians) that is needed to face the center of the object.
	 * 
	 * All distances are measured in inches.
	 */
	public final double theta;
	public final double v;
	public final double zeta;
	public final double omega;
	public final double gamma;
	public visionField(double[] in){
		theta = in[0];
		v = in[1];
		zeta = in[2];
		omega = in[3];
		gamma = in[4];
	}
	
	@SuppressWarnings("null")
	public visionField(){
		theta = (Double) null;
		v = (Double) null;
		zeta = (Double) null;
		omega = (Double) null;
		gamma = (Double) null;
	}
}
