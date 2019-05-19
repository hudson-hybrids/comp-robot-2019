/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.controller;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Controller extends Joystick {
    final int SOLENOID_PANEL_FORWARD_BUTTON;
    final int SOLENOID_PANEL_REVERSE_BUTTON;
    final int SOLENOID_PANEL_PUSH_BUTTON;
    final int SOLENOID_PANEL_UNPUSH_BUTTON;
    final int SOLENOID_CARGO_RAISE_BUTTON;
    final int SOLENOID_CARGO_LOWER_BUTTON;
    
    Controller(
        final int ID,
        final int SOLENOID_PANEL_FORWARD_BUTTON, 
        final int SOLENOID_PANEL_REVERSE_BUTTON, 
        final int SOLENOID_PANEL_PUSH_BUTTON,
        final int SOLENOID_PANEL_UNPUSH_BUTTON,
        final int SOLENOID_CARGO_RAISE_BUTTON,
        final int SOLENOID_CARGO_LOWER_BUTTON) {
        
        super(ID);
        this.SOLENOID_PANEL_FORWARD_BUTTON = SOLENOID_PANEL_FORWARD_BUTTON;
        this.SOLENOID_PANEL_REVERSE_BUTTON = SOLENOID_PANEL_REVERSE_BUTTON;
        this.SOLENOID_PANEL_PUSH_BUTTON = SOLENOID_PANEL_PUSH_BUTTON;
        this.SOLENOID_PANEL_UNPUSH_BUTTON = SOLENOID_PANEL_UNPUSH_BUTTON;
        this.SOLENOID_CARGO_RAISE_BUTTON = SOLENOID_CARGO_RAISE_BUTTON;
        this.SOLENOID_CARGO_LOWER_BUTTON = SOLENOID_CARGO_LOWER_BUTTON;
    }

    void controlSolenoidDigital(DoubleSolenoid doubleSolenoid, int forwardControl, int reverseControl) {
        if (getRawButton(forwardControl) && getRawButton(reverseControl)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kOff);
        }
        else if (getRawButton(forwardControl)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kForward);
        }
        else if (getRawButton(reverseControl)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
        }
        if (getRawButtonReleased(forwardControl) || getRawButtonReleased(reverseControl)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kOff);
        }
    }
}
