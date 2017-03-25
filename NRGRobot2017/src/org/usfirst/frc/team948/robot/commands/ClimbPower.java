package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimbPower extends Command {

	private final double DEFAULT_FORWARD_POWER = 0.9;
	private final double DEFAULT_BACKWARD_POWER = 0.35;

	private double power;
	private boolean forward;

	public ClimbPower(boolean forward) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.climb);
		this.forward = forward;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		if (forward) {
			this.power = RobotMap.preferences.getDouble(PreferenceKeys.CLIMB_FORWARD_POWER, DEFAULT_FORWARD_POWER);
		} else {
			this.power = -RobotMap.preferences.getDouble(PreferenceKeys.CLIMB_BACKWARD_POWER, DEFAULT_BACKWARD_POWER);
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.climb.rawClimb(power);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.climb.rawStop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
