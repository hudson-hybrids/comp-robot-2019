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

import frc.robot.ArcadeDriveController;
import frc.robot.TankDriveController;
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
    private ArcadeDriveController joystick = new ArcadeDriveController(RobotMap.JOYSTICK_ID, 3, 5, 4, 6, 7, 8, 1, 2);
    private TankDriveController gamepad = new TankDriveController(RobotMap.GAMEPAD_ID, 3, 4, 1, 2, 7, 8, 5, 1, 3, 2, 6, 5);
    private DifferentialDrive differentialDrive = new DifferentialDrive(leftGroup, rightGroup);

    private Timer timer = new Timer();

    //Drive code for autonomous and teleoperated
    private void teleopDrive() {
        gamepad.drive(differentialDrive, panelAdjustSolenoid, panelPushSolenoid, cargoSolenoid);
        joystick.drive(differentialDrive, panelAdjustSolenoid, panelPushSolenoid, cargoSolenoid);

        Timer.delay(0.005);
    }

	//Runs at initialization
	@Override
	public void robotInit() {
		//Set autonomous modes (configurable via Smart Dashboard)
		AUTO_MODE_CHOOSER.setDefaultOption(AUTO_MODE_TELEOP, AUTO_MODE_TELEOP);
		AUTO_MODE_CHOOSER.addOption(AUTO_MODE_1, AUTO_MODE_1);
        SmartDashboard.putData("AUTO_MODES", AUTO_MODE_CHOOSER);
        CameraServer.getInstance().startAutomaticCapture();

        joystick.setCanDrive(true);
        //gamepad.setCanDrive(true);
        //joystick.setCanControlSolenoids(true);
        gamepad.setCanControlSolenoids(true);
        differentialDrive.setSafetyEnabled(true);
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