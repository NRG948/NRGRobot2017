package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ManualDrive extends Command {
	public ManualDrive() {
		this.requires(Robot.drive);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void execute() {
		double leftJoystick = OI.leftJoystick.getY();
		double rightJoystick = OI.rightJoystick.getY();
		Robot.drive.tankDrive(-leftJoystick, -rightJoystick);
	}

	@Override
	protected void end() {
		Robot.drive.stop();
	}

	@Override
	protected void interrupted() {
		end();
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub

	}

}
