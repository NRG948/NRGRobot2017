package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BallCollect extends Command {

	public static final double BALL_COLLECT_POWER = 0.15;
	private boolean in;
	
	public BallCollect(boolean in) {
		this.in = in;
		requires(Robot.ballCollector);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double power = in ? BALL_COLLECT_POWER : -BALL_COLLECT_POWER;
		Robot.ballCollector.rawCollect(power);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.ballCollector.rawCollect(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
