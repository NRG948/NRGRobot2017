package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.ManualDrive;
import org.usfirst.frc.team948.utilities.MathUtil;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drive extends Subsystem implements PIDOutput {

	private PIDController drivePID;
	private volatile double PIDOutput;
	// private double desiredHeading;
	// private double tolerance;
	private int prevError;
	private int counter;
	// private boolean inHighGear = false;
	// private boolean gearChanged = false;

	private static final double PID_MIN_OUTPUT = 0.05;
	private static final double PID_MAX_OUTPUT = 0.5;

	private static final double DEFAULT_DRIVE_LOWGEAR_P = 0.081;
	private static final double DEFAULT_DRIVE_LOWGEAR_I = 0.016;
	private static final double DEFAULT_DRIVE_LOWGEAR_D = 0.072;

	private static final double DEFAULT_DRIVE_HIGHGEAR_P = 0.081;
	private static final double DEFAULT_DRIVE_HIGHGEAR_I = 0.016;
	private static final double DEFAULT_DRIVE_HIGHGEAR_D = 0.072;

	private static final double TICKS_PER_FOOT = 1480;// RobotMap.preferences.getDouble(PreferenceKeys.ticksPerFoot,
													// 1480);
	private static final double TICKS_PER_FOOT_TOLERANCE = 1;// RobotMap.preferences.getDouble(PreferenceKeys.ticksPerFoot,
																// 1480);

	private double kp;
	private double ki;
	private double kd;

	public void initDefaultCommand() {
		setDefaultCommand(new ManualDrive());
	}

	@Override
	public void pidWrite(double output) {
		PIDOutput = output;
	}

	public void drivePIDInit(double p, double i, double d, double setPoint, double tolerance, int toleranceBuffLength) {
		System.out.println("P = " + p + ", I = " + i + ", D = " + d);
		drivePID = new PIDController(p, i, d, RobotMap.continuousGyro, this);
		drivePID.reset();
		drivePID.setOutputRange(-1, 1);
		if (tolerance != 0.0) {
			drivePID.setAbsoluteTolerance(tolerance);
			drivePID.setToleranceBuffer(toleranceBuffLength);
		}
		drivePID.setSetpoint(setPoint);
		PIDOutput = 0;
		drivePID.enable();
		SmartDashboard.putString("kp, ki, kd", drivePID.getP() + ", " + drivePID.getI() + ", " + drivePID.getD());
		SmartDashboard.putNumber("set point", drivePID.getSetpoint());
	}

	public void driveOnHeadingInit(double heading) {
		// if(gearChanged){
		if (RobotMap.solenoid.get() == RobotMap.IN_HIGH_GEAR) {
			kp = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_HIGH_GEAR_P, DEFAULT_DRIVE_HIGHGEAR_P);
			ki = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_HIGH_GEAR_I, DEFAULT_DRIVE_HIGHGEAR_I);
			kd = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_HIGH_GEAR_D, DEFAULT_DRIVE_HIGHGEAR_D);
			// drivePID.setPID(kp, ki, kd);
		} else {
			kp = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_LOW_GEAR_P, DEFAULT_DRIVE_LOWGEAR_P);
			ki = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_LOW_GEAR_I, DEFAULT_DRIVE_LOWGEAR_I);
			kd = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_LOW_GEAR_D, DEFAULT_DRIVE_LOWGEAR_D);
			// drivePID.setPID(kp, ki, kd);
		}
		// gearChanged = false;
		// }
		drivePIDInit(kp, ki, kd, heading, 0, 0);
	}

	public void driveOnHeading(double power) {
		// Limits the PID output proportionally to the current error
		// Prevents erratic corrections at high speed
		double error = drivePID.getError();
		double outputRange = MathUtil.clamp(
				PID_MIN_OUTPUT + (Math.abs(error) / 15.0) * (PID_MAX_OUTPUT - PID_MIN_OUTPUT), 0, PID_MAX_OUTPUT);
		drivePID.setOutputRange(-outputRange, outputRange);

		double currentPIDOutput = MathUtil.clamp(PIDOutput, -PID_MAX_OUTPUT, PID_MAX_OUTPUT);
		SmartDashboard.putNumber("driveOnHeading error", drivePID.getError());
		SmartDashboard.putNumber("driveOnHeading output", currentPIDOutput);
		SmartDashboard.putNumber("driveOnHeading rawPower", power);
		double pL = power;
		double pR = power;

		// go straight but correct with the pidOutput
		// given the pid output, rotate accordingly

		if (power > 0) {
			if (currentPIDOutput > 0) {
				pR -= currentPIDOutput;
			} else {
				pL += currentPIDOutput;
			}
		} else {
			if (currentPIDOutput > 0) {
				pL += currentPIDOutput;
			} else {
				pR -= currentPIDOutput;
			}
		}

		SmartDashboard.putNumber("Left Drive Straight output", pL);
		SmartDashboard.putNumber("Right Drive Straight output", pR);
		tankDrive(pL, pR);
	}

	public void driveOnHeadingEnd() {
		drivePID.reset();
		drivePID.free();
		PIDOutput = 0;
		stop();
	}

	public void tankDrive(double leftPower, double rightPower) {
		RobotMap.motorFrontLeft.set(leftPower);
		RobotMap.motorBackLeft.set(leftPower);
		RobotMap.motorFrontRight.set(-rightPower);
		RobotMap.motorBackRight.set(-rightPower);
	}

	public void stop() {
		RobotMap.motorBackLeft.disable();
		RobotMap.motorFrontLeft.disable();
		RobotMap.motorBackRight.disable();
		RobotMap.motorFrontRight.disable();
	}

	public void turnToHeadingInit(double desiredHeading) {
		double tP = RobotMap.preferences.getDouble(PreferenceKeys.TURN_P, 0.084);
		double tI = RobotMap.preferences.getDouble(PreferenceKeys.TURN_I, 0.0153);
		double tD = RobotMap.preferences.getDouble(PreferenceKeys.TURN_D, 0.116);
		double tolerance = RobotMap.preferences.getDouble(PreferenceKeys.TURN_TOLERANCE, 1.0);
		int toleranceBuffer = RobotMap.preferences.getInt(PreferenceKeys.TURN_TOLERANCE_BUFFER, 6);
		drivePIDInit(tP, tI, tD, desiredHeading, tolerance, toleranceBuffer);
		SmartDashboard.putNumber("desired heading", desiredHeading);
		prevError = 0;
		counter = 0;
	}

	public void turnToHeading(double power) {
		double currentPIDOutput = PIDOutput;
		double scaledPower = currentPIDOutput * power;
		SmartDashboard.putNumber("turnToHeading error", drivePID.getError());
		SmartDashboard.putNumber("turnToHeading output", currentPIDOutput);
		SmartDashboard.putNumber("turnToHeading scaledPower", scaledPower);
		tankDrive(scaledPower, -scaledPower);
	}

	public void turnToHeadingEnd() {
		drivePID.reset();
		stop();
	}

	public boolean isOnHeading() {
		return drivePID.onTarget();
	}

	// public void changeGearTracker(boolean gear) {
	// gearChanged = gear == inHighGear ? false : true;
	// inHighGear = gear;
	// }

	public double getFeetFromUltrasoundVolts() {
		return (RobotMap.ultrasound.getVoltage() - 0.0255) / (.0242 * 12);
	}

	public double getUltrasoundVolts() {
		return RobotMap.ultrasound.getVoltage();
	}

	public double getTicksFromFeet(double feet) {
		return feet * RobotMap.preferences.getDouble(PreferenceKeys.TICKS_PER_FOOT, 1480);
	}

	public double getTicksPerFoot() {
		return TICKS_PER_FOOT;
	}

	public double getTicksPerFootTolerance() {
		return TICKS_PER_FOOT_TOLERANCE;
	}
}
