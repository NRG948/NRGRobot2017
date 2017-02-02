package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.RobotMap;

public class ResetSensors extends CommandBase {
	public ResetSensors() {
	}

	protected void initialize() {

	}

	protected void execute() {
		RobotMap.leftEncoder.reset();
		RobotMap.rightEncoder.reset();
		RobotMap.navx.reset();

	}

	protected boolean isFinished() {
		return Math.abs(RobotMap.navx.getAngle()) < 0.5;

	}

	protected void end() {

	}

	protected void interrupted() {
	}

}

