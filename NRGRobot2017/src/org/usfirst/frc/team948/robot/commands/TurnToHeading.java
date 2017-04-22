package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Turns the robot to face a specific heading
 */
public class TurnToHeading extends Command {
	protected double desiredHeading;
	private double power;

	public TurnToHeading(double desiredHeading, double power) {
		this.requires(Robot.drive);
		this.power = Math.abs(power);
		this.desiredHeading = desiredHeading;
	}

	public TurnToHeading(double desiredHeading) {
		this(desiredHeading, 1.0);
	}
	
	protected void initialize() {
		if (power == 0.0) {
			power = RobotMap.preferences.getDouble(PreferenceKeys.AUTONOMOUS_TURN_POWER, 0.5);
		}
		Robot.drive.turnToHeadingInitNoPID(desiredHeading);
		System.out.println("Turning to " + (int) desiredHeading + "\n");
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
		System.out.println("Turn has ended with error " + Robot.drive.getTurnError() + "\n");
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
