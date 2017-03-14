package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.subsystems.Drive.Direction;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveToXY extends Command {

	@Override
	public void execute() {
		double x2 = RobotMap.preferences.getDouble(PreferenceKeys.X2, 0.0);
		double y2 = RobotMap.preferences.getDouble(PreferenceKeys.Y2, 0.0);
		double x1 = Robot.positionTracker.getX();
		double y1 = Robot.positionTracker.getY();
		double theta = Math.atan2(x2 - x1, y2 - y1) * 180 / Math.PI;
		double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		SmartDashboard.putString("DriveToXY: heading, distance", String.format("%.2f, %.2f", theta, distance));
		new CommandGroup() {
			{
				addSequential(new TurnToHeading(theta));
				addSequential(new DriveStraightDistance(distance, Direction.FORWARD));
			}
		}.start();
	}

	@Override
	protected boolean isFinished() {
		return true;
	}
}
