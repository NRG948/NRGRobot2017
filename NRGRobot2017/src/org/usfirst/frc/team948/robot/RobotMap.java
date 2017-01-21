package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.utilities.AHRSGyro;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Victor;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	private static int defualtmotorFrontLeft = 0;
	private static int defualtmotorFrontRight = 1;
	private static int defualtmotorBackLeft = 2;
	private static int defualtmotorBackRight = 3;
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;
	
	public static AHRS ahrs = new AHRS(SPI.Port.kMXP);

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	public static Victor motorFrontLeft = new Victor(Robot.preferences.getInt(PreferenceKeys.motorFrontLeft, defualtmotorFrontLeft));
	public static Victor motorFrontRight = new Victor(Robot.preferences.getInt(PreferenceKeys.motorFrontRight, defualtmotorFrontRight));
	public static Victor motorBackLeft = new Victor(Robot.preferences.getInt(PreferenceKeys.motorBackLeft, defualtmotorBackLeft));
	public static Victor motorBackRight = new Victor(Robot.preferences.getInt(PreferenceKeys.motorBackRight, defualtmotorBackRight));
	
	public static AHRSGyro driveGyro;

}
