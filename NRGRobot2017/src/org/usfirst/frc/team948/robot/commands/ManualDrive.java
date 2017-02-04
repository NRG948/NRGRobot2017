package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ManualDrive extends Command {
	public ManualDrive() {
		this.requires(Robot.drive);
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void execute() {
		double leftJoystick = -OI.leftJoystick.getY();
		double rightJoystick = -OI.rightJoystick.getY();
		Robot.drive.tankDrive(leftJoystick, rightJoystick);
		
		SmartDashboard.putNumber("Left Joystick", leftJoystick);
		SmartDashboard.putNumber("Right Joystick", rightJoystick);
	}

	@Override
	protected void end() {
		Robot.drive.stop();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
