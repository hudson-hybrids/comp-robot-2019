/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;

public class Camera {
    private int ID;
    private String name;

    public Camera(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public void start() {
        CameraServer.getInstance().startAutomaticCapture(ID);
    }

    public void stop() {
        CameraServer.getInstance().removeCamera(name);
    }
}