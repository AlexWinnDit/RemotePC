package com.acompany.ServerPC;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import javax.imageio.ImageIO;


class ScreenSender extends Thread {

    private Socket socket = null;
    private Robot robot = null;
    private Rectangle rectangle = null;
    private boolean continueLoop = true;

    OutputStream oos = null;

    public ScreenSender(Socket socket, Robot robot, Rectangle rect) {
        this.socket = socket;
        this.robot = robot;
        rectangle = rect;
        start();
    }

    public void run() {

        try {
            oos = socket.getOutputStream();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        while (continueLoop) {
            BufferedImage image = robot.createScreenCapture(rectangle);

            try {
                ImageIO.write(image, "jpeg", oos);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

