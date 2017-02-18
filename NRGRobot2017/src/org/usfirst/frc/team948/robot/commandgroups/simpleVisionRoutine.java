package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.Turn;
import org.usfirst.frc.team948.utilities.visionField;
import org.usfirst.frc.team948.utilities.visionProc;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class simpleVisionRoutine extends CommandGroup {
	visionField a;
	public simpleVisionRoutine(visionProc proc){
		if(proc.dataExists()){
			a = proc.getData();
			addSequential(new ResetSensors());
			addSequential(new ShiftGears(false));
			if(a.zeta != 0){
				addSequential(new Turn(90.0*a.zeta, 0.5));
				addSequential(new DriveStraightDistance(Math.abs(a.zeta)/12,0.5));
				addSequential(new Turn(-90.0*a.zeta,0.5));
			}
			addSequential(new DriveStraightDistance(Math.abs(a.v/2),0.5));
		}
	}
}
