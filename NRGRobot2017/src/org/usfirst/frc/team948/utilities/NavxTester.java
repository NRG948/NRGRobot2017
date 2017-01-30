package org.usfirst.frc.team948.utilities;

import org.usfirst.frc.team948.robot.RobotMap;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NavxTester {
	private static AHRS ahrs = RobotMap.navx;
	
	public static void display(){
		SmartDashboard.putNumber("Raw gyro x-axis", RobotMap.navx.getRawGyroX());
		SmartDashboard.putNumber("Raw gyro y-axis", RobotMap.navx.getRawGyroY());
		SmartDashboard.putNumber("Raw gyro z-axis" , RobotMap.navx.getRawGyroZ());
		SmartDashboard.putNumber("X acceleration", RobotMap.navx.getRawAccelX());
		SmartDashboard.putNumber("Y acceleration", RobotMap.navx.getRawAccelY());
		SmartDashboard.putNumber("Z acceleration", RobotMap.navx.getRawAccelZ());
		
	}
	
}
