package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CommandBase extends Command {
	public static Drive drive = Robot.drive;

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
}
