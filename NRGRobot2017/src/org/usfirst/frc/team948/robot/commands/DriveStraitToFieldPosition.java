package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.MathUtil;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraitToFieldPosition extends Command {
	private static final double SLOW_DOWN_DISTANCE = 28;
	private static final double KDELTA = 180;
	private final double xTarget;
	private final double yTarget;
	private final boolean noChicken;
	private double currentTheta;
	private double thetaToTargetCurrent;
	private double xCurrent;
	private double yCurrent;
	private double distanceToTargetCurrent;
	private boolean cutOff = false;

	public DriveStraitToFieldPosition(double x, double y, boolean stopIfCutOff) {
		xTarget = x;
		yTarget = y;
		noChicken = stopIfCutOff;
		requires(Robot.drive);
	}
	
	protected void getFromTracker(){
		xCurrent = Robot.positionTracker.getX();
		yCurrent = Robot.positionTracker.getY();
		currentTheta = RobotMap.continuousGyro.getAngle();
		cutOff = Robot.positionTracker.objectInfront(true);
		thetaToTargetCurrent = Math.toDegrees(Math.atan2(xTarget - xCurrent, yTarget - yCurrent));
		distanceToTargetCurrent = Math.sqrt((xTarget - xCurrent) * (xTarget - xCurrent) + (yTarget - yCurrent) * (yTarget - yCurrent));
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.drive.driveOnHeadingInit(RobotMap.continuousGyro.getAngle());
		Robot.ledStrip.setGettingData(true);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		getFromTracker();
		if(noChicken && cutOff){
			System.out.println("Was cut off");
			end();
		}
		double normalizedHeadingDelta = (thetaToTargetCurrent - currentTheta)/180;
		double updatedHeading;
		if (distanceToTargetCurrent > 18 || Math.abs(normalizedHeadingDelta) > 0.1) {
			updatedHeading = RobotMap.continuousGyro.getAngle() + KDELTA * normalizedHeadingDelta;
			Robot.ledStrip.writeData(false);
		} else {
			Robot.ledStrip.writeData(true);
			updatedHeading = Robot.drive.getAutonomousHeading();
		}
		double distanceToSlow = RobotMap.preferences.getDouble("SLOW_DOWN_DISTANCE", SLOW_DOWN_DISTANCE);
		double power = MathUtil.clamp(distanceToTargetCurrent / distanceToSlow, 0.15, 1);
		Robot.drive.driveOnHeading(power, updatedHeading);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		// TODO Figure out correct distance
		return distanceToTargetCurrent < 12;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.ledStrip.setGettingData(false);
		Robot.drive.driveOnHeadingEnd();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		System.out.println("Field position drive was interrupted");
		end();
	}	
}
