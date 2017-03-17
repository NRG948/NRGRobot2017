package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.MathUtil;
import org.usfirst.frc.team948.utilities.NewVisionProc;
import org.usfirst.frc.team948.utilities.VisionField;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class VisionDriveToPeg extends Command {
	private static final double SLOW_DOWN_DISTANCE = 29;
	private static final double KZETA = 20;
	private final NewVisionProc proc;
	private double targetDistance = Double.MAX_VALUE;

	public VisionDriveToPeg() {
		this.proc = Robot.visionProcessor;
		requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.drive.driveOnHeadingInit(RobotMap.continuousGyro.getAngle());
		Robot.ledStrip.setGettingData(true);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (proc.dataExists() == false) {
			Robot.drive.stop();
			System.out.println("No Vision Data");
			return;
		}
		VisionField field = proc.getData();
		targetDistance = field.v;
		SmartDashboard.putNumber("Vision: Theta", field.theta);
		SmartDashboard.putNumber("Vision: V", field.v);
		SmartDashboard.putNumber("Vision: Zeta", field.zeta);
		SmartDashboard.putNumber("Vision: Omega", field.omega);
		SmartDashboard.putNumber("Vision: Gamma", field.gamma);
		double updatedHeading;
		if (targetDistance > 18 || Math.abs(field.zeta) > 0.1) {
			updatedHeading = RobotMap.continuousGyro.getAngle() + KZETA * field.zeta;
			Robot.ledStrip.writeData(false);
		} else {
			Robot.ledStrip.writeData(true);
			updatedHeading = Robot.drive.getAutonomousHeading();
		}
		double distanceToSlow = RobotMap.preferences.getDouble("SLOW_DOWN_DISTANCE", SLOW_DOWN_DISTANCE);
		double power = MathUtil.clamp(targetDistance / distanceToSlow, 0.15, 1);
		Robot.drive.driveOnHeading(power, updatedHeading);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		// TODO Figure out correct distance
		return targetDistance < 12;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.ledStrip.setGettingData(false);
		Robot.drive.driveOnHeadingEnd();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		System.out.println("Vision drive was interrupted");
		end();
	}	
}
