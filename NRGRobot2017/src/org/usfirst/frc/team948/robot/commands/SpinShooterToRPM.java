package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class SpinShooterToRPM extends Command {
	public SpinShooterToRPM() {
		requires(Robot.shooter);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.shooter.rampToRPMinit();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double targetRPM = getTargetRPM();
		Robot.shooter.rampToRPM(targetRPM);
	}

	public static double getTargetRPM() {
		return (OI.leftJoystick.getZ() + 1) * 10 + 2900;// 2700 - 3300 RPM.
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
