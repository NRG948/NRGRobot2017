package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.commands.FeedBall;
import org.usfirst.frc.team948.robot.commands.Shoot;
import org.usfirst.frc.team948.robot.commands.SpinShooterToRPM;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootSequence extends CommandGroup {
	public ShootSequence(){
		//addParallel(new SpinShooterToRPM()); encoder not working
		addParallel(new Shoot());
		addSequential(new FeedBall(0.5, true));
	}
	
	
}
