package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShiftGears extends Command {
	private boolean highGear;
	
    public ShiftGears(boolean highGear) {
    	this.highGear = highGear;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drive.changeGearTracker(highGear);
    	RobotMap.solenoid.set(highGear ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
