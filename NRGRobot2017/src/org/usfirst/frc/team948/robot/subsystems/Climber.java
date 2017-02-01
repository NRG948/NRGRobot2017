package org.usfirst.frc.team948.robot.subsystems;

import org.usfirst.frc.team948.robot.commands.ClimbPower;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new ClimbPower(0.0));
    }
    
    public void rawTurn(double power){
    	
    	//Climber wheels turn at the power of the input
    }
    
    public void rawStop(){
    	
    	//Stop climbing or turning
    }
}

