// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */

  TalonFX leftMotor = new TalonFX(10);
  TalonFX rightMotor = new TalonFX(15);


  public Shooter() {
    TalonFXConfiguration leftConfig = new TalonFXConfiguration();
    leftConfig.Slot0.kP = ShooterConstants.shooterP;
    leftConfig.Slot0.kV = ShooterConstants.shooterV;
    leftConfig.Slot0.kS = ShooterConstants.shooterS;

    leftConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;


    leftMotor.getConfigurator().apply(leftConfig);


    TalonFXConfiguration rightConfig = new TalonFXConfiguration();
    rightConfig.Slot0.kP = ShooterConstants.shooterP;
    rightConfig.Slot0.kV = ShooterConstants.shooterV;
    rightConfig.Slot0.kS = ShooterConstants.shooterS;

    rightConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    rightMotor.getConfigurator().apply(rightConfig);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }

  public void setMotorRpm(double rpm){
    leftMotor.setControl(new VelocityVoltage(rpm / 60));
    rightMotor.setControl(new VelocityVoltage(rpm / 60));
  }

  public void stop(){
    leftMotor.set(0);
    rightMotor.set(0);
  }

  
}
