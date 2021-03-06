package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Sets the Robot's drive gearbox to high or low gear.
 */
public class ShiftGears extends Command {
	private boolean highGear;

	public ShiftGears(boolean highGear) {
		requires(Robot.gearbox);
		this.highGear = highGear;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (highGear) {
			Robot.gearbox.setHighGear();
		} else {
			Robot.gearbox.setLowGear();
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return true;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
