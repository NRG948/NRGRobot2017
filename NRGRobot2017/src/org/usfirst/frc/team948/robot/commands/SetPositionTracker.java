package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.Robot.AutoPosition;
import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class SetPositionTracker extends Command {
	private AutoPosition position;

	public SetPositionTracker(Robot.AutoPosition position) {
		this.position = position;
	}

	@Override
	protected void execute() {
		Robot.positionTracker.setXY(position.x, position.y);
		RobotMap.continuousGyro.setHeadingOffset(position.initialHeading);
	}

	@Override
	protected boolean isFinished() {
		return true;
	}
}
