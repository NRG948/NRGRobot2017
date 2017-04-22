package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class FeedBalls extends Command {
	private boolean waitForRPM;
	private double timeoutSeconds;
	private Timer timer = new Timer();

	public FeedBalls(boolean waitForRPM, double timeoutSeconds) {
		this.waitForRPM = waitForRPM;
		this.timeoutSeconds = timeoutSeconds;
		requires(Robot.ballFeeder);
	}

	public FeedBalls(boolean waitForRPM) {
		this(waitForRPM, 15);
	}
	
	
	public FeedBalls() {
		this(true, 15);
	}

	@Override
	protected void initialize() {
		timer.start();
	}

	@Override
	protected void execute() {
		if (!waitForRPM || Robot.shooter.onTargetRPM()) {
			Robot.ballFeeder.openBallGate();
		}
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		if (timer.get() >= timeoutSeconds) {
			timer.stop();
			return true;
		}
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
