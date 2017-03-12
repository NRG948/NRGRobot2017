package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

public class BallFeeder extends Subsystem {

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

	public void start(double power) {
		// bottom motor ejects ball with negative power;
		RobotMap.shooterWheelBottom.set(-1);
		RobotMap.conveyorBelt.set(power);
	}

	public void stop() {
		RobotMap.shooterWheelBottom.disable();
		RobotMap.conveyorBelt.disable();
	}

}
