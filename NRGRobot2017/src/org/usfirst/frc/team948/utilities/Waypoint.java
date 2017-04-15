package org.usfirst.frc.team948.utilities;

import org.usfirst.frc.team948.robot.Robot;

public class Waypoint {
	private double x, y, power;
	private WaypointPredicate waypointPredicate;

	public Waypoint(double x, double y, double power, WaypointPredicate waypointPredicate) {
		this.x = x;
		this.y = y;
		this.power = power;
		this.waypointPredicate = waypointPredicate;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getPower() {
		return power;
	}

	public boolean isFinished() {
		return waypointPredicate.isAtWaypoint(this);
	}
	
	public static class GreaterThanY implements WaypointPredicate {
		@Override
		public boolean isAtWaypoint(Waypoint w) {
			return Robot.positionTracker.getY() > w.getY();
		}
	}
	
	public static class WithinInches implements WaypointPredicate {
		private double tolerance;

		public WithinInches(double tolerance) {
			this.tolerance = tolerance;
		}
		
		@Override
		public boolean isAtWaypoint(Waypoint w) {
			double dX = Robot.positionTracker.getX() - w.getX();
			double dY = Robot.positionTracker.getY() - w.getY();
			
			return dX * dX + dY * dY <= tolerance * tolerance;
		}
	}
}
