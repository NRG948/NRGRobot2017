package org.usfirst.frc.team948.utilities;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class ContinuousGyro implements PIDSource {
	public AHRS navx;

	public ContinuousGyro(AHRS navx) {
		this.navx = navx;
	}

	public double getAngle() {
		return navx.getAngle();
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
}
