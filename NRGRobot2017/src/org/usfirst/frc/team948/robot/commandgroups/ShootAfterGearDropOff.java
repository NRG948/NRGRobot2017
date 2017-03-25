package org.usfirst.frc.team948.robot.commandgroups;

import static org.usfirst.frc.team948.robot.subsystems.Drive.Direction.BACKWARD;

import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.FeedBalls;
import org.usfirst.frc.team948.robot.commands.SetInitPoz;
import org.usfirst.frc.team948.robot.commands.SpinShooterToRPM;
import org.usfirst.frc.team948.robot.commands.TurnToHeading;
import org.usfirst.frc.team948.robot.commands.WaitUntilGearDrop;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootAfterGearDropOff extends CommandGroup {
	private static final int SHOOTER_RPM_NEAR_PEG = 3100;
	
	public ShootAfterGearDropOff(double delayTime) {
		addSequential(new SetInitPoz(0, 0, 60)); // test parameters; remove later
		addParallel(new SpinShooterToRPM(SHOOTER_RPM_NEAR_PEG));
		addSequential(new WaitUntilGearDrop(delayTime));
		addSequential(new DriveStraightDistance(55, BACKWARD));
		addSequential(new TurnToHeading(165));
		addSequential(new FeedBalls());
	}
}
