package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

//toggle light command
public class FlipCameraLight extends Command {
	private boolean turnOn = false;
	private boolean toggle;

	public FlipCameraLight() {
		toggle = true;
		requires(Robot.cameraLight);
	}

	public FlipCameraLight(boolean newValue) {
		toggle = false;
		turnOn = newValue;
		requires(Robot.cameraLight);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (toggle) {
			turnOn = !Robot.cameraLight.isOn();
		}
		if (turnOn) {
			Robot.cameraLight.turnOn();
		}
		else {
			Robot.cameraLight.turnOff();
		}
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
