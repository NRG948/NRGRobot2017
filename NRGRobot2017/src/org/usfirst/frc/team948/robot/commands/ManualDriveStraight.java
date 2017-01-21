package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.subsystems.Drive;
import org.usfirst.frc.team948.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ManualDriveStraight extends Command {
private double heading;
    public ManualDriveStraight() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//add drive functionality
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drive.rawStop();
    	Robot.drive.setHeading(heading);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
