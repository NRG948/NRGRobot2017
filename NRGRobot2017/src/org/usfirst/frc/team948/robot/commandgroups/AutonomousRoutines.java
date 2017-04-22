package org.usfirst.frc.team948.robot.commandgroups;

import static org.usfirst.frc.team948.robot.subsystems.Drive.Direction.BACKWARD;
import static org.usfirst.frc.team948.robot.subsystems.Drive.Direction.FORWARD;
import static org.usfirst.frc.team948.utilities.Waypoint.CoordinateType.ABSOLUTE;
import static org.usfirst.frc.team948.utilities.Waypoint.CoordinateType.RELATIVE;

import java.util.ArrayList;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.Robot.AutoMovement;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.DelaySeconds;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.FeedBalls;
import org.usfirst.frc.team948.robot.commands.FlipCameraLight;
import org.usfirst.frc.team948.robot.commands.FollowWaypoints;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.SetPositionTracker;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.ShootAfterGearDrop2;
import org.usfirst.frc.team948.robot.commands.SpinShooterGivenRawPower;
import org.usfirst.frc.team948.robot.commands.SpinShooterToCalculatedRPM;
import org.usfirst.frc.team948.robot.commands.SpinShooterToRPM;
import org.usfirst.frc.team948.robot.commands.StopShooterAndBallFeed;
import org.usfirst.frc.team948.robot.commands.Turn;
import org.usfirst.frc.team948.robot.commands.TurnToBoiler;
import org.usfirst.frc.team948.robot.commands.TurnToHeading;
import org.usfirst.frc.team948.robot.commands.VisionDriveToPeg;
import org.usfirst.frc.team948.robot.commands.WaitUntilGearDrop;
import org.usfirst.frc.team948.utilities.PreferenceKeys;
import org.usfirst.frc.team948.utilities.Waypoint;
import org.usfirst.frc.team948.utilities.Waypoint.WithinInches;
import org.usfirst.frc.team948.utilities.Waypoint.GreaterThanY;
import org.usfirst.frc.team948.utilities.Waypoint.LessThanX;
import org.usfirst.frc.team948.utilities.Waypoint.GreaterThanX;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousRoutines extends CommandGroup {
	private static final double BLUE_CENTER_STARTING_X = 161.85;
	private static final double BLUE_ROBOT_LENGTH = 39;
	private static final double DRIVE_TO_AIRSHIP_TIMEOUT = 3.0;
	private static final int RPM_TOUCHING_BOILER = 2900;
	private static final int RPM_18_INCHES_FROM_BOILER = 2950;
	private static final double AUTO_DRIVE_POWER = 1.0;
	
	private Robot.AutoPosition autoPosition;
	public boolean moveAfterGear;
	private AutoMovement autoMovement;

	public AutonomousRoutines(Robot.AutoPosition autoPosition, Robot.AutoMovement autoMovement) {
		this.autoPosition = autoPosition;
		this.autoMovement = autoMovement;
		System.out.println("The auto Position is "+autoPosition);
		System.out.println("The auto movement is "+autoMovement);
		double delay = RobotMap.preferences.getDouble(PreferenceKeys.GEAR_DROP_TIME, 0.75);

		addSequential(new ResetSensors());
		addSequential(new ShiftGears(false));
		addSequential(new SetPositionTracker(autoPosition));
		
		switch (autoPosition) {
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

//	public Robot.AutoPosition returnPosition() {
//		return this.autoPosition;
//	}

	private class RedLeft extends CommandGroup {
		private double delayTime;

		public RedLeft(double delayTime) {
			this.delayTime = delayTime;
//			addSequential(new ResetSensors());
//			addSequential(new ShiftGears(false));
//			addSequential(new SetPositionTracker(Robot.AutoPosition.RED_LEFT));
			addSequential(new DriveStraightDistance(82, FORWARD));
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

//			addSequential(new ResetSensors());
//			addSequential(new ShiftGears(false));
//			addSequential(new SetPositionTracker(Robot.AutoPosition.RED_CENTER));
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

//			addSequential(new ResetSensors());
//			addSequential(new ShiftGears(false));
//			addSequential(new SetPositionTracker(Robot.AutoPosition.RED_RIGHT));
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
//			addSequential(new ResetSensors());
//			addSequential(new ShiftGears(false));
//			addSequential(new SetPositionTracker(Robot.AutoPosition.BLUE_RIGHT));
			addSequential(new DriveStraightDistance(82, FORWARD));
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
		}
	}

	private class BlueCenter extends CommandGroup {
		private double delayTime;

		public BlueCenter(double delayTime) {
			SmartDashboard.putBoolean("move after gear (blue center)", moveAfterGear);
			this.delayTime = delayTime;

//			addSequential(new ResetSensors());
//			addSequential(new ShiftGears(false));
//			addSequential(new SetPositionTracker(Robot.AutoPosition.BLUE_CENTER));
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

//			addSequential(new ResetSensors());
//			addSequential(new ShiftGears(false));
//			addSequential(new SetPositionTracker(Robot.AutoPosition.BLUE_LEFT));
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
//			addSequential(new ResetSensors());
//			addSequential(new ShiftGears(false));
//			addSequential(new SetPositionTracker(Robot.AutoPosition.BLUE_SHOOT_THEN_GEAR));
			addParallel(new SpinShooterGivenRawPower(0.85));
			addSequential(new DriveStraightDistance(74, BACKWARD, 1));
			addParallel(new SpinShooterToCalculatedRPM());
			addSequential(new TurnToBoilerAndFeedBalls());
		}
	}

	private class OldRedShootOnly extends CommandGroup {
		public OldRedShootOnly() {
//			addSequential(new ResetSensors());
//			addSequential(new ShiftGears(false));
//			addSequential(new SetPositionTracker(Robot.AutoPosition.RED_SHOOT_THEN_GEAR));
			addParallel(new SpinShooterGivenRawPower(0.85));
			addSequential(new DriveStraightDistance(74, BACKWARD, 1));
			addParallel(new SpinShooterToCalculatedRPM());
			addSequential(new TurnToBoilerAndFeedBalls());
		}
	}
	
	private class RedShootThenGear extends CommandGroup {
		boolean usePath = false;
		private ArrayList<Waypoint> firstPath = new ArrayList<Waypoint>();
		private ArrayList<Waypoint> secondPath = new ArrayList<Waypoint>();
		
		public RedShootThenGear(double delayTime) {
			double shootTimeOut = RobotMap.preferences.getDouble(PreferenceKeys.AUTO_SHOOT_TIMEOUT, 7.0);
			addParallel(new SpinShooterToRPM(RPM_TOUCHING_BOILER));
			addSequential(new FeedBalls(true, shootTimeOut));
			addSequential(new StopShooterAndBallFeed());
			addSequential(new DriveStraightDistance(18.0, BACKWARD, 1));
			if (usePath) {
				addSequential(new Turn(-142));
				addSequential(new FlipCameraLight(true));
				firstPath.add(new Waypoint(Robot.FIELD_WIDTH - 70, 95.5, ABSOLUTE, AUTO_DRIVE_POWER, new GreaterThanY()));
				firstPath.add(new Waypoint(Robot.FIELD_WIDTH - 88, 106, ABSOLUTE, AUTO_DRIVE_POWER, new LessThanX()));
				addSequential(new FollowWaypoints(firstPath));
				addSequential(new VisionDriveToPeg());
			} else {
				addSequential(new TurnToHeading(0));	
				addSequential(new DriveStraightDistance(42, FORWARD));
				addSequential(new FlipCameraLight(true));
				addSequential(new Turn(-60));
				addSequential(new PressToPeg());
			}
			addSequential(new FlipCameraLight(false));
			addSequential(new WaitUntilGearDrop(delayTime));
			addSequential(new ShiftGears(true));
			addSequential(new DelaySeconds(0.1));
			addSequential(new DriveStraightDistance(20, BACKWARD));
			addSequential(new TurnToHeading(0));
			secondPath.add(new Waypoint(0, 35, RELATIVE, AUTO_DRIVE_POWER, new GreaterThanY()));
			secondPath.add(new Waypoint(-17, 70, RELATIVE, AUTO_DRIVE_POWER, new GreaterThanY()));
			secondPath.add(new Waypoint(-35, 105, RELATIVE, AUTO_DRIVE_POWER, new GreaterThanY()));
			secondPath.add(new Waypoint(-53, 140, RELATIVE, AUTO_DRIVE_POWER, new GreaterThanY()));
			secondPath.add(new Waypoint(-70, 175, RELATIVE, AUTO_DRIVE_POWER, new GreaterThanY()));
			secondPath.add(new Waypoint(-75, 200, RELATIVE, AUTO_DRIVE_POWER, new GreaterThanY()));
			addSequential(new FollowWaypoints(secondPath));
		}
	}
	
	private class BlueShootThenGear extends CommandGroup {
		boolean usePath = false;
		private ArrayList<Waypoint> firstPath = new ArrayList<Waypoint>();
		private ArrayList<Waypoint> secondPath = new ArrayList<Waypoint>();
		
		public BlueShootThenGear(double delayTime){
			double shootTimeOut = RobotMap.preferences.getDouble(PreferenceKeys.AUTO_SHOOT_TIMEOUT, 4.0);
			addParallel(new SpinShooterToRPM(RPM_18_INCHES_FROM_BOILER));
			addSequential(new DriveStraightDistance(18.0, BACKWARD, 1));
			addSequential(new TurnToBoiler());
			addSequential(new FeedBalls(true, shootTimeOut));
			addSequential(new StopShooterAndBallFeed());
			if (usePath) {
				addSequential(new Turn(140));
				addSequential(new FlipCameraLight(true));
				firstPath.add(new Waypoint(70 - 0, 95.5 - 20, ABSOLUTE, AUTO_DRIVE_POWER, new WithinInches(4)));
				firstPath.add(new Waypoint(88, 106, ABSOLUTE, AUTO_DRIVE_POWER, new GreaterThanX()));
				addSequential(new FollowWaypoints(firstPath));
				addSequential(new VisionDriveToPeg());
			} else {
				addSequential(new TurnToHeading(0));	
				addSequential(new DriveStraightDistance(45, FORWARD));
				addSequential(new FlipCameraLight(true));
				addSequential(new Turn(60));
				addSequential(new PressToPeg());
			}
			addSequential(new FlipCameraLight(false));
			addSequential(new WaitUntilGearDrop(delayTime));
			addSequential(new ShiftGears(true));
			addSequential(new DelaySeconds(0.1));
			addSequential(new DriveStraightDistance(20, BACKWARD));
			addSequential(new TurnToHeading(0));
			secondPath.add(new Waypoint(0, 35, RELATIVE, AUTO_DRIVE_POWER, new GreaterThanY()));
			secondPath.add(new Waypoint(17, 70, RELATIVE, AUTO_DRIVE_POWER, new GreaterThanY()));
			secondPath.add(new Waypoint(35, 105, RELATIVE, AUTO_DRIVE_POWER, new GreaterThanY()));
			secondPath.add(new Waypoint(53, 140, RELATIVE, AUTO_DRIVE_POWER, new GreaterThanY()));
			secondPath.add(new Waypoint(70, 175, RELATIVE, AUTO_DRIVE_POWER, new GreaterThanY()));
			secondPath.add(new Waypoint(75, 200, RELATIVE, AUTO_DRIVE_POWER, new GreaterThanY()));
			addSequential(new FollowWaypoints(secondPath));
//			addSequential(new Turn(20, shootTimeOut));
//			addSequential(new FeedBalls(true), shootTimeOut);
//			addSequential(new DriveStraightDistance(18.0, BACKWARD, 1));
//			addSequential(new StopShooterAndBallFeed());
//			addSequential(new Turn(135));
//			addSequential(new DriveStraightDistance(52, FORWARD));
//			addSequential(new Turn(45));
//			addSequential(new PressToPeg());
//			addSequential(new WaitUntilGearDrop(delayTime));
//			addSequential(new DriveStraightDistance(20, BACKWARD));
//			addSequential(new TurnToHeading(0));
//			addSequential(new ShiftGears(true));
//			addSequential(new DelaySeconds(0.1));
//			addSequential(new DriveStraightDistance(113, FORWARD));
//			addSequential(new TurnToHeading(40));
//			addSequential(new DriveStraightDistance(170, FORWARD));
//			addSequential(new TurnToHeading(5));
		}
	}
	
	public static class TestPath extends CommandGroup {
		final double WAYPOINT_SPEED = 0.5;
		WithinInches sixInches = new WithinInches(6);
		ArrayList<Waypoint> testPath = new ArrayList<Waypoint>();
		
		public TestPath()
		{
			testPath.add(new Waypoint(-24, 24, RELATIVE, WAYPOINT_SPEED, sixInches));
			testPath.add(new Waypoint(-48, 24, RELATIVE, WAYPOINT_SPEED, sixInches));
			testPath.add(new Waypoint(-60, 36, RELATIVE, WAYPOINT_SPEED, sixInches));
			testPath.add(new Waypoint(-54, 60, RELATIVE, WAYPOINT_SPEED, sixInches));
			testPath.add(new Waypoint(-24, 72, RELATIVE, WAYPOINT_SPEED, sixInches));
			addSequential(new FollowWaypoints(testPath));
		}
	}
	
	public static class TestPathRedRight extends CommandGroup {
		final double WAYPOINT_SPEED = 0.5;
		WithinInches sixInches = new WithinInches(6);
		ArrayList<Waypoint> testPath = new ArrayList<Waypoint>();
		
		public TestPathRedRight()
		{
			testPath.add(new Waypoint(0, 40, RELATIVE, WAYPOINT_SPEED, sixInches));
			testPath.add(new Waypoint(-110, 155, RELATIVE, WAYPOINT_SPEED, sixInches));
			testPath.add(new Waypoint(-26, 109, RELATIVE, WAYPOINT_SPEED, sixInches));
			addSequential(new DriveStraightDistance(20, BACKWARD));
			addSequential(new FollowWaypoints(testPath));
		}
	}
}
