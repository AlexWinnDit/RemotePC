package com.acompany.ServerPC;

import javax.swing.*;

public class Server {
    public static void main(String[] args) {
        Security frame1 = new Security();
        frame1.setSize(300, 80);
        frame1.setLocation(500, 300);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(true);

    }
}
