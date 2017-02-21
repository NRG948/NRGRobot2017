package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command which waits for gear to be taken out of robot.
 */
public class WaitUntilGearDrop extends Command {

	public WaitUntilGearDrop() {
		requires(Robot.drive);
	}

	protected boolean isFinished() {
		return RobotMap.gearSensor.get();
	}
}
