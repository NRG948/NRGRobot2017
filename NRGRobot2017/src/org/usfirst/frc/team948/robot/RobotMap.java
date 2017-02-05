package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.utilities.ContinuousGyro;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;

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
	public static Encoder leftEncoder = new Encoder(0,1);
	public static Encoder rightEncoder = new Encoder(2,3);
	public static LiveWindowSendable gyroChannel;
	public static AnalogInput ultrasound = new AnalogInput(0);
	public static Victor shooterWheel = new Victor(4);
	public static Encoder shooterEncoder = new Encoder(4,5);
	
	public static AHRS navx = new AHRS(SPI.Port.kMXP);
	public static ContinuousGyro continuousGyro = new ContinuousGyro(navx);
	
	public static Preferences preferences = Preferences.getInstance();
	
	public static DoubleSolenoid solenoid = new DoubleSolenoid(1, 0);
	public static Compressor compressor = new Compressor(1);
	
	public static void init() {
		LiveWindow.addActuator("Drive Subsystem", "Speed Controller Front Left Victor",(Victor) motorFrontLeft);
		LiveWindow.addActuator("Drive Subsystem", "Speed Controller Front Right Victor",(Victor) motorFrontRight);
		LiveWindow.addActuator("Drive Subsystem", "Speed Controller Back Left Victor",(Victor) motorBackLeft);
		LiveWindow.addActuator("Drive Subsystem", "Speed Controller Back Right Victor",(Victor) motorBackRight);
		
		LiveWindow.addSensor("Drive Substem", "leftDriveEncoder", leftEncoder);
		LiveWindow.addSensor("Drive Subsystem", "rightDriveEncoder", rightEncoder);
		LiveWindow.addSensor("Drive Subsystem", "Gyro Channel", (LiveWindowSendable) gyroChannel);
	}

}
