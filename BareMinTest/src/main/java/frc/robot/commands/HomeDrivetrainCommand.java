package frc.robot.commands;


import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveModule;

/**
 * A command to home the drivetrain by spinning the modules until the magnetic sensors are triggered, then setting the module angles to 0
 */
public class HomeDrivetrainCommand extends Command {
    private final SwerveSubsystem swerveSubsystem;


    public HomeDrivetrainCommand(SwerveSubsystem swerveSubsystem) {
        this.swerveSubsystem = swerveSubsystem;
        addRequirements(swerveSubsystem);
    }

    @Override
    public void initialize() {
        // Start spinning the modules counter-clockwise slowly
        for (SwerveModule swerveModule : swerveSubsystem.getModules()) {
            swerveModule.getAngleMotor().set(Constants.HOMING_SPEED);
        }

    }

    @Override
    public void execute() {
        SwerveModule[] modules = swerveSubsystem.getModules();
        // Loop over the modules and their corresponding magnetic sensors
        for(int i = 0; i < modules.length; i++) {
            // For each module, check if the magnetic sensor is triggered. If it is, stop the module and set its angle to 0
            if (swerveSubsystem.getMagSensor(i)) {
                modules[i].getAngleMotor().set(0);

                Object turnMotor = modules[i].getAngleMotor().getMotor();

                if(turnMotor instanceof SparkMax sparkMax){
                    sparkMax.getEncoder().setPosition(Constants.MODULE_OFFSETS[i]);
                    
                } else {
                    DriverStation.reportError("Angle Motor not set as SparkMax", false);
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public void end() {
    }

}
