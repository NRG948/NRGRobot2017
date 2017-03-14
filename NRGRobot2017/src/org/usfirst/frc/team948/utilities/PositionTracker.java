package org.usfirst.frc.team948.utilities;

import java.util.Timer;
import java.util.TimerTask;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;

public class PositionTracker {
	private double x, y;
	private double prevLeftEncoder, prevRightEncoder;
	private Timer timer;

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
//		double distance = ((Math.abs(leftDelta) > Math.abs(rightDelta)) ? leftDelta : rightDelta)
//				/ Robot.drive.getTicksPerInch();
		double distance = (leftDelta + rightDelta) / 2 / Robot.drive.getTicksPerInch();
		double heading = RobotMap.continuousGyro.getAngle() * Math.PI / 180;
		x += distance * Math.sin(heading);
		y += distance * Math.cos(heading);
		prevLeftEncoder = leftEncoder;
		prevRightEncoder = rightEncoder;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void start() {
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				updatePosition();
			}

		}, 0, 50);
	}

	@Override
	public String toString() {
		int xFeet = (int) (x / 12);
		int xInches = (int) (x - xFeet * 12);
		int yFeet = (int) (y / 12);
		int yInches = (int) (y - yFeet * 12);
		return xFeet + "'" + xInches + "\" , " + yFeet + "'" + yInches + "\"";

	}

	public synchronized void reset() {
		prevLeftEncoder = 0;
		prevRightEncoder = 0;
		x = 0;
		y = 0;
	}
}
