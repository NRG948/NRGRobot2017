package org.usfirst.frc.team948.utilities;

import edu.wpi.first.wpilibj.AnalogInput;

public class UltrasonicSensor {

	private AnalogInput ultrasonicSensor;

	public UltrasonicSensor(int port) {
		ultrasonicSensor = new AnalogInput(port);
	}

	public double getDistanceInches() {
		// obtained from the calibration of the sensor.
		return 116.63 * ultrasonicSensor.getVoltage() - 1.9821;
	}

	public double getDistanceInches(double volts) {
		// obtained from the calibration of the 1030 sensor
		return 116.63 * volts - 1.9821;
	}

	public double getVoltage() {
		return ultrasonicSensor.getVoltage();
	}

}
