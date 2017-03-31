package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.robot.Robot.AutoMovement;
import org.usfirst.frc.team948.robot.Robot.AutoPosition;
import org.usfirst.frc.team948.robot.Robot.PegPosition;
import org.usfirst.frc.team948.robot.commandgroups.PressToPeg;
import org.usfirst.frc.team948.robot.commandgroups.ShootAfterGearDropOff;
import org.usfirst.frc.team948.robot.commandgroups.ShootSequence;
import org.usfirst.frc.team948.robot.commands.BallCollect;
import org.usfirst.frc.team948.robot.commands.ClimbPower;
import org.usfirst.frc.team948.robot.commands.FeedBalls;
import org.usfirst.frc.team948.robot.commands.FlipCameraLight;
import org.usfirst.frc.team948.robot.commands.Interrupt;
import org.usfirst.frc.team948.robot.commands.ManualDriveStraight;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.Shoot;
import org.usfirst.frc.team948.robot.commands.SpinShooterToRPM;
import org.usfirst.frc.team948.robot.commands.StopSuckingBalls;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public static Joystick arduinoJoystick = new Joystick(0);
	public static Joystick leftJoystick = new Joystick(1);
	public static Joystick rightJoystick = new Joystick(2);
	public static JoystickButton manualDriveStraight = new JoystickButton(leftJoystick, 1);
	public static JoystickButton interruptButton = new JoystickButton(leftJoystick, 2);
	public static JoystickButton pressToPeg = new JoystickButton(leftJoystick, 3);
	public static JoystickButton climberForwards = new JoystickButton(leftJoystick, 6);
	public static JoystickButton climberReverse = new JoystickButton(leftJoystick, 7);
	public static JoystickButton cameraLightSwitch = new JoystickButton(leftJoystick, 8);
	public static JoystickButton testShooterRPM = new JoystickButton(leftJoystick, 9);
	public static JoystickButton resetSensorsButton = new JoystickButton(leftJoystick, 11);

	public static JoystickButton rightTrigger = new JoystickButton(rightJoystick, 1);
	public static JoystickButton ShootingAuto = new JoystickButton(rightJoystick, 2);
	public static JoystickButton shoot = new JoystickButton(rightJoystick, 3);
	public static JoystickButton ejectBalls = new JoystickButton(rightJoystick, 6);
	public static JoystickButton acquireBalls = new JoystickButton(rightJoystick, 7);
	public static JoystickButton testShootAfterGearDrop = new JoystickButton(rightJoystick, 8); // test
																								// button
	public static JoystickButton shootSequence = new JoystickButton(rightJoystick, 9);
	public static JoystickButton switchLowGear = new JoystickButton(rightJoystick, 10);
	public static JoystickButton switchHighGear = new JoystickButton(rightJoystick, 11);

	public static final JoystickButton climberButton = new JoystickButton(arduinoJoystick, 1);
	public static final JoystickButton shootOnly = new JoystickButton(arduinoJoystick, 2);
	public static final JoystickButton autoLeft = new JoystickButton(arduinoJoystick, 4);
	public static final JoystickButton autoMiddle = new JoystickButton(arduinoJoystick, 5);
	public static final JoystickButton autoRight = new JoystickButton(arduinoJoystick, 6);
	public static final JoystickButton driveLong = new JoystickButton(arduinoJoystick, 7);
	public static final JoystickButton driveShort = new JoystickButton(arduinoJoystick, 8);
	public static final JoystickButton driveAirship = new JoystickButton(arduinoJoystick, 9);
	public static final JoystickButton shootAfterGear = new JoystickButton(arduinoJoystick, 10);
	public static final JoystickButton acquirerForward = new JoystickButton(arduinoJoystick, 11);
	public static final JoystickButton acquirerBackward = new JoystickButton(arduinoJoystick, 13);
	public static final JoystickButton feedShooter = new JoystickButton(arduinoJoystick, 14);
	public static final JoystickButton rpmShooter = new JoystickButton(arduinoJoystick, 15);

	public static void buttonInit() {
		rightTrigger.whenPressed(new ShiftGears(true));
		rightTrigger.whenReleased(new ShiftGears(false));
		manualDriveStraight.whileHeld(new ManualDriveStraight());
		resetSensorsButton.whenPressed(new ResetSensors());
		switchHighGear.whenPressed(new ShiftGears(true));
		switchLowGear.whenPressed(new ShiftGears(false));
		climberForwards.whileHeld(new ClimbPower(true));
		climberReverse.whileHeld(new ClimbPower(false));
		climberButton.whileHeld(new ClimbPower(true));
		cameraLightSwitch.whenPressed(new FlipCameraLight());
		interruptButton.whenPressed(new Interrupt());
		pressToPeg.whenReleased(new PressToPeg());
		shoot.whileHeld(new Shoot());
		testShooterRPM.toggleWhenActive(new SpinShooterToRPM());
		shootSequence.whenPressed(new ShootSequence());
		acquirerForward.whenPressed(new BallCollect(true));
		acquirerBackward.whenPressed(new BallCollect(false));
		feedShooter.whenActive(new FeedBalls());
		feedShooter.whenInactive(new StopSuckingBalls());
		// feeder.toggleWhenActive(new FeedBall(0.5, false));
		rpmShooter.whenReleased(new SpinShooterToRPM());
		rpmShooter.whenPressed(new SpinShooterToRPM(0));
		ejectBalls.toggleWhenActive(new BallCollect(false));
		acquireBalls.toggleWhenActive(new BallCollect(true));
		testShootAfterGearDrop.whenPressed(new ShootAfterGearDropOff(getPegPosition()));
	}

	private static PegPosition getPegPosition() {

		PegPosition pegPosition = null;
		if (autoLeft.get()) {
			pegPosition = PegPosition.LEFT;
		} else if (autoMiddle.get()) {
			pegPosition = PegPosition.CENTER;
		} else if (autoRight.get()) {
			pegPosition = PegPosition.RIGHT;
		}					

		return PegPosition.LEFT;
	}

	public static AutoPosition getAutoPosition() {
		AutoPosition autoPosition = null;
		DriverStation ds = DriverStation.getInstance();
		if (RobotMap.preferences.getBoolean(PreferenceKeys.USE_POSITION_CHOOSER, true)) {
			autoPosition = Robot.autoPositionChooser.getSelected();
		} else {
			if (ds.getAlliance() == DriverStation.Alliance.Red) {
				if (OI.shootOnly.get()) {
					autoPosition = AutoPosition.RED_SHOOT_ONLY;
				} else if (OI.autoLeft.get()) {
					autoPosition = AutoPosition.RED_LEFT;
				} else if (OI.autoRight.get()) {
					autoPosition = AutoPosition.RED_RIGHT;
				} else if (OI.autoMiddle.get()) {
					autoPosition = AutoPosition.RED_CENTER;
				}
			} else {
				if (OI.shootOnly.get()) {
					autoPosition = AutoPosition.BLUE_SHOOT_ONLY;
				} else if (OI.autoLeft.get()) {
					autoPosition = AutoPosition.BLUE_LEFT;
				} else if (OI.autoRight.get()) {
					autoPosition = AutoPosition.BLUE_RIGHT;
				} else if (OI.autoMiddle.get()) {
					autoPosition = AutoPosition.BLUE_CENTER;
				}
			}
		}

		return autoPosition;
	}

	public static AutoMovement getAutoMovement() {
		AutoMovement autoMovement = null;
		if (OI.driveAirship.get()) {
			autoMovement = AutoMovement.STOP_AT_AIRSHIP;
		} else if (OI.driveShort.get()) {
			autoMovement = AutoMovement.STOP_AT_AUTOLINE;
		} else if (OI.driveLong.get()) {
			autoMovement = AutoMovement.CONTINUE_TO_END;
		} else if (OI.shootAfterGear.get()) {
			autoMovement = AutoMovement.SHOOT_AFTER_GEAR_DROP;
		}

		return autoMovement;
	}
}
