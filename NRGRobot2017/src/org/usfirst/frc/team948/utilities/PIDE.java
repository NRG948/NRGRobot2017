package org.usfirst.frc.team948.utilities;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Subsystem;

public enum PIDE {
	turn("turn"),drive("drive");
	private PIDController controler;
	private double p = 1;
	private double i = 1;
	private double d = 1;
	private String prefP = "P";
	private String prefI = "I";
	private String prefD = "D";
	private PIDSource source;
	private PIDOutput system;
	
	private PIDE(String S,double P,double I, double D){
		if(S.equals("turn") || S.equals("drive")){
			source = (PIDSource) RobotMap.driveGyro;
			system = Robot.drive;
		}
		prefP = S + prefP;
		p = Robot.preferences.getDouble(prefP, P);
		i = Robot.preferences.getDouble(prefI, I);
		d = Robot.preferences.getDouble(prefD, D);
		controler = new PIDController(p, i, d, source, system);
	}
	
	private PIDE(String S){
		if(S.equals("turn") || S.equals("drive")){
			source = (PIDSource) RobotMap.driveGyro;
			system = Robot.drive;
		}
		prefP = S + prefP;
		p = Robot.preferences.getDouble(prefP, p);
		i = Robot.preferences.getDouble(prefI, i);
		d = Robot.preferences.getDouble(prefD, d);
		controler = new PIDController(p, i, d, source, system);
	}
	
	public PIDController set(){
		controler.reset();
		controler.enable();
		return controler;
	}
	
	public PIDController con(){
		return controler;
	}
	
	public PIDSource getInput(){
		return source;
	}
	
	public PIDOutput getSystem(){
		return system;
	}
	
	public void clean(){
		controler.free();
	}
}
