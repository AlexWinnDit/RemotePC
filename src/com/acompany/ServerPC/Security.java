package com.acompany.ServerPC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Security extends JFrame implements ActionListener {
    private static String port = "4907";
    private JButton SUBMIT;
    private JPanel panel;
    private JLabel label1, label2;
    private JTextField text1, text2;
    private String value1;
    private JLabel label;

    Security() {
        label1 = new JLabel();
        label1.setText("Set Password");
        text1 = new JTextField(15);

        label = new JLabel();
        label.setText("");


        this.setLayout(new BorderLayout());

        SUBMIT = new JButton("SUBMIT");

        panel = new JPanel(new GridLayout(2, 1));
        panel.add(label1);
        panel.add(text1);

        panel.add(label);
        panel.add(SUBMIT);
        add(panel, BorderLayout.CENTER);
        SUBMIT.addActionListener(this);
        setTitle("Set Password to connect to the Client");

    }

    public void actionPerformed(ActionEvent ae) {


        value1 = text1.getText();
        dispose();
        new Connection(Integer.parseInt(port), value1);
    }

    public String getValue1() {

        return value1;
    }


}
