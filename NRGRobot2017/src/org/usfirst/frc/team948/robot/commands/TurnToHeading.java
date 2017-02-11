package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnToHeading extends Command {
	private double desiredHeading;
	private double power;

	public TurnToHeading(double desiredHeading, double power) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		this.power = power;
		this.desiredHeading = desiredHeading;
		this.requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.drive.turnToHeadingInit(desiredHeading);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.drive.turnToHeading(power);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.drive.isOnHeading();
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.turnToHeadingEnd();
		Robot.drive.setAutonomousHeading(desiredHeading);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
