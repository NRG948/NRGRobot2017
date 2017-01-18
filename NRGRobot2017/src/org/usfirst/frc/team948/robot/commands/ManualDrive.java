package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.command.Command;

public class ManualDrive extends Command {
	public ManualDrive(){
		this.requires(Robot.drive);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void execute() {
		double leftJoystick = Robot.oi.leftJoystick.getY();
		double rightJoystick = Robot.oi.rightJoystick.getY();
		Robot.drive.tankDrive(leftJoystick, rightJoystick);
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
