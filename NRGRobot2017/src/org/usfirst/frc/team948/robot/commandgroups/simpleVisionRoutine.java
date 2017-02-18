package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.commands.DelaySeconds;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.FlipCameraLight;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.Turn;
import org.usfirst.frc.team948.utilities.visionField;
import org.usfirst.frc.team948.utilities.visionProc;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class simpleVisionRoutine extends CommandGroup {
	public static final double scalar = 1/12.0;
	visionField a;
	public simpleVisionRoutine(visionProc proc){
		addSequential(new FlipCameraLight(true));
		addSequential(new DelaySeconds(0.1));
		if(proc.dataExists()){
			a = proc.getData();
			addSequential(new ShiftGears(false));
			if(a.zeta != 0){
				addSequential(new Turn(90.0*a.zeta, 0.5));
				addSequential(new DriveStraightDistance(Math.abs(a.zeta)*scalar,0.5));
				addSequential(new Turn(-90.0*a.zeta,0.5));
			}
			addSequential(new DriveStraightDistance(Math.abs(a.v)*scalar,0.5));
		}
		addSequential(new FlipCameraLight(false));
	}
}
