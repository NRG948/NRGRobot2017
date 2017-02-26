package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TurnToPegCenter extends Command {

	private double turnAngle;
	private double power;
	public TurnToPegCenter() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {

		if (Robot.visionProcessor.dataExists()) {
			turnAngle = Robot.visionProcessor.getData().theta*180/Math.PI;
		} else {
			turnAngle = 0;
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		new CommandGroup(){
			{
				addSequential(new Turn(turnAngle, RobotMap.preferences.getDouble(PreferenceKeys.AUTONOMOUS_TURN_POWER, 0.5)), 0.5);
			}
		}.start();
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
