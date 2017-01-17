package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drive extends Subsystem implements PIDOutput {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void tankDrive(int leftPower, int rightPower){
    	RobotMap.motorFrontLeft.set(leftPower);
    	RobotMap.motorFrontRight.set(rightPower*-1);
    	RobotMap.motorBackLeft.set(leftPower);
    	RobotMap.motorBackRight.set(rightPower*-1);
    }
	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		
	}
}

