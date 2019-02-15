/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import frc.robot.Controller;

public class TankDriveController extends Controller {
    final int MOTOR_RIGHT_AXIS;
    final int MOTOR_LEFT_AXIS;
    final int MOTOR_RIGHT_SPEED_AXIS;
    final int MOTOR_LEFT_SPEED_AXIS;
    final int MOTOR_RIGHT_SLOW_BUTTON;
    final int MOTOR_LEFT_SLOW_BUTTON;

    double rightSpeed;
    double leftSpeed;

    TankDriveController(
        final int ID, 
        final int SOLENOID_PANEL_FORWARD_BUTTON, 
        final int SOLENOID_PANEL_REVERSE_BUTTON, 
        final int SOLENOID_PANEL_PUSH_BUTTON,
        final int SOLENOID_PANEL_UNPUSH_BUTTON,
        final int SOLENOID_CARGO_RAISE_BUTTON,
        final int SOLENOID_CARGO_LOWER_BUTTON,
        final int MOTOR_RIGHT_AXIS,
        final int MOTOR_LEFT_AXIS,
        final int MOTOR_RIGHT_SPEED_AXIS,
        final int MOTOR_LEFT_SPEED_AXIS,
        final int MOTOR_RIGHT_SLOW_BUTTON,
        final int MOTOR_LEFT_SLOW_BUTTON) {

        super(
            ID, 
            SOLENOID_PANEL_FORWARD_BUTTON, 
            SOLENOID_PANEL_REVERSE_BUTTON, 
            SOLENOID_PANEL_PUSH_BUTTON,
            SOLENOID_PANEL_UNPUSH_BUTTON,
            SOLENOID_CARGO_RAISE_BUTTON,
            SOLENOID_CARGO_LOWER_BUTTON);
        this.MOTOR_RIGHT_AXIS = MOTOR_RIGHT_AXIS;
        this.MOTOR_LEFT_AXIS = MOTOR_LEFT_AXIS;
        this.MOTOR_RIGHT_SPEED_AXIS = MOTOR_RIGHT_SPEED_AXIS;
        this.MOTOR_LEFT_SPEED_AXIS = MOTOR_LEFT_SPEED_AXIS;
        this.MOTOR_RIGHT_SLOW_BUTTON = MOTOR_RIGHT_SLOW_BUTTON;
        this.MOTOR_LEFT_SLOW_BUTTON = MOTOR_LEFT_SLOW_BUTTON;
    }

    private void setSpeed() {
        double rightSpeedMultiplier = 0.6;
        double leftSpeedMultiplier = 0.6;

        if (getRawAxis(MOTOR_RIGHT_SPEED_AXIS) > 0.3 && !getRawButton(MOTOR_RIGHT_SLOW_BUTTON)) {
            rightSpeedMultiplier = 1.0;
        }
        else if (getRawAxis(MOTOR_RIGHT_SPEED_AXIS) <= 0.3 && getRawButton(MOTOR_RIGHT_SLOW_BUTTON)) {
            rightSpeedMultiplier = 0.4;
        }

        if (getRawAxis(MOTOR_LEFT_SPEED_AXIS) > 0.3 && !getRawButton(MOTOR_LEFT_SLOW_BUTTON)) {
            leftSpeedMultiplier = 1.0;
        }
        else if (getRawAxis(MOTOR_LEFT_SPEED_AXIS) <= 0.3 && getRawButton(MOTOR_LEFT_SLOW_BUTTON)) {
            leftSpeedMultiplier = 0.4;
        }

        rightSpeed = -MOTOR_RIGHT_AXIS * rightSpeedMultiplier;
        leftSpeed = -MOTOR_LEFT_AXIS * leftSpeedMultiplier;
    }

    void drive(DifferentialDrive drive) {
        setSpeed();
        drive.tankDrive(leftSpeed, rightSpeed);
    }
}