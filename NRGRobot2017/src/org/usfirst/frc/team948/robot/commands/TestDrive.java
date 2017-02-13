package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TestDrive extends Command {
	private double power;

	public TestDrive() {
		this.requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		this.power = OI.rightJoystick.getZ();
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("power for test",power);
		Robot.drive.tankDrive(power, power);
	}

	@Override
	protected void end() {
		Robot.drive.stop();
	}

	@Override
	protected void interrupted() {
		end();
	}

	protected boolean isFinished() {
		return false;
	}

}
