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

	private static final int REQUIRED_ON_TARGET_COUNT = 3;
	public static final int MAX_RPM_SAMPLES = 10;
	private static final double TICKS_PER_REVOLUTION = 1024;// measured
															// 3/11/2017
	private double[] RPMValues = new double[MAX_RPM_SAMPLES];
	public final static double RPM_TOLERANCE = 50.0;
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
	private int onTargetCount;
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		// setDefaultCommand(new Shoot());
	}

	public void setPower(double power) {
		// Ejecting balls requires negative power values.
		RobotMap.shooterWheelTop.set(-power);
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

	public double getAverageRPM(int numberOfValues){
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

	public void rampToRPMinit() {
		onTargetCount = 0;
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
				wheelPower = 0.85;
			}
		} else {
			// Turn_Half_Back_P
			double diff = targetRPM - currentRPM;
			boolean onTargetRPM = (Math.abs(diff) <= RPM_TOLERANCE);
			if(onTargetRPM){
				onTargetCount++;
			}else{
				onTargetCount = 0;
			}
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
	public boolean onTargetRPM(){
		return onTargetCount >= REQUIRED_ON_TARGET_COUNT;
	}
	
	public void rampToRPMEnd() {
		setPower(0);
	}

}
