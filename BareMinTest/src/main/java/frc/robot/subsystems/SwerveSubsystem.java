// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.io.File;
import java.util.function.Supplier;

import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import swervelib.SwerveModule;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

public class SwerveSubsystem extends SubsystemBase {  
  double maxSpeed = Units.feetToMeters(4.5);
  File directory = new File(Filesystem.getDeployDirectory(),"swerve");
  SwerveDrive swerveDrive;
  
  // Array of the magnetic sensors. The order must match the order of the swerve modules returned by getModules().
  private final DigitalInput[] moduleMagSensors = new DigitalInput[]  {
      new DigitalInput(0),
      new DigitalInput(1),
      new DigitalInput(2),
      new DigitalInput(3)};


    public SwerveSubsystem() {

      SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
  
      try
      {
        swerveDrive = new SwerveParser(directory).createSwerveDrive(Constants.MAX_SPEED, new Pose2d());
        // Alternative method if you don't want to supply the conversion factor via JSON files.
        // swerveDrive = new SwerveParser(directory).createSwerveDrive(maximumSpeed, angleConversionFactor, driveConversionFactor);
      } catch (Exception e)
      {
        throw new RuntimeException(e);
      }

      
    }
  
  
    @Override
    public void periodic() {
    }
  
  
    /**
     * @return Swerve Drive Object
     */
    public SwerveDrive getSwerveDrive() {
      return swerveDrive;
    }
  
    /**
     * Field oriented drive method
     * @param velocity Input chassis speeds
     */
    public void driveFieldOriented(ChassisSpeeds velocity) {
      swerveDrive.driveFieldOriented(velocity);
    }
  
    /**
     * Field oriented drive command
     * @param velocity Input ChassisSpeeds Supplier
     * @return Drive Command
     */
    public Command driveFieldOriented(Supplier<ChassisSpeeds> velocity) {
      return run(() -> {
        swerveDrive.driveFieldOriented(velocity.get());;
      });
    }
  
    /**
     * Resets gyro heading to 0
     */
    public void zeroGyro() {
      swerveDrive.zeroGyro();
    }

  /**
   * Gets the swerve modules from the swerve drive.
   * 
   * @return an array of the swerve modules
   */
  public SwerveModule[] getModules() {
    return swerveDrive.getModules();
  }

  /**
   * Get specific module's mag sensor value
   * @param i module 0-3
   * @return true if triggered
   */
  public boolean getMagSensor(int i) {
    return !moduleMagSensors[i].get();
  }


}