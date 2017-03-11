package org.usfirst.frc.team948.robot.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team948.robot.commands.DelaySeconds;
import org.usfirst.frc.team948.robot.commands.FlipCameraLight;
import org.usfirst.frc.team948.robot.commands.VisionDriveToPeg;

public class PressToPeg extends CommandGroup {
	public PressToPeg(){
		addSequential (new FlipCameraLight(true));
		addSequential (new DelaySeconds(0.1));
		addSequential (new VisionDriveToPeg());
		addSequential (new FlipCameraLight(false));
	}
}
