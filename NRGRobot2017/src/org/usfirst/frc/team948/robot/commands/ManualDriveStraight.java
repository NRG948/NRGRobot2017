package org.usfirst.frc.team948.robot.commands;
import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class ManualDriveStraight extends CommandBase {
	public ManualDriveStraight() {
		requires(drive);
	}

	@Override 
	protected void initialize() {
		drive.driveOnHeadingInit(RobotMap.navx.getAngle());
	} 
	
	@Override 
	protected void execute(){
		drive.driveOnHeading(OI.leftJoystick.getY());
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
