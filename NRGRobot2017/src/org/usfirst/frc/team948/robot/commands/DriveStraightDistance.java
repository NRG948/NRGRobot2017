package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightDistance extends Command {
	private double power;
	private double ticks;
	private double encoderLeftStart;
	private double encoderRightStart;
	private double ticksPerFoot;
	private double distance;
	private double ticksTraveled;
	private static final double TOLERANCE = 148;

	public DriveStraightDistance(double distance, double power) {
		this.power = power;
		this.distance = distance;
		requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		ticksPerFoot = RobotMap.preferences.getDouble(PreferenceKeys.ticksPerFoot, 1480);
		ticks = ticksPerFoot * distance;
		encoderLeftStart = RobotMap.leftEncoder.get();
		encoderRightStart = RobotMap.rightEncoder.get();
		Robot.drive.driveOnHeadingInit(RobotMap.continuousGyro.getAngle());
	}

	@Override
	protected void execute() {
		double leftTicks = Math.abs(RobotMap.leftEncoder.get() - encoderLeftStart);
		double rightTicks = Math.abs(RobotMap.rightEncoder.get() - encoderRightStart);
		ticksTraveled = Math.max(leftTicks, rightTicks);
		double currentPower = power * Math.min(1, 2*(ticks - ticksTraveled) / ticksPerFoot);
		Robot.drive.driveOnHeading(currentPower);
	}

	@Override
	protected boolean isFinished() {
		return Math.abs(ticksTraveled - ticks) <= TOLERANCE;
	}

	@Override
	protected void end() {
		Robot.drive.driveOnHeadingEnd();
	}

	@Override
	protected void interrupted() {
		end();
	}

}
