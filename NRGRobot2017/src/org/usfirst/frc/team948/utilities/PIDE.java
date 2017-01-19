package org.usfirst.frc.team948.utilities;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.RobotMap;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class PIDE {
	private PIDController controler;
	private double PIDOutput = 0;
	private double endValue;
	private double prevError;
	private double error;
	private double p = 1;
	private double i = 1;
	private double d = 1;
	private double c = 0;
	private double r = 2;
	private String prefP = "P";
	private String prefI = "I";
	private String prefD = "D";
	private String prefC = "C";
	private String prefR = "R";
	private PIDSource source;
	private PIDOutput system;
	
	public PIDE(String S,double P,double I, double D, double C, double R){
		if(S.equals("turn") || S.equals("drive")){
			source = (PIDSource) RobotMap.driveGyro;
			system = (PIDOutput) Robot.drive;
		}
		stringSet(S);
		p = Robot.preferences.getDouble(prefP, P);
		i = Robot.preferences.getDouble(prefI, I);
		d = Robot.preferences.getDouble(prefD, D);
		c = Robot.preferences.getDouble(prefC, C);
		r = Robot.preferences.getDouble(prefR, R);
		controler = new PIDController(p, i, d, source, system);
		controler.setOutputRange(c-Math.abs(r),c+Math.abs(r));
	}
	
	public PIDE(String S,double P,double I, double D){
		if(S.equals("turn") || S.equals("drive")){
			source = (PIDSource) RobotMap.driveGyro;
			system = (PIDOutput) Robot.drive;
		}
		stringSet(S);
		p = Robot.preferences.getDouble(prefP, P);
		i = Robot.preferences.getDouble(prefI, I);
		d = Robot.preferences.getDouble(prefD, D);
		c = Robot.preferences.getDouble(prefC, c);
		r = Robot.preferences.getDouble(prefR, r);
		controler = new PIDController(p, i, d, source, system);
		controler.setOutputRange(c-Math.abs(r),c+Math.abs(r));
	}
	
	public PIDE(String S,double C,double R){
		if(S.equals("turn") || S.equals("drive")){
			source = (PIDSource) RobotMap.driveGyro;
			system = (PIDOutput) Robot.drive;
		}
		stringSet(S);
		p = Robot.preferences.getDouble(prefP, p);
		i = Robot.preferences.getDouble(prefI, i);
		d = Robot.preferences.getDouble(prefD, d);
		c = Robot.preferences.getDouble(prefC, C);
		r = Robot.preferences.getDouble(prefR, R);
		controler = new PIDController(p, i, d, source, system);
		controler.setOutputRange(c-Math.abs(r),c+Math.abs(r));
	}
	
	public PIDE(String S){
		if(S.equals("turn") || S.equals("drive")){
			source = (PIDSource) RobotMap.driveGyro;
			system = (PIDOutput) Robot.drive;
		}
		stringSet(S);
		p = Robot.preferences.getDouble(prefP, p);
		i = Robot.preferences.getDouble(prefI, i);
		d = Robot.preferences.getDouble(prefD, d);
		c = Robot.preferences.getDouble(prefC, c);
		r = Robot.preferences.getDouble(prefR, r);
		controler = new PIDController(p, i, d, source, system);
		controler.setOutputRange(c-Math.abs(r),c+Math.abs(r));
	}
	private void stringSet(String S){
		prefP = S + prefP;
		prefI = S + prefI;
		prefD = S + prefD;
		prefC = S + prefC;
		prefR = S + prefR;
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
	
	public double PIDOut(){
		return PIDOutput;
	}
	
	public double targetValue(){
		return endValue;
	}
	
	public void setTargetValue(double value){
		endValue = value;
	}
	
	public double getPreviosError(){
		return prevError;
	}
	
	public double getError(){
		return error;
	}
	
	public void setError(double e){
		prevError = error;
		error = e;
	}
	
	public void clean(){
		controler.free();
	}
}