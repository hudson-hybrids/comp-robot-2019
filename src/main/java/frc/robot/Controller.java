/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controller extends Joystick {
    final int SOLENOID_PANEL_FORWARD_BUTTON;
    final int SOLENOID_PANEL_REVERSE_BUTTON;
    final int SOLENOID_PANEL_PUSH_BUTTON;
    final int SOLENOID_PANEL_UNPUSH_BUTTON;
    final int SOLENOID_CARGO_RAISE_BUTTON;
    final int SOLENOID_CARGO_LOWER_BUTTON;

    int MOTOR_RIGHT_AXIS = 0;
    int MOTOR_LEFT_AXIS = 0;

    int MOTOR_SPEED_BUTTON = 0;
    int MOTOR_SLOW_BUTTON = 0;

    int MOTOR_RIGHT_SPEED_AXIS = 0;
    int MOTOR_LEFT_SPEED_AXIS = 0;
    int MOTOR_RIGHT_SLOW_BUTTON = 0;
    int MOTOR_LEFT_SLOW_BUTTON = 0;

    //Joystick constructor
    Controller(
        final int ID, 
        final int SOLENOID_PANEL_FORWARD_BUTTON, 
        final int SOLENOID_PANEL_REVERSE_BUTTON, 
        final int SOLENOID_PANEL_PUSH_BUTTON,
        final int SOLENOID_PANEL_UNPUSH_BUTTON,
        final int SOLENOID_CARGO_RAISE_BUTTON,
        final int SOLENOID_CARGO_LOWER_BUTTON,
        int MOTOR_SPEED_BUTTON,
        int MOTOR_SLOW_BUTTON) {

        super(ID);
        this.SOLENOID_PANEL_FORWARD_BUTTON = SOLENOID_PANEL_FORWARD_BUTTON;
        this.SOLENOID_PANEL_REVERSE_BUTTON = SOLENOID_PANEL_REVERSE_BUTTON;
        this.SOLENOID_PANEL_PUSH_BUTTON = SOLENOID_PANEL_PUSH_BUTTON;
        this.SOLENOID_PANEL_UNPUSH_BUTTON = SOLENOID_PANEL_UNPUSH_BUTTON;
        this.SOLENOID_CARGO_RAISE_BUTTON = SOLENOID_CARGO_RAISE_BUTTON;
        this.SOLENOID_CARGO_LOWER_BUTTON = SOLENOID_CARGO_LOWER_BUTTON;
        this.MOTOR_SPEED_BUTTON = MOTOR_SPEED_BUTTON;
        this.MOTOR_SLOW_BUTTON = MOTOR_SLOW_BUTTON;
    }

    //Gamepad constructor
    Controller(
        final int ID, 
        final int SOLENOID_PANEL_FORWARD_BUTTON, 
        final int SOLENOID_PANEL_REVERSE_BUTTON, 
        final int SOLENOID_PANEL_PUSH_BUTTON,
        final int SOLENOID_PANEL_UNPUSH_BUTTON,
        final int SOLENOID_CARGO_RAISE_BUTTON,
        final int SOLENOID_CARGO_LOWER_BUTTON,
        int MOTOR_RIGHT_AXIS,
        int MOTOR_LEFT_AXIS,
        int MOTOR_RIGHT_SPEED_AXIS,
        int MOTOR_LEFT_SPEED_AXIS,
        int MOTOR_RIGHT_SLOW_BUTTON,
        int MOTOR_LEFT_SLOW_BUTTON) {

        super(ID);
        this.SOLENOID_PANEL_FORWARD_BUTTON = SOLENOID_PANEL_FORWARD_BUTTON;
        this.SOLENOID_PANEL_REVERSE_BUTTON = SOLENOID_PANEL_REVERSE_BUTTON;
        this.SOLENOID_PANEL_PUSH_BUTTON = SOLENOID_PANEL_PUSH_BUTTON;
        this.SOLENOID_PANEL_UNPUSH_BUTTON = SOLENOID_PANEL_UNPUSH_BUTTON;
        this.SOLENOID_CARGO_RAISE_BUTTON = SOLENOID_CARGO_RAISE_BUTTON;
        this.SOLENOID_CARGO_LOWER_BUTTON = SOLENOID_CARGO_LOWER_BUTTON;
        this.MOTOR_RIGHT_SPEED_AXIS = MOTOR_RIGHT_SPEED_AXIS;
        this.MOTOR_LEFT_SPEED_AXIS = MOTOR_LEFT_SPEED_AXIS;
        this.MOTOR_RIGHT_SLOW_BUTTON = MOTOR_RIGHT_SLOW_BUTTON;
        this.MOTOR_LEFT_SLOW_BUTTON = MOTOR_SLOW_BUTTON;
    }
}
