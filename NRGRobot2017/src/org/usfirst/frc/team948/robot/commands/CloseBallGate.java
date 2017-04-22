package org.usfirst.frc.team948.robot.commands;

import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CloseBallGate extends Command {
    public CloseBallGate() {
        requires(Robot.ballFeeder);
    }

    protected boolean isFinished() {
    	Robot.ballFeeder.closeBallGate();
        return true;
    }
}
