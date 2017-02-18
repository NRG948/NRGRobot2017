package org.usfirst.frc.team948.robot.commands;
import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class ManualDriveStraight extends Command {
	private double desiredHeading;
	public ManualDriveStraight() {
		requires(Robot.drive);
	}

	@Override 
	protected void initialize() {
		desiredHeading = RobotMap.continuousGyro.getAngle();
		Robot.drive.driveOnHeadingInit(desiredHeading);
	} 
	
	@Override 
	protected void execute(){
		Robot.drive.driveOnHeading(-OI.leftJoystick.getY(), desiredHeading);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override 
	protected void end() {
		Robot.drive.driveOnHeadingEnd();
	}
	
	@Override 
	protected void interrupted() {
		end(); 
	}
}
