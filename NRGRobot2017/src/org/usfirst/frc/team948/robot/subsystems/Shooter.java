package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.Shoot;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * 
 */

public class Shooter extends Subsystem {

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		//setDefaultCommand(new Shoot());
	}
	
	//Put methods for controlling this subsystem
	public void rawShoot(double power){
		RobotMap.shooterWheel.set(power);
	}
	public void stop(){
		RobotMap.shooterWheel.disable();
	}
}
