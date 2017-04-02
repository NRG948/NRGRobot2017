package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.MultiChannelLED;

/**
 *
 */
public class GearLEDControler {
	private MultiChannelLED gearLEDs;
	private boolean gettingData = false;
	private boolean onTarget = false;

	public GearLEDControler(MultiChannelLED gearLEDs) {
		this.gearLEDs = gearLEDs;
	}

	public void updateLights() {
		if (OI.climberButton.get()) {
			gearLEDs.turnRedOn();
			gearLEDs.turnBlueOn();
		} else if (!RobotMap.lowerGearSensor.get()) {
			gearLEDs.turnBlueOn();

			if (gettingData && onTarget) {
				gearLEDs.turnRedOn();
			} else {
				gearLEDs.turnRedOff(); // not on target OR not getting data
			}

			gearLEDs.turnGreenOff();
		} else if (!RobotMap.upperGearSensor.get()) {
			gearLEDs.turnRedOn();

			if (gettingData && onTarget) {
				gearLEDs.turnGreenOn();
			} else {
				gearLEDs.turnGreenOff();
			}

			gearLEDs.turnBlueOff();
		} else {
			gearLEDs.turnBlueOff();
			gearLEDs.turnRedOff();

			if (gettingData && onTarget) {
				gearLEDs.turnGreenOn();
			} else {
				gearLEDs.turnGreenOff();
			}
		}

	}

	public void setGettingData(boolean isGettingData) {
		gettingData = isGettingData;
	}

	public void writeData(boolean isOnTarget) {
		if (gettingData) {
			onTarget = isOnTarget;
		} else {
			System.out.println("You need to set gettingData to true...");
		}
	}
}
