package com.acompany.ClientPC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class Frame extends Thread {
    String width = "", height = "";
    private JFrame frame = new JFrame();

    //JDesktopPane represents the main container that will contain all connected clients' screens

    private JDesktopPane desktop = new JDesktopPane();
    private Socket cSocket = null;
    private JInternalFrame interFrame = new JInternalFrame("Server Screen", true, true, true);
    private JPanel cPanel = new JPanel();

    public Frame(Socket cSocket, String width, String height) {

        this.width = width;
        this.height = height;
        this.cSocket = cSocket;
        start();
    }

    //Draw GUI per each connected client

    public void drawGUI() {
        frame.add(desktop, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Show thr frame in maximized state

        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);        //CHECK THIS LINE
        frame.setVisible(true);
        interFrame.setLayout(new BorderLayout());
        interFrame.getContentPane().add(cPanel, BorderLayout.CENTER);
        interFrame.setSize(100, 100);
        desktop.add(interFrame);

        try {
            //Initially show the internal frame maximized
            interFrame.setMaximum(true);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }

        //This allows to handle KeyListener events
        cPanel.setFocusable(true);
        interFrame.setVisible(true);

    }

    public void run() {
        //Used to read screenshots
        InputStream in = null;

        drawGUI();

        try {
            in = cSocket.getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Client receiving screenshots
        new ScreenReceiver(in, cPanel);
        //Client sending events to the client
        new EventHandler(cSocket, cPanel, width, height);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }


}
