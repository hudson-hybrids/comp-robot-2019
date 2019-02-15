/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import frc.robot.Controller;

public class ArcadeDriveController extends Controller {
    final int MOTOR_SPEED_BUTTON;
    final int MOTOR_SLOW_BUTTON;

    private double xSpeed;
    private double zRotation;

    ArcadeDriveController(
        final int ID, 
        final int SOLENOID_PANEL_FORWARD_BUTTON, 
        final int SOLENOID_PANEL_REVERSE_BUTTON, 
        final int SOLENOID_PANEL_PUSH_BUTTON,
        final int SOLENOID_PANEL_UNPUSH_BUTTON,
        final int SOLENOID_CARGO_RAISE_BUTTON,
        final int SOLENOID_CARGO_LOWER_BUTTON,
        final int MOTOR_SPEED_BUTTON,
        final int MOTOR_SLOW_BUTTON) {

        super(
            ID, 
            SOLENOID_PANEL_FORWARD_BUTTON, 
            SOLENOID_PANEL_REVERSE_BUTTON, 
            SOLENOID_PANEL_PUSH_BUTTON,
            SOLENOID_PANEL_UNPUSH_BUTTON,
            SOLENOID_CARGO_RAISE_BUTTON,
            SOLENOID_CARGO_LOWER_BUTTON);
        this.MOTOR_SPEED_BUTTON = MOTOR_SPEED_BUTTON;
        this.MOTOR_SLOW_BUTTON = MOTOR_SLOW_BUTTON;
    }

    private void setSpeed() {
        double xSpeedMultiplier = 0.0;
        double zRotationMultiplier = 0.0;

        if (getTrigger() && !getRawButton(MOTOR_SLOW_BUTTON)) {
            xSpeedMultiplier = 1.0;
            zRotationMultiplier = 0.8;
        }
        else if (!getTrigger() && getRawButton(MOTOR_SLOW_BUTTON)) {
            xSpeedMultiplier = 0.4;
            zRotationMultiplier = 0.32;
        }
        else {
            xSpeedMultiplier = 0.6;
            zRotationMultiplier = 0.48;
        }

        xSpeed = -getY() * xSpeedMultiplier;
        zRotation = getX() * zRotationMultiplier;
    }

    void drive(DifferentialDrive drive) {
        setSpeed();
        drive.arcadeDrive(xSpeed, zRotation);
    }
}