// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.annotation.Target;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.IntakeActuator;
import frc.robot.subsystems.Intaker;

public class IntakeActuate extends Command{
    IntakeActuator intakeActuator;
    Intaker intaker;
    CommandXboxController controller;    
    boolean intakeExtended = false;
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

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //intakeActuator.moveIntake(Rotation2d.fromRotations(.54));//using this just to test if button moves motor
    //  after verifying that button moves motor, test to see if it toggles in and out.
    if(intakeActuator.getIntakeActuatorHomedState()){ 
        if(intakeExtended){
            intaker.setSpeed(.2);
            new WaitCommand(2.0);
            //intakeIn();
            System.out.println("Intake Speed 0.2");
            Commands.waitSeconds(2.0);
            intakeActuator.moveIntake(Rotation2d.fromRotations(0.1));
            System.out.println("Intake Retracted");
            intaker.setSpeed(0.0);
            System.out.println("Intake Speed 0.0");
            intakeExtended = false;
        } else {
            intaker.setSpeed(-.2);
            new WaitCommand(2.0);
            //intakeOut();
            System.out.println("Intake Speed -0.2");
            Commands.waitSeconds(1.0);
            intakeActuator.moveIntake(Rotation2d.fromRotations(0.43));
            System.out.println("Intake Extended");
            //intaker.setSpeed(-.2);
            System.out.println("Intake Speed -0.2");
            intakeExtended = true;
        }
    }

    System.out.println("Extension execution finished");
  }

  public Command intakeIn(){
    return new SequentialCommandGroup(
        new InstantCommand(() -> intaker.setSpeed(.2)),
        new InstantCommand(() -> System.out.println("Intake Speed 0.2")),
        new WaitCommand(2),
        new InstantCommand(() -> intakeActuator.moveIntake(Rotation2d.fromRotations(0.1)))
    );
  }

  public Command intakeOut(){
    return new SequentialCommandGroup(
        new InstantCommand(() -> intaker.setSpeed(-.2)),
        new InstantCommand(() -> System.out.println("Intake Speed -0.2")),
        new WaitCommand(2),
        new InstantCommand(() -> intakeActuator.moveIntake(Rotation2d.fromRotations(0.43)))
    );
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
