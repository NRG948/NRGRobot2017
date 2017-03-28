package org.usfirst.frc.team948.robot.commandgroups;

import static org.usfirst.frc.team948.robot.subsystems.Drive.Direction.BACKWARD;

import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.FeedBalls;
import org.usfirst.frc.team948.robot.commands.SpinShooterToRPM;
import org.usfirst.frc.team948.robot.commands.TurnToBoiler;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootAfterGearDropOff extends CommandGroup {

	public ShootAfterGearDropOff() {
		addParallel(new SpinShooterToRPM(3000));// 

		addSequential(new DriveStraightDistance(72, BACKWARD));
		addParallel(new SpinShooterToRPM(true));// true means use position
												// tracker.
		addSequential(new TurnToBoiler());// true means use position
												// tracker.
		addSequential(new FeedBalls());

	}
}
