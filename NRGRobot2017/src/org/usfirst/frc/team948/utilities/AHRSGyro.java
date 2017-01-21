package org.usfirst.frc.team948.utilities;

import org.usfirst.frc.team948.robot.RobotMap;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;

public class AHRSGyro extends GyroBase implements PIDSource, Gyro, LiveWindowSendable {

	private static AHRS ahrs = RobotMap.ahrs;
	private double prevAngle = 0;
	private int rots = 0;
	
	@Override
	public void calibrate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		ahrs.zeroYaw();
		prevAngle = 0;
		rots = 0;
	}

	@Override
	public double getAngle() {
		// TODO Auto-generated method stub
		double yaw = ahrs.getYaw();
		double angle = yaw < 0 ? 360 - yaw : yaw;
		if(prevAngle > 270 && angle < 90){
			rots++;
		}else if(prevAngle < 90 && angle > 270){
			rots++;
		}
		prevAngle = angle;
		return yaw;
	}

	@Override
	public double getRate() {
		// TODO Auto-generated method stub
		return ahrs.getRawGyroX();
	}
	
	@Override
	public double pidGet() {
		return getAngle();
	}

}
