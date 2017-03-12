package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.commands.FeedBall;
import org.usfirst.frc.team948.robot.commands.TestShooterRPM;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootSequence extends CommandGroup {
	public ShootSequence(){
		addParallel(new TestShooterRPM());
		addSequential(new FeedBall(.5,true));
	}
	
	
}
