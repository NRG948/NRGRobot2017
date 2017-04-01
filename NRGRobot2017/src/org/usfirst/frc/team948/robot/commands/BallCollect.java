package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BallCollect extends Command {
	public static final double BALL_COLLECT_POWER = 0.6;
	private BallCollectDirection direction;
	
	public enum BallCollectDirection
	{
		IN, OUT, OFF;
	}
	
	public BallCollect(BallCollectDirection direction) {
		requires(Robot.ballCollector);
		this.direction = direction;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.ballCollector.rawCollect(BALL_COLLECT_POWER, direction);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.ballCollector.rawStop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
