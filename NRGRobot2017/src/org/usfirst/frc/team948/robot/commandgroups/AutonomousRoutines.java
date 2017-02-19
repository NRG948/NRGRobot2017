package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.commands.DelaySeconds;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.SetAutonomousHeading;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
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
			case RED_LEFT:
				addSequential(new RedLeft(3));
				break;
			case RED_CENTER:
				addSequential(new RedCenter(3));
				break;
			case RED_RIGHT:
				addSequential(new RedRight(3));
				break;
			case BLUE_RIGHT:
				addSequential(new BlueRight(3));
				break;
			case BLUE_CENTER:
				addSequential(new BlueCenter(3));
				break;
			case BLUE_LEFT:
				addSequential(new BlueLeft(3));
				break;
			case STAY:
				addSequential(new Stay());
				break;
		}
		
	}
	private class RedLeft extends CommandGroup {
		private double delayTime;
		
		public RedLeft(double delayTime) {
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
	private class RedCenter extends CommandGroup {
		private double power;
		private double delayTime;
		
		public RedCenter(double delayTime) {
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
	private class RedRight extends CommandGroup {
		private double power;
		private double delayTime;
		
		public RedRight(double power) {
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
	private class BlueRight extends CommandGroup {
		private double power;
		private double delayTime;
		
		public BlueRight(double delayTime) {
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(53.5 / 12.0, Drive.Direction.FORWARD));
			addSequential(new TurnToHeading(-60));
			addSequential(new DriveStraightDistance(59.0 / 12.0, Drive.Direction.FORWARD));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(20.0 / 12.0, Drive.Direction.BACKWARD));
			addSequential(new TurnToHeading(6));
			addParallel(new ShiftGears(true));
			addSequential(new DriveStraightDistance(300 / 12.0 , Drive.Direction.FORWARD));
			addSequential(new ShiftGears(false));
		}
	}
	private class BlueCenter extends CommandGroup {
		private double power;
		private double delayTime;
		
		public BlueCenter(double delayTime) {
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new DriveStraightDistance(53 / 12.0, Drive.Direction.FORWARD));
			addSequential(new DelaySeconds(this.delayTime));
			addSequential(new DriveStraightDistance(38 / 12.0, Drive.Direction.BACKWARD));
			addSequential(new TurnToHeading(65));
			addSequential(new DriveStraightDistance(136.875 / 12.0, Drive.Direction.FORWARD));
			addSequential(new TurnToHeading(0));
			addSequential(new DriveStraightDistance(326 / 12.0, Drive.Direction.FORWARD ));
			
		}
	}
	private class BlueLeft extends CommandGroup {
		private double power;
		private double delayTime;
		
		public BlueLeft(double delayTime) {
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
	
	private class Stay extends CommandGroup {
	
	}
}
