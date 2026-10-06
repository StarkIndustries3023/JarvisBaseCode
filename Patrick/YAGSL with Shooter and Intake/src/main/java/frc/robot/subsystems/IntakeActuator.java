package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
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
    private BooleanPublisher ifTriggeredOncePub = intakeTable.getBooleanTopic("Retract triggered once").publish();
    private BooleanPublisher intakeActuatorHomedPub = intakeTable.getBooleanTopic("Intake Actuator Homed").publish();


    private double retractedAngle = 0.0;
    private double extendedAngle = .53;

    private boolean intakeActuatorHomedState = false;

    private boolean currentIntakeState = false;

    private boolean ifTriggeredOnce = false;

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
        intakeActuatorConfig.closedLoop.p(4).i(0.0).d(.001);
        intakeActuatorConfig.closedLoop.maxMotion
        .maxAcceleration(2)
        .cruiseVelocity(20);

        intakeActuator.configure(intakeActuatorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    

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


  }

  
  //#region Network Table Setters

  public boolean getExtendedLimitState() {
    return !extendedLimitSwitch.get();
  }

  public boolean getRetractedLimitState() {
    setIfTriggeredOnce();
    return !retractedLimitSwitch.get();
  }

  public boolean getIntakeState(){
    return currentIntakeState;
  }

   public void setIfTriggeredOnce(){
    if(!retractedLimitSwitch.get())
    ifTriggeredOnce = true;
  }

  //The following function may not be needed if the pub just sets intakeActuatorHomedState(its already boolean)
  public boolean getIntakeActuatorHomedState(){ 
    return intakeActuatorHomedState;
  }

  public void setIntakeState(boolean newIntakeState){
    currentIntakeState = newIntakeState;
  }

  public double IntakeActuatorAngle() {
    intakeActuatorAngle = intakeActuator.getEncoder().getPosition();
    return intakeActuatorAngle;
  }

  public double IntakeActuatorAbsoluteAngle() {
    intakeActuatorAbsoluteAngle = intakeActuator.getAbsoluteEncoder().getPosition();
    return intakeActuatorAbsoluteAngle;
  }
  //#endregion

  public void homeIntakeActuator(){
    if(!intakeActuatorHomedState){
      if(getRetractedLimitState()){
      intakeActuator.getEncoder().setPosition(0.0);
      System.out.println("Home Intake Actuator commmanded");
      intakeActuatorHomedState = true;
    } else {
      System.out.println("Not Homed due to Retracted Limit Switch State false");
    }
    } else{
      System.out.println("Found in homed state");
    }
    
  }

  public void moveIntake(Rotation2d target){
     if(target.getRotations() > .55 || target.getRotations() < -0.5){   
      DriverStation.reportWarning("Invalid intake target: " + target, false);
      return;
    } else {
      if(intakeActuatorHomedState){
         System.out.println("Acutuator Homed");
         intakeActuatorController.setSetpoint(target.getRotations(), ControlType.kPosition);
      }
      System.out.println("Acutuator function called");
    }
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
    setIntakeState(currentIntakeState);

    //adding table info for shooter output to elastic pjk 9/16/26

    extendedLimitPub.set(getExtendedLimitState()); 
    retractedLimitPub.set( getRetractedLimitState()); 
    //extendedLimitPub.set(getExtendedLimitState());
    //retractedLimitPub.set(getRetractedLimitState());
    intakeActuatorAnglePub.set(IntakeActuatorAngle());
    intakeActuatorAbsoluteAnglePub.set(IntakeActuatorAbsoluteAngle());
    ifTriggeredOncePub.set(ifTriggeredOnce);
    intakeActuatorHomedPub.set(intakeActuatorHomedState);
  }

}
