package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.ManualDrive;
import org.usfirst.frc.team948.utilities.MathUtil;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drive extends Subsystem implements PIDOutput {

	private PIDController drivePID;
	private double PIDOutput;
	private PIDController turnPID;
	public IterativeRobot periodic = new IterativeRobot();
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

	public double drivePIDInit(double p, double i, double d) {
		drivePID = new PIDController(p, i, d, RobotMap.navx, this);
		drivePID.reset();
		drivePID.setOutputRange(-0.5, 0.5);
		PIDOutput = 0;
		drivePID.enable();
		return RobotMap.navx.getAngle();
	}

	public void driveOnHeadingInit() {
		double kp = RobotMap.prefs.getDouble(PreferenceKeys.Drive_On_Heading_P, 0.06);
		double ki = RobotMap.prefs.getDouble(PreferenceKeys.Drive_On_Heading_I, 0.003);
		double kd = RobotMap.prefs.getDouble(PreferenceKeys.Drive_On_Heading_D, 0.3);
		drivePIDInit(kp, ki, kd);

		SmartDashboard.putString("kp, ki, kd", kp + ", " + ki + ", " + kd);
		SmartDashboard.putNumber("set point", drivePID.getSetpoint());
	}

	public void driveOnHeading(double power, double heading) {
		// go straight but correct with the pidOutput
		// given the pid output, rotate accordingly
		drivePID.setSetpoint(heading);
		double error = drivePID.getSetpoint() - RobotMap.navx.getAngle();
		double outputRange = MathUtil.clampM(
				PID_MIN_OUTPUT + (Math.abs(error) / 15.0) * (PID_MAX_OUTPUT - PID_MIN_OUTPUT), 0, PID_MAX_OUTPUT);
		drivePID.setOutputRange(-outputRange, outputRange);
		double currentPIDOutput = MathUtil.clampM(PIDOutput, -PID_MAX_OUTPUT, PID_MAX_OUTPUT);
		SmartDashboard.putNumber("Pid output", PIDOutput);
		SmartDashboard.putNumber("driveOnHeading error", error);
		double pL = power;
		double pR = power;

		if (currentPIDOutput > 0) {
			pR -= currentPIDOutput;
		} else {
			pL += currentPIDOutput;
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
		RobotMap.motorFrontLeft.set(-leftPower);
		RobotMap.motorBackLeft.set(-leftPower);
		RobotMap.motorFrontRight.set(rightPower);
		RobotMap.motorBackRight.set(rightPower);
	}

	public void stop() {
		RobotMap.motorBackLeft.disable();
		RobotMap.motorFrontLeft.disable();
		RobotMap.motorBackRight.disable();
		RobotMap.motorFrontRight.disable();
	}

	public void turnToHeadingInit() {
		double tP = RobotMap.prefs.getDouble(PreferenceKeys.turnP, 0);
		double tI = RobotMap.prefs.getDouble(PreferenceKeys.turnI, 0);
		double tD = RobotMap.prefs.getDouble(PreferenceKeys.turnD, 0);
		turnPID = new PIDController(tP, tI, tD, (PIDSource) RobotMap.gyro, this);
		turnPID.setSetpoint(desiredHeading);
		turnPID.setAbsoluteTolerance(tolerance);
		SmartDashboard.putNumber("desired heading", desiredHeading);
		prevError = 0;
		counter = 0;
	}

	public void turnToHeading(int desiredHeading, double power) {
		periodic.teleopPeriodic();
		double currentError = turnPID.getError();
		SmartDashboard.putNumber("TurnPID ouput", PIDOutput);

	}

	public void turnToHeadingEnd() {
		turnPID.reset();
		stop();
	}
}
