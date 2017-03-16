package org.usfirst.frc.team948.robot.commands;

import java.util.Timer;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class FeedBall extends Command {
	private boolean waitForRPM;
	private double power;
	private double startTime;

	public FeedBall(double power, boolean waitForRPM) {
		this.waitForRPM = waitForRPM;
		this.power = power;
		requires(Robot.ballFeeder);
	}

	@Override
	protected void initialize() {
		// delay
		startTime = System.currentTimeMillis() + 1000;
	}

	@Override
	protected void execute() {
		// if(!waitForRPM || Robot.shooter.onTargetRPM()){
		if (!waitForRPM || startTime <= System.currentTimeMillis()) {
			Robot.ballFeeder.start(power);
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
