package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.MathUtil;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraightToDistanceFromWall extends Command {
	private double power;
	private double distanceFromWall;
	//position 3 13ft 9-1/2 in
	

	private double encoderLeftStart;
	private double encoderRightStart;

	private double ticksToTravel;
	private double ticksTraveled;
	private double desiredHeading;
	
	public DriveStraightToDistanceFromWall(double power, double distanceFromWall) {
		requires(Robot.drive);
		this.power = power;
		this.distanceFromWall = distanceFromWall;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		double currentDistanceFromWall = MathUtil.clamp(RobotMap.ultraSound.getFeetFromUltrasoundVolts(), 0.91 /*10.93 in*/, 16.51 /*198.12 in*/);
		ticksToTravel = Robot.drive.getTicksFromInches(currentDistanceFromWall - distanceFromWall); //how far to move in terms of ticks
		encoderLeftStart = RobotMap.leftEncoder.get();
		encoderRightStart = RobotMap.rightEncoder.get();
		desiredHeading = Robot.drive.getAutonomousHeading();
		Robot.drive.driveOnHeadingInit(desiredHeading);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double leftTicks = Math.abs(RobotMap.leftEncoder.get() - encoderLeftStart);
		double rightTicks = Math.abs(RobotMap.rightEncoder.get() - encoderRightStart);
		ticksTraveled = Math.max(leftTicks, rightTicks);
		double currentPower = power * Math.min(1, 2 * (ticksToTravel - ticksTraveled) / Robot.drive.getTicksPerInch());
		Robot.drive.driveOnHeading(currentPower, desiredHeading);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Math.abs(ticksTraveled - ticksToTravel) <= Robot.drive.getDistanceToleranceInTicks();
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.driveOnHeadingEnd();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
