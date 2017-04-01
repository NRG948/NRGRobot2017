package org.usfirst.frc.team948.utilities;

import org.usfirst.frc.team948.robot.Robot.PegPosition;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class ContinuousGyro implements PIDSource {
	public AHRS navx;
	private double headingOffset;

	public ContinuousGyro(AHRS navx) {
		this.navx = navx;
	}
	
	public void setHeadingOffset(double offset)
	{
		this.headingOffset = offset;
	}

	public double getAngle() {
		return navx.getAngle() + headingOffset;
	}

	@Override
	public double pidGet() {
		return getAngle();
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	public void setAtPeg(PegPosition pegPosition) {
		navx.reset();
		headingOffset = pegPosition.angle;
	}
}
