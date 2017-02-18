package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.commands.DelaySeconds;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.SetAutonomousHeading;
import org.usfirst.frc.team948.robot.commands.TurnToHeading;
import org.usfirst.frc.team948.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousRoutines extends CommandGroup {
	Robot.AutoPosition autoPosition;

	public AutonomousRoutines(Robot.AutoPosition autoPosition) {
		this.autoPosition = autoPosition;
		
		addSequential(new ResetSensors());
		
		switch (this.autoPosition) {
			case POSITION_ONE:
				addSequential(new PositionOne(3));
				break;
			case POSITION_TWO:
				addSequential(new PositionTwo(3));
				break;
			case POSITION_THREE:
				addSequential(new PositionThree(3));
				break;
			case POSITION_FOUR:
				addSequential(new PositionFour(3));
				break;
			case POSITION_FIVE:
				addSequential(new PositionFive(3));
				break;
			case POSITION_SIX:
				addSequential(new PositionSix(3));
				break;
			case POSITION_SEVEN:
				addSequential(new PositionSeven());
				break;
		}
		
	}
	private class PositionOne extends CommandGroup {
		private double delayTime;
		
		public PositionOne(double delayTime) {
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(98.82 / 12.0, Drive.Direction.FORWARD));
			addSequential(new TurnToHeading(60));
			addSequential(new DriveStraightDistance(51.932 / 12.0, Drive.Direction.FORWARD));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(12.0 / 12.0, Drive.Direction.BACKWARD));
			addSequential(new SetAutonomousHeading(-90));
			addSequential(new DriveStraightDistance((51.932-12.0) / 12.0, Drive.Direction.BACKWARD));
			//addSequential(new DriveStraightDistance(51.932 / 12.0, Drive.Direction.BACKWARD));
			//addSequential(new TurnToHeading(-90 , Drive.Direction.FORWARD));
			addSequential(new DriveStraightDistance(50 / 12, Drive.Direction.FORWARD));
			addSequential(new TurnToHeading(0));
			addSequential(new DriveStraightDistance(325.28 /12, Drive.Direction.FORWARD));
		}
	}
	private class PositionTwo extends CommandGroup {
		private double power;
		private double delayTime;
		
		public PositionTwo(double delayTime) {
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(105 / 12.0, Drive.Direction.FORWARD));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(7 / 12.0, Drive.Direction.BACKWARD));
			addSequential(new TurnToHeading(-90));
			addSequential(new DriveStraightDistance(136.875 / 12.0 , Drive.Direction.FORWARD));
			addSequential(new TurnToHeading(0));
			addSequential(new DriveStraightDistance(326 / 12.0, Drive.Direction.FORWARD));
		}
	}
	private class PositionThree extends CommandGroup {
		private double power;
		private double delayTime;
		
		public PositionThree(double power) {
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(98.82 / 12.0, Drive.Direction.FORWARD));
			addSequential(new TurnToHeading(-60));
			addSequential(new DriveStraightDistance(51.932 / 12.0, Drive.Direction.FORWARD));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(51.932 / 12.0, Drive.Direction.BACKWARD));
			addSequential(new TurnToHeading(0));
			addSequential(new DriveStraightDistance(88.6 / 12.0 , Drive.Direction.FORWARD));
			addSequential(new TurnToHeading(-43.54));
			addSequential(new DriveStraightDistance( 327.563 /12.0, Drive.Direction.FORWARD));
			addSequential(new TurnToHeading(0));
		}
	}
	private class PositionFour extends CommandGroup {
		private double power;
		private double delayTime;
		
		public PositionFour(double delayTime) {
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(98.82 / 12.0, Drive.Direction.FORWARD));
			addSequential(new TurnToHeading(-60));
			addSequential(new DriveStraightDistance(51.932 / 12.0, Drive.Direction.FORWARD));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(51.932 / 12.0, Drive.Direction.BACKWARD));
//			addSequential(new TurnToHeading(90));
			addSequential(new TurnToHeading(-90));
			addSequential(new DriveStraightDistance(88.6 / 12.0 , Drive.Direction.BACKWARD));
//			addSequential(new DriveStraightDistance(88.6 / 12.0 , Drive.Direction.FORWARD));
			addSequential(new SetAutonomousHeading(0));
			addSequential(new DriveStraightDistance( 327.563 /12.0, Drive.Direction.FORWARD));
		}
	}
	private class PositionFive extends CommandGroup {
		private double power;
		private double delayTime;
		
		public PositionFive(double delayTime) {
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(105 / 12.0, Drive.Direction.FORWARD));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(7 / 12.0, Drive.Direction.BACKWARD));
			addSequential(new TurnToHeading(90));
			addSequential(new DriveStraightDistance(136.875 / 12.0, Drive.Direction.FORWARD));
			addSequential(new TurnToHeading(0));
			addSequential(new DriveStraightDistance(326 / 12.0, Drive.Direction.FORWARD ));
			
		}
	}
	private class PositionSix extends CommandGroup {
		private double power;
		private double delayTime;
		
		public PositionSix(double delayTime) {
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(98.82 / 12.0, Drive.Direction.FORWARD));
			addSequential(new TurnToHeading(-60));
			addSequential(new DriveStraightDistance(51.932 / 12.0, Drive.Direction.FORWARD));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(51.932 / 12.0, Drive.Direction.BACKWARD));
			addSequential(new TurnToHeading(0));
			addSequential(new DriveStraightDistance(325.28 / 12.0, Drive.Direction.FORWARD));
		}
	}
	
	private class PositionSeven extends CommandGroup {
	
	}
}
