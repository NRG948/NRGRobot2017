package org.usfirst.frc.team948.robot.commandgroups;

import static org.usfirst.frc.team948.robot.subsystems.Drive.Direction.BACKWARD;

import org.usfirst.frc.team948.robot.Robot.PegPosition;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.SpinShooterToCalculatedRPM;
import org.usfirst.frc.team948.robot.commands.SpinShooterToRPM;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootAfterGearDropOff extends CommandGroup {

	public ShootAfterGearDropOff(PegPosition position) {
		double distance = 0;
		switch (position){
		case LEFT:
		case RIGHT:
			distance = 72;
			break;
		case CENTER:
			distance = 36;
			break;
		}
		addParallel(new SpinShooterToRPM(3000));
		addSequential(new DriveStraightDistance(distance, BACKWARD));
		
		addParallel(new SpinShooterToCalculatedRPM());
		addSequential(new TurnToBoilerAndFeedBalls());
	}
}
