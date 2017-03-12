package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.utilities.MathUtil;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 */
public class Shooter extends Subsystem {

	public static final int MAX_RPM_SAMPLES = 10;
	private static final double TICKS_PER_REVOLUTION = 1024;// measured
															// 3/11/2017
	private double[] RPMValues = new double[MAX_RPM_SAMPLES];
	private double TARGET_RPM = 9001.0;
	private double TOP_PERCENTAGE = 0.7;
	private double RPM_TOLERENCE = 3.0;
	private double currentPower = 0.0;
	private int index;
	private int currentCount = 0;

	public volatile double currentRPM = 0.0;
	private double prevEncoder;
	private long prevTime;
	private boolean passedThreshold;
	private double wheelPower;
	private double prevDiff;
	private double h0;
	private double kp;

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		// setDefaultCommand(new Shoot());
	}

	// Put methods for controlling this subsystem
	public void rawShoot(double power) {
		RobotMap.shooterWheelTop.set(power);
	}

	public void stop() {
		RobotMap.shooterWheelTop.disable();
	}

	private void addRPMValueToArray() {
		RPMValues[index] = currentRPM;
		index++;
		index %= RPMValues.length;
		currentCount = Math.min(currentCount + 1, MAX_RPM_SAMPLES);
	}

	public double getAverageRPM(int numberOfValues) throws Exception {
		if (currentCount == 0)
			return 0;
		if (numberOfValues <= 0) {
			numberOfValues = 1;
		} else if (currentCount < numberOfValues) {
			numberOfValues = currentCount;
		}
		numberOfValues = Math.min(numberOfValues, currentCount);
		double sum = 0;
		for (int i = 1; i <= numberOfValues; i++) {
			sum += RPMValues[(index - i) % RPMValues.length];
		}
		return sum / numberOfValues;
	}

	public void updateRPM() {
		double currentEncoder = RobotMap.shooterEncoder.getDistance();
		long currentTime = System.nanoTime();
		currentRPM = ((currentEncoder - prevEncoder) / (currentTime - prevTime)) * 60 * 1e+9 / TICKS_PER_REVOLUTION;
		prevEncoder = currentEncoder;
		prevTime = currentTime;
		addRPMValueToArray();
	}

	public void updatePower() {
		updateRPM();
		double rollingAvRPM;
		try {
			rollingAvRPM = getAverageRPM(10);
		} catch (Exception e) {
			e.printStackTrace();
			rollingAvRPM = currentRPM;
		}
		double targetValue = RobotMap.preferences.getDouble("SHOOTER_TOP_PERCENTAGE", TOP_PERCENTAGE)
				* RobotMap.preferences.getDouble("TARGET_SHOOTER_RPM", TARGET_RPM);
		double delta = MathUtil.deadband(rollingAvRPM - targetValue,
				RobotMap.preferences.getDouble("SHOOTER_RPM_TOLERENCE ", RPM_TOLERENCE));
		if (delta != 0) {
			double htanValue = (RobotMap.preferences.getDouble("SHOOTER_CORRECTION_CONSTANT_M", 2.0) * delta)
					/ targetValue;
			htanValue = Math.tanh(htanValue);
			htanValue += RobotMap.preferences.getDouble("SHOOTER_CORRECTION_CONSTANT_A", 0.0);
			htanValue = Math.copySign(Math.min(1.0, Math.abs(htanValue)), htanValue);
			currentPower += htanValue;
		}
		setPower(currentPower);
	}

	public void setPower(double power) {
		RobotMap.shooterWheelTop.set(power);
	}

	public void rampToRPMinit() {
		passedThreshold = false;
		h0 = 0;
		prevDiff = 0;
		wheelPower = 1;
		kp = SmartDashboard.getNumber(PreferenceKeys.TAKE_HALF_BACK_P, 0.000015);
	}

	public void rampToRPM(double targetRPM) {
		updateRPM();
		SmartDashboard.putNumber("Current RPM", currentRPM);
		if (!passedThreshold) {
			if (currentRPM > targetRPM) {
				passedThreshold = true;
				wheelPower = 0.42;
			}
		} else {
			// Turn_Half_Back_P
			double diff = targetRPM - currentRPM;
			wheelPower += diff * kp;
			wheelPower = MathUtil.clamp(wheelPower, -1.0, 1.0);
			// if we just crossed over the target RPM, take back half
			if (diff * prevDiff < 0) {
				if (h0 != 0) {
					wheelPower = (wheelPower + h0) / 2;
				}
				h0 = wheelPower;
			}
			prevDiff = diff;
		}
		setPower(wheelPower);
		SmartDashboard.putNumber("Shooter output", wheelPower);
		
	}

	public void rampToRPMEnd() {
		setPower(0);
	}

}
