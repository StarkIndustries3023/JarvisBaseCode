// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import edu.wpi.first.math.util.Units;
import frc.robot.util.Gains;

public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
    public static final double DEADBAND = 0.05;
  }

  public static final double MAX_SPEED = Units.feetToMeters(2);

  public static final double HOMING_SPEED = 0.15;
  

  
  public static class CAN_ID {
    public static final int INTAKE_ACTUATOR_ID = 11;
    public static final int IMU_ID = 20;
  }

  public static class GAINS {
    public static Gains INTAKE_ACTUATOR = new Gains(20, 0, 0, 0, 12);
  }

  public static final Double[] MODULE_OFFSETS = {315.0, 225.0, 45.0, 135.0};

}
