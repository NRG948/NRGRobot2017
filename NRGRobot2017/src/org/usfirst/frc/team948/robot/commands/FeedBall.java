package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class FeedBall extends Command {
	private boolean waitForRPM;

	public FeedBall(boolean waitForRPM) {
		this.waitForRPM = waitForRPM;
		requires(Robot.ballFeeder);
	}
	
	public FeedBall() {
		this(true);
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		if (!waitForRPM || Robot.shooter.onTargetRPM()) {
			Robot.ballFeeder.start();
		}
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void end() {
		Robot.ballFeeder.stop();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
