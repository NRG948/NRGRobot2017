package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * 
 */

public class Shooter extends Subsystem {

	public static final int MAX_RPM_SAMPLES = 100;

	private static final double TICKS_PER_REVOLUTION = 256;// value needs to be
															// tested
	private double[] RPMValues = new double[MAX_RPM_SAMPLES];
	private int index;
	private int currentCount;

	public volatile double currentRPM;

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		// setDefaultCommand(new Shoot());
	}

	// Put methods for controlling this subsystem
	public void rawShoot(double power) {
		RobotMap.shooterWheel.set(power);
	}

	public void stop() {
		RobotMap.shooterWheel.disable();
	}

	public void addRPMValueToArrays() {
		RPMValues[index] = currentRPM;
		index++;
		index %= RPMValues.length;
		currentCount = Math.min(currentCount + 1, MAX_RPM_SAMPLES);
	}

	public double getAverageRPM(int numberOfValues) {
		if (numberOfValues <= 0) {
			numberOfValues = 1;
		}
		numberOfValues = Math.min(numberOfValues, currentCount);
		double sum = 0;
		for (int i = 1; i <= numberOfValues; i++) {
			if (index - i >= 0) {
				sum += RPMValues[index - i];
			} else {
				sum += RPMValues[RPMValues.length + index - i];
			}
		}
		return sum / numberOfValues;
	}

	public void updateRPM() {
		currentRPM = RobotMap.shooterEncoder.getRate() / TICKS_PER_REVOLUTION * 60;
		// currentEncoder = RobotMap.shooterEncoder.getDistance();
		// currentTime = System.nanoTime();
		// currentRPM = ((currentEncoder - prevEncoder) / (currentTime -
		// prevTime)) * 60000000000.0;
		// prevEncoder = currentEncoder;
		// prevTime = currentTime;
	}

}
