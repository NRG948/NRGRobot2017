package org.usfirst.frc.team948.robot.commandgroups;

import static org.usfirst.frc.team948.robot.subsystems.Drive.Direction.BACKWARD;
import static org.usfirst.frc.team948.robot.subsystems.Drive.Direction.FORWARD;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.Robot.AutoMovement;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.FlipCameraLight;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.TurnToHeading;
import org.usfirst.frc.team948.robot.commands.TurnToPegCenter;
import org.usfirst.frc.team948.robot.commands.VisionDriveToPeg;
import org.usfirst.frc.team948.robot.commands.WaitUntilGearDrop;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousRoutines extends CommandGroup {
	private static final double DRIVE_TO_AIRSHIP_TIMEOUT = 3.0;
	private Robot.AutoPosition autoPosition;
	public boolean moveAfterGear;
	private AutoMovement autoMovement;

	public AutonomousRoutines(Robot.AutoPosition autoPosition, Robot.AutoMovement autoMovement) {
		this.autoPosition = autoPosition;
		this.autoMovement = autoMovement;
		if (autoMovement == AutoMovement.STAY) {
			addSequential(new Stay());
			return;
		}
		double delay = RobotMap.preferences.getDouble(PreferenceKeys.GEAR_DROP_TIME, 1);

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
			addSequential(new DriveStraightDistance(69, FORWARD));
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
				if (autoMovement == Robot.AutoMovement.CONTINUE_TO_END) {
					addSequential(new DriveStraightDistance(300, FORWARD));
				} else {
					addSequential(new DriveStraightDistance(40, FORWARD));
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
			addSequential(new WaitUntilGearDrop(this.delayTime));
			if (autoMovement != Robot.AutoMovement.STOP_AT_AIRSHIP) {
				addSequential(new DriveStraightDistance(60, BACKWARD));
				addSequential(new TurnToHeading(-65));
				addSequential(new DriveStraightDistance(125, FORWARD));
				addSequential(new TurnToHeading(0));
				addSequential(new ShiftGears(true));
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
			addSequential(new DriveStraightDistance(83.537, FORWARD));
			addSequential(new TurnToHeading(-61));
			if (RobotMap.autoWithVision) {
				addSequential(new PressToPeg());
			} else {
				addSequential(new DriveStraightDistance(82.4, FORWARD), DRIVE_TO_AIRSHIP_TIMEOUT);
			}
			addSequential(new WaitUntilGearDrop(this.delayTime));
			if (autoMovement != Robot.AutoMovement.STOP_AT_AIRSHIP) {
				addSequential(new WaitUntilGearDrop(this.delayTime));
				addSequential(new DriveStraightDistance(20, BACKWARD));
				addSequential(new TurnToHeading(0));
				addSequential(new ShiftGears(true));
				addSequential(new DriveStraightDistance(113, FORWARD));
				addSequential(new TurnToHeading(-40));
				if (autoMovement == Robot.AutoMovement.CONTINUE_TO_END) {
					addSequential(new DriveStraightDistance(200, FORWARD));
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
			addSequential(new DriveStraightDistance(69, FORWARD));
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
				if (autoMovement == Robot.AutoMovement.CONTINUE_TO_END) {
					addSequential(new DriveStraightDistance(300, FORWARD));
				} else {
					addSequential(new DriveStraightDistance(40, FORWARD));
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
			addSequential(new ShiftGears(false));
			if (RobotMap.autoWithVision) {
				addSequential(new PressToPeg());
			} else {
				addSequential(new DriveStraightDistance(76, FORWARD), DRIVE_TO_AIRSHIP_TIMEOUT);
			}
			addSequential(new WaitUntilGearDrop(this.delayTime));
			if (autoMovement != Robot.AutoMovement.STOP_AT_AIRSHIP) {
				addSequential(new DriveStraightDistance(60, BACKWARD));
				addSequential(new TurnToHeading(65));
				addSequential(new DriveStraightDistance(125, FORWARD));
				addSequential(new TurnToHeading(0));
				addSequential(new ShiftGears(true));
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
			addSequential(new DriveStraightDistance(85.537, FORWARD));
			addSequential(new TurnToHeading(60));
			if (RobotMap.autoWithVision) {
				addSequential(new PressToPeg());
			} else {
				addSequential(new DriveStraightDistance(82.4, FORWARD), DRIVE_TO_AIRSHIP_TIMEOUT);
			}
			addSequential(new WaitUntilGearDrop(this.delayTime));
			if (autoMovement != Robot.AutoMovement.STOP_AT_AIRSHIP) {
				addSequential(new WaitUntilGearDrop(this.delayTime));
				addSequential(new DriveStraightDistance(20, BACKWARD));
				addSequential(new TurnToHeading(0));
				addSequential(new ShiftGears(true));
				addSequential(new DriveStraightDistance(113, FORWARD));
				addSequential(new TurnToHeading(40));
				if (autoMovement == Robot.AutoMovement.CONTINUE_TO_END) {
					addSequential(new DriveStraightDistance(200, FORWARD));
					addSequential(new TurnToHeading(5));
				} else {
					// we already went pass the auto line
				}
				addSequential(new ShiftGears(false));
			}
		}
	}

	private class Stay extends CommandGroup {
	}

}
