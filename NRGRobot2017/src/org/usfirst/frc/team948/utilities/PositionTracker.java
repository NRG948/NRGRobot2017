package org.usfirst.frc.team948.utilities;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

public class PositionTracker {
	private static final double percentForSignificance = 0.75;
	private static final double objectDetectionDistance = 3.0; // inches
	private static final int updatesBack = 10;
	private double x, y;
	private double prevLeftEncoder, prevRightEncoder;
	private double[] ultraSonicReadouts = new double[updatesBack];
	private int index = 0;

	public static final double G = 32.2 * 12;
	public static final double BLUE_BOILER_X = 8.75; // 6.045;
	public static final double BLUE_BOILER_Y = 8.75; // 6.3;
	private static final double SHOOTER_TO_CENTER_ANGLE = 38.4;// Math.toDegrees(Math.atan(10.5/13.25))
	private static final double DISTANCE_ROBOT_CENTER_TO_SHOOTER = 16.91;// Math.sqrt((13.25^2)+(10.5^2))
	public static final double SHOOTER_HEIGHT = 24;
	public static final double BOILER_HEIGHT = 97;
	public static final double SHOOTER_WHEEL_RADIUS = 2;
	public static final double BLUE_LEFT_PEG_X = 128;
	public static final double BLUE_LEFT_PEG_Y = 131;
	public static final double BLUE_CENTER_PEG_X = 159;
	public static final double BLUE_CENTER_PEG_Y = 113;
	public static final double ROBOT_LENGTH = 39; // with bumpers

	public void init(double x, double y) {
		this.x = x;
		this.y = y;
		prevLeftEncoder = RobotMap.leftEncoder.get();
		prevRightEncoder = RobotMap.rightEncoder.get();
	}

	public void updatePosition() {
		double leftEncoder = RobotMap.leftEncoder.get();
		double rightEncoder = RobotMap.rightEncoder.get();
		double leftDelta = leftEncoder - prevLeftEncoder;
		double rightDelta = rightEncoder - prevRightEncoder;
		// double distance = ((Math.abs(leftDelta) > Math.abs(rightDelta)) ?
		// leftDelta : rightDelta)
		// / Robot.drive.getTicksPerInch();
		double distance = (leftDelta + rightDelta) / 2 / Robot.drive.getTicksPerInch();
		double heading = RobotMap.continuousGyro.getAngle() * Math.PI / 180;
		x += distance * Math.sin(heading);
		y += distance * Math.cos(heading);
		prevLeftEncoder = leftEncoder;
		prevRightEncoder = rightEncoder;
		ultraSonicReadouts[index] = RobotMap.ultraSound.getVoltage();
		index++;
		index %= updatesBack;
	}

	public void setAtPeg(Robot.PegPosition position) {
		x = position.x - ROBOT_LENGTH / 2 * Math.sqrt(3) / 2;
		y = position.y - ROBOT_LENGTH / 2 / 2;
	}

	public void setXY(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public boolean objectInfront(boolean useAverageing) {
		if (useAverageing) {
			double agglutinatedNumber = 0.0;
			for (double a : ultraSonicReadouts) {
				agglutinatedNumber += RobotMap.ultraSound.getDistanceInches(a) <= objectDetectionDistance ? 1.0 : 0.0;
			}
			return agglutinatedNumber / updatesBack >= percentForSignificance;
		} else {
			return RobotMap.ultraSound
					.getDistanceInches(ultraSonicReadouts[updatesBack - 1]) <= objectDetectionDistance;
		}
	}

	public String toStringFeetInches() {
		int xFeet = (int) (x / 12);
		int xInches = (int) (x - xFeet * 12);
		int yFeet = (int) (y / 12);
		int yInches = (int) (y - yFeet * 12);
		return xFeet + "'" + xInches + "\" , " + yFeet + "'" + yInches + "\"";

	}

	@Override
	public String toString() {
		return String.format("x=%.2f, y=%.2f", x, y);
	}

	public double getTurnAngleToBoiler() {
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
		double alpha = Math.toDegrees(Math.atan2(y - BLUE_BOILER_Y, x - BLUE_BOILER_X));
		double turn = (270 - (gyro + alpha)) % 360;
		if (turn > 180) {
			turn = -(360 - turn);
		}
		double d = Math.sqrt((x - BLUE_BOILER_X) * (x - BLUE_BOILER_X) + (y - BLUE_BOILER_Y) * (y - BLUE_BOILER_Y));
		double gamma = DISTANCE_ROBOT_CENTER_TO_SHOOTER / d * Math.sin(Math.toRadians(SHOOTER_TO_CENTER_ANGLE));
		// only if d>>r
		gamma = Math.toDegrees(gamma);

		return turn + gamma;
	}

	public double getShooterRPM() {
		double d = Math.sqrt((x - BLUE_BOILER_X) * (x - BLUE_BOILER_X) + (y - BLUE_BOILER_Y) * (y - BLUE_BOILER_Y));
		double shooterAngle = Math.toRadians(16); // assumes angle is 16 (fixed
													// angle)
		double exitVelocity = (d / Math.sin(shooterAngle)
				* Math.sqrt(G / 2 / (d / Math.tan(shooterAngle) + SHOOTER_HEIGHT - BOILER_HEIGHT)));
		double rpm = 60 / (2 * Math.PI) * exitVelocity / (SHOOTER_WHEEL_RADIUS / 2);
		return rpm * 1.1;//increase by 10 %

	}

	public synchronized void reset() {
		prevLeftEncoder = 0;
		prevRightEncoder = 0;
		x = 0;
		y = 0;
	}
}
