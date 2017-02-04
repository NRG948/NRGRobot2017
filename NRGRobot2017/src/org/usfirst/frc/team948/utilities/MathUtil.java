package org.usfirst.frc.team948.utilities;

public class MathUtil {
	public static double deadband(double input, double range){
		return Math.abs(input) < range ? 0 : input;
	}
	
	public static double clampR(double input, double center, double range){
		return input > center + range ? center + range : input < center - range ? center - range : input;
	}
	
	public static double clampM(double input, double min, double max){
		return input > max ? max : input < min ? min : input;
	}
}
