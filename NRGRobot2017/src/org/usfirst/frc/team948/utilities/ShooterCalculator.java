package org.usfirst.frc.team948.utilities;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;

public class ShooterCalculator {
	public static final double G = 32.2 * 12;
	public static final double BLUE_BOILER_X = 8.75; // 6.045;
	public static final double BLUE_BOILER_Y = 8.75; // 6.3;
	public static final double RED_BOILER_X = Robot.FIELD_WIDTH - 8.75; // 6.045;
	public static final double RED_BOILER_Y = 8.75; // 6.3;

	private static final double SHOOTER_TO_CENTER_ANGLE = 38.4;// Math.toDegrees(Math.atan(10.5/13.25))
	private static final double DISTANCE_ROBOT_CENTER_TO_SHOOTER = 16.91;// Math.sqrt((13.25^2)+(10.5^2))
	public static final double SHOOTER_HEIGHT = 24;
	public static final double BOILER_HEIGHT = 97;
	public static final double SHOOTER_WHEEL_RADIUS = 2;

	public double getTurnAngleToBoiler() {
		double x = Robot.positionTracker.getX();
		double y = Robot.positionTracker.getY();
		double boilerX, boilerY;
		if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue) {
			boilerX = BLUE_BOILER_X;
			boilerY = BLUE_BOILER_Y;

		} else {
			boilerX = RED_BOILER_X;
			boilerY = RED_BOILER_Y;
		}
		// TODO code is for Blue Boiler. Red Boiler needs to be implemented.
		// Distance from the center of the robot to the shooter r
		// inches
		// Angle between the longitudinal axis on the robot and shooter
		// theta0
		// Correction to the turn angle to align the shooter with the boiler
		// Correction gamma such that tan (gamma) = r sin(theta0 + gamma) /
		// (d +
		// r cos(theta0 + gamma))
		double gyro = RobotMap.continuousGyro.getAngle();
		double alpha = Math.toDegrees(Math.atan2(y - boilerY, x - boilerX));
		double turn = (270 - (gyro + alpha)) % 360;
		if (turn > 180) {
			turn = -(360 - turn);
		}
		double d = Math.sqrt((x - boilerX) * (x - boilerX) + (y - boilerY) * (y - boilerY));
		double gamma = DISTANCE_ROBOT_CENTER_TO_SHOOTER / d * Math.sin(Math.toRadians(SHOOTER_TO_CENTER_ANGLE));
		// only if d>>r
		gamma = Math.toDegrees(gamma);

		return turn + gamma;
	}

	public double getShooterRPM() {
		double x = Robot.positionTracker.getX();
		double y = Robot.positionTracker.getY();
		double boilerX, boilerY;
		if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue) {
			boilerX = BLUE_BOILER_X;
			boilerY = BLUE_BOILER_Y;

		} else {
			boilerX = RED_BOILER_X;
			boilerY = RED_BOILER_Y;
		}
		double d = Math.sqrt((x - boilerX) * (x - boilerX) + (y - boilerY) * (y - boilerY));
		double shooterAngle = Math.toRadians(16); // assumes angle is 16 (fixed
													// angle)
		double exitVelocity = (d / Math.sin(shooterAngle)
				* Math.sqrt(G / 2 / (d / Math.tan(shooterAngle) + SHOOTER_HEIGHT - BOILER_HEIGHT)));
		double rpm = 60 / (2 * Math.PI) * exitVelocity / (SHOOTER_WHEEL_RADIUS / 2);
		return rpm * 1.1;// increase by 10 %

	}

}
