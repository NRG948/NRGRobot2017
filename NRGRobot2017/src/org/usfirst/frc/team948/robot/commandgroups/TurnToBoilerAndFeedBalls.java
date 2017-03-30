package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.commands.FeedBalls;
import org.usfirst.frc.team948.robot.commands.TurnToBoiler;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class TurnToBoilerAndFeedBalls extends CommandGroup {
	public TurnToBoilerAndFeedBalls() {
		addSequential(new TurnToBoiler());
		addSequential(new FeedBalls());
	}
}
