package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Sets the coordinates of the center of the robot at the start of the match!
 */
public class SetInitPoz extends Command {
	private double x;
	private double y;

	public SetInitPoz(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.positionTracker.setXY(x, y);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return true;
	}
}
