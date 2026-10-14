// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.subsystems.IntakeActuator;

public class Intaker extends SubsystemBase{
    /** Creates a new Intaker. */

  SparkMax intakerMotor = new SparkMax(13, MotorType.kBrushless);
  SparkMaxConfig intakerConfig;

  public boolean intakeActuatorAngleSafe = false;

  public Intaker() {
    intakerConfig = new SparkMaxConfig();
    intakerConfig
    .inverted(true)
    .idleMode(IdleMode.kBrake)
    .smartCurrentLimit(38)
    .voltageCompensation(12);
  }

  public Boolean getIntakeActuatorAngleSafe(){
     if(IntakeActuator.getIntakeActuatorAtExtendedAngle()){
      //IntakeActuator.getEncoder().getPosition();
      System.out.println("Intake angle set correct");
     }
    return intakeActuatorAngleSafe;
  
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setSpeed(double speed){
    if(getIntakeActuatorAngleSafe()){
      intakerMotor.set(speed);
      } else {
        intakerMotor.set(0);
        System.out.println("Intake failed to reach safe angle");
      }
    }


}
