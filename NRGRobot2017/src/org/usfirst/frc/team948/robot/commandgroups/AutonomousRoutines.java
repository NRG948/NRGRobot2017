package org.usfirst.frc.team948.robot.commandgroups;

import static org.usfirst.frc.team948.robot.subsystems.Drive.Direction.BACKWARD;
import static org.usfirst.frc.team948.robot.subsystems.Drive.Direction.FORWARD;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.Robot.AutoMovement;
import org.usfirst.frc.team948.robot.Robot.PegPosition;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.DelaySeconds;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.FeedBalls;
import org.usfirst.frc.team948.robot.commands.FlipCameraLight;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.SetInitPoz;
import org.usfirst.frc.team948.robot.commands.SetPositionTracker;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.ShootAfterGearDrop2;
import org.usfirst.frc.team948.robot.commands.SpinShooterGivenRawPower;
import org.usfirst.frc.team948.robot.commands.SpinShooterToCalculatedRPM;
import org.usfirst.frc.team948.robot.commands.SpinShooterToRPM;
import org.usfirst.frc.team948.robot.commands.StopShooterAndBallFeed;
import org.usfirst.frc.team948.robot.commands.Turn;
import org.usfirst.frc.team948.robot.commands.TurnToHeading;
import org.usfirst.frc.team948.robot.commands.WaitUntilGearDrop;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousRoutines extends CommandGroup {
	private static final double BLUE_CENTER_STARTING_X = 161.85;
	private static final double BLUE_ROBOT_LENGTH = 39;
	private static final double DRIVE_TO_AIRSHIP_TIMEOUT = 3.0;
	private static final int RPM_TOUCHING_BOILER = 2900;
	private Robot.AutoPosition autoPosition;
	public boolean moveAfterGear;
	private AutoMovement autoMovement;

	public AutonomousRoutines(Robot.AutoPosition autoPosition, Robot.AutoMovement autoMovement) {
		this.autoPosition = autoPosition;
		this.autoMovement = autoMovement;

		double delay = RobotMap.preferences.getDouble(PreferenceKeys.GEAR_DROP_TIME, 0.75);

		switch (this.autoPosition) {
		case RED_LEFT:
			addSequential(new RedLeft(delay));
			break;
		case RED_CENTER:
			addSequential(new RedCenter(delay));
			break;
		case RED_RIGHT:
			addSequential(new RedRight(delay));
			break;
		case BLUE_RIGHT:
			addSequential(new BlueRight(delay));
			break;
		case BLUE_CENTER:
			addSequential(new BlueCenter(delay));
			break;
		case BLUE_LEFT:
			addSequential(new BlueLeft(delay));
			break;
		case BLUE_SHOOT_THEN_GEAR:
			addSequential(new BlueShootThenGear(delay));
			break;
		case RED_SHOOT_THEN_GEAR:
			addSequential(new RedShootThenGear(delay));
			break;
			
		}
	}

	public Robot.AutoPosition returnPosition() {
		return this.autoPosition;
	}

	private class RedLeft extends CommandGroup {
		private double delayTime;

		public RedLeft(double delayTime) {
			this.delayTime = delayTime;
			addSequential(new ResetSensors());
			addSequential(new ShiftGears(false));
			addSequential(new DriveStraightDistance(79, FORWARD));
			addSequential(new TurnToHeading(60));
			if (RobotMap.autoWithVision) {
				addSequential(new PressToPeg());
			} else {
				addSequential(new DriveStraightDistance(42.0, FORWARD), DRIVE_TO_AIRSHIP_TIMEOUT);
			}
			addSequential(new WaitUntilGearDrop(this.delayTime));
			if (autoMovement != Robot.AutoMovement.STOP_AT_AIRSHIP) {
				addSequential(new WaitUntilGearDrop(this.delayTime));
				addSequential(new DriveStraightDistance(20.0, BACKWARD));
				addSequential(new TurnToHeading(-6));
				addSequential(new ShiftGears(true));
				addSequential(new DelaySeconds(0.1));
				if (autoMovement == Robot.AutoMovement.CONTINUE_TO_END) {
					addSequential(new DriveStraightDistance(300, FORWARD));
				} else {
					addSequential(new DriveStraightDistance(50, FORWARD));
				}
				addSequential(new ShiftGears(false));
			}
		}
	}

	private class RedCenter extends CommandGroup {
		private double delayTime;

		public RedCenter(double delayTime) {
			SmartDashboard.putBoolean("move after gear (blue center)", moveAfterGear);
			this.delayTime = delayTime;

			addSequential(new ResetSensors());
			addSequential(new ShiftGears(false));
			if (RobotMap.autoWithVision) {
				addSequential(new PressToPeg());
			} else {
				addSequential(new DriveStraightDistance(76, FORWARD), DRIVE_TO_AIRSHIP_TIMEOUT);
			}
			if (autoMovement != Robot.AutoMovement.STOP_AT_AIRSHIP) {
				addSequential(new WaitUntilGearDrop(this.delayTime));
				if (autoMovement == Robot.AutoMovement.SHOOT_AFTER_GEAR_DROP) {
					addSequential(new ShootAfterGearDrop2());
					return;
				}
				addSequential(new DriveStraightDistance(60, BACKWARD));
				addSequential(new TurnToHeading(-65));
				addSequential(new DriveStraightDistance(125, FORWARD));
				addSequential(new TurnToHeading(0));
				addSequential(new ShiftGears(true));
				addSequential(new DelaySeconds(0.1));
				if (autoMovement == Robot.AutoMovement.CONTINUE_TO_END) {
					addSequential(new DriveStraightDistance(300, FORWARD));
				} else {
					addSequential(new DriveStraightDistance(50, FORWARD));
				}
				addSequential(new ShiftGears(false));
			}
		}
	}

	private class RedRight extends CommandGroup {
		private double delayTime;

		public RedRight(double delayTime) {
			this.delayTime = delayTime;

			addSequential(new ResetSensors());
			addSequential(new ShiftGears(false));
			addSequential(new DriveStraightDistance(80, FORWARD));
			addSequential(new TurnToHeading(-61));
			if (RobotMap.autoWithVision) {
				addSequential(new PressToPeg());
			} else {
				addSequential(new DriveStraightDistance(82.4, FORWARD), DRIVE_TO_AIRSHIP_TIMEOUT);
			}
			if (autoMovement != Robot.AutoMovement.STOP_AT_AIRSHIP) {
				addSequential(new WaitUntilGearDrop(this.delayTime));
				if (autoMovement == Robot.AutoMovement.SHOOT_AFTER_GEAR_DROP) {
					addSequential(new ShootAfterGearDrop2());
					return;
				}
				addSequential(new DriveStraightDistance(20, BACKWARD));
				addSequential(new TurnToHeading(0));
				addSequential(new ShiftGears(true));
				addSequential(new DelaySeconds(0.1));
				addSequential(new DriveStraightDistance(113, FORWARD));
				addSequential(new TurnToHeading(-40));
				if (autoMovement == Robot.AutoMovement.CONTINUE_TO_END) {
					addSequential(new DriveStraightDistance(170, FORWARD));
					addSequential(new TurnToHeading(-5));
				} else {
					// we already went pass the auto line
				}
				addSequential(new ShiftGears(false));
			}
		}
	}

	private class BlueRight extends CommandGroup {
		private double delayTime;

		public BlueRight(double delayTime) {
			this.delayTime = delayTime;
			addSequential(new FlipCameraLight(true));
			addSequential(new ResetSensors());
			addSequential(new ShiftGears(false));
			addSequential(new DriveStraightDistance(79, FORWARD));
			addSequential(new TurnToHeading(-60));
			// Turn to peg center
			if (RobotMap.autoWithVision) {
				addSequential(new PressToPeg());
			} else {
				addSequential(new DriveStraightDistance(42, FORWARD), DRIVE_TO_AIRSHIP_TIMEOUT);
			}
			addSequential(new WaitUntilGearDrop(this.delayTime));
			if (autoMovement != Robot.AutoMovement.STOP_AT_AIRSHIP) {
				addSequential(new WaitUntilGearDrop(this.delayTime));
				addSequential(new DriveStraightDistance(20.0, BACKWARD));
				addSequential(new TurnToHeading(6));
				addSequential(new ShiftGears(true));
				addSequential(new DelaySeconds(0.1));
				if (autoMovement == Robot.AutoMovement.CONTINUE_TO_END) {
					addSequential(new DriveStraightDistance(300, FORWARD));
				} else {
					addSequential(new DriveStraightDistance(50, FORWARD));
				}
				addSequential(new ShiftGears(false));
			}
			addSequential(new FlipCameraLight(true));
		}
	}

	private class BlueCenter extends CommandGroup {
		private double delayTime;

		public BlueCenter(double delayTime) {
			SmartDashboard.putBoolean("move after gear (blue center)", moveAfterGear);
			this.delayTime = delayTime;

			addSequential(new ResetSensors());
			addSequential(new SetInitPoz(BLUE_CENTER_STARTING_X, BLUE_ROBOT_LENGTH / 2));
			addSequential(new ShiftGears(false));
			if (RobotMap.autoWithVision) {
				addSequential(new PressToPeg());
			} else {
				addSequential(new DriveStraightDistance(76, FORWARD), DRIVE_TO_AIRSHIP_TIMEOUT);
			}
			if (autoMovement != Robot.AutoMovement.STOP_AT_AIRSHIP) {
				addSequential(new WaitUntilGearDrop(this.delayTime));
				if (autoMovement == Robot.AutoMovement.SHOOT_AFTER_GEAR_DROP) {
					addSequential(new ShootAfterGearDrop2());
					return;
				}
				addSequential(new DriveStraightDistance(60, BACKWARD));
				addSequential(new TurnToHeading(65));
				addSequential(new DriveStraightDistance(125, FORWARD));
				addSequential(new TurnToHeading(0));
				addSequential(new ShiftGears(true));
				addSequential(new DelaySeconds(0.1));
				if (autoMovement == Robot.AutoMovement.CONTINUE_TO_END) {
					addSequential(new DriveStraightDistance(300, FORWARD));
				} else {
					addSequential(new DriveStraightDistance(50, FORWARD));
				}
				addSequential(new ShiftGears(false));
			}
		}
	}

	private class BlueLeft extends CommandGroup {
		private double delayTime;

		public BlueLeft(double delayTime) {
			this.delayTime = delayTime;

			addSequential(new ResetSensors());
			addSequential(new ShiftGears(false));
			addSequential(new SetPositionTracker(Robot.AutoPosition.BLUE_LEFT));
			addSequential(new DriveStraightDistance(80, FORWARD));
			addSequential(new TurnToHeading(60));
			if (RobotMap.autoWithVision) {
				addSequential(new PressToPeg());
			} else {
				addSequential(new DriveStraightDistance(82.4, FORWARD), DRIVE_TO_AIRSHIP_TIMEOUT);
			}
			if (autoMovement != Robot.AutoMovement.STOP_AT_AIRSHIP) {
				if (autoMovement == Robot.AutoMovement.SHOOT_AFTER_GEAR_DROP) {
					addSequential(new ShootAfterGearDrop2());
					return;
				}
				addSequential(new WaitUntilGearDrop(this.delayTime));
				addSequential(new DriveStraightDistance(20, BACKWARD));
				addSequential(new TurnToHeading(0));
				addSequential(new ShiftGears(true));
				addSequential(new DelaySeconds(0.1));
				addSequential(new DriveStraightDistance(113, FORWARD));
				addSequential(new TurnToHeading(40));
				if (autoMovement == Robot.AutoMovement.CONTINUE_TO_END) {
					addSequential(new DriveStraightDistance(170, FORWARD));
					addSequential(new TurnToHeading(5));
				} else {
					// we already went pass the auto line
				}
				addSequential(new ShiftGears(false));
			}
		}
	}

	private class OldBlueShootOnly extends CommandGroup {
		public OldBlueShootOnly() {
			addSequential(new ResetSensors());
			addSequential(new ShiftGears(false));
			addSequential(new SetPositionTracker(Robot.AutoPosition.BLUE_SHOOT_THEN_GEAR));
			addParallel(new SpinShooterGivenRawPower(0.85));
			addSequential(new DriveStraightDistance(74, BACKWARD, 1));
			addParallel(new SpinShooterToCalculatedRPM());
			addSequential(new TurnToBoilerAndFeedBalls());
		}
	}

	private class OldRedShootOnly extends CommandGroup {
		public OldRedShootOnly() {
			addSequential(new ResetSensors());
			addSequential(new ShiftGears(false));
			addSequential(new SetPositionTracker(Robot.AutoPosition.RED_SHOOT_THEN_GEAR));
			addParallel(new SpinShooterGivenRawPower(0.85));
			addSequential(new DriveStraightDistance(74, BACKWARD, 1));
			addParallel(new SpinShooterToCalculatedRPM());
			addSequential(new TurnToBoilerAndFeedBalls());
		}
	}
	
	private class RedShootThenGear extends CommandGroup {

		public RedShootThenGear(double delayTime){
			double shootTimeOut = RobotMap.preferences.getDouble(PreferenceKeys.AUTO_SHOOT_TIMEOUT, 7.0);
			addSequential(new SpinShooterToRPM(RPM_TOUCHING_BOILER));
			addSequential(new FeedBalls(true), shootTimeOut);
			//addSequential(new DriveStraightDistance(8.0, BACKWARD, 1), shootTimeOut);
			addSequential(new StopShooterAndBallFeed());
			addSequential(new DriveStraightDistance(18.0, BACKWARD, 1));
			addSequential(new Turn(-142));
			addSequential(new DriveStraightDistance(20, FORWARD));
			addSequential(new Turn(-45));
			addSequential(new PressToPeg());
			addSequential(new WaitUntilGearDrop(delayTime));
			addSequential(new DriveStraightDistance(20, BACKWARD));
			addSequential(new TurnToHeading(0));
			addSequential(new ShiftGears(true));
			addSequential(new DelaySeconds(0.1));
			addSequential(new DriveStraightDistance(113, FORWARD));
			addSequential(new TurnToHeading(-40));
			addSequential(new DriveStraightDistance(170, FORWARD));
			addSequential(new TurnToHeading(-5));
		}
	}
	private class BlueShootThenGear extends CommandGroup {
		public BlueShootThenGear(double delayTime){
			double shootTimeOut = RobotMap.preferences.getDouble(PreferenceKeys.AUTO_SHOOT_TIMEOUT, 7.0);
			addSequential(new SpinShooterToRPM(RPM_TOUCHING_BOILER));
			addSequential(new Turn(20, shootTimeOut));
			addSequential(new FeedBalls(true), shootTimeOut);
			addSequential(new DriveStraightDistance(18.0, BACKWARD, 1));
			addSequential(new StopShooterAndBallFeed());
			addSequential(new Turn(135));
			addSequential(new DriveStraightDistance(52, FORWARD));
			addSequential(new Turn(45));
			addSequential(new PressToPeg());
			addSequential(new WaitUntilGearDrop(delayTime));
			addSequential(new DriveStraightDistance(20, BACKWARD));
			addSequential(new TurnToHeading(0));
			addSequential(new ShiftGears(true));
			addSequential(new DelaySeconds(0.1));
			addSequential(new DriveStraightDistance(113, FORWARD));
			addSequential(new TurnToHeading(40));
			addSequential(new DriveStraightDistance(170, FORWARD));
			addSequential(new TurnToHeading(5));
		}
	}
}
