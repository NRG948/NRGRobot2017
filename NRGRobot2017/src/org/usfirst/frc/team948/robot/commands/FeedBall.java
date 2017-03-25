package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class FeedBall extends Command {
	private static final double DEFAULT_CONVEYOR_POWER = 0.5;
	private boolean waitForRPM;
	private double conveyorPower;

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
	}

	@Override
	protected void execute() {
		if (!waitForRPM || Robot.shooter.onTargetRPM()) {
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
