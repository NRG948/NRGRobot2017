package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.utilities.ContinuousGyro;
import org.usfirst.frc.team948.utilities.MultichanelLED;
import org.usfirst.frc.team948.utilities.UltrasonicSensor;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
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
	// public static final double TURN_P;
	// public static final double TURN_I;
	// public static final double TURN_D;

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
	public static Victor motorFrontLeft = new Victor(0);
	public static Victor motorBackLeft = new Victor(1);
	
	public static Victor motorFrontRight = new Victor(2);
	public static Victor motorBackRight = new Victor(3);

	public static Victor climberMotor = new Victor(8);
	
	public static Victor ballCollectorInOutMotor = new Victor(5);
	public static Victor ballCollectorUpDownMotor = new Victor(4);

	public static Victor shooterWheelTop = new Victor(6);
	public static Victor shooterWheelBottom = new Victor(9);
	public static Victor conveyorBelt = new Victor(7);
	
	public static Encoder leftEncoder = new Encoder(0, 1, true);
	public static Encoder rightEncoder = new Encoder(2, 3, false);
	
	public static LiveWindowSendable gyroChannel;
	public static AnalogInput ultrasound = new AnalogInput(0);
	
	public static DigitalInput upperGearSensor = new DigitalInput(8);
	public static DigitalInput lowerGearSensor = new DigitalInput(9);
	
	public static Encoder shooterEncoder = new Encoder(4, 5, true);

	public static AHRS navx = new AHRS(SPI.Port.kMXP);
	public static ContinuousGyro continuousGyro = new ContinuousGyro(navx);

	public static Preferences preferences = Preferences.getInstance();
	public static Compressor compressor = new Compressor();

	public static DoubleSolenoid gearboxSolenoid = new DoubleSolenoid(6, 7);
	public static MultichanelLED gearLight = new MultichanelLED(1,0,2); //1,0,2 stand for Red (Top), Green (Both), Blue (Bottom)
	
	public static Solenoid cameraLight = new Solenoid(5);

	public static final DoubleSolenoid.Value IN_HIGH_GEAR = DoubleSolenoid.Value.kForward;
	public static final DoubleSolenoid.Value IN_LOW_GEAR = DoubleSolenoid.Value.kReverse;
//	Need to set the real values
//	public static final UltrasonicSensor rangeFinder = new UltrasonicSensor(0);
	public static PowerDistributionPanel pdp = new PowerDistributionPanel();
	public static boolean autoWithVision;
	static void init() {
		LiveWindow.addActuator("Drive Subsystem", "Front Left Victor", motorFrontLeft);
		LiveWindow.addActuator("Drive Subsystem", "Front Right Victor", motorFrontRight);
		LiveWindow.addActuator("Drive Subsystem", "Back Left Victor", motorBackLeft);
		LiveWindow.addActuator("Drive Subsystem", "Back Right Victor", motorBackRight);

		LiveWindow.addSensor("Drive Subsystem", "leftDriveEncoder", leftEncoder);
		LiveWindow.addSensor("Drive Subsystem", "rightDriveEncoder", rightEncoder);
		LiveWindow.addSensor("Drive Subsystem", "Gyro Channel", gyroChannel);
	}

}
