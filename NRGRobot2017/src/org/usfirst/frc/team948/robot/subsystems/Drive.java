package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.ManualDrive;
import org.usfirst.frc.team948.utilities.MathUtil;
import org.usfirst.frc.team948.utilities.PreferenceKeys;
import org.usfirst.frc.team948.utilities.SmartDashboardGroups;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 *
 */
public class Drive extends Subsystem implements PIDOutput, Sendable {

	public enum Direction {
		FORWARD, BACKWARD
	}

	private PIDController drivePID;
	private volatile double PIDOutput;
	private double prevTurnError;
	private int cyclesOnTarget;
	private double autonomousHeading = 0;

	private static final double PID_MIN_OUTPUT = 0.05;
	private static final double PID_MAX_OUTPUT = 0.5;
	private static final double SLOW_DOWN_ERROR = 10.0;
	private static final double MIN_POWER_TURN = 0.15;

	private static final double DEFAULT_DRIVE_LOWGEAR_P = 0.081;
	private static final double DEFAULT_DRIVE_LOWGEAR_I = 0.016;
	private static final double DEFAULT_DRIVE_LOWGEAR_D = 0.072;
	private static final double DEFAULT_DRIVE_LOWGEAR_F = 0.0;

	private static final double DEFAULT_DRIVE_HIGHGEAR_P = 0.081;
	private static final double DEFAULT_DRIVE_HIGHGEAR_I = 0.016;
	private static final double DEFAULT_DRIVE_HIGHGEAR_D = 0.072;
	private static final double DEFAULT_DRIVE_HIGHGEAR_F = 0.0;

	private static final double DEFAULT_TICKS_PER_INCH = 121;
	private final double DEFAULT_DISTANCE_TOLERANCE = 1.0 * DEFAULT_TICKS_PER_INCH;

	private static final double GOOD_VOLTAGE_THRESHOLD = 10.0;
	private static final double LOW_VOLTAGE_THRESHOLD = 7.0;
	private static final double SCALE_BACK_FACTOR = 0.97;

	private double kp;
	private double ki;
	private double kd;
	private double kf;
	private double turnError;
	private double turnTolerance;
	private int requiredCyclesOnTarget;
	private double driveScaleFactor = 1.0;

	public double getTurnError() {
		return turnError;
	}

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
		if (SmartDashboardGroups.PID) {
			SmartDashboard.putString("Current PID kp, ki, kd, kf",
					drivePID.getP() + ", " + drivePID.getI() + ", " + drivePID.getD() + ", " + drivePID.getF());
			SmartDashboard.putNumber("Current PID setpoint", drivePID.getSetpoint());
		}
	}

	public void driveOnHeadingInit(double desiredHeading) {
		if (Robot.gearbox.isHighGear()) {
			kp = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_HIGH_GEAR_P, DEFAULT_DRIVE_HIGHGEAR_P);
			ki = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_HIGH_GEAR_I, DEFAULT_DRIVE_HIGHGEAR_I);
			kd = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_HIGH_GEAR_D, DEFAULT_DRIVE_HIGHGEAR_D);
			kf = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_HIGH_GEAR_F, DEFAULT_DRIVE_HIGHGEAR_F);
		} else {
			kp = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_LOW_GEAR_P, DEFAULT_DRIVE_LOWGEAR_P);
			ki = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_LOW_GEAR_I, DEFAULT_DRIVE_LOWGEAR_I);
			kd = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_LOW_GEAR_D, DEFAULT_DRIVE_LOWGEAR_D);
			kf = RobotMap.preferences.getDouble(PreferenceKeys.DRIVE_ON_HEADING_LOW_GEAR_F, DEFAULT_DRIVE_LOWGEAR_F);
		}
		drivePIDInit(kp, ki, kd, desiredHeading, 0, 0);
	}
	
	public void driveOnHeadingInit() {
		driveOnHeadingInit(0);
	}

	public void driveOnHeading(double power, double desiredHeading) {
		drivePID.setSetpoint(desiredHeading);
		setAutonomousHeading(desiredHeading);
		double error = drivePID.getError();
		double outputRange = MathUtil.clamp(
				PID_MIN_OUTPUT + (Math.abs(error) / 15.0) * (PID_MAX_OUTPUT - PID_MIN_OUTPUT), 0, PID_MAX_OUTPUT);
		drivePID.setOutputRange(-outputRange, outputRange);

		double currentPIDOutput = MathUtil.clamp(PIDOutput + kf, -PID_MAX_OUTPUT, PID_MAX_OUTPUT);

		if (SmartDashboardGroups.PID) {
			SmartDashboard.putNumber("driveOnHeading PID error", drivePID.getError());
			SmartDashboard.putNumber("driveOnHeading PID output", currentPIDOutput);
			SmartDashboard.putNumber("driveOnHeading rawPower", power);
			SmartDashboard.putNumber("desired heading", desiredHeading);
		}
		
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

		if (SmartDashboardGroups.PID) {
			SmartDashboard.putNumber("Left driveOnHeading power", pL);
			SmartDashboard.putNumber("Right driveOnHeading power", pR);
		}
		
		tankDrive(pL, pR);
	}

	public void driveOnHeadingEnd() {
		drivePID.reset();
		drivePID.free();
		PIDOutput = 0;
		stop();
	}

	public void tankDrive(double leftPower, double rightPower) {
		double batteryVoltage = DriverStation.getInstance().getBatteryVoltage();

		if (batteryVoltage < LOW_VOLTAGE_THRESHOLD) {
			driveScaleFactor *= SCALE_BACK_FACTOR;
		} else if (batteryVoltage > GOOD_VOLTAGE_THRESHOLD) {
			driveScaleFactor = 1.0;
		}

		leftPower *= driveScaleFactor;
		rightPower *= driveScaleFactor;

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

	// public void turnToHeadingInit(double desiredHeading) {
	// double tP = RobotMap.preferences.getDouble(PreferenceKeys.TURN_P, 0.084);
	// double tI = RobotMap.preferences.getDouble(PreferenceKeys.TURN_I,
	// 0.0153);
	// double tD = RobotMap.preferences.getDouble(PreferenceKeys.TURN_D, 0.116);
	// double tolerance =
	// RobotMap.preferences.getDouble(PreferenceKeys.TURN_TOLERANCE, 1.0);
	// int toleranceBuffer =
	// RobotMap.preferences.getInt(PreferenceKeys.TURN_TOLERANCE_BUFFER, 6);
	// drivePIDInit(tP, tI, tD, desiredHeading, tolerance, toleranceBuffer);
	// SmartDashboard.putNumber("turnToHeading desired heading",
	// desiredHeading);
	// prevError = 0;
	// cyclesOnTarget = 0;
	// }
	//
	// public void turnToHeading(double power) {
	// double currentPIDOutput = PIDOutput;
	// double scaledPower = currentPIDOutput * power;
	// SmartDashboard.putNumber("turnToHeading error", drivePID.getError());
	// SmartDashboard.putNumber("turnToHeading output", currentPIDOutput);
	// SmartDashboard.putNumber("turnToHeading scaledPower", scaledPower);
	// tankDrive(scaledPower, -scaledPower);
	// }
	//
	// public void turnToHeadingEnd(double newHeading) {
	// setAutonomousHeading(newHeading);
	// drivePID.reset();
	// stop();
	// }
	//
	// public boolean isOnHeading() {
	// return drivePID.onTarget();
	// }

	public void turnToHeadingInitNoPID(double desiredHeading) {
		setAutonomousHeading(desiredHeading);
		turnError = desiredHeading - RobotMap.continuousGyro.getAngle();
		requiredCyclesOnTarget = RobotMap.preferences.getInt(PreferenceKeys.TURN_TOLERANCE_BUFFER, 6);
		turnTolerance = RobotMap.preferences.getDouble(PreferenceKeys.TURN_TOLERANCE, 1.0);
		kp = RobotMap.preferences.getDouble(PreferenceKeys.TURN_P, .025);
		SmartDashboard.putNumber("desired heading", desiredHeading);
		cyclesOnTarget = 0;
		prevTurnError = 0;
	}

	public void turnToHeadingNoPID(double maxPower) {
		turnError = getAutonomousHeading() - RobotMap.continuousGyro.getAngle();
		double angularVel = prevTurnError - turnError;
		double predictedErrorNextCycle = turnError - angularVel;
		double adjustedPower = MathUtil.clamp(Math.abs(predictedErrorNextCycle * kp), MIN_POWER_TURN, maxPower);
		if (SmartDashboardGroups.PID) {
			SmartDashboard.putNumber("turnToHeadingNoPID turnError", turnError);
			SmartDashboard.putNumber("turnToHeadingNoPID scaledPower", adjustedPower);
		}
		adjustedPower = Math.copySign(adjustedPower, predictedErrorNextCycle);

		// shut power off if current or predicted error within tolerance
		if (Math.abs(turnError) <= turnTolerance) {
			cyclesOnTarget++;
			adjustedPower = 0;
		} else {
			cyclesOnTarget = 0;
		}

		if (Math.abs(predictedErrorNextCycle) <= turnTolerance) {
			adjustedPower = 0;
		}

		tankDrive(adjustedPower, -adjustedPower);
		prevTurnError = turnError;
	}

	// public void turnToHeadingNoPID(double power) {
	// turnError = getAutonomousHeading() - RobotMap.continuousGyro.getAngle();
	// double adjustedPower = Math.abs(turnError) > SLOW_DOWN_ERROR ? power :
	// MIN_POWER_TURN;
	// SmartDashboard.putNumber("turnToHeading2 turnError", turnError);
	// SmartDashboard.putNumber("turnToHeading2 scaledPower", adjustedPower);
	// adjustedPower = Math.copySign(adjustedPower, turnError);
	// if (Math.abs(turnError) <= turnTolerance) {
	// cyclesOnTarget++;
	// adjustedPower = 0;
	// } else {
	// cyclesOnTarget = 0;
	// }
	// tankDrive(adjustedPower, -adjustedPower);
	// }

	public void turnToHeadingEndNoPID(double newHeading) {
		stop();
	}

	public boolean isOnHeadingNoPID() {
		return cyclesOnTarget >= requiredCyclesOnTarget;
	}

	

	public double getTicksFromInches(double inches) {
		return inches * RobotMap.preferences.getDouble(PreferenceKeys.TICKS_PER_INCH, DEFAULT_TICKS_PER_INCH);
	}

	public double getTicksPerInch() {
		return RobotMap.preferences.getDouble(PreferenceKeys.TICKS_PER_INCH, DEFAULT_TICKS_PER_INCH);
	}

	public double getDistanceToleranceInTicks() {
		return RobotMap.preferences.getDouble(PreferenceKeys.DISTANCE_TOLERANCE_IN_TICKS, DEFAULT_DISTANCE_TOLERANCE);
	}

	public double getAutonomousHeading() {
		return autonomousHeading;
	}

	public void setAutonomousHeading(double autonomousHeading) {
		this.autonomousHeading = autonomousHeading;
	}

	@Override
	public String getSmartDashboardType() {
		return "Drive";
	}
	private ITable m_table;

	@Override
	public void initTable(ITable subtable) {
		m_table = subtable;
		updateTable();
	}

	@Override
	public ITable getTable() {
		return m_table;
	}

	public void updateTable() {
		if (m_table != null) {
			if(drivePID != null){
				m_table.putString("Drive PID kp, ki, kd, kf",
						drivePID.getP() + ", " + drivePID.getI() + ", " + drivePID.getD() + ", " + drivePID.getF());
				m_table.putNumber("Drive PID setpoint", drivePID.getSetpoint());
			}else{
				m_table.putString("Drive PID kp, ki, kd, kf", "drivePID is null.");
				m_table.putNumber("Drive PID setpoint", 0.0);
			}
			m_table.putNumber("Left Power", RobotMap.motorBackLeft.get());
			m_table.putNumber("Right Power", RobotMap.motorBackRight.get());
		}
	}
}