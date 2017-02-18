package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * Robot drives straight using autonomousHeading.
 * To go forward use positive distance and positive power.
 * To go backwards use positive distance and negative power.
 * 
 * autonomousHeading() not working, so switched back to continuousGyro.getHeading()
 */
public class DriveStraightDistance extends Command {
	private double power;
	private double distance;

	private double encoderLeftStart;
	private double encoderRightStart;

	private double ticksToTravel;
	private double ticksTraveled;
	private double desiredHeading;

	public DriveStraightDistance(double distance, double power) {
		this.power = power;
		this.distance = Math.abs(distance);
		requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		ticksToTravel = Robot.drive.getTicksFromFeet(distance);
		encoderLeftStart = RobotMap.leftEncoder.get();
		encoderRightStart = RobotMap.rightEncoder.get();
		desiredHeading = Robot.drive.getAutonomousHeading();
		Robot.drive.driveOnHeadingInit(desiredHeading);
	}

	@Override
	protected void execute() {
		double leftTicks = Math.abs(RobotMap.leftEncoder.get() - encoderLeftStart);
		double rightTicks = Math.abs(RobotMap.rightEncoder.get() - encoderRightStart);
//		ticksTraveled = Math.max(leftTicks, rightTicks);
		ticksTraveled = (leftTicks + rightTicks) / 2;
		SmartDashboard.putNumber("DriveStraightDistance distance traveled", ticksTraveled / Robot.drive.getTicksPerFoot());
		double currentPower = power * Math.min(1, 2 * (ticksToTravel - ticksTraveled) / Robot.drive.getTicksPerFoot());
		Robot.drive.driveOnHeading(currentPower, desiredHeading);
	}

	@Override
	protected boolean isFinished() {
		double ticksRemaining = Math.abs(ticksTraveled - ticksToTravel);
		boolean isDone = ticksRemaining <= Robot.drive.getTicksPerFootTolerance();
		return isDone;
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