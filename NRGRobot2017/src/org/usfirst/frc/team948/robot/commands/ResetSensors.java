package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class ResetSensors extends Command {
	public ResetSensors() {
		requires(Robot.drive);
	}

	protected void execute() {
		Robot.drive.setAutonomousHeading(0);
		RobotMap.leftEncoder.reset();
		RobotMap.rightEncoder.reset();
		Robot.positionTracker.reset();
		RobotMap.navx.reset();
		RobotMap.continuousGyro.navx.setAngleAdjustment(0);
		RobotMap.pdp.clearStickyFaults();
		RobotMap.shooterEncoder.reset();
	}

	protected boolean isFinished() {
		return Math.abs(RobotMap.navx.getAngle()) < 0.5;
	}
}
