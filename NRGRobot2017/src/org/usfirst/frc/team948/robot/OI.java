package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.robot.commands.ClimbPower;
import org.usfirst.frc.team948.robot.commands.ManualDriveStraight;
import org.usfirst.frc.team948.robot.commands.ResetSensors;
import org.usfirst.frc.team948.robot.commands.ShiftGears;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	public static Joystick leftJoystick = new Joystick(1);
	public static Joystick rightJoystick = new Joystick(2);
	public static JoystickButton leftTrigger = new JoystickButton(leftJoystick, 1);
	public static JoystickButton rightTrigger = new JoystickButton(rightJoystick, 1);
	public static JoystickButton resetSensorsButton = new JoystickButton(leftJoystick, 11);
	public static JoystickButton switchHighGear = new JoystickButton(rightJoystick, 10);
	public static JoystickButton switchLowGear = new JoystickButton(rightJoystick, 11);
	public static JoystickButton climberForwards = new JoystickButton(leftJoystick, 6);
	public static JoystickButton climberReverse = new JoystickButton(leftJoystick, 7);

	public static void buttonInit() {
		leftTrigger.whileHeld(new ManualDriveStraight());
		resetSensorsButton.whenPressed(new ResetSensors());
		switchHighGear.whenPressed(new ShiftGears(true));
		switchLowGear.whenPressed(new ShiftGears(false));
		climberForwards.whileHeld(new ClimbPower(0.50));
		climberReverse.whileHeld(new ClimbPower(-0.35));
	}
	
}
