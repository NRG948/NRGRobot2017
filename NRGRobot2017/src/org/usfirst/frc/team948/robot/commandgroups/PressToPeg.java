package org.usfirst.frc.team948.robot.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team948.robot.commands.DelaySeconds;
import org.usfirst.frc.team948.robot.commands.FlipCameraLight;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.VisionDriveToPeg;

public class PressToPeg extends CommandGroup {
	public PressToPeg(){
		//switch to low gear to prevent crashing into the airship.
		addSequential (new ShiftGears(false));
		addSequential (new FlipCameraLight(true));
		addSequential (new DelaySeconds(0.1));
		addSequential (new VisionDriveToPeg());
		addSequential (new FlipCameraLight(false));
	}
}
