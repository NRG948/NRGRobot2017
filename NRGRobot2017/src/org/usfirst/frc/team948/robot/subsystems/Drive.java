package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.Robot;
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
	private double prevError;
	private int counter;
	private double autonomousHeading = 0;

	private static final double PID_MIN_OUTPUT = 0.05;
	private static final double PID_MAX_OUTPUT = 0.5;
	private static final double SLOW_DOWN_ERROR = 5.0;
	private static final double MIN_POWER_TURN = 0.25;

	private static final double DEFAULT_DRIVE_LOWGEAR_P = 0.081;
	private static final double DEFAULT_DRIVE_LOWGEAR_I = 0.016;
	private static final double DEFAULT_DRIVE_LOWGEAR_D = 0.072;

	private static final double DEFAULT_DRIVE_HIGHGEAR_P = 0.081;
	private static final double DEFAULT_DRIVE_HIGHGEAR_I = 0.016;
	private static final double DEFAULT_DRIVE_HIGHGEAR_D = 0.072;

	private static final double DEFAULT_TICKS_PER_FOOT = 1480;
	private static final double DEFAULT_TICKS_PER_FOOT_TOLERANCE = DEFAULT_TICKS_PER_FOOT / 12;

	private double kp;
	private double ki;
	private double kd;
	private double turnError;
	private double turnTolerance;

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
		SmartDashboard.putString("Current PID kp, ki, kd",
				drivePID.getP() + ", " + drivePID.getI() + ", " + drivePID.getD());
		SmartDashboard.putNumber("Current PID setpoint", drivePID.getSetpoint());
	}

	public void driveOnHeadingInit(double heading) {
		if (Robot.gearbox.isHighGear()) {
			kp = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_HIGH_GEAR_P, DEFAULT_DRIVE_HIGHGEAR_P);
			ki = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_HIGH_GEAR_I, DEFAULT_DRIVE_HIGHGEAR_I);
			kd = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_HIGH_GEAR_D, DEFAULT_DRIVE_HIGHGEAR_D);
		} else {
			kp = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_LOW_GEAR_P, DEFAULT_DRIVE_LOWGEAR_P);
			ki = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_LOW_GEAR_I, DEFAULT_DRIVE_LOWGEAR_I);
			kd = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_LOW_GEAR_D, DEFAULT_DRIVE_LOWGEAR_D);
		}
		drivePIDInit(kp, ki, kd, heading, 0, 0);
	}

	public void driveOnHeading(double power) {
		double error = drivePID.getError();
		double outputRange = 1;
//				MathUtil.clamp(
//				PID_MIN_OUTPUT + (Math.abs(error) / 15.0) * (PID_MAX_OUTPUT - PID_MIN_OUTPUT), 0, PID_MAX_OUTPUT);
		drivePID.setOutputRange(-outputRange, outputRange);

		double currentPIDOutput = MathUtil.clamp(PIDOutput, -PID_MAX_OUTPUT, PID_MAX_OUTPUT);

		SmartDashboard.putNumber("driveOnHeading PID error", drivePID.getError());
		SmartDashboard.putNumber("driveOnHeading PID output", currentPIDOutput);
		SmartDashboard.putNumber("driveOnHeading rawPower", power);

		double pL = power;
		double pR = power;

		// go straight but correct with the pidOutput
		// given the pid output, rotate accordingly

		if (power > 0) { // moving forward
			if (currentPIDOutput > 0) {
				// turning to the left because right is too high -> decrease
				// right
				pR -= currentPIDOutput;
			} else {
				// turning to the right because left is too high-> decrease left
				pL += currentPIDOutput;
			}
		} else { // moving backward
			if (currentPIDOutput > 0) {
				// turning to the right because left is too high -> decrease
				// left
				pL += currentPIDOutput;
			} else {
				// turning to the left because right is too high -> decrease
				// right
				pR -= currentPIDOutput;
			}
		}

		SmartDashboard.putNumber("Left driveOnHeading power", pL);
		SmartDashboard.putNumber("Right driveOnHeading power", pR);
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
		SmartDashboard.putNumber("turnToHeading desired heading", desiredHeading);
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

	public void turnToHeadingEnd(double newHeading) {
		setAutonomousHeading(newHeading);
		drivePID.reset();
		stop();
	}

	public boolean isOnHeading() {
		return drivePID.onTarget();
	}

	public void turnToHeadingInit2(double desiredHeading) {
		setAutonomousHeading(desiredHeading);
		turnError = desiredHeading - RobotMap.continuousGyro.getAngle();
		turnTolerance = RobotMap.preferences.getDouble(PreferenceKeys.TURN_TOLERANCE, 1.0);
		SmartDashboard.putNumber("desired heading", desiredHeading);
	}

	public void turnToHeading2(double power) {
		turnError = getAutonomousHeading() - RobotMap.continuousGyro.getAngle();
		double adjustedPower = Math.abs(turnError) > SLOW_DOWN_ERROR ? power : MIN_POWER_TURN;
		SmartDashboard.putNumber("turnToHeading2 turnError", turnError);
		SmartDashboard.putNumber("turnToHeading2 scaledPower", adjustedPower);
		adjustedPower = Math.copySign(adjustedPower, turnError);
		tankDrive(adjustedPower, -adjustedPower);
	}

	public void turnToHeadingEnd2(double newHeading) {
		stop();
	}

	public boolean isOnHeading2() {
		return turnError <= turnTolerance;
	}

	public double getFeetFromUltrasoundVolts() {
		return (RobotMap.ultrasound.getVoltage() - 0.0255) / (.0242 * 12);
	}

	public double getUltrasoundVolts() {
		return RobotMap.ultrasound.getVoltage();
	}

	public double getTicksFromFeet(double feet) {
		return feet * RobotMap.preferences.getDouble(PreferenceKeys.TICKS_PER_FOOT, DEFAULT_TICKS_PER_FOOT);
	}

	public double getTicksPerFoot() {
		return RobotMap.preferences.getDouble(PreferenceKeys.TICKS_PER_FOOT, DEFAULT_TICKS_PER_FOOT);
	}

	public double getTicksPerFootTolerance() {
		return RobotMap.preferences.getDouble(PreferenceKeys.TICKS_PER_FOOT_TOLERANCE,
				DEFAULT_TICKS_PER_FOOT_TOLERANCE);
	}

	public double getAutonomousHeading() {
		return autonomousHeading;
	}

	public void setAutonomousHeading(double autonomousHeading) {
		this.autonomousHeading = autonomousHeading;
	}
}
