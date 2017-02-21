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
public class simpleVisionRoutine extends Command {
	private final visionProc proc;

    public simpleVisionRoutine(visionProc proccesor) {
    	proc = proccesor;
    }

    protected void initialize() {

    }

    protected void execute() {
		new CommandGroup(){
			{
				final double scalar = 1/12.0;
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
					if(a.zeta != 0){
//						addSequential(new Turn(90.0*a.zeta, 0.5));
//						addSequential(new DriveStraightDistance(Math.abs(a.zeta)*scalar, Drive.Direction.FORWARD,0.5));
//						addSequential(new Turn(-90.0*a.zeta,0.5));
					}
//					addSequential(new DriveStraightDistance(Math.abs(a.v)*scalar, Drive.Direction.FORWARD,0.5));
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
