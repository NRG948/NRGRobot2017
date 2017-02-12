package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.commands.DelaySeconds;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.TurnToHeading;

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
				addSequential(new PositionSeven());
				break;
		}
	}
	private class PositionOne extends CommandGroup {
		private double power;
		private double delayTime;
		
		public PositionOne(double power, double delayTime) {
			this.power = power;
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(98.82 / 12.0, this.power));
			addSequential(new TurnToHeading(60 , this.power));
			addSequential(new DriveStraightDistance(51.932 / 12.0, this.power));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(51.932 / 12.0, -this.power));
			addSequential(new TurnToHeading(-90 , this.power));
			addSequential(new DriveStraightDistance(50 / 12, this.power));
			addSequential(new TurnToHeading(0, this.power));
			addSequential(new DriveStraightDistance(325.28 / 12.0, this.power));
		}
	}
	private class PositionTwo extends CommandGroup {
		private double power;
		private double delayTime;
		
		public PositionTwo(double power, double delayTime) {
			this.power = power;
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(105 / 12.0, this.power));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(7 / 12.0, -this.power));
			addSequential(new TurnToHeading(-90 , this.power));
			addSequential(new DriveStraightDistance(136.875 / 12.0 , this.power));
			addSequential(new TurnToHeading(0, this.power));
			addSequential(new DriveStraightDistance(326 / 12.0, this.power));
		}
	}
	private class PositionThree extends CommandGroup {
		private double power;
		private double delayTime;
		
		public PositionThree(double power, double delayTime) {
			this.power = power;
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(98.82 / 12.0, this.power));
			addSequential(new TurnToHeading(-60 , this.power ));
			addSequential(new DriveStraightDistance(51.932 / 12.0, this.power));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(51.932 / 12.0, -this.power));
			addSequential(new TurnToHeading(0 , this.power));
			addSequential(new DriveStraightDistance(88.6 / 12.0 , this.power));
			addSequential(new TurnToHeading(-43.54 , this.power));
			addSequential(new DriveStraightDistance( 327.563 /12.0, this.power));
			addSequential(new TurnToHeading(0, this.power));
		}
	}
	private class PositionFour extends CommandGroup {
		private double power;
		private double delayTime;
		
		public PositionFour(double power, double delayTime) {
			this.power = power;
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(98.82 / 12.0, this.power));
			addSequential(new TurnToHeading(-60 , this.power ));
			addSequential(new DriveStraightDistance(51.932 / 12.0, this.power));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(51.932 / 12.0, -this.power));
			addSequential(new TurnToHeading(0 , this.power));
			addSequential(new DriveStraightDistance(88.6 / 12.0 , this.power));
			addSequential(new TurnToHeading(-43.54 , this.power));
			addSequential(new DriveStraightDistance( 327.563 /12.0, this.power));
			addSequential(new TurnToHeading(0, this.power));
		}
	}
	private class PositionFive extends CommandGroup {
		private double power;
		private double delayTime;
		
		public PositionFive(double power, double delayTime) {
			this.power = power;
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(105 / 12.0, this.power));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(7 / 12.0, -this.power));
			addSequential(new TurnToHeading(90 , this.power));
			addSequential(new DriveStraightDistance(136.875 / 12.0, this.power));
			addSequential(new TurnToHeading( 0 , this.power));
			addSequential(new DriveStraightDistance(326 / 12.0, this.power ));
			
		}
	}
	private class PositionSix extends CommandGroup {
		private double power;
		private double delayTime;
		
		public PositionSix(double power, double delayTime) {
			this.power = power;
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(98.82 / 12.0, this.power));
			addSequential(new TurnToHeading(-60 , this.power ));
			addSequential(new DriveStraightDistance(51.932 / 12.0, this.power));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(51.932 / 12.0, -this.power));
			addSequential(new TurnToHeading(0 , this.power));
			addSequential(new DriveStraightDistance(325.28 / 12.0, this.power));
		}
	}
	
	private class PositionSeven extends CommandGroup {
	
	}
}
