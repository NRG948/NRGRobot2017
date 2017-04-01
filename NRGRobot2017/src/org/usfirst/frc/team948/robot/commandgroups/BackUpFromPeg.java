package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.Robot.PegPosition;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.subsystems.Drive.Direction;

import edu.wpi.first.wpilibj.DriverStation;

public class BackUpFromPeg extends DriveStraightDistance {

	public BackUpFromPeg() {
		super(0, Direction.BACKWARD);
	}

	@Override
	protected void initialize() {
		PegPosition pegPosition;
		if (DriverStation.getInstance().isAutonomous())
		{
			pegPosition = OI.getPegPosition();
		}
		else
		{
			pegPosition = Robot.pegLocator.getPegPosition();
			Robot.positionTracker.setAtPeg(pegPosition);
			RobotMap.continuousGyro.setAtPeg(pegPosition);
			
		}
		
		switch (pegPosition)
		{
			case LEFT:
			case RIGHT:
				distance = 72;
				break;
			case CENTER:
				distance = 36;
				break;
		}
		// letting the DriveStraightDistance command do the rest
		super.initialize();
	}
}
