/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.inria.papart.procam.camera;

import fr.inria.papart.procam.ProjectiveDeviceP;
import fr.inria.papart.procam.Utils;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacpp.opencv_core.IplImage;
import processing.core.PApplet;
import processing.core.PImage;

/**
 *
 * @author Jérémy Laviole - jeremy.laviole@inria.fr
 */
public class ProjectorAsCamera extends Camera {

    public void setImage(IplImage image) {
        this.currentImage = image;
    }

    public void setCalibration(String fileName) {
        try {
            this.calibrationFile = fileName;
            pdp = ProjectiveDeviceP.loadProjectorDevice(parent, fileName);
            camIntrinsicsP3D = pdp.getIntrinsics();
            this.width = pdp.getWidth();
            this.height = pdp.getHeight();
            this.undistort = pdp.handleDistorsions();
        } catch (Exception e) {
            e.printStackTrace();

            System.err.println("Camera: error reading the calibration " + pdp
                    + "file" + fileName + " \n" + e);
        }

        System.out.println("Calibration : ");
        camIntrinsicsP3D.print();
    }

    static public void convertARProjParams(PApplet parent, String calibrationFile,
            String calibrationARtoolkit) {
        try {
            Utils.convertProjParam(parent, calibrationFile, calibrationARtoolkit);
        } catch (Exception ex) {
            System.out.println("Error converting projector to ARToolkit "
                    + calibrationFile + " " + calibrationARtoolkit
                    + ex);
        }
    }

    @Override
    public void start() {
        return;
    }

    /**
     * Not implemented.
     *
     * @return
     */
    @Override
    public PImage getPImage() {
        return null;
    }

    /**
     * Not used.
     */
    @Override
    public void grab() {
    }

    /**
     * Not used.
     */
    @Override
    public void close() {
    }

}