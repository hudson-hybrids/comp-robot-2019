/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

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
    private Joystick joystick = new Joystick(RobotMap.JOYSTICK_ID);
    private DifferentialDrive drive = new DifferentialDrive(leftGroup, rightGroup);

    private Timer timer = new Timer();

    //Drive code for autonomous and teleoperated
    private void teleopDrive() {
        controlSolenoid(panelAdjustSolenoid, RobotMap.SOLENOID_PANEL_FORWARD_BUTTON, RobotMap.SOLENOID_PANEL_REVERSE_BUTTON);
        controlSolenoid(panelPushSolenoid, RobotMap.SOLENOID_PANEL_PUSH_BUTTON, RobotMap.SOLENOID_PANEL_UNPUSH_BUTTON);
        controlSolenoid(cargoSolenoid, RobotMap.SOLENOID_CARGO_RAISE_BUTTON, RobotMap.SOLENOID_CARGO_LOWER_BUTTON);

        double ySpeedMultiplier = 1.0;
        double xSpeedMultiplier = 1.0;
        if (joystick.getTrigger() && !joystick.getRawButton(RobotMap.MOTOR_SLOW_BUTTON)) {
            ySpeedMultiplier = 1.0;
            xSpeedMultiplier = 0.8;
        }
        else if (!joystick.getTrigger() && joystick.getRawButton(RobotMap.MOTOR_SLOW_BUTTON)) {
            ySpeedMultiplier = 0.4;
            xSpeedMultiplier = 0.32;
        }
        else {
            ySpeedMultiplier = 0.6;
            xSpeedMultiplier = 0.48;
        }
        double xSpeed = -joystick.getY() * ySpeedMultiplier;
        double zRotation = joystick.getX() * xSpeedMultiplier;

        drive.arcadeDrive(xSpeed, zRotation);
        Timer.delay(0.005);
    }

    //Control all solenoid buttons
    private void controlSolenoid(DoubleSolenoid doubleSolenoid, int forwardButton, int reverseButton) {
        if (joystick.getRawButton(forwardButton) && joystick.getRawButton(reverseButton)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kOff);
        }
        else if (joystick.getRawButton(forwardButton)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kForward);
        }
        else if (joystick.getRawButton(reverseButton)) {
            doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
        }
        if (joystick.getRawButtonReleased(forwardButton) || joystick.getRawButtonReleased(reverseButton)) {
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