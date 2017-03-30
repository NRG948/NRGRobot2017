package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class SpinShooterGivenRawPower extends Command{
	private double targetPower;

	public SpinShooterGivenRawPower() {
		requires(Robot.shooter);
	}
	public SpinShooterGivenRawPower(double power) {
		requires(Robot.shooter);
		targetPower= power;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.shooter.setPower(targetPower);
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
		end();
	}

}
