
package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.TurnToHeading;
import org.usfirst.frc.team948.robot.subsystems.Climber;
import org.usfirst.frc.team948.robot.subsystems.Drive;
import org.usfirst.frc.team948.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.DoubleSolenoid;
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

	Command autonomousCommand;
	SendableChooser chooser = new SendableChooser();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		OI.buttonInit();
		// chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Turn to -90", new TurnToHeading(-90, 0.5));
		SmartDashboard.putData("Turn to +90", new TurnToHeading(90, 0.5));
		SmartDashboard.putData("Turn to 0", new TurnToHeading(0, 0.5));
		SmartDashboard.putData("Drive 15 Feet", new DriveStraightDistance(15,1.0));
		SmartDashboard.putData("High Gear", new ShiftGears(true));
		SmartDashboard.putData("Low Gear", new ShiftGears(false));
		drive.changeGearTracker(false);
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
		// autonomousCommand = chooser.getSelected();

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
		SmartDashboard.putNumber("right joystick y", OI.rightJoystick.getY());
		SmartDashboard.putNumber("left joystick y", OI.leftJoystick.getY());
		SmartDashboard.putNumber("left encoder", RobotMap.leftEncoder.get());
		SmartDashboard.putNumber("right encoder", RobotMap.rightEncoder.get());
		SmartDashboard.putNumber("Ultrasound", RobotMap.ultrasound.getVoltage());
		SmartDashboard.putNumber("Shooter", RobotMap.shooterEncoder.get());
		SmartDashboard.putBoolean("In High Gear", RobotMap.solenoid.get() == DoubleSolenoid.Value.kForward);
		SmartDashboard.putString("Solenoid Value", RobotMap.solenoid.get().toString());

	}
}
