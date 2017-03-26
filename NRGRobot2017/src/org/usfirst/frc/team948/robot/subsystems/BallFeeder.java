package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.StopSuckingBalls;

import edu.wpi.first.wpilibj.command.Subsystem;

public class BallFeeder extends Subsystem {

	private static final double DEFAULT_CONVEYOR_POWER = 0.5;
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new StopSuckingBalls());
	}

	public void start() {
		start(DEFAULT_CONVEYOR_POWER);
	}

	public void start(double conveyorPower) {
		// bottom motor ejects ball with negative power
		RobotMap.shooterWheelBottom.set(-1);
		RobotMap.conveyorBelt.set(conveyorPower);
	}

	public void stop() {
		RobotMap.shooterWheelBottom.set(0);
//		RobotMap.shooterWheelBottom.disable();
		RobotMap.conveyorBelt.disable();
	}
}
