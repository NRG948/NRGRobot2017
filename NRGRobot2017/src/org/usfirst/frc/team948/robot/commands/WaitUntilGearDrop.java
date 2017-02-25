package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Command which waits for gear to be taken out of robot.
 */
public class WaitUntilGearDrop extends Command {
	private Timer timer;
	private double desiredDelayAfterGearDropped;
	private boolean timerStarted = false;

	public WaitUntilGearDrop(double seconds) {
		desiredDelayAfterGearDropped = seconds;
		requires(Robot.drive);
	}

	protected void initialize() {
		timer = new Timer();
	}

	protected boolean isFinished() {
		if (RobotMap.lowerGearSensor.get() && RobotMap.upperGearSensor.get()) {
			if (!timerStarted) {
				timerStarted = true;
				timer.start();
			}
		} else {
			timerStarted = false;
			timer.stop();
			timer.reset();
		}
		return timer.get() >= desiredDelayAfterGearDropped;
	}

	// Called once after isFinished returns true
	protected void end() {
		timer.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
