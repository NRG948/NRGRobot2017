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

	public static final double BLUE_BOILER_X = 6.045;
	public static final double BLUE_BOILER_Y = 6.3;

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

	@Override
	public String toString() {
		int xFeet = (int) (x / 12);
		int xInches = (int) (x - xFeet * 12);
		int yFeet = (int) (y / 12);
		int yInches = (int) (y - yFeet * 12);
		return xFeet + "'" + xInches + "\" , " + yFeet + "'" + yInches + "\"";

	}

	public double getTurnAngleToBlueBoiler() {
		double alpha = Math.toDegrees(Math.atan2(x - BLUE_BOILER_X, y - BLUE_BOILER_Y));
		double turn = (270 - (RobotMap.navx.getYaw() + alpha)) % 360;
		if (turn > 180) {
			turn = -(360 - turn);
		}
		// Distance from the center of the robot to the shooter r = 15 inches
		// Angle between the longitudinal axis on the robot and shooter theta0 =
		// 45.
		// Correction to the turn angle to align the shooter with the boiler
		// Correction gamma such that tan (gamma) = r sin(theta0 + gamma) / (d +
		// r cos(theta0 + gamma))
		double d = Math.sqrt((x - BLUE_BOILER_X) * (x - BLUE_BOILER_X) + (y - BLUE_BOILER_Y) * (y - BLUE_BOILER_Y));
		double gamma = 15 / d / Math.sin(Math.PI / 4);// works only if d>>15
		gamma *= 180 / Math.PI;
		return turn + gamma;
	}

	public synchronized void reset() {
		prevLeftEncoder = 0;
		prevRightEncoder = 0;
		x = 0;
		y = 0;
	}
}
