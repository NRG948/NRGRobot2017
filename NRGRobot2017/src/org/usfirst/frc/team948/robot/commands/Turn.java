package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Robot does a relative turn by a given angle in degrees. Positive is clockwise
 * Negative is counter clockwise
 */
public class Turn extends Command {
	private double desiredHeading;
	private double power;
	private double degreesToTurn;

	public Turn(double degreesToTurn, double power) {
		this.requires(Robot.drive);
		this.power = power;
		this.degreesToTurn = degreesToTurn;
	}
	
	public Turn(double degreesToTurn) {
		this(degreesToTurn, 1.0);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		this.desiredHeading = Robot.drive.getAutonomousHeading() + degreesToTurn;
		Robot.drive.turnToHeadingInitNoPID(desiredHeading);
		SmartDashboard.putString("Turn command: degrees to turn, desired heading = ",
				degreesToTurn + ", " + desiredHeading);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.drive.turnToHeadingNoPID(power);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.drive.isOnHeadingNoPID();
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.turnToHeadingEndNoPID(desiredHeading);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run.
	protected void interrupted() {
		end();
	}
}
