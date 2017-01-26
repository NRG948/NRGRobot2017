package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.ManualDrive;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drive extends Subsystem implements PIDOutput {

	private PIDController drivePID;
	private double pidOutput;

	@Override
	public void pidWrite(double output) {
		pidOutput = output;
	}

	public void driveOnHeadingInit(double heading) {
		double kp = RobotMap.prefs.getDouble("Heading_P", RobotMap.STRAIGHT_KP);
		double ki = RobotMap.prefs.getDouble("Heading_I", RobotMap.STRAIGHT_KI);
		double kd = RobotMap.prefs.getDouble("Heading_D", RobotMap.STRAIGHT_KD);
		SmartDashboard.putString("kp, ki, kd", kp + ", " + ki + ", " + kd);
		drivePID = new PIDController(kp, ki, kd, RobotMap.navx, this);
		drivePID.setSetpoint(heading);
		drivePID.setOutputRange(-0.5, 0.5);
		drivePID.enable();
		SmartDashboard.putNumber("set point ", drivePID.getSetpoint());
	}

	public void driveOnHeading(double power) {
		// go straight but correct with the pidOutput
		// given the pid output, rotate accordingly
		double pFL = power - pidOutput;
		double pFR = power + pidOutput;
		double pBL = power + pidOutput;
		double pBR = power - pidOutput;
		double pL = power + pidOutput;
		double pR = power - pidOutput;
		double error = RobotMap.navx.getAngle() - drivePID.getSetpoint();
		SmartDashboard.putNumber("heading error", error);
		SmartDashboard.putNumber("Pid output", pidOutput);
		SmartDashboard.putNumber("power straight drive", power);
		RobotMap.motorFrontLeft.set(pFL);
		RobotMap.motorFrontRight.set(pFR * -1);
		RobotMap.motorBackLeft.set(pBL);
		RobotMap.motorBackRight.set(pBR * -1);
		tankDrive(pL, pR);
	}

	public void driveOnHeadingEnd() {
		drivePID.reset();
		drivePID.free();
		pidOutput = 0;
		stop();
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ManualDrive());
	}

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
}
