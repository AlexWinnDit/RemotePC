package com.acompany.ClientPC;

import javax.swing.*;
import java.net.Socket;

public class Client {
    private static String port = "4907";

    public static void main(String[] args) {
        String ip = JOptionPane.showInputDialog("Please enter server ip");
        new Client().initialize(ip, Integer.parseInt(port));
    }

    private void initialize(String ip, int port) {
        try {

            Socket sc = new Socket(ip, port);
            System.out.println("Connecting to the Server");
            //Authentication class is responsible for security purposes
            Authentication frame1 = new Authentication(sc);

            frame1.setSize(300, 80);
            frame1.setLocation(500, 300);
            frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame1.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



