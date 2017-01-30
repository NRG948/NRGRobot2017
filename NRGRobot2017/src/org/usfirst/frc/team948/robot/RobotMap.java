package org.usfirst.frc.team948.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Victor;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// public static final double STRAIGHT_KP = 0.06;
	// public static final double STRAIGHT_KI = 0.003;
	// public static final double STRAIGHT_KD = 0.3;
//	 public static final double TURN_P;
//	 public static final double TURN_I;
//	 public static final double TURN_D;

	// table of values to store on the roborio and possibly modify on the
	// smartdashboard
	public static Preferences prefs = Preferences.getInstance();

	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	public static Victor motorFrontLeft = new Victor(2);
	public static Victor motorFrontRight = new Victor(0);
	public static Victor motorBackLeft = new Victor(3);
	public static Victor motorBackRight = new Victor(1);
	public static int gyroChannel;

	public static AHRS navx = new AHRS(SPI.Port.kMXP);
	
	
	
	public static DoubleSolenoid solenoid = new DoubleSolenoid(1, 0);
	public static Compressor compressor = new Compressor(1);
	

}
