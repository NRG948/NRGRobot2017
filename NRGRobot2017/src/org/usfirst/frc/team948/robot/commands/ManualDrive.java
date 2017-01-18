package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.command.Command;

public class ManualDrive extends Command {

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void execute() {
		double leftJoystick = Robot.oi.leftJoystick.getY();
		double rightJoystick = Robot.oi.rightJoystick.getY();
	}

	@Override
	protected void end() {
		Drive.rawStop();
		Drive.setDesiredHeadingFromGyro(); 
	}

	@Override
	protected void interrupted() {
		end();
	}
	

}
