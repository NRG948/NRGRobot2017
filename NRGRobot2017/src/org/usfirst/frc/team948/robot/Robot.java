
package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.robot.commandgroups.AutonomousRoutines;
import org.usfirst.frc.team948.robot.commandgroups.SimpleVisionRoutine;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.Turn;
import org.usfirst.frc.team948.robot.commands.TurnToHeading;
import org.usfirst.frc.team948.robot.subsystems.BallCollector;
import org.usfirst.frc.team948.robot.subsystems.CameraLight;
import org.usfirst.frc.team948.robot.subsystems.Climber;
import org.usfirst.frc.team948.robot.subsystems.Drive;
import org.usfirst.frc.team948.robot.subsystems.Gearbox;
import org.usfirst.frc.team948.robot.subsystems.Shooter;
import org.usfirst.frc.team948.utilities.visionProc;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final Drive drive = new Drive();
	public static final Climber climb = new Climber();
	public static final Shooter shooter = new Shooter();
	public static final Gearbox gearbox = new Gearbox();
	public static final CameraLight cameraLight = new CameraLight();
	public static final BallCollector ballCollector = new BallCollector();

	private static final double TURN_POWER = 1.0;

	private static boolean moveAfterGear = true;

	public static UsbCamera camera;
	visionProc VisionProccesor;

	Command autonomousCommand;
	SendableChooser<AutoPosition> autoPositionChooser;
	SendableChooser<AutoMovement> autoMovementChooser;
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	public enum AutoPosition {
		RED_LEFT, RED_CENTER, RED_RIGHT, BLUE_RIGHT, BLUE_CENTER, BLUE_LEFT, STAY;
	}
	public enum AutoMovement {
		STOP_AT_AIRSHIP, CONTINUE_TO_AUTOLINE, CONTINUE_TO_END;
	}

	@Override
	public void robotInit() {

		// Vision Tracking
		camera = CameraServer.getInstance().startAutomaticCapture();
		// camera.setResolution(640, 380);x
		camera.setExposureManual(-11);
		cameraLight.turnOff();
		VisionProccesor = new visionProc().start();

		// Driver Station
		OI.buttonInit();

		// Autonomous Routine
		autoPositionChooser = new SendableChooser<AutoPosition>();
		autoPositionChooser.addObject("Red left", AutoPosition.RED_LEFT);
		autoPositionChooser.addObject("Red center", AutoPosition.RED_CENTER);
		autoPositionChooser.addObject("Red right", AutoPosition.RED_RIGHT);
		autoPositionChooser.addObject("Blue left", AutoPosition.BLUE_LEFT);
		autoPositionChooser.addObject("Blue center", AutoPosition.BLUE_CENTER);
		autoPositionChooser.addObject("Blue right", AutoPosition.BLUE_RIGHT);
		autoPositionChooser.addDefault("Stay", AutoPosition.STAY);
		
		autoMovementChooser = new SendableChooser<AutoMovement>();
		autoMovementChooser.addObject("Continue to end", AutoMovement.CONTINUE_TO_END);
		autoMovementChooser.addObject("Continue to auto", AutoMovement.CONTINUE_TO_AUTOLINE);
		autoMovementChooser.addDefault("Stop at airship", AutoMovement.STOP_AT_AIRSHIP);
	
		// SmartDashboard for Drive SubSystem Commands
		SmartDashboard.putData("Choose autonomous routine", autoPositionChooser);
		SmartDashboard.putData(drive);
		SmartDashboard.putData("Turn to -90", new TurnToHeading(-90, TURN_POWER));
		SmartDashboard.putData("Turn to 180", new TurnToHeading(180, TURN_POWER));
		SmartDashboard.putData("Turn to +90", new TurnToHeading(90, TURN_POWER));
		SmartDashboard.putData("Turn to 0", new TurnToHeading(0, TURN_POWER));
		SmartDashboard.putData("Turn -90", new Turn(-90, TURN_POWER));
		SmartDashboard.putData("Turn +90", new Turn(90, TURN_POWER));
		SmartDashboard.putData("Drive 15 Feet", new DriveStraightDistance(15, Drive.Direction.FORWARD, 1.0));
		SmartDashboard.putData("Drive 5 Feet", new DriveStraightDistance(5, Drive.Direction.FORWARD, 1.0));
		SmartDashboard.putData("Switch High Gear", new ShiftGears(true));
		SmartDashboard.putData("Switch Low Gear", new ShiftGears(false));
		SmartDashboard.putData("Activate simple vision", new SimpleVisionRoutine(VisionProccesor));

		// Start in Low gear
		gearbox.setLowGear();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		periodicAll();
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = new AutonomousRoutines(autoPositionChooser.getSelected(), autoMovementChooser.getSelected());
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		periodicAll();
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();

	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		periodicAll();
		Scheduler.getInstance().run();

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		periodicAll();
		LiveWindow.run();
	}

	public void periodicAll() {
		SmartDashboard.putNumber("Yaw angle", RobotMap.navx.getYaw());
		SmartDashboard.putNumber("Continuous angle", RobotMap.continuousGyro.getAngle());
		SmartDashboard.putNumber("Right joystick y", OI.rightJoystick.getY());
		SmartDashboard.putNumber("Left joystick y", OI.leftJoystick.getY());
		SmartDashboard.putNumber("Left encoder", RobotMap.leftEncoder.get());
		SmartDashboard.putNumber("Right encoder", RobotMap.rightEncoder.get());
		SmartDashboard.putNumber("Ultrasound sensor", RobotMap.ultrasound.getVoltage());
		SmartDashboard.putNumber("Shooter encoder", RobotMap.shooterEncoder.get());
		SmartDashboard.putBoolean("High gear?", gearbox.isHighGear());
		SmartDashboard.putString("Solenoid value", RobotMap.gearboxSolenoid.get().toString());
		try {
			SmartDashboard.putData("PDP", RobotMap.pdp);
		} catch (Exception e) {
		}
		SmartDashboard.putNumber("Channel 13", RobotMap.pdp.getCurrent(13));
		SmartDashboard.putNumber("Channel 14", RobotMap.pdp.getCurrent(14));
		SmartDashboard.putNumber("Channel 3", RobotMap.pdp.getCurrent(3));
		SmartDashboard.putNumber("Channel 0", RobotMap.pdp.getCurrent(0));

		SmartDashboard.putNumber("Right joystick z", OI.rightJoystick.getZ());
		SmartDashboard.putNumber("FrontLeft", RobotMap.motorFrontLeft.get());
		SmartDashboard.putNumber("BackLeft", RobotMap.motorBackLeft.get());
		SmartDashboard.putNumber("FrontRight", RobotMap.motorFrontRight.get());
		SmartDashboard.putNumber("BackRight", RobotMap.motorBackRight.get());
		SmartDashboard.putBoolean("gear sensor", RobotMap.gearSensor.get());
		// SmartDashboard.putNumber("Camera", targetCam.getBrightness());
	}
}
