package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shoot extends Command {

	public Shoot() {
		this.requires(Robot.shooter);
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void execute() {
		double shooterPower = 0.5 + 0.25 * (OI.leftJoystick.getZ() + 1);
		Robot.shooter.setPower(shooterPower);
		SmartDashboard.putNumber("Shooter power", shooterPower);
	}

	@Override
	protected void end() {
		Robot.shooter.stop();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
