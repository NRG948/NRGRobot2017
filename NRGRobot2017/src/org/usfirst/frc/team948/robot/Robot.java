package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.robot.commandgroups.AutonomousRoutines;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.DriveToXY;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.SpinShooterToRPM;
import org.usfirst.frc.team948.robot.commands.Turn;
import org.usfirst.frc.team948.robot.commands.TurnToHeading;
import org.usfirst.frc.team948.robot.commands.VisionDriveToPeg;
import org.usfirst.frc.team948.robot.commands.WaitUntilGearDrop;
import org.usfirst.frc.team948.robot.subsystems.BallCollector;
import org.usfirst.frc.team948.robot.subsystems.BallFeeder;
import org.usfirst.frc.team948.robot.subsystems.CameraLight;
import org.usfirst.frc.team948.robot.subsystems.Climber;
import org.usfirst.frc.team948.robot.subsystems.Drive;
import org.usfirst.frc.team948.robot.subsystems.GearLEDControler;
import org.usfirst.frc.team948.robot.subsystems.Gearbox;
import org.usfirst.frc.team948.robot.subsystems.Shooter;
import org.usfirst.frc.team948.utilities.NewVisionProc;
import org.usfirst.frc.team948.utilities.PegLocator;
import org.usfirst.frc.team948.utilities.PositionTracker;
import org.usfirst.frc.team948.utilities.PreferenceKeys;
import org.usfirst.frc.team948.utilities.ShooterCalculator;
import org.usfirst.frc.team948.utilities.TestGroup;
import org.usfirst.frc.team948.utilities.VisionField;

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

	public static final int CAMERA_RESOLUTION_WIDTH = 160;
	public static final int CAMERA_RESOLUTION_HEIGHT = 120;
	public static final Drive drive = new Drive();
	public static final Climber climb = new Climber();
	public static final Shooter shooter = new Shooter();
	public static final Gearbox gearbox = new Gearbox();
	public static final CameraLight cameraLight = new CameraLight();
	public static final BallCollector ballCollector = new BallCollector();
	public static final BallFeeder ballFeeder = new BallFeeder();
	public static GearLEDControler ledStrip;
	public static final double ROBOT_LENGTH = 39; // with bumpers
	public static final double ROBOT_WIDTH = 35; // with bumpers
	public static final double FIELD_WIDTH = 323.7;
	public static final double ROBOT_X_BOILER = ROBOT_WIDTH+35;
	private static final double TURN_POWER = 1.0;
	
	public static final double BLUE_LEFT_X = 93 - ROBOT_WIDTH / 2;
	public static final double RED_RIGHT_X = FIELD_WIDTH - BLUE_LEFT_X;
	
	public static final double RED_LEFT_X = 76 - ROBOT_WIDTH / 2;
	public static final double BLUE_RIGHT_X = FIELD_WIDTH - RED_LEFT_X;
	
	public static UsbCamera camera;
	// public static VisionProc visionProcessor;
	public static NewVisionProc visionProcessor;

	public static Command autonomousCommand;
	public static SendableChooser<AutoPosition> autoPositionChooser;
	public static SendableChooser<AutoMovement> autoMovementChooser;
	public static PositionTracker positionTracker = new PositionTracker();
	public static ShooterCalculator shooterCalculator = new ShooterCalculator();
	public static PegLocator pegLocator = new PegLocator();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	public enum AutoPosition {
		RED_CENTER(FIELD_WIDTH / 2, ROBOT_LENGTH / 2), 
		BLUE_CENTER(FIELD_WIDTH / 2, ROBOT_LENGTH / 2), 
		
		RED_LEFT(RED_LEFT_X, ROBOT_LENGTH / 2), 
		BLUE_RIGHT(BLUE_RIGHT_X, ROBOT_LENGTH / 2), 
		
		RED_RIGHT(RED_RIGHT_X, ROBOT_LENGTH / 2), 
		BLUE_LEFT(BLUE_LEFT_X, ROBOT_LENGTH / 2),
		
		//X and y coordinates are no longer accurate and not used.
		RED_SHOOT_THEN_GEAR(FIELD_WIDTH -ROBOT_X_BOILER, ROBOT_LENGTH / 2, 140),
		BLUE_SHOOT_THEN_GEAR(ROBOT_X_BOILER, ROBOT_LENGTH / 2, -135);
		
		public double x, y, initialHeading;
		
		private AutoPosition(double x, double y, double initialHeading) {
			this.x = x;
			this.y = y;
			this.initialHeading = initialHeading;
		}
		
		private AutoPosition(double x, double y) {
			this(x, y, 0);
		}
	}

	public enum AutoMovement {
		SHOOT_ONLY , SHOOT_AFTER_GEAR_DROP, STOP_AT_AIRSHIP, STOP_AT_AUTOLINE, CONTINUE_TO_END;
	}

	public enum PegPosition {
		LEFT(128, 131, 60), CENTER(158, 113, 0), RIGHT(188, 131, -60);
		public double x, y, angle;

		private PegPosition(double x, double y, double angle) {
			this.x = x;
			this.y = y;
			this.angle = angle;
		}
	}

	@Override
	public void robotInit() {
		System.out.println("\nRobot init\n");
		ledStrip = new GearLEDControler(RobotMap.gearLight);

		// Vision Tracking
		camera = CameraServer.getInstance().startAutomaticCapture();
		// camera.setResolution(CAMERA_RESOLUTION_WIDTH,
		// CAMERA_RESOLUTION_HEIGHT);
		camera.setExposureManual(0);
		cameraLight.turnOff();
		visionProcessor = new NewVisionProc().start();
		positionTracker.init(0, 0);
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
	    autoPositionChooser.addObject("Blue Shoot Only", AutoPosition.BLUE_SHOOT_THEN_GEAR);
	    autoPositionChooser.addObject("Red Shoot Only", AutoPosition.RED_SHOOT_THEN_GEAR);

		autoMovementChooser = new SendableChooser<AutoMovement>();
		autoMovementChooser.addDefault("Shoot After Gear", AutoMovement.SHOOT_AFTER_GEAR_DROP);
		autoMovementChooser.addObject("Continue to end",
				AutoMovement.CONTINUE_TO_END);
		autoMovementChooser.addObject("Continue to auto",
				AutoMovement.STOP_AT_AUTOLINE);
		autoMovementChooser.addDefault("Stop at airship",
				AutoMovement.STOP_AT_AIRSHIP);

		// SmartDashboard for Drive SubSystem Commands
		SmartDashboard.putData("Choose autonomous position",
				autoPositionChooser);
		if (RobotMap.preferences.getBoolean(
				PreferenceKeys.USE_POSITION_CHOOSER, true)) {
			SmartDashboard.putData("Choose autonomous movement",
					autoMovementChooser);
		}
		SmartDashboard.putData(drive);
		// SmartDashboard.putData("Turn to -90", new TurnToHeading(-90,
		// TURN_POWER));
		// SmartDashboard.putData("Turn to 180", new
		// TurnToHeading(180,TURN_POWER));
		// SmartDashboard.putData("Turn to +90", new TurnToHeading(90,
		// TURN_POWER));
		SmartDashboard.putData("Turn to 0", new TurnToHeading(0, TURN_POWER));
		SmartDashboard.putData("Turn -90", new Turn(-90, TURN_POWER));
		SmartDashboard.putData("Turn +90", new Turn(90, TURN_POWER));
		SmartDashboard.putData("Drive 15 Feet", new DriveStraightDistance(
				15 * 12.0, Drive.Direction.FORWARD, 1.0));
		SmartDashboard.putData("Drive 5 Feet", new DriveStraightDistance(
				5 * 12.0, Drive.Direction.FORWARD, 1.0));
		SmartDashboard.putData("Switch High Gear", new ShiftGears(true));
		SmartDashboard.putData("Switch Low Gear", new ShiftGears(false));
		// SmartDashboard.putData("Activate simple vision", new
		// SimpleVisionRoutine(visionProcessor));
		SmartDashboard.putData("Test wait until gear drop",
				new WaitUntilGearDrop(2));
		SmartDashboard.putData("Drive to Peg", new VisionDriveToPeg());
		// To test Drive to XY command
		SmartDashboard.putData("Test DriveToXY", new DriveToXY());
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
		System.out.println("Disabled init\n");
		visionProcessor.disableProcessing();
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
		System.out.println("Auto init\n");
		// schedule the autonomous command
		visionProcessor.enableProcessing();
		RobotMap.autoWithVision = true; // temp change
															// OI.driveWithVision.get();
		autonomousCommand = new AutonomousRoutines(OI.getAutoPosition(),
				OI.getAutoMovement());
		System.out.println("Vision = " + RobotMap.autoWithVision + "\n");
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		periodicAll();
		positionTracker.updatePosition();
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		System.out.println("Teleop init\n");
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
//		visionProcessor.enableProcessing(); // for calibration
		visionProcessor.disableProcessing(); // disable vision processing in teleop to prevent memory faults
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		periodicAll();
		positionTracker.updatePosition();
		Scheduler.getInstance().run();
		boolean visionHasData = visionProcessor.dataExists();
		if (visionHasData) {
			VisionField field = visionProcessor.getData();
			SmartDashboard.putNumber("Vision: V", field.v);
			SmartDashboard.putNumber("Vision: Zeta", field.zeta);
			SmartDashboard.putNumber("Vision: distance to target",
					field.distanceToTarget);
		}
		SmartDashboard.putBoolean("Vision has Data", visionHasData);
		RobotMap.shooterAngleServo.set((OI.rightJoystick.getZ() + 1) / 2);
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
		AutoPosition position = OI.getAutoPosition();
		if (position != null) {// if smart dashboard is not open it gives a null
			// pointer exception.
			SmartDashboard.putString("Auto position", position.toString());
		}
		SmartDashboard.putString("Peg position", pegLocator.getPegPosition().toString()); // testing peg position of robot
		SmartDashboard.putNumber("Shooter Encoder", RobotMap.shooterEncoder.get());
		SmartDashboard.putNumber("Shooter servo value",
				(OI.rightJoystick.getZ() + 1) / 2);
		SmartDashboard.putNumber("Right joystick y", OI.rightJoystick.getY());
		SmartDashboard.putNumber("Left joystick y", OI.leftJoystick.getY());
		SmartDashboard.putNumber("Left encoder", RobotMap.leftEncoder.get());
		SmartDashboard.putNumber("Right encoder", RobotMap.rightEncoder.get());
		String ultraSoundData = String.format("%.2f, %.2f",
				RobotMap.ultraSound.getVoltage(),
				RobotMap.ultraSound.getDistanceInches());
		SmartDashboard.putString("Ultrasound sensor: volts , Inches",
				ultraSoundData);
		SmartDashboard.putBoolean("High gear?", gearbox.isHighGear());
		SmartDashboard.putString("Solenoid value", RobotMap.gearboxSolenoid
				.get().toString());
		SmartDashboard.putNumber("Turn to boiler angle",
				shooterCalculator.getTurnAngleToBoiler());
		SmartDashboard.putNumber("RPM from position tracker",
				shooterCalculator.getShooterRPM());
		SmartDashboard.putNumber("gyro", RobotMap.continuousGyro.getAngle());

		SmartDashboard.putNumber("Channel 13", RobotMap.pdp.getCurrent(13));
		SmartDashboard.putNumber("Channel 14", RobotMap.pdp.getCurrent(14));
		SmartDashboard.putNumber("Channel 3", RobotMap.pdp.getCurrent(3));
		SmartDashboard.putNumber("Channel 0", RobotMap.pdp.getCurrent(0));

		SmartDashboard.putNumber("Right joystick z", OI.rightJoystick.getZ());
		SmartDashboard.putNumber("FrontLeft", RobotMap.motorFrontLeft.get());
		SmartDashboard.putNumber("BackLeft", RobotMap.motorBackLeft.get());
		SmartDashboard.putNumber("FrontRight", RobotMap.motorFrontRight.get());
		SmartDashboard.putNumber("BackRight", RobotMap.motorBackRight.get());
		SmartDashboard.putBoolean("Upper gear sensor",
				!RobotMap.upperGearSensor.get());
		boolean haveGearLow = !RobotMap.lowerGearSensor.get();
		boolean haveGearHigh = !RobotMap.upperGearSensor.get();
		SmartDashboard.putNumber("Test Target RPM",
				SpinShooterToRPM.getTargetRPM());
		// boolean visionOnTarget = !RobotMap.visionOnTarget.get();

		SmartDashboard.putBoolean("Lower gear sensor", haveGearLow);
		SmartDashboard.putBoolean("Upper gear sensor", haveGearHigh);
		SmartDashboard
				.putString("Position Tracker", positionTracker.toString());
		SmartDashboard.putNumber("Blocked via averaging",
				positionTracker.objectInfront(true) ? 1.0 : 0.0);
		SmartDashboard.putNumber("Blocked via raw data",
				positionTracker.objectInfront(false) ? 1.0 : 0.0);
		// SmartDashboard.putBoolean("Vision on target", visionOnTarget);

		// if (visionOnTarget){
		// RobotMap.gearLight.turnGreenOn();
		// }
		// else{
		// RobotMap.gearLight.turnGreenOff();
		// }

		ledStrip.updateLights();

		// SmartDashboard.putNumber("Camera", targetCam.getBrightness());
		visionProcessor.dataExists();
	}
}
