// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.annotation.Target;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.IntakeActuator;
import frc.robot.subsystems.Intaker;
import frc.robot.Constants;

public class IntakeActuate extends Command{
    IntakeActuator intakeActuator;
    Intaker intaker;
    CommandXboxController controller;    
    boolean intakeExtended = false;
    boolean intakeActuatorAtSetpoint = false;
    //boolean ifTriggeredOnce = false;

      /** Creates a new Intake. */
     /*  
    public IntakeActuate(IntakeActuator intakeActuator){
        addRequirements(intakeActuator);

        this.intakeActuator = intakeActuator;
    }
    */
  
       public IntakeActuate(IntakeActuator intakeActuator,Intaker intaker) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeActuator, intaker);

    this.intakeActuator = intakeActuator;
    this.intaker = intaker;
    //this.controller = controller;
    }
    
    
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intakeActuator.homeIntakeActuator();
    if(intakeExtended){

    }

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(intakeActuator.getIntakeActuatorHomedState()){ 
        if(intakeExtended){
            intaker.setSpeed(-.2);
            System.out.println("Intake Speed -0.2");
            intakeActuator.moveIntake(Rotation2d.fromRotations(Constants.IntakeAcutatorConstants.retractedAngle));
            if(intakeActuator.getIntakeActuatorAtExtendedAngle()){
                intakeActuatorAtSetpoint = true;
            }
            else{
                intakeActuatorAtSetpoint = false;
            }
        }
        else {
            intaker.setSpeed(.2);
            System.out.println("Intake Speed 0.2");
            intakeActuator.moveIntake(Rotation2d.fromRotations(Constants.IntakeAcutatorConstants.extendedAngle));
            if(intakeActuator.getIntakeActuatorAtRetractedAngle()){
                intakeActuatorAtSetpoint = true;
            }
            else{
                intakeActuatorAtSetpoint = false;
            }
        }
    }
    System.out.println("Extension execution finished");
    }


  
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (intakeExtended){
         //intaker.setSpeed(0);
         System.out.println("Intake Retracted");
        intakeExtended = false;
    } else {
         //intaker.setSpeed(0);
         System.out.println("Intake Extended");
        intakeExtended = true;
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    System.out.println("Extension command finished");
    //intakeActuatorAtSetpoint = intakeActuator.getIntakeActuatorAtSetPoint();
    return intakeActuatorAtSetpoint;
  }

}
