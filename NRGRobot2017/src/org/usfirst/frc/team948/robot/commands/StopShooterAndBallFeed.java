package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StopShooterAndBallFeed extends Command {
    public StopShooterAndBallFeed() {
        requires(Robot.shooter);
        requires(Robot.ballFeeder);
    }

    protected boolean isFinished() {
    	Robot.shooter.stop();
    	Robot.ballFeeder.stop();
        return false;
    }

}
