// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.IndexerConstants;
import frc.robot.subsystems.Indexer;

public class Index extends Command {
    Indexer indexer;
    CommandXboxController controller;

  /** Creates a new Index. */
  public Index(Indexer indexer, CommandXboxController controller) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(indexer);

    this.indexer = indexer;
    this.controller = controller;

    }
    
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    indexer.setSpeed(controller.getLeftTriggerAxis() * 0.1);
    //shooter.setMotorRpm(controller.getRightTriggerAxis() * ShooterConstants.maxRPM);
    //System.out.println("Indexer triggered");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //indexer.setSpeed(0);
    //System.out.println("Indexer untriggered");
    return false;
  }
}