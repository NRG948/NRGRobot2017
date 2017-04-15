package org.usfirst.frc.team948.robot.commands;

import java.util.ArrayList;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.utilities.Waypoint;

import edu.wpi.first.wpilibj.command.Command;

public class FollowWaypoints extends Command {
	private int waypointIndex;
	private double targetX, targetY;
	Waypoint currentWaypoint;
	private ArrayList<Waypoint> waypoints;
	
	public FollowWaypoints(ArrayList<Waypoint> waypoints) {
		this.waypoints = waypoints;
		this.waypointIndex = -1;
	}
	
	private void initializeNextWaypoint()
	{
		waypointIndex++;
		if (waypointIndex < waypoints.size()) {
			currentWaypoint = waypoints.get(waypointIndex);
			targetX += currentWaypoint.getX();
			targetY += currentWaypoint.getY();
		}
	}
	
	public void initialize() {
		targetX = Robot.positionTracker.getX();
		targetY = Robot.positionTracker.getY();
		initializeNextWaypoint();
		Robot.drive.driveOnHeadingInit();
	}
	
	public void execute() {
		if (currentWaypoint != null) {
			double dX = targetX - Robot.positionTracker.getX();
			double dY = targetY - Robot.positionTracker.getY();
			double heading = Math.toDegrees(Math.atan2(dX, dY));
			Robot.drive.driveOnHeading(currentWaypoint.getPower(), heading);
		}
	}
	
	@Override
	protected boolean isFinished() {
		if (currentWaypoint != null && currentWaypoint.isFinished()) {
			initializeNextWaypoint();
		}
		return waypointIndex >= waypoints.size();
	}
	
	protected void end() {
		Robot.drive.driveOnHeadingEnd();
	}
	
	protected void interrupted() {
		end();
	}
}
