package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.subsystems.Shooter;
import org.usfirst.frc.team948.utilities.MathUtil;
import org.usfirst.frc.team948.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RampToRPM extends Command {
	private Shooter shooterWheel = Robot.shooter;
	private boolean passedThreshold;
	private double wheelOutput;
	private double targetRPM;
	private double prevDiff;
	private double p;
	private double H0;

	public RampToRPM(double RPM) {
		requires(Robot.shooter);
		targetRPM = RPM;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		wheelOutput = 1;
		p = SmartDashboard.getNumber(PreferenceKeys.TAKE_HALF_BACK_P, 0.000015);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		shooterWheel.updateRPM();
		shooterWheel.addRPMValueToArrays();
		if (!passedThreshold) {
			RobotMap.shooterWheel.set(wheelOutput);
			if (shooterWheel.currentRPM - targetRPM > 0) {
				passedThreshold = true;
				wheelOutput = 0.42;
			}
			SmartDashboard.putNumber("shooter output", wheelOutput);
		} else {
			//Turn_Half_Back_P
			double diff = targetRPM - shooterWheel.currentRPM;
			wheelOutput += diff * p;
			wheelOutput = MathUtil.clamp(wheelOutput, -1.0, 1.0);
			if (diff * prevDiff < 0) {
				if (H0 != 0) {
					wheelOutput = (wheelOutput + H0) / 2;
				}
				H0 = wheelOutput;
			}
			RobotMap.shooterWheel.set(wheelOutput);
			SmartDashboard.putNumber("Shooter output", wheelOutput);
			prevDiff = diff;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
