package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.MathUtil;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DelaySeconds extends Command {
	private Timer timer;
	private double desiredSeconds;
	
	public DelaySeconds(double seconds) {
		this.desiredSeconds = seconds;
		
		timer = new Timer();
		timer.reset();
		timer.start();
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return timer.get() >= desiredSeconds;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
