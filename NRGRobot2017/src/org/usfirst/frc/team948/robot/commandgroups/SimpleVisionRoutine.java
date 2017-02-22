package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.commands.DelaySeconds;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.FlipCameraLight;
import org.usfirst.frc.team948.robot.commands.ShiftGears;
import org.usfirst.frc.team948.robot.commands.Turn;
import org.usfirst.frc.team948.robot.subsystems.Drive;
import org.usfirst.frc.team948.utilities.visionField;
import org.usfirst.frc.team948.utilities.visionProc;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class SimpleVisionRoutine extends Command {
	private final visionProc proc;

    public SimpleVisionRoutine(visionProc proccesor) {
    	proc = proccesor;
    }

    protected void initialize() {

    }

    protected void execute() {
		new CommandGroup(){
			{
				final double wallDistance = 0.0;
				final double scalar = 1.0;
				addSequential(new FlipCameraLight(true));
				addSequential(new DelaySeconds(0.1));
				boolean bool = proc.dataExists();
				if(bool){
					visionField a = proc.getData();
					SmartDashboard.putNumber("Vision: Theta", a.theta);
					SmartDashboard.putNumber("Vision: V", a.v);
					SmartDashboard.putNumber("Vision: Zeta", a.zeta);
					SmartDashboard.putNumber("Vision: Omega", a.omega);
					SmartDashboard.putNumber("Vision: Gamma", a.gamma);
					addSequential(new ShiftGears(false));
					addSequential(new Turn(-1.0*(a.gamma*180.0)/Math.PI));
					addSequential(new DriveStraightDistance(Math.sqrt((a.omega*a.omega)+((a.v-wallDistance)*(a.v-wallDistance)))*scalar, Drive.Direction.FORWARD));
					if(a.zeta != 0){
					}
				}
				addSequential(new FlipCameraLight(false));
			}
		}.start();
    }

    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
