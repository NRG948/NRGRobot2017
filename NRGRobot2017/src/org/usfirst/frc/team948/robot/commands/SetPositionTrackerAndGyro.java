package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.Robot.PegPosition;

import edu.wpi.first.wpilibj.command.Command;

public class SetPositionTrackerAndGyro extends Command {
	private PegPosition position;
	
	public SetPositionTrackerAndGyro(Robot.PegPosition position) {
		this.position = position;
	}

	@Override
	protected void execute() {
		RobotMap.continuousGyro.navx.reset();
		RobotMap.continuousGyro.navx.setAngleAdjustment(position.angle);
		Robot.positionTracker.setAtPeg(position);
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
