package org.usfirst.frc.team948.utilities;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;

public class PositionTracker {
	private static final double percentForSignificance = 0.75;
	private static final double objectDetectionDistance = 3.0; // inches
	private static final int updatesBack = 10;
	private double x, y;
	private double prevLeftEncoder, prevRightEncoder;
	private double[] ultraSonicReadouts = new double[updatesBack];
	private int index = 0;

	
	// public static final double BLUE_LEFT_PEG_X = 128;
	// public static final double BLUE_LEFT_PEG_Y = 131;
	// public static final double BLUE_CENTER_PEG_X = 159;
	// public static final double BLUE_CENTER_PEG_Y = 113;

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
		x = position.x - Robot.ROBOT_LENGTH / 2 * Math.sqrt(3) / 2;
		y = position.y - Robot.ROBOT_LENGTH / 2 / 2;
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
	public synchronized void reset() {
		prevLeftEncoder = 0;
		prevRightEncoder = 0;
		x = 0;
		y = 0;
	}
}
