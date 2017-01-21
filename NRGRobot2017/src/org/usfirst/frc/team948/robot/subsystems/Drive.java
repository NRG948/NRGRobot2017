package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.commands.ManualDrive;
import org.usfirst.frc.team948.utilities.PIDE;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drive extends Subsystem {
	private static PIDE sPIDE;
	private static PIDController sPID;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        setDefaultCommand(new ManualDrive());
    }
    public void rawTankDrive(double leftPower, double rightPower){
    	RobotMap.motorFrontLeft.set(leftPower);
    	RobotMap.motorFrontRight.set(-rightPower);
    	RobotMap.motorBackLeft.set(leftPower);
    	RobotMap.motorBackRight.set(-rightPower);
    }
    public void rawStop() {
    	RobotMap.motorBackLeft.disable();
    	RobotMap.motorBackRight.disable();
    	RobotMap.motorFrontLeft.disable();
    	RobotMap.motorFrontRight.disable();
    }
    
    public void intitTurnPID(){
    	sPIDE = new PIDE("turn");
    	sPID = sPIDE.con();
    }
    
    public void initDrivePID(){
       	sPIDE = new PIDE("drive");
    	sPID = sPIDE.con();
    }
}

