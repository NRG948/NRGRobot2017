package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.commands.FeedBall;
import org.usfirst.frc.team948.robot.commands.SpinShooterToRPM;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Starts shooting automatically after the shooter wheel hits the target RPM.
 */
public class ShootSequence extends CommandGroup {
	public ShootSequence() {
		addParallel(new SpinShooterToRPM());
		// addParallel(new Shoot());
		addSequential(new FeedBall());
	}
}
