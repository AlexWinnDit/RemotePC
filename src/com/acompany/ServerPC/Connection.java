package com.acompany.ServerPC;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {

    private ServerSocket socket = null;
    private DataInputStream password = null;
    private DataOutputStream verify = null;


    Connection(int port, String value1) {
        Robot robot = null;
        Rectangle rectangle = null;
        try {
            System.out.println("Awaiting Connection from Client");
            socket = new ServerSocket(port);

            GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gDev = gEnv.getDefaultScreenDevice();

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            String width = "" + dim.getWidth();
            String height = "" + dim.getHeight();
            rectangle = new Rectangle(dim);
            robot = new Robot(gDev);

            drawGUI();

            while (true) {
                Socket sc = socket.accept();
                password = new DataInputStream(sc.getInputStream());
                verify = new DataOutputStream(sc.getOutputStream());

                String pssword = password.readUTF();

                if (pssword.equals(value1)) {
                    verify.writeUTF("valid");
                    verify.writeUTF(width);
                    verify.writeUTF(height);
                    new ScreenSender(sc, robot, rectangle);
                    new EventHandler(sc, robot);
                } else {
                    verify.writeUTF("Invalid");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawGUI() {
    }

}
