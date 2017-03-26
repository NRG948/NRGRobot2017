package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.subsystems.Drive;
import org.usfirst.frc.team948.robot.subsystems.Drive.Direction;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Robot drives straight using autonomousHeading. To go forward use positive
 * distance and positive power. To go backwards use positive distance and
 * negative power.
 * 
 * autonomousHeading() not working, so switched back to
 * continuousGyro.getHeading()
 */
public class DriveStraightDistance extends Command {
	private final double DEFAULT_AUTONOMOUS_POWER = 0.5;

	private double power;
	private double distance;
	private double toleranceInInches;

	private double encoderLeftStart;
	private double encoderRightStart;

	private double ticksToTravel;
	private double ticksTraveled;
	private double desiredHeading;

	private final double SLOW_DOWN_DISTANCE = 6.0;
	private Direction direction;
	private int cyclesWithinTolerance;

	public DriveStraightDistance(double distance, Drive.Direction direction, double power) {
		this.direction = direction;
		this.power = (direction == Drive.Direction.FORWARD) ? Math.abs(power) : -Math.abs(power);
		this.distance = Math.abs(distance);
		requires(Robot.drive);
	}

	public DriveStraightDistance(double distance, Drive.Direction direction) {
		this(distance, direction, 0.0);
	}

	@Override
	protected void initialize() {
		ticksToTravel = Robot.drive.getTicksFromInches(distance);
		encoderLeftStart = RobotMap.leftEncoder.get();
		encoderRightStart = RobotMap.rightEncoder.get();
		desiredHeading = Robot.drive.getAutonomousHeading();
		toleranceInInches = Robot.drive.getDistanceToleranceInTicks() / Robot.drive.getTicksPerInch();

		if (power == 0.0) {
			String key = Robot.gearbox.isHighGear() ? PreferenceKeys.AUTONOMOUS_HIGHGEAR_POWER
					: PreferenceKeys.AUTONOMOUS_LOWGEAR_POWER;
			power = Math.abs(RobotMap.preferences.getDouble(key, DEFAULT_AUTONOMOUS_POWER));
			if (direction == Drive.Direction.BACKWARD) {
				power = -power;
			}
		}
		Robot.drive.driveOnHeadingInit(desiredHeading);
		cyclesWithinTolerance = 0;
	}

	@Override
	protected void execute() {
		double leftTicks = Math.abs(RobotMap.leftEncoder.get() - encoderLeftStart);
		double rightTicks = Math.abs(RobotMap.rightEncoder.get() - encoderRightStart);
		// we don't average the two in case one encoder dies
		ticksTraveled = Math.max(leftTicks, rightTicks);
		SmartDashboard.putNumber("DriveStraightDistance distance traveled",
				ticksTraveled / Robot.drive.getTicksPerInch());
		double distanceRemaining = (ticksToTravel - ticksTraveled) / Robot.drive.getTicksPerInch();
		double slowDownDistance = Robot.gearbox.isHighGear() ? 3 * SLOW_DOWN_DISTANCE : SLOW_DOWN_DISTANCE;
		double currentPower = power * Math.min(1, distanceRemaining / slowDownDistance);
		if (Math.abs(distanceRemaining) < toleranceInInches) {
			cyclesWithinTolerance++;
			currentPower = 0.0;
		} else {
			if (Math.abs(distanceRemaining) < 3) {
				currentPower = 0.3 * Math.signum(distanceRemaining) * Math.signum(power);
			}
			cyclesWithinTolerance = 0;
		}
		// if we went too far reverse the motors.
		Robot.drive.driveOnHeading(currentPower, desiredHeading);
	}

	@Override
	protected boolean isFinished() {
		return cyclesWithinTolerance >= 5;
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