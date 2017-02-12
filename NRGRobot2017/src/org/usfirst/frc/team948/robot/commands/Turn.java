package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Robot does a relative turn by a given angle 
 * Positive is clockwise
 * Negative is counter clockwise
 */
public class Turn extends Command {
	private double desiredHeading;
	private double power;

	public Turn(double angle, double power) {
		this.requires(Robot.drive);
		this.power = power;
		this.desiredHeading = Robot.drive.getAutonomousHeading() + angle;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.drive.turnToHeadingInit2(desiredHeading);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.drive.turnToHeading2(power);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.drive.isOnHeading2();
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.turnToHeadingEnd2(desiredHeading);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
