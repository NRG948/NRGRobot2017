package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Spins up the top shooter wheel to a given RPM setpoint (either fixed, or from
 * joystick).
 * 
 * Command only ends when interrupted.
 */
public class SpinShooterToRPM extends Command {

	protected double targetRPM;
	private boolean getRPMFromJoystick = false;

	public SpinShooterToRPM() {
		requires(Robot.shooter);
		getRPMFromJoystick = true;
	}

	public SpinShooterToRPM(double rpm) {
		requires(Robot.shooter);
		targetRPM = rpm;
	}

	public static double getTargetRPM() {
		return (OI.leftJoystick.getZ() + 1) * 50 + 2900; // 2900 - 2950 RPM
	}
	
	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.shooter.rampToRPMinit();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (getRPMFromJoystick) {
			targetRPM = getTargetRPM();
		}
		Robot.shooter.rampToRPM(targetRPM);
	}


	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.shooter.rampToRPMEnd();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
