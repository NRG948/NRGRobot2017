package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
/** 
 * requires all subsystems, then stops them and itself immediately
 * a pseudo E-stop
 */
public class Interrupt extends Command {
	
	public Interrupt() {
		requires(Robot.ballCollector);
		requires(Robot.ballFeeder);
		requires(Robot.cameraLight);
		requires(Robot.climb);
		requires(Robot.drive);
		requires(Robot.gearbox);
		requires(Robot.shooter);
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
