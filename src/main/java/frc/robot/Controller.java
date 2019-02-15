/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

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

    void controlSolenoid(DoubleSolenoid doubleSolenoid, int forwardButton, int reverseButton) {
        if (getRawButton(forwardButton) && getRawButton(reverseButton)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kOff);
        }
        else if (getRawButton(forwardButton)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kForward);
        }
        else if (getRawButton(reverseButton)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
        }
        if (getRawButtonReleased(forwardButton) || getRawButtonReleased(reverseButton)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kOff);
        }
    }
}
