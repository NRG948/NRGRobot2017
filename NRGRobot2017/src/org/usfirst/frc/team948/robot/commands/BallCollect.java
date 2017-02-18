package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BallCollect extends Command {

	public static final double BALL_COLLECT_POWER = 0.15;
	private static int countIn;
	private static int countOut;
	private boolean in;

	public BallCollect(boolean in) {
		this.in = in;
		requires(Robot.ballCollector);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		if(in) {
			countOut = 0;
			countIn++;
		} else {
			countIn = 0;
			countOut++;
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double powerIn = ((countIn % 2 == 1) ?  BALL_COLLECT_POWER : 0);
		double powerOut = ((countOut % 2 == 1) ?  -BALL_COLLECT_POWER : 0);
		Robot.ballCollector.rawCollect(in ? powerIn : powerOut);
		

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}

 