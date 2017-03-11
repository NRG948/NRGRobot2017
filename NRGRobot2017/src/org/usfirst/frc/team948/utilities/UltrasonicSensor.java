package org.usfirst.frc.team948.utilities;

import edu.wpi.first.wpilibj.AnalogInput;

public class UltrasonicSensor {
	
	private double AdditiveTerm;
	
	private double DivisorTerm;
	
	private AnalogInput untrasonicSensor;
	
	public UltrasonicSensor(int Port, double AdditiveConstant, double DivisorConstant){
		untrasonicSensor = new AnalogInput(Port);
		DivisorTerm = DivisorConstant;
		AdditiveTerm = AdditiveConstant;
	}
	
	public double get(){
		return (untrasonicSensor.getVoltage()/DivisorTerm) + AdditiveTerm;
	}
	
	public void setDivisor(double newValue){
		DivisorTerm = newValue;
	}
	
	public void setAdditive(double newValue){
		AdditiveTerm = newValue;
	}

}
