package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.Robot.PegPosition;

import edu.wpi.first.wpilibj.command.Command;

public class SetPositionTracker extends Command {
	private PegPosition position;

	public SetPositionTracker(Robot.PegPosition position) {
		this.position = position;
	}

	@Override
	protected void execute() {
		Robot.positionTracker.setAtPeg(position);
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
