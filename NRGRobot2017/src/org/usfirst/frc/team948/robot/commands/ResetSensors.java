package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class ResetSensors extends Command {
	public ResetSensors() {
	}

	protected void initialize() {

	}

	protected void execute() {
		Robot.drive.setAutonomousHeading(0);
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

