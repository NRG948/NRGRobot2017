package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

public class CameraLight extends Subsystem {

	@Override
	protected void initDefaultCommand() {
	}

	public void turnOn() {
		RobotMap.cameraLight.set(true);
	}

	public void turnOff() {
		RobotMap.cameraLight.set(false);
	}

	public boolean isOn() {
		return RobotMap.cameraLight.get();
	}
}
