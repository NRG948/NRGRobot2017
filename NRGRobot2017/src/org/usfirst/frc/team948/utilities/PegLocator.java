package org.usfirst.frc.team948.utilities;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.Robot.PegPosition;

public class PegLocator {
	/*
	 * Assuming robot is at one of the pegs, return the location of the peg
	 */
	public PegPosition getPegPosition() {
		double heading = RobotMap.continuousGyro.getAngle();
		double sign = Math.signum(heading);
		heading = Math.abs(heading) % 360;

		if (heading > 180) {
			heading = sign * (heading - 360);
		} else {
			heading *= sign;
		}

		if (heading > 30) {
			return PegPosition.LEFT;
		} else if (heading > -30) {
			return PegPosition.CENTER;
		} else {
			return PegPosition.RIGHT;
		}
	}
}
