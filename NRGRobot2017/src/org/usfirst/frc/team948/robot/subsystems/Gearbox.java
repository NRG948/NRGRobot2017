package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Gearbox extends Subsystem {

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

	public void setHighGear() {
		RobotMap.gearboxSolenoid.set(RobotMap.IN_HIGH_GEAR);
	}
	
	public void setLowGear() {
		RobotMap.gearboxSolenoid.set(RobotMap.IN_LOW_GEAR);
	}
	
	public boolean isHighGear()
	{
		return RobotMap.gearboxSolenoid.get() == RobotMap.IN_HIGH_GEAR;
	}
}
