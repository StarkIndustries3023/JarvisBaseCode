package frc.robot.commands;

import java.util.Arrays;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveModule;
import swervelib.motors.SparkMaxSwerve;

/**
 * A command to home the drivetrain by spinning the modules until the magnetic sensors are triggered, then setting the module angles to 0
 */
public class HomeDrivetrainCommand extends Command {
    private final SwerveSubsystem swerveSubsystem;
    public boolean wasHomed = false;


    public HomeDrivetrainCommand(SwerveSubsystem swerveSubsystem) {
        this.swerveSubsystem = swerveSubsystem;
        

        addRequirements(swerveSubsystem);
    }

    @Override
    public void initialize() {
        // Start spinning the modules counter-clockwise slowly
        for (SwerveModule swerveModule : swerveSubsystem.getModules()) {
            swerveModule.getAngleMotor().set(0.15);
        }
        wasHomed = false;

        System.out.println("Command Started");
    }

    @Override
    public void execute() {
        SwerveModule[] modules = swerveSubsystem.getModules();
        DigitalInput[] moduleMagSensors = swerveSubsystem.getModuleMagSensors();
        // Loop over the modules and their corresponding magnetic sensors
        for(int i = 0; i < modules.length; i++) {
            // For each module, check if the magnetic sensor is triggered. If it is, stop the module and set its angle to 0
            if (!moduleMagSensors[i].get()) {
                modules[i].getAngleMotor().set(0);

                Object turnMotor = modules[i].getAngleMotor().getMotor();

                if(turnMotor instanceof SparkMax sparkMax){
                    sparkMax.getEncoder().setPosition(getModuleOffset(i));
                } else {
                    DriverStation.reportError("Angle Motor not set as SparkMax", false);
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        // The command is finished when all the magnetic sensors are triggered, meaning all the modules are homed

        // return Arrays.stream(swerveSubsystem.getModuleMagSensors()).allMatch(DigitalInput::get);

        return false;
    }

    public void end() {
        System.out.println("Command Ended");
        wasHomed = true;
    }

    private double getModuleOffset(int moduleNumber){
        if(moduleNumber == 0){
            return 315;
        } else if(moduleNumber == 1){
            return 225;
        } else if(moduleNumber == 2){
            return 45;
        } else if(moduleNumber == 3){
            return 135;
        } else {
            return 0;
        }
    }
}
