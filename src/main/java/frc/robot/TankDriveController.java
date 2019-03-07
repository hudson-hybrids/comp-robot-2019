/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import frc.robot.Controller;

public class TankDriveController extends Controller {
    final int RIGHT_STICK_AXIS;
    final int LEFT_STICK_AXIS;
    final int RIGHT_TRIGGER_AXIS;
    final int LEFT_TRIGGER_AXIS;
    final int RIGHT_SHOULDER_BUTTON;
    final int LEFT_SHOULDER_BUTTON;

    private boolean canDrive = false;
    private boolean canControlSolenoids = false;

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
        final int RIGHT_STICK_AXIS,
        final int LEFT_STICK_AXIS,
        final int RIGHT_TRIGGER_AXIS,
        final int LEFT_TRIGGER_AXIS,
        final int RIGHT_SHOULDER_BUTTON,
        final int LEFT_SHOULDER_BUTTON) {

        super(
            ID, 
            SOLENOID_PANEL_FORWARD_BUTTON, 
            SOLENOID_PANEL_REVERSE_BUTTON, 
            SOLENOID_PANEL_PUSH_BUTTON,
            SOLENOID_PANEL_UNPUSH_BUTTON,
            SOLENOID_CARGO_RAISE_BUTTON,
            SOLENOID_CARGO_LOWER_BUTTON);
        this.RIGHT_STICK_AXIS = RIGHT_STICK_AXIS;
        this.LEFT_STICK_AXIS = LEFT_STICK_AXIS;
        this.RIGHT_TRIGGER_AXIS = RIGHT_TRIGGER_AXIS;
        this.LEFT_TRIGGER_AXIS = LEFT_TRIGGER_AXIS;
        this.RIGHT_SHOULDER_BUTTON = RIGHT_SHOULDER_BUTTON;
        this.LEFT_SHOULDER_BUTTON = LEFT_SHOULDER_BUTTON;
    }

    private void setSpeed() {
        double rightSpeedMultiplier = 0.6;
        double leftSpeedMultiplier = 0.6;

        if (getRawAxis(RIGHT_TRIGGER_AXIS) > 0.3 && !getRawButton(RIGHT_SHOULDER_BUTTON)) {
            rightSpeedMultiplier = 1.0;
        }
        else if (getRawAxis(RIGHT_TRIGGER_AXIS) <= 0.3 && getRawButton(RIGHT_SHOULDER_BUTTON)) {
            rightSpeedMultiplier = 0.4;
        }

        if (getRawAxis(LEFT_TRIGGER_AXIS) > 0.3 && !getRawButton(LEFT_SHOULDER_BUTTON)) {
            leftSpeedMultiplier = 1.0;
        }
        else if (getRawAxis(LEFT_TRIGGER_AXIS) <= 0.3 && getRawButton(LEFT_SHOULDER_BUTTON)) {
            leftSpeedMultiplier = 0.4;
        }

        rightSpeed = getRawAxis(RIGHT_STICK_AXIS) * rightSpeedMultiplier;
        leftSpeed = getRawAxis(LEFT_STICK_AXIS) * leftSpeedMultiplier;
    }

    private void controlSolenoidAnalog(DoubleSolenoid doubleSolenoid, int stickAxis) {
        if (getRawAxis(stickAxis) >= 0.3) {
            doubleSolenoid.set(DoubleSolenoid.Value.kForward);
        }
        else if (getRawAxis(stickAxis) <= -0.3) {
            doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
        }
        else {
            doubleSolenoid.set(DoubleSolenoid.Value.kOff);
        }
    }
    private void controlSolenoidPOV(DoubleSolenoid doubleSolenoid) {
        final int POV_UP = 1;
        final int POV_DOWN = 2;
        if (getPOV() == POV_UP) {
            doubleSolenoid.set(DoubleSolenoid.Value.kForward);
        }
        else if (getPOV() == POV_DOWN) {
            doubleSolenoid.set(DoubleSolenoid.Value.kForward);
        }
        else {
            doubleSolenoid.set(DoubleSolenoid.Value.kOff);
        }
    }

    void setCanDrive(boolean canDrive) {
        this.canDrive = canDrive;
    }
    void setCanControlSolenoids(boolean canControlSolenoids) {
        this.canControlSolenoids = canControlSolenoids;
    }

    void drive(DifferentialDrive drive, DoubleSolenoid panelAdjustSolenoid, DoubleSolenoid panelPushSolenoid, DoubleSolenoid cargoSolenoid) {
        if (canDrive) {
            if (canControlSolenoids) {
                controlSolenoidDigital(panelAdjustSolenoid, SOLENOID_PANEL_FORWARD_BUTTON, SOLENOID_PANEL_REVERSE_BUTTON);
                controlSolenoidDigital(panelPushSolenoid, SOLENOID_PANEL_PUSH_BUTTON, SOLENOID_PANEL_UNPUSH_BUTTON);
                controlSolenoidDigital(cargoSolenoid, SOLENOID_CARGO_RAISE_BUTTON, SOLENOID_CARGO_LOWER_BUTTON);
            }
            setSpeed();
            drive.tankDrive(rightSpeed, leftSpeed);
        }
        else if (canControlSolenoids) {
            controlSolenoidAnalog(panelAdjustSolenoid, LEFT_STICK_AXIS);
            controlSolenoidAnalog(panelPushSolenoid, RIGHT_STICK_AXIS);
            controlSolenoidPOV(cargoSolenoid);
        }
    }
}