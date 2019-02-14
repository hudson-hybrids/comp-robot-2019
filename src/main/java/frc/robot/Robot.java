/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.robot.Controller;
import frc.robot.RobotMap;

public class Robot extends TimedRobot {
    //Autonomous mode management
    private String currentAutoMode;
    private final SendableChooser<String> AUTO_MODE_CHOOSER = new SendableChooser<>();
	private static final String AUTO_MODE_TELEOP = "AUTO_MODE_TELEOP";
	private static final String AUTO_MODE_1 = "AUTO_MODE_1";
    
    private WPI_VictorSPX frontRightMotor = new WPI_VictorSPX(RobotMap.FRONT_RIGHT_MOTOR_ID);
    private WPI_VictorSPX backRightMotor = new WPI_VictorSPX(RobotMap.BACK_RIGHT_MOTOR_ID);
    private WPI_VictorSPX frontLeftMotor = new WPI_VictorSPX(RobotMap.FRONT_LEFT_MOTOR_ID);
    private WPI_VictorSPX backLeftMotor = new WPI_VictorSPX(RobotMap.BACK_LEFT_MOTOR_ID);

    private SpeedControllerGroup rightGroup = new SpeedControllerGroup(frontRightMotor, backRightMotor);
    private SpeedControllerGroup leftGroup = new SpeedControllerGroup(frontLeftMotor, backLeftMotor);

    private DoubleSolenoid panelAdjustSolenoid = new DoubleSolenoid(RobotMap.SOLENOID_PANEL_FORWARD_ID, RobotMap.SOLENOID_PANEL_REVERSE_ID);
    private DoubleSolenoid panelPushSolenoid = new DoubleSolenoid(RobotMap.SOLENOID_PANEL_PUSH_ID, RobotMap.SOLENOID_PANEL_UNPUSH_ID);
    private DoubleSolenoid cargoSolenoid = new DoubleSolenoid(RobotMap.SOLENOID_CARGO_RAISE_ID, RobotMap.SOLENOID_CARGO_LOWER_ID);
    private Controller joystick = new Controller(RobotMap.JOYSTICK_ID, 5, 3, 6, 4, 8, 7, 1, 2);
    private Controller gamepad = new Controller(RobotMap.GAMEPAD_ID, 4, 3, 2, 1, 8, 7, 5, 1, 3, 2, 6, 5);
    private DifferentialDrive drive = new DifferentialDrive(leftGroup, rightGroup);

    private Timer timer = new Timer();

    private void setSpeed(Controller controller, double speed1, double speed2) {
        double speedMultiplier1;
        double speedMultiplier2;

        //Gamepad
        if (controller.MOTOR_SPEED_BUTTON == 0) {
            speedMultiplier1 = 0.6;
            speedMultiplier2 = 0.6;

            if (controller.getRawAxis(controller.MOTOR_RIGHT_SPEED_AXIS) > 0.3 && !controller.getRawButton(controller.MOTOR_RIGHT_SLOW_BUTTON)) {
                speedMultiplier1 = 1.0;
            }
            else if (controller.getRawAxis(controller.MOTOR_RIGHT_SPEED_AXIS) <= 0.3 && controller.getRawButton(controller.MOTOR_RIGHT_SLOW_BUTTON)) {
                speedMultiplier1 = 0.4;
            }

            if (controller.getRawAxis(controller.MOTOR_LEFT_SPEED_AXIS) > 0.3 && !controller.getRawButton(controller.MOTOR_LEFT_SLOW_BUTTON)) {
                speedMultiplier2 = 1.0;
            }
            else if (controller.getRawAxis(controller.MOTOR_LEFT_SPEED_AXIS) <= 0.3 && controller.getRawButton(controller.MOTOR_LEFT_SLOW_BUTTON)) {
                speedMultiplier2 = 0.4;
            }

            speed1 = controller.MOTOR_RIGHT_AXIS * speedMultiplier1;
            speed2 = controller.MOTOR_LEFT_AXIS * speedMultiplier2;
        }
        //Joystick
        else {
            if (controller.getTrigger() && !controller.getRawButton(controller.MOTOR_SLOW_BUTTON)) {
                speedMultiplier1 = 1.0;
                speedMultiplier2 = 0.8;
            }
            else if (!controller.getTrigger() && controller.getRawButton(controller.MOTOR_SLOW_BUTTON)) {
                speedMultiplier1 = 0.4;
                speedMultiplier2 = 0.32;
            }
            else {
                speedMultiplier1 = 0.6;
                speedMultiplier2 = 0.48;
            }

            speed1 = -controller.getY() * speedMultiplier1;
            speed2 = controller.getX() * speedMultiplier2;
        }
    }

    //Drive code for autonomous and teleoperated
    private void teleopDrive() {
        controlSolenoid(joystick, panelAdjustSolenoid, joystick.SOLENOID_PANEL_FORWARD_BUTTON, joystick.SOLENOID_PANEL_REVERSE_BUTTON);
        controlSolenoid(joystick, panelPushSolenoid, joystick.SOLENOID_PANEL_PUSH_BUTTON, joystick.SOLENOID_PANEL_UNPUSH_BUTTON);
        controlSolenoid(joystick, cargoSolenoid, joystick.SOLENOID_CARGO_RAISE_BUTTON, joystick.SOLENOID_CARGO_LOWER_BUTTON);

        controlSolenoid(gamepad, panelAdjustSolenoid, gamepad.SOLENOID_PANEL_FORWARD_BUTTON, gamepad.SOLENOID_PANEL_REVERSE_BUTTON);
        controlSolenoid(gamepad, panelPushSolenoid, gamepad.SOLENOID_PANEL_PUSH_BUTTON, gamepad.SOLENOID_PANEL_UNPUSH_BUTTON);
        controlSolenoid(gamepad, cargoSolenoid, gamepad.SOLENOID_CARGO_RAISE_BUTTON, gamepad.SOLENOID_CARGO_LOWER_BUTTON);

        //Speed 1: X Speed/Right Speed
        //Speed 2: Z Rotation/Left speed
        double speed1 = 0.0;
        double speed2 = 0.0;
        setSpeed(joystick, speed1, speed2);
        setSpeed(gamepad, speed1, speed2);

        drive.arcadeDrive(speed1, speed2);
        drive.tankDrive(speed1, speed2);
        Timer.delay(0.005);
    }

    //Control all solenoid buttons
    private void controlSolenoid(Controller controller, DoubleSolenoid doubleSolenoid, int forwardButton, int reverseButton) {
        if (controller.getRawButton(forwardButton) && controller.getRawButton(reverseButton)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kOff);
        }
        else if (controller.getRawButton(forwardButton)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kForward);
        }
        else if (controller.getRawButton(reverseButton)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
        }
        if (controller.getRawButtonReleased(forwardButton) || controller.getRawButtonReleased(reverseButton)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kOff);
        }
    }

	//Runs at initialization
	@Override
	public void robotInit() {
		//Set autonomous modes (configurable via Smart Dashboard)
		AUTO_MODE_CHOOSER.setDefaultOption(AUTO_MODE_TELEOP, AUTO_MODE_TELEOP);
		AUTO_MODE_CHOOSER.addOption(AUTO_MODE_1, AUTO_MODE_1);
        SmartDashboard.putData("AUTO_MODES", AUTO_MODE_CHOOSER);
        
        CameraServer.getInstance().startAutomaticCapture();

        drive.setSafetyEnabled(true);
	}
	
	//Runs each cycle
	@Override
	public void robotPeriodic() {
		
	}
	
	//Runs at autonomous mode initialization
	@Override
	public void autonomousInit() {
		currentAutoMode = AUTO_MODE_CHOOSER.getSelected();
        System.out.println("Selected Auto Mode: " + currentAutoMode);
        
        //Use timer to manage autonomous actions
        timer.reset();
        timer.start();
	}
	
	//Runs during autonomous mode
	@Override
	public void autonomousPeriodic() {
		switch (currentAutoMode) {
            case AUTO_MODE_TELEOP:
                teleopDrive();
                break;
            case AUTO_MODE_1:
                drive.arcadeDrive(0.5, 0.0);
                Timer.delay(0.5);
                drive.arcadeDrive(-0.5, 0.0);
                break;
		}
	}
	
	//Runs during teleoperation mode
	@Override
	public void teleopPeriodic() {
        teleopDrive();
	}
	
	//Runs during test mode
	@Override
	public void testPeriodic() {
		
	}
}