package org.usfirst.frc.team948.robot.commands;

import java.util.Timer;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class FeedBall extends Command {
	private static final double DEFAULT_CONVEYOR_POWER = 0.5;
	private boolean waitForRPM;
	private double conveyorPower;
	private double startTime;

	public FeedBall(double conveyorPower, boolean waitForRPM) {
		this.waitForRPM = waitForRPM;
		this.conveyorPower = conveyorPower;
		requires(Robot.ballFeeder);
	}
	
	public FeedBall() {
		this(DEFAULT_CONVEYOR_POWER, true);
	}

	@Override
	protected void initialize() {
		// delay
		startTime = System.currentTimeMillis() + 1000;
	}

	@Override
	protected void execute() {
		if (!waitForRPM || Robot.shooter.onTargetRPM()) {
			// if (!waitForRPM || startTime <= System.currentTimeMillis()) {
			Robot.ballFeeder.start(conveyorPower);
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
