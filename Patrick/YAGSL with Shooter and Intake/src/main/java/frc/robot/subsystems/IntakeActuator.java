package frc.robot.subsystems;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
//import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
//import com.revrobotics.spark.SparkLowLevel.MotorType;
//import com.revrobotics.spark.SparkMax;
//import com.revrobotics.spark.SparkBase.ControlType;
//import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;
//import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeActuator extends SubsystemBase{
    SparkFlex intakeActuator;
    SparkFlexConfig intakeAcutatoConfig;
    SparkClosedLoopController intakeAcutatorController;

    

}
