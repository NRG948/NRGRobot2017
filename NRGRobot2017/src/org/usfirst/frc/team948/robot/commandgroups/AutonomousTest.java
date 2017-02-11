package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.TurnToHeading;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousTest extends CommandGroup {
	public AutonomousTest(){
		addSequential(new ResetSensors());
		addSequential(new DriveStraightDistance(5.0, 0.5));
		addSequential(new TurnToHeading(180.0, 1.0));
		addSequential(new DriveStraightDistance(5.0, 0.5));
		addSequential(new TurnToHeading(0.0, 1.0));
	}
}
