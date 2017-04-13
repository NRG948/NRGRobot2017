package org.usfirst.frc.team948.robot.commands;

import static org.usfirst.frc.team948.robot.subsystems.Drive.Direction.BACKWARD;
import static org.usfirst.frc.team948.robot.subsystems.Drive.Direction.FORWARD;

import org.usfirst.frc.team948.robot.OI;
import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import org.usfirst.frc.team948.robot.Robot.PegPosition;
import org.usfirst.frc.team948.robot.commandgroups.TurnToBoilerAndFeedBalls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootAfterGearDrop2 extends Command {

	public ShootAfterGearDrop2() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		PegPosition position;
		if (DriverStation.getInstance().isAutonomous()) {
			position = OI.getPegPosition();
		} else {
			position = Robot.pegLocator.getPegPosition();
			Robot.positionTracker.setAtPeg(position);
			RobotMap.continuousGyro.setAtPeg(position);
		}
		Alliance alliance = DriverStation.getInstance().getAlliance();
		// if at red left peg of the blue right peg don't shoot.
		if ((position == PegPosition.LEFT && alliance == Alliance.Red)
				|| (position == PegPosition.RIGHT && alliance == Alliance.Blue)) {
			return;
		}
		CommandGroup cmdGroup = new CommandGroup();
		cmdGroup.addParallel(new SpinShooterToRPM(3000));
		switch (position) {
		case LEFT:
			cmdGroup.addSequential(new DriveStraightDistance(18, BACKWARD));
			cmdGroup.addSequential(new Turn(162));
			cmdGroup.addParallel(new DriveStraightDistance(94, FORWARD));
			break;
		case CENTER:
			cmdGroup.addSequential(new DriveStraightDistance(16, BACKWARD));
			cmdGroup.addSequential(new TurnToBoiler());
			cmdGroup.addSequential(new DriveStraightDistance(126, FORWARD));
			break;
		case RIGHT:
			cmdGroup.addSequential(new DriveStraightDistance(18, BACKWARD));
			cmdGroup.addSequential(new Turn(-156));
			cmdGroup.addParallel(new DriveStraightDistance(94, FORWARD));
			break;
		}

//		cmdGroup.addParallel(new SpinShooterToCalculatedRPM());
//		cmdGroup.addSequential(new TurnToBoilerAndFeedBalls());
		cmdGroup.addSequential(new FeedBalls());
		cmdGroup.start();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return true;
	}
}
