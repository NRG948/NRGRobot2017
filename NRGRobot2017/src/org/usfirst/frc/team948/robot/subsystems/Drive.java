package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.ManualDrive;
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
	private double pidOutput;
	private PIDController turnPID;
	public IterativeRobot periodic = new IterativeRobot();
	private double desiredHeading;
	private double tolerance;
	private int prevError;
	private int counter;
	
	

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
		double pL = power + pidOutput;
		double pR = power - pidOutput;
		double error = RobotMap.navx.getAngle() - drivePID.getSetpoint();
		SmartDashboard.putNumber("heading error", error);
		SmartDashboard.putNumber("pid heading error", drivePID.getError());
		SmartDashboard.putNumber("Pid output", pidOutput);
		SmartDashboard.putNumber("power straight drive", power);
		SmartDashboard.putNumber("power left", pL);
		SmartDashboard.putNumber("power right", pR);
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
	public void turnToHeadingInit(){
		double tP = RobotMap.prefs.getDouble("Turn P",RobotMap.TURN_P );
		double tI = RobotMap.prefs.getDouble("Turn I", RobotMap.TURN_I);
		double tD = RobotMap.prefs.getDouble("Turn D", RobotMap.TURN_D);
		turnPID = new PIDController(tP,tI,tD, (PIDSource)RobotMap.gyro,this);
		turnPID.setSetpoint(desiredHeading);
		turnPID.setAbsoluteTolerance(tolerance);
		SmartDashboard.putNumber("desired heading", desiredHeading);
		prevError = 0;
		counter = 0;
		
		
		
		
	}
	public void turnToHeading(int desiredHeading,double power){
		periodic.teleopPeriodic();
		double currentError = turnPID.getError();
		SmartDashboard.putNumber("TurnPID ouput", PIDOutput );
		
	}
	public void turnToHeadingEnd(){
		turnPID.reset();
		stop();
	}
}
