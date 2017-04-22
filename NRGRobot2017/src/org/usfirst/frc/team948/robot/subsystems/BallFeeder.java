package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

public class BallFeeder extends Subsystem {
	private static final double SHOOTER_GATE_OPEN = 0.5;
	private static final double SHOOTER_GATE_CLOSE = 0.1;
	private static final double DEFAULT_CONVEYOR_POWER = 0.5;
	
	@Override
	protected void initDefaultCommand() {
	}

	public void stop() {
		RobotMap.shooterGate.setDisabled();
	}
	
	public void openBallGate() {
		RobotMap.shooterGate.set(SHOOTER_GATE_OPEN);
	}
	
	public void closeBallGate() {
		RobotMap.shooterGate.set(SHOOTER_GATE_CLOSE);
	}
}
