package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.VisionField;
import org.usfirst.frc.team948.utilities.VisionProc;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class VisionDriveToPeg extends Command {
	private static final double KZETA = 20;
	private final VisionProc proc;
	private double targetDistance = Double.MAX_VALUE;

	public VisionDriveToPeg(VisionProc proc) {
		this.proc = proc;
		requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.drive.driveOnHeadingInit(RobotMap.continuousGyro.getAngle());
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (proc.dataExists() == false) {
			Robot.drive.stop();
			System.out.println("No Vision Data");
			return;
		}
		VisionField field = proc.getData();
		SmartDashboard.putNumber("Vision: Theta", field.theta);
		SmartDashboard.putNumber("Vision: V", field.v);
		SmartDashboard.putNumber("Vision: Zeta", field.zeta);
		SmartDashboard.putNumber("Vision: Omega", field.omega);
		SmartDashboard.putNumber("Vision: Gamma", field.gamma);
		double heading = RobotMap.continuousGyro.getAngle();
		double updatedHeading = heading + KZETA * field.zeta;
		Robot.drive.driveOnHeading(0.4, updatedHeading);
		targetDistance = field.v;
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		// TODO Figure out correct distance
		return targetDistance < 12;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.driveOnHeadingEnd();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		System.out.println("Vision drive was interrupted");
		end();
	}
}
