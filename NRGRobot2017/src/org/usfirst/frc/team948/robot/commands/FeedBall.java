package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class FeedBall extends Command {
	private boolean waitForRPM;
	private double power;
	
	public FeedBall(double power, boolean waitForRPM) {
		this.waitForRPM = waitForRPM;
		this.power = power;
		requires(Robot.ballFeeder);
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		if(!waitForRPM || Robot.shooter.onTargetRPM()){
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
