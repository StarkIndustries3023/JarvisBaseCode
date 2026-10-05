// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.annotation.Target;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.IntakeActuator;
import frc.robot.subsystems.Intaker;

public class IntakeActuate extends Command{
    IntakeActuator intakeActuator;
    Intaker intaker;
    CommandXboxController controller;    
    boolean intakeExtended = false;

      /** Creates a new Intake. */
      
    public IntakeActuate(IntakeActuator intakeActuator){
        addRequirements(intakeActuator);

        this.intakeActuator = intakeActuator;
    }
    
  
     /*  public IntakeActuate(IntakeActuator intakeActuator,Intaker intaker) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeActuator, intaker);

    this.intakeActuator = intakeActuator;
    this.intaker = intaker;
    //this.controller = controller;
    }*/
    
    
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //intakeActuator.moveIntake(Rotation2d.fromRotations(.54));//using this just to test if button moves motor
    // after verifying that button moves motor, test to see if it toggles in and out.
    if(intakeExtended){
        intaker.setSpeed(-.20);
        intakeActuator.moveIntake(Rotation2d.fromRotations(0.0));
        intaker.setSpeed(0.0);
        System.out.println("Intake Retracted");
        intakeExtended = false;
    } else {
        intaker.setSpeed(.20);
        intakeActuator.moveIntake(Rotation2d.fromRotations(.53));
        System.out.println("Intake Extended");
        intaker.setSpeed(.67);
        intakeExtended = true;
    }


    System.out.println("Extension execution finished");
  }
  
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //indexer.setSpeed(0);
    System.out.println("Extension command finished");
    return true;
  }

}
