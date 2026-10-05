package frc.robot.subsystems;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
//import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
//import com.revrobotics.spark.SparkLowLevel.MotorType;
//import com.revrobotics.spark.SparkMax;
//import com.revrobotics.spark.SparkBase.ControlType;
//import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;
//import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;

import frc.robot.subsystems.Intaker;

public class IntakeActuator extends SubsystemBase{
    SparkFlex intakeActuator = new SparkFlex(12, MotorType.kBrushless);
    SparkFlexConfig intakeActuatorConfig;
    SparkClosedLoopController intakeActuatorController;

    private NetworkTableInstance inst;
    private NetworkTable intakeTable = NetworkTableInstance.getDefault().getTable("Intake Actuator");
    private BooleanPublisher extendedLimitPub = intakeTable.getBooleanTopic("Extended Limit").publish();
    private BooleanPublisher retractedLimitPub = intakeTable.getBooleanTopic("Retracted Limit").publish();
    private DoublePublisher intakeActuatorAnglePub = intakeTable.getDoubleTopic("Intake Actuator Angle").publish();
    private DoublePublisher intakeActuatorAbsoluteAnglePub = intakeTable.getDoubleTopic("Intake Actuator Absolute Angle").publish();

    private double retractedAngle = 0.0;
    private double extendedAngle = .53;

    private boolean currentIntakeState = false;

    double actuatorEncoderOffset = 0;

    private double intakeActuatorAngle = 0;
    private double intakeActuatorAbsoluteAngle = 0;
    
    DigitalInput extendedLimitSwitch = new DigitalInput(6);
    private boolean extendedLimit = false;
    DigitalInput retractedLimitSwitch = new DigitalInput(5);
    private boolean retractedLimit = true;

    public IntakeActuator() {
        intakeActuatorController = intakeActuator.getClosedLoopController();
        //endEffectorController = endEffector.getClosedLoopController();

        // intakeActuator Config
        intakeActuatorConfig = new SparkFlexConfig();
        intakeActuatorConfig
        .inverted(false)
        .idleMode(IdleMode.kBrake)
        .smartCurrentLimit(37)
        .voltageCompensation(12);
        intakeActuatorConfig.encoder
        .positionConversionFactor(1.0/(3 * 3 * 9))
        .velocityConversionFactor(1.0/(3 * 3 * 9));
        intakeActuatorConfig.closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .positionWrappingEnabled(false)
        .outputRange(-Constants.GAINS.INTAKE_ACTUATOR.peakOutput, Constants.GAINS.INTAKE_ACTUATOR.peakOutput);
        // actuatorConfig.softLimit
        // .reverseSoftLimit(90)
        // .forwardSoftLimit(actuatorEncoderOffset)
        // .reverseSoftLimitEnabled(true)
        // .forwardSoftLimitEnabled(true);
        intakeActuatorConfig.closedLoop.maxMotion
        .maxAcceleration(4)
        .cruiseVelocity(45);
    

    //SparkBaseSetter intakeActuatorSetter = new SparkBaseSetter(new SparkBaseSetter.SparkConfiguration(actuator, actuatorConfig));
    //intakeActuatorSetter.setPID(Constants.GAINS.INTAKE_ACTUATOR);
    //PIDDisplay.PIDList.addOption("Intake actuator", intakeActuatorSetter);


    //End Efector Config
    //endEffectorConfig = new SparkMaxConfig();
    //endEffectorConfig
    //.smartCurrentLimit(30)
    //.inverted(false)
    //.idleMode(IdleMode.kBrake)
    //.voltageCompensation(12);
    //endEffectorConfig.encoder
    //.positionConversionFactor(1.0/Constants.INTAKE.MANIPULATOR_GEAR_RATIO)
    //.velocityConversionFactor(1.0/Constants.INTAKE.MANIPULATOR_GEAR_RATIO); 
    //endEffectorConfig.closedLoop
    //.feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    //.positionWrappingEnabled(false)
    //.outputRange(-Constants.GAINS.INTAKE_MANIPULATOR.peakOutput, Constants.GAINS.INTAKE_MANIPULATOR.peakOutput);
    

    //SparkBaseSetter intakeEndEffectorSetter = new SparkBaseSetter(new SparkBaseSetter.SparkConfiguration(endEffector, endEffectorConfig));
    //intakeEndEffectorSetter.setPID(Constants.GAINS.INTAKE_MANIPULATOR);
    //PIDDisplay.PIDList.addOption("Intake End Efector", intakeEndEffectorSetter);

    // Network tables
    inst = NetworkTableInstance.getDefault();
    intakeTable = inst.getTable("Elastic/Intake");
    //lowerLimitPub = intakeTable.getBooleanTopic("Lower Limit").publish();
    //upperLimitPub = intakeTable.getBooleanTopic("Upper Limit").publish();
    intakeActuatorAnglePub = intakeTable.getDoubleTopic("Actuator angle").publish();
    intakeActuatorAbsoluteAnglePub = intakeTable.getDoubleTopic("Actuator abs. angle").publish();
  }
  
  //#region Network Table Setters

  public boolean getExtendedLimitState() {
    return !extendedLimitSwitch.get();
  }

  public boolean getRetractedLimitState() {
    return !retractedLimitSwitch.get();
  }

  public boolean getIntakeState(){
    return currentIntakeState;
  }

  public void setIntakeState(boolean newIntakeState){
    currentIntakeState = newIntakeState;
  }

  public double IntakeActuatorAngle() {
    return intakeActuatorAngle;
  }

  public double IntakeActuatorAbsoluteAngle() {
    return intakeActuatorAbsoluteAngle;
  }
  //#endregion

  public void moveIntake(Rotation2d target){
     if(target.getRotations() > .55 || target.getRotations() < -0.5){   
      DriverStation.reportWarning("Invalid intake target: " + target, false);
      return;
    }
  intakeActuatorController.setSetpoint(target.getRotations(), ControlType.kPosition);
  }
  /* Here only for reference on building intake extension function.

    public Command intakeOutCommand(){
    return new SequentialCommandGroup(
      new InstantCommand(() -> setIntakeState(true), this),
      new InstantCommand(() -> intakerConfig.idleMode(IdleMode.kCoast)),
      new InstantCommand(() -> runIntake(-500), this),
      new InstantCommand(() -> moveIntake(Rotation2d.fromRotations(.54)), this),
      new WaitUntilCommand(() -> actuator.getEncoder().getPosition() > .46)
      .withTimeout(2),
      new InstantCommand(() -> endEffector.set(0), this),
      new InstantCommand(() -> endEffectorConfig.idleMode(IdleMode.kBrake))
    );
    }

  public Command intakeInCommand(){
    return new SequentialCommandGroup(
      new InstantCommand(() -> setIntakeState(false), this),  
      //new InstantCommand(() -> endEffectorConfig.idleMode(IdleMode.kCoast), this),
      //new InstantCommand(() -> runIntake(500), this),
      new InstantCommand(() -> moveIntake(Rotation2d.fromRotations(0)), this),
      //new WaitUntilCommand(() -> actuator.getEncoder().getPosition() < .2).withTimeout(2),
      //new InstantCommand(() -> endEffector.set(0), this),
      //new InstantCommand(() -> endEffectorConfig.idleMode(IdleMode.kBrake), this)
    );
  }
*/
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    //adding table info for shooter output to elastic pjk 9/16/26

    extendedLimitPub.set(false); //used to test elastic table
    retractedLimitPub.set(true); //used to test elastic table, use next lines to display actual state pjk 10/5/26
    //extendedLimitPub.set(getExtendedLimitState());
    //retractedLimitPub.set(getRetractedLimitState());
  }

}
