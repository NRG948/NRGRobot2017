package org.usfirst.frc.team948.utilities;

import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;

public class MultiChannelLED {
	
	private final Solenoid red;
	private final Solenoid blue;
	private final Solenoid green;
	
	public MultiChannelLED(int redChannelNumber, int greenChannelNumber, int blueChannelNumber){
		red = new Solenoid(redChannelNumber);
		blue = new Solenoid(blueChannelNumber);
		green = new Solenoid(greenChannelNumber);
	}
	
	public void turnRedOn() {
		red.set(true);
	}

	public void turnRedOff() {
		red.set(false);
	}

	public boolean isRedOn() {
		return red.get();
	}
	
	public void turnBlueOn() {
		blue.set(true);
	}

	public void turnBlueOff() {
		blue.set(false);
	}

	public boolean isBlueOn() {
		return blue.get();
	}
	
	public void turnGreenOn() {
		green.set(true);
	}

	public void turnGreenOff() {
		green.set(false);
	}

	public boolean isGreenOn() {
		return green.get();
	}
}
