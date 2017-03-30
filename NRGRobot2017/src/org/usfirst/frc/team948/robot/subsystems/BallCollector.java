package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

public class BallCollector extends Subsystem {

	@Override
	protected void initDefaultCommand() {
	}

	public void rawCollect(double power, boolean in) {
		// turn on the ball collector
		if (in)
		{			
			RobotMap.ballCollectorInOutMotor.set(power);
			RobotMap.ballCollectorUpDownMotor.set(Math.abs(power)); // Math.abs() is safeguard, as up-down motor can't go in reverse
		}
		else
		{
			RobotMap.ballCollectorInOutMotor.set(-power);
		}
	}

	public void rawStop() {
		// turn off the ball collector
		RobotMap.ballCollectorInOutMotor.disable();
		RobotMap.ballCollectorUpDownMotor.disable();
	}
}
