package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	public static final int defualtmotorFrontLeft = 0;
	public static final int defualtmotorFrontRight = 1;
	public static final int defualtmotorBackLeft = 2;
	public static final int defualtmotorBackRight = 3;
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	public static Victor motorFrontLeft = new Victor(Robot.preferences.getInt(PreferenceKeys.defualtmotorFrontLeft, defualtmotorFrontLeft));
	public static Victor motorFrontRight = new Victor(Robot.preferences.getInt(PreferenceKeys.defualtmotorFrontRight, defualtmotorFrontRight));
	public static Victor motorBackLeft = new Victor(Robot.preferences.getInt(PreferenceKeys.defualtmotorBackLeft, defualtmotorBackLeft));
	public static Victor motorBackRight = new Victor(Robot.preferences.getInt(PreferenceKeys.defualtmotorBackRight, defualtmotorBackRight));
	
	//Change this to be the gyro class when done
	public static Encoder driveGyro;
}
