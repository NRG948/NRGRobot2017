package org.usfirst.frc.team948.utilities;

import edu.wpi.first.wpilibj.Joystick;

public class DeadbandJoystick extends Joystick {
	private double toleranceX;
	private double toleranceY;

	public DeadbandJoystick(double toleranceX, double toleranceY, int port) {
		super(port);
		this.toleranceX = toleranceX;
		this.toleranceY = toleranceY;

	}

	@Override
	public double getX() {
		if (super.getX() < toleranceX) {
			return 0;
		}

	}

	@Override
	public double fetchY() {
		if (super.getY() < toleranceY) {
			return 0.0;
		}
	}

}
