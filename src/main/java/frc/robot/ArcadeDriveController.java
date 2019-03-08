/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.opencv.core.Mat;
import edu.wpi.cscore.CvSource;

import frc.robot.Controller;
import frc.robot.Camera;

public class ArcadeDriveController extends Controller {
    final int MOTOR_SPEED_BUTTON;
    final int MOTOR_SLOW_BUTTON;
    final int CONTROL_180_BUTTON;
    final int CONTROL_INVERT_BUTTON;
    private boolean canDrive = false;
    private boolean canControlSolenoids = false;

    private double xSpeed;
    private double zRotation;

    private boolean control180Pressed = false;
    private boolean invertButtonPressed = false;
    private boolean inverted = false;

    boolean frontCameraOn = true;
    boolean backCameraOn = false;
    boolean buttonChangePressed = false;

    ArcadeDriveController(
        final int ID, 
        final int SOLENOID_PANEL_FORWARD_BUTTON, 
        final int SOLENOID_PANEL_REVERSE_BUTTON, 
        final int SOLENOID_PANEL_PUSH_BUTTON,
        final int SOLENOID_PANEL_UNPUSH_BUTTON,
        final int SOLENOID_CARGO_RAISE_BUTTON,
        final int SOLENOID_CARGO_LOWER_BUTTON,
        final int MOTOR_SPEED_BUTTON,
        final int MOTOR_SLOW_BUTTON,
        final int CONTROL_180_BUTTON,
        final int CONTROL_INVERT_BUTTON) {

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
        this.CONTROL_180_BUTTON = CONTROL_180_BUTTON;
        this.CONTROL_INVERT_BUTTON = CONTROL_INVERT_BUTTON;
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

        if (!inverted) {
            xSpeed = getY() * xSpeedMultiplier;
            zRotation = getX() * zRotationMultiplier;
        }
        else {
            xSpeed = -getY() * xSpeedMultiplier;
            zRotation = -getX() * zRotationMultiplier;
        }
    }

    void setCanDrive(boolean canDrive) {
        this.canDrive = canDrive;
    }
    void setCanControlSolenoids(boolean canControlSolenoids) {
        this.canControlSolenoids = canControlSolenoids;
    }

    private void drive180() {
        if (getRawButton(CONTROL_180_BUTTON)) {
            control180Pressed = true;
        }
        if (!getRawButton(CONTROL_180_BUTTON) && control180Pressed) {
            //Rotate
        }
    }

    private void invertControls() {
        if (getRawButton(CONTROL_INVERT_BUTTON)) {
            invertButtonPressed = true;
        }
        if (!getRawButton(CONTROL_INVERT_BUTTON) && invertButtonPressed) {
            inverted = !inverted;
        }
    }

    void controlCameras(Camera frontCamera, Camera backCamera, Mat mat, CvSource cvSource) {

    }

    void drive(DifferentialDrive drive, DoubleSolenoid panelAdjustSolenoid, DoubleSolenoid panelPushSolenoid, DoubleSolenoid cargoSolenoid) {
        if (canControlSolenoids) {
            controlSolenoidDigital(panelAdjustSolenoid, SOLENOID_PANEL_FORWARD_BUTTON, SOLENOID_PANEL_REVERSE_BUTTON);
            controlSolenoidDigital(panelPushSolenoid, SOLENOID_PANEL_PUSH_BUTTON, SOLENOID_PANEL_UNPUSH_BUTTON);
            controlSolenoidDigital(cargoSolenoid, SOLENOID_CARGO_RAISE_BUTTON, SOLENOID_CARGO_LOWER_BUTTON);
        }
        if (canDrive) {
            setSpeed();
            drive180();
            invertControls();
            drive.arcadeDrive(xSpeed, zRotation);
        }
    }
}