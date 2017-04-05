package org.usfirst.frc.team948.robot.commandgroups;

import static org.usfirst.frc.team948.robot.subsystems.Drive.Direction.*;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.Robot.PegPosition;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.SpinShooterToCalculatedRPM;
import org.usfirst.frc.team948.robot.commands.SpinShooterToRPM;
import org.usfirst.frc.team948.robot.commands.TurnToBoiler;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootAfterGearDropOff extends CommandGroup {

	public ShootAfterGearDropOff() {
		addParallel(new SpinShooterToRPM(3000));
		PegPosition position = Robot.pegLocator.getPegPosition();
		
		switch (position) {
		case LEFT:
			if (DriverStation.getInstance().getAlliance() != Alliance.Blue)
			{
				return;
			}
			addSequential(new DriveStraightDistance(72, BACKWARD));
			break;
		case RIGHT:
			if (DriverStation.getInstance().getAlliance() != Alliance.Red)
			{
				return;
			}
			addSequential(new DriveStraightDistance(72, BACKWARD));
			break;
		case CENTER:
			addSequential(new DriveStraightDistance(24, BACKWARD));
			addSequential(new TurnToBoiler());
			addSequential(new DriveStraightDistance(60, FORWARD));
		}
		
		addParallel(new SpinShooterToCalculatedRPM());
		addSequential(new TurnToBoilerAndFeedBalls());
	}
}
