package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.Turn;
import org.usfirst.frc.team948.robot.commands.TurnToHeading;
import org.usfirst.frc.team948.utilities.visionProc;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class simpleVisionRoutine extends CommandGroup {
	double[] a = new double[5];
	public simpleVisionRoutine(visionProc proc){
		if(proc.dataExists()){
			a = proc.getData();
			addSequential(new ResetSensors());
			addSequential(new ShiftGears(false));
			if(a[2] != 0){
				addSequential(new Turn(90.0*a[2], 0.5));
				addSequential(new DriveStraightDistance(Math.abs(a[3])/12,0.5));
				addSequential(new Turn(-90.0*a[2],0.5));
			}
			addSequential(new DriveStraightDistance(Math.abs(a[1]/2),0.5));
		}
	}
}
