package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

public class SpinShooterToCalculatedRPM extends SpinShooterToRPM {
	public SpinShooterToCalculatedRPM() {
		super(0);
	}

	protected void initialize() {
		targetRPM = Robot.shooterCalculator.getShooterRPM();
		Robot.shooter.rampToRPMinit();
	}
}
