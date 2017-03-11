package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.MathUtil;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * 
 */

public class Shooter extends Subsystem {

	public static final int MAX_RPM_SAMPLES = 10;

	private static final double TICKS_PER_REVOLUTION = 256;// value needs to be
															// tested
	private double[] RPMValues = new double[MAX_RPM_SAMPLES];
	private double TARGET_RPM = 9001.0;
	private double TOP_PERCENTAGE = 0.7;
	private double RPM_TOLERENCE = 3.0;
	private double currentPower = 0.0;
	private int index;
	private int currentCount = 0;

	public volatile double currentRPM = 0.0;

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

	private void addRPMValueToArray() {
		RPMValues[index] = currentRPM;
		index++;
		index %= RPMValues.length;
		currentCount = Math.min(currentCount + 1, MAX_RPM_SAMPLES);
	}

	public double getAverageRPM(int numberOfValues) throws Exception {
		if(currentCount == 0)
			return 0;
		if (numberOfValues <= 0) {
			numberOfValues = 1;
		}else if(currentCount < numberOfValues){
		 numberOfValues = currentCount;
		}
		numberOfValues = Math.min(numberOfValues, currentCount);
		double sum = 0;
		for (int i = 1; i <= numberOfValues; i++) {
			sum += RPMValues[(index - i)%RPMValues.length];
		}
		return sum / numberOfValues;
	}

	public void updateRPM() {
		currentRPM = (RobotMap.shooterEncoder.getRate() / TICKS_PER_REVOLUTION) * 60;
		addRPMValueToArray();
	}
	
	public void updatePower(){
		updateRPM();
		double rollingAvRPM;
		try {
			rollingAvRPM = getAverageRPM(10);
		} catch (Exception e) {
			e.printStackTrace();
			rollingAvRPM = currentRPM;
		}
		double targetValue = RobotMap.preferences.getDouble("SHOOTER_TOP_PERCENTAGE", TOP_PERCENTAGE)*RobotMap.preferences.getDouble("TARGET_SHOOTER_RPM", TARGET_RPM);
		double delta = MathUtil.deadband(rollingAvRPM -targetValue ,RobotMap.preferences.getDouble("SHOOTER_RPM_TOLERENCE ", RPM_TOLERENCE));
		if(delta != 0){
			double htanValue = (RobotMap.preferences.getDouble("SHOOTER_CORRECTION_CONSTANT_M", 2.0)*delta) / targetValue;
			htanValue = Math.tanh(htanValue);
			htanValue += RobotMap.preferences.getDouble("SHOOTER_CORRECTION_CONSTANT_A", 0.0);
			htanValue = Math.copySign(Math.min(1.0, Math.abs(htanValue)),htanValue);
			currentPower += htanValue;
		}
		RobotMap.shooterWheel.set(currentPower);
	}

}
