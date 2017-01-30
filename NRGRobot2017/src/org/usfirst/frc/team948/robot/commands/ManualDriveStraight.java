package org.usfirst.frc.team948.robot.commands;
import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

public class ManualDriveStraight extends CommandBase {
	private double heading;
	public ManualDriveStraight() {
		requires(Robot.drive);
	}

	@Override 
	protected void initialize() {
		Robot.drive.driveOnHeadingInit();
		heading = RobotMap.navx.getAngle();
	} 
	
	@Override 
	protected void execute(){
		Robot.drive.driveOnHeading(OI.leftJoystick.getY(), heading);
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
