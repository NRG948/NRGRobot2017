package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

public class BallCollector extends Subsystem {

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}

	public void rawCollect(double power) {
		// turn on the ball collector
		RobotMap.ballCollectorInOutMotor.set(power);
		RobotMap.ballCollectorUpDownMotor.set(power);
	}

	public void rawStop() {
		// turn off the ball collector
		RobotMap.ballCollectorInOutMotor.disable();
		RobotMap.ballCollectorUpDownMotor.disable();
	}
}
