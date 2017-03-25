package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class StopSuckingBalls extends Command {

	public StopSuckingBalls() {
		requires(Robot.ballFeeder);
	}

	@Override
	protected void initialize() {
		Robot.ballFeeder.stop();
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
