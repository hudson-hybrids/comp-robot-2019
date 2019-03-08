/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import org.opencv.core.Mat;

public class Camera {
    private int ID;
    private UsbCamera camera;
    private CvSink cvSink;
    public static final int WIDTH_RESOLUTION = 640;
    public static final int HEIGHT_RESOLUTION = 480;

    public Camera(int ID) {
        this.ID = ID;
        camera = CameraServer.getInstance().startAutomaticCapture(ID);
        camera.setResolution(WIDTH_RESOLUTION, HEIGHT_RESOLUTION);
        cvSink = CameraServer.getInstance().getVideo(camera);

        cvSink.setEnabled(false);
    }

    public void setEnabled(boolean enabled) {
        cvSink.setEnabled(enabled);
        //Display Frame: cvSource.putFrame(mat);
    }

    public void grabFrame(Mat mat) {
        cvSink.grabFrame(mat);
    }

    static public void displayFrame(CvSource cvSource, Mat mat) {
        cvSource.putFrame(mat);
    }
}