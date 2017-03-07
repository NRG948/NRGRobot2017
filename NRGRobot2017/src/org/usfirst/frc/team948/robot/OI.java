package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.robot.Robot.AutoPosition;
import org.usfirst.frc.team948.robot.commands.BallCollect;
import org.usfirst.frc.team948.robot.commands.ClimbPower;
import org.usfirst.frc.team948.robot.commands.FlipCameraLight;
import org.usfirst.frc.team948.robot.commands.Interrupt;
import org.usfirst.frc.team948.robot.commands.ManualDriveStraight;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.StopTestDrive;
import org.usfirst.frc.team948.robot.commands.TestDrive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public static Joystick leftJoystick = new Joystick(1);
	public static Joystick rightJoystick = new Joystick(2);
	public static JoystickButton leftTrigger = new JoystickButton(leftJoystick, 1);
	public static JoystickButton interruptButton = new JoystickButton(leftJoystick, 10);
	public static JoystickButton resetSensorsButton = new JoystickButton(leftJoystick, 11);
	public static JoystickButton switchHighGear = new JoystickButton(rightJoystick, 10);
	public static JoystickButton switchLowGear = new JoystickButton(rightJoystick, 11);
	public static JoystickButton climberForwards = new JoystickButton(leftJoystick, 6);
	public static JoystickButton climberReverse = new JoystickButton(leftJoystick, 7);
	public static JoystickButton cameraLightSwitch = new JoystickButton(leftJoystick, 8);
	public static JoystickButton acquireBalls = new JoystickButton(rightJoystick, 7);
	public static JoystickButton rightTrigger = new JoystickButton(rightJoystick, 1);

	public static final Joystick arduinoJoystick = new Joystick(0);
	public static final Button climberButton = new JoystickButton(arduinoJoystick, 1);
	public static final Button fieldBlue = new JoystickButton(arduinoJoystick, 3);
	public static final Button fieldRed = new JoystickButton(arduinoJoystick, 2);
	public static final Button autoLeft = new JoystickButton(arduinoJoystick, 4);
	// the middle position is when both the left and right button states are
	// false
	public static final Button autoMiddle = new JoystickButton(arduinoJoystick, 5);
	public static final Button autoRight = new JoystickButton(arduinoJoystick, 6);
	public static final Button stay = new JoystickButton(arduinoJoystick, 10);
	public static final Button driveAirship = new JoystickButton(arduinoJoystick, 9);
	public static final Button driveShort = new JoystickButton(arduinoJoystick, 8);
	public static final Button driveLong = new JoystickButton(arduinoJoystick, 7);

	public static void buttonInit() {
		rightTrigger.whenPressed(new ShiftGears(true));
		rightTrigger.whenReleased(new ShiftGears(false));
		leftTrigger.whileHeld(new ManualDriveStraight());
		resetSensorsButton.whenPressed(new ResetSensors());
		switchHighGear.whenPressed(new ShiftGears(true));
		switchLowGear.whenPressed(new ShiftGears(false));
		climberForwards.whileHeld(new ClimbPower(true));
		climberReverse.whileHeld(new ClimbPower(false));
		cameraLightSwitch.whenPressed(new FlipCameraLight());
		acquireBalls.toggleWhenActive(new BallCollect());
		interruptButton.whenPressed(new Interrupt());
	}

	public static AutoPosition getAutoPosition() {
		AutoPosition autoPosition = null;
		if (RobotMap.usePositionChooser) {
			autoPosition = Robot.autoPositionChooser.getSelected();
		} else {
			if (OI.fieldRed.get()) {
				if (OI.autoLeft.get()) {
					System.out.println("Red Left");
					autoPosition = AutoPosition.RED_LEFT;
				} else if (OI.autoRight.get()) {
					System.out.println("Red Right");
					autoPosition = AutoPosition.RED_RIGHT;
				} else if (OI.autoMiddle.get()) {
					System.out.println("Red Center");
					autoPosition = AutoPosition.RED_CENTER;
				}
			} else if (OI.fieldBlue.get()) {
				if (OI.autoLeft.get()) {
					System.out.println("Blue Left");
					autoPosition = AutoPosition.BLUE_LEFT;
				} else if (OI.autoRight.get()) {
					System.out.println("Blue Right");
					autoPosition = AutoPosition.BLUE_RIGHT;
				} else if (OI.autoMiddle.get()) {
					System.out.println("Blue Center");
					autoPosition = AutoPosition.BLUE_CENTER;
				}
			}
		}

		return autoPosition;
	}
}
