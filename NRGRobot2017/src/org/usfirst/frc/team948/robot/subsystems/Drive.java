package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.ManualDrive;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drive extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        setDefaultCommand(new ManualDrive());
    }
    public void tankDrive(double leftPower, double rightPower){
    	RobotMap.motorFrontLeft.set(leftPower);
    	RobotMap.motorFrontRight.set(rightPower*-1);
    	RobotMap.motorBackLeft.set(leftPower);
    	RobotMap.motorBackRight.set(rightPower*-1);
    }
    public void stop() {
    	RobotMap.motorBackLeft.disable();
    	RobotMap.motorBackRight.disable();
    	RobotMap.motorFrontLeft.disable();
    	RobotMap.motorFrontRight.disable();
    }
}

