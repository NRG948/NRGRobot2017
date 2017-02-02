package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.ManualDrive;
import org.usfirst.frc.team948.utilities.MathUtil;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDController.NullTolerance;
import edu.wpi.first.wpilibj.PIDController.Tolerance;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drive extends Subsystem implements PIDOutput {

	private PIDController drivePID;
	private volatile double PIDOutput;
	// public IterativeRobot periodic = new IterativeRobot();
	private double desiredHeading;
	private double tolerance;
	private int prevError;
	private int counter;

	private final double PID_MIN_OUTPUT = 0.05;
	private final double PID_MAX_OUTPUT = 0.5;

	public void initDefaultCommand() {
		setDefaultCommand(new ManualDrive());
	}

	@Override
	public void pidWrite(double output) {
		PIDOutput = output;
	}

	public void drivePIDInit(double p, double i, double d, double setPoint, double tolerance, int toleranceBuffLength) {
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
		double kp = RobotMap.preferences.getDouble(PreferenceKeys.Drive_On_Heading_P, 0.081);
		double ki = RobotMap.preferences.getDouble(PreferenceKeys.Drive_On_Heading_I, 0.016);
		double kd = RobotMap.preferences.getDouble(PreferenceKeys.Drive_On_Heading_D, 0.072);
		drivePIDInit(kp, ki, kd, heading, 0, 0);
		
	}

	public void driveOnHeading(double power) {
		// go straight but correct with the pidOutput
		// given the pid output, rotate accordingly
		double currentPIDOutput = PIDOutput;
		SmartDashboard.putNumber("driveOnHeading error", drivePID.getError());
		SmartDashboard.putNumber("driveOnHeading output", currentPIDOutput);
		SmartDashboard.putNumber("driveOnHeading rawPower", power);
		double pL = power;
		double pR = power;

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

	// turning in progress
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
		double tP = RobotMap.preferences.getDouble(PreferenceKeys.turnP, 0.084);
		double tI = RobotMap.preferences.getDouble(PreferenceKeys.turnI, 0.0153);
		double tD = RobotMap.preferences.getDouble(PreferenceKeys.turnD, 0.116);
		double tolerance = RobotMap.preferences.getDouble(PreferenceKeys.turnTolerance, 1.0);
		int toleranceBuffer = RobotMap.preferences.getInt(PreferenceKeys.turnToleranceBuffer, 6);
		drivePIDInit(tP, tI, tD, desiredHeading, tolerance, toleranceBuffer);
		SmartDashboard.putNumber("desired heading", desiredHeading);
		prevError = 0;
		counter = 0;
	}

	public void turnToHeading(double power) {
		double currentPIDOutput = PIDOutput;
		double scaledPower= currentPIDOutput*power;
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
}
