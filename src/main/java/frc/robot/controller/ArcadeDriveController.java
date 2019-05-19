/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.controller;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;

import frc.robot.controller.Controller;

public class ArcadeDriveController extends Controller {
    private final int MOTOR_SPEED_BUTTON;
    private final int MOTOR_SLOW_BUTTON;
    private final int CONTROL_INVERT_BUTTON;
    private final int AUTO_ALIGN_CONTINUE_BUTTON;
    private final int AUTO_ALIGN_STOP_BUTTON;
    private boolean canDrive = false;
    private boolean canControlSolenoids = false;

    private double xSpeed;
    private double zRotation;
    
    private boolean invertButtonPressed = false;
    private boolean inverted = false;
    private boolean slowDriveLock = false;
    private boolean autoAlignMode = false;
    private boolean autoAlignStartButtonPressed = false;
    private boolean autoAlignStopButtonPressed = false;
    private boolean autoAlignPaused = false;

    private NetworkTableInstance tableInstance;
    private NetworkTable table;
    private Timer timer = new Timer();

    public ArcadeDriveController(
        final int ID, 
        final int SOLENOID_PANEL_FORWARD_BUTTON, 
        final int SOLENOID_PANEL_REVERSE_BUTTON, 
        final int SOLENOID_PANEL_PUSH_BUTTON,
        final int SOLENOID_PANEL_UNPUSH_BUTTON,
        final int SOLENOID_CARGO_RAISE_BUTTON,
        final int SOLENOID_CARGO_LOWER_BUTTON,
        final int MOTOR_SPEED_BUTTON,
        final int MOTOR_SLOW_BUTTON,
        final int CONTROL_INVERT_BUTTON,
        final int AUTO_ALIGN_CONTINUE_BUTTON,
        final int AUTO_ALIGN_STOP_BUTTON) {

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
        this.CONTROL_INVERT_BUTTON = CONTROL_INVERT_BUTTON;
        this.AUTO_ALIGN_CONTINUE_BUTTON = AUTO_ALIGN_CONTINUE_BUTTON;
        this.AUTO_ALIGN_STOP_BUTTON = AUTO_ALIGN_STOP_BUTTON;

        tableInstance = NetworkTableInstance.getDefault();
        tableInstance.startClient();
        tableInstance.setServer("192.168.1.38");
        table = tableInstance.getTable("VideoTable");
    }

    private void setSpeed() {
        double xSpeedMultiplier = 0.0;
        double zRotationMultiplier = 0.0;

        if (slowDriveLock) {
            if (getRawButton(MOTOR_SLOW_BUTTON)) {
                xSpeedMultiplier = 0.4;
                zRotationMultiplier = 0.3;
            }
            else {
                xSpeedMultiplier = 0.5;
                zRotationMultiplier = 0.38;
            }
        }
        else {
            if (getTrigger() && !getRawButton(MOTOR_SLOW_BUTTON)) {
                xSpeedMultiplier = 1.0;
                zRotationMultiplier = 0.5;
            }
            else if (!getTrigger() && getRawButton(MOTOR_SLOW_BUTTON)) {
                xSpeedMultiplier = 0.4;
                zRotationMultiplier = 0.32;
            }
            else {
                xSpeedMultiplier = 0.6;
                zRotationMultiplier = 0.48;
            }
        }

        if (!inverted) {
            xSpeed = getY() * xSpeedMultiplier;
            zRotation = getZ() * zRotationMultiplier;
        }
        else {
            xSpeed = -getY() * xSpeedMultiplier;
            zRotation = getZ() * zRotationMultiplier;
        }
    }
    private void invertControls() {
        if (getRawButton(CONTROL_INVERT_BUTTON)) {
            invertButtonPressed = true;
        }
        if (!getRawButton(CONTROL_INVERT_BUTTON) && invertButtonPressed) {
            System.out.println("INVERTED: " + inverted);
            inverted = !inverted;
            invertButtonPressed = false;
        }
    }
    /*
    private void autoAlign(DifferentialDrive drive) {
        if (getRawButton(AUTO_ALIGN_CONTINUE_BUTTON)) {
            autoAlignStartButtonPressed = true;
        }
        if (!getRawButton(AUTO_ALIGN_CONTINUE_BUTTON) && autoAlignStartButtonPressed && !autoAlignMode) {
            autoAlignMode = true;
            autoAlignStartButtonPressed = false;
        }

        while (autoAlignMode) {
            double hatchOffset = 0;
            if (tableInstance.getEntry("centerOffset").exists()) {
                hatchOffset = tableInstance.getEntry("centerOffset").getValue().getDouble();
            }

            drive.arcadeDrive(0.0, -hatchOffset * 0.2);
            Timer.delay(1.0);
            drive.arcadeDrive(-0.2, 0.0);
            Timer.delay(1.0);
            drive.arcadeDrive(0.0, hatchOffset * 0.2);
            Timer.delay(1.0);
            drive.arcadeDrive(0.2, 0.0);
            Timer.delay(1.0);

            boolean buttonResponse = false;
            while (!buttonResponse) {
                if (getRawButton(AUTO_ALIGN_STOP_BUTTON)) {
                    autoAlignStopButtonPressed = true;
                }
                if (!getRawButton(AUTO_ALIGN_STOP_BUTTON) && autoAlignStopButtonPressed) {
                    autoAlignMode = false;
                    buttonResponse = true;
                    autoAlignStopButtonPressed = false;
                }

                if (getRawButton(AUTO_ALIGN_CONTINUE_BUTTON)) {
                    autoAlignStartButtonPressed = true;
                }
                if (!getRawButton(AUTO_ALIGN_CONTINUE_BUTTON) && autoAlignStartButtonPressed) {
                    buttonResponse = true;
                    autoAlignStartButtonPressed = false;
                }
            }
        }
    }
    */
    private void autoAlign(DifferentialDrive drive) {
        double hatchOffset = 0.0;
        
        if (getRawButton(AUTO_ALIGN_CONTINUE_BUTTON)) {
            autoAlignStartButtonPressed = true;
        }
        if (!getRawButton(AUTO_ALIGN_CONTINUE_BUTTON) && autoAlignStartButtonPressed) {
            if (!autoAlignMode || autoAlignPaused) {
                if (tableInstance.getEntry("centerOffset").exists()) {
                    hatchOffset = tableInstance.getEntry("centerOffset").getValue().getDouble();
                }

                autoAlignMode = true;
                timer.reset();
                timer.start();
                autoAlignPaused = false;
                autoAlignStartButtonPressed = false;
            }
        }

        if (getRawButton(AUTO_ALIGN_STOP_BUTTON)) {
            autoAlignStopButtonPressed = true;
        }
        if (!getRawButton(AUTO_ALIGN_STOP_BUTTON) && autoAlignStopButtonPressed) {
            autoAlignMode = false;
            autoAlignPaused = false;
            autoAlignStopButtonPressed = false;
        }

        if (!autoAlignPaused) {
            if (timer.get() < 1.0) {
                drive.arcadeDrive(0.0, -hatchOffset * 0.2);
            }
            else if (timer.get() < 2.0) {
                drive.arcadeDrive(-0.2, 0.0);
            }
            else if (timer.get() < 3.0) {
                drive.arcadeDrive(0.0, hatchOffset * 0.2);
            }
            else if (timer.get() < 4.0) {
                drive.arcadeDrive(0.2, 0.0);
            }
            else if (timer.get() >= 4.0) {
                autoAlignPaused = true;
            }
        }
    }

    public void setCanDrive(boolean canDrive) {
        this.canDrive = canDrive;
    }
    public void setCanControlSolenoids(boolean canControlSolenoids) {
        this.canControlSolenoids = canControlSolenoids;
    }
    public void setSlowDriveLock(boolean slowDriveLock) {
        this.slowDriveLock = slowDriveLock;
    }
    public void drive(DifferentialDrive drive, DoubleSolenoid panelAdjustSolenoid, DoubleSolenoid panelPushSolenoid, DoubleSolenoid cargoSolenoid, boolean DEMO) {
        if (canControlSolenoids) {
            if (DEMO) {
                controlSolenoidDigital(panelPushSolenoid, SOLENOID_PANEL_PUSH_BUTTON, SOLENOID_PANEL_UNPUSH_BUTTON);
                controlSolenoidDigital(cargoSolenoid, SOLENOID_CARGO_RAISE_BUTTON, SOLENOID_CARGO_LOWER_BUTTON);
            }
            else {
                controlSolenoidDigital(panelAdjustSolenoid, SOLENOID_PANEL_FORWARD_BUTTON, SOLENOID_PANEL_REVERSE_BUTTON);
                controlSolenoidDigital(panelPushSolenoid, SOLENOID_PANEL_PUSH_BUTTON, SOLENOID_PANEL_UNPUSH_BUTTON);
                controlSolenoidDigital(cargoSolenoid, SOLENOID_CARGO_RAISE_BUTTON, SOLENOID_CARGO_LOWER_BUTTON);
            }
        }
        if (canDrive) {
            setSpeed();
            invertControls();
            drive.arcadeDrive(xSpeed, zRotation);

            //autoAlign(drive);
        }
    }
}