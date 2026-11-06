// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
//import frc.robot.Constants.IntakerConstants;
import frc.robot.subsystems.Intaker;

public class Intake extends Command {
    Intaker intaker;
    CommandXboxController controller;
    boolean motorseton = false;

  /** Creates a new Intake. */
  public Intake(Intaker intaker) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intaker);

    this.intaker = intaker;
    //this.controller = controller;

    }
    
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(motorseton){
        intaker.setSpeed(0);
        System.out.println("speed 0");
        motorseton = false;
    } else {
        intaker.setSpeed(-0.67);
        System.out.println("speed -.67");
        motorseton = true;
    }
    
    //intaker.setSpeed(-0.67);
    //shooter.setMotorRpm(controller.getRightTriggerAxis() * ShooterConstants.maxRPM);
    System.out.println("Intanker triggered");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //indexer.setSpeed(0);
    System.out.println("Intaker trigger finished");
    return true;
  }


}
