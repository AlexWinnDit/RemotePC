package com.acompany.ServerPC;

import com.acompany.ServerPC.SetPassword;

public class Server {
    public static void main(String[] args) {
        SetPassword frame1 = new SetPassword();
        frame1.setSize(300, 80);
        frame1.setLocation(500, 300);
        frame1.setVisible(true);
    }
}
