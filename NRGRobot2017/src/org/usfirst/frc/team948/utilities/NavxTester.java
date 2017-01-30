package org.usfirst.frc.team948.utilities;

import org.usfirst.frc.team948.robot.RobotMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NavxTester {
	private static AHRS ahrs = RobotMap.navx();
	
	public static void display(){
		SmartDashboard.putNumber("Raw gyro x-axis", navx.getRawGyroX());
		SmartDashboard.putNumber("Raw gyro y-axis", navx.getRawGyroY());
		SmartDashboard.putNumber("Raw gyro z-axis" , navx.getRawGyroZ());
		SmartDashboard.putNumber("X acceleration", navx.getRawAccelX());
		SmartDashboard.putNumber("Y acceleration", navx.getRawAccelY());
		SmartDashboard.putNumber("Z acceleration", navx.getRawAccelZ());
		
	}
	
}
