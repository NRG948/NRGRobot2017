package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

/**
 *
 */
public class TurnToBoiler extends TurnToHeading {

	public TurnToBoiler() {
		super(0);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		desiredHeading = Robot.positionTracker.getTurnAngleToBoiler() + RobotMap.continuousGyro.getAngle();
		Robot.drive.turnToHeadingInitNoPID(desiredHeading);
	}
}
