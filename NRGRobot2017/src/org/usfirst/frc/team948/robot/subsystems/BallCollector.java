package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.BallCollect.BallCollectDirection;

import edu.wpi.first.wpilibj.command.Subsystem;

public class BallCollector extends Subsystem {

	@Override
	protected void initDefaultCommand() {
	}

	public void rawCollect(double power, BallCollectDirection direction) {
		// turn on the ball collector
		switch (direction)
		{
			case IN:
				RobotMap.ballCollectorInOutMotor.set(power);
				RobotMap.ballCollectorUpDownMotor.set(Math.abs(power)); // Math.abs() is safeguard, as up-down motor can't go in reverse
				break;
			case OUT:
				RobotMap.ballCollectorInOutMotor.set(-power);
				break;
			case OFF:
				rawStop();
				break;
		}
	}

	public void rawStop() {
		// turn off the ball collector
		RobotMap.ballCollectorInOutMotor.disable();
		RobotMap.ballCollectorUpDownMotor.disable();
	}
}
