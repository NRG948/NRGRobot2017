package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.commands.DelaySeconds;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.ResetSensors;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousRoutines extends CommandGroup {
	Robot.AutoPosition autoPosition;

	public AutonomousRoutines(Robot.AutoPosition autoPosition) {
		this.autoPosition = autoPosition;
		
		addSequential(new ResetSensors());
		
		switch (this.autoPosition) {
			case POSITION_ONE:
				break;
			case POSITION_TWO:
				addSequential(new PositionTwo(0.5, 3));
				break;
			case POSITION_THREE:
				break;
			case POSITION_FOUR:
				break;
			case POSITION_FIVE:
				break;
			case POSITION_SIX:
				break;
			case POSITION_SEVEN:
				break;
		}
	}
	
	private class PositionTwo extends CommandGroup {
		private double power;
		private double delayTime;
		
		public PositionTwo(double power, double delayTime) {
			this.power = power;
			this.delayTime = delayTime;
			
			addSequential(new DriveStraightDistance(105 / 12.0, this.power));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(105 / 12.0, -this.power));
		}
	}
}
