package com.acompany.Server;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerT {

    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    private static ObjectInputStream inO;

    public static void main(String[] args) {
        try {
            try  {
                server = new ServerSocket(4004); // серверсокет прослушивает порт 4004
                System.out.println("Сервер запущен!"); // хорошо бы серверу
                //   объявить о своем запуске
                clientSocket = server.accept(); // accept() будет ждать пока
                //кто-нибудь не захочет подключиться
                try { // установив связь и воссоздав сокет для общения с клиентом можно перейти
                    // к созданию потоков ввода/вывода.
                    // теперь мы можем принимать сообщения
                    //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    // и отправлять
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    inO = new ObjectInputStream(clientSocket.getInputStream());
                    //System.out.println(inO.readInt());
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    frame.setSize(1280, 720);
                    while (true) {
                        for (int i = 0; i < 25; i++) {


                            int length = inO.readInt();
                            byte[] bytes = new byte[length];
                            System.out.println("got" + length);
                            out.write("got" + length + "\n");
                            out.flush(); // выталкиваем все из буфера
                            inO.readFully(bytes);
                            ByteArrayInputStream baos = new ByteArrayInputStream(bytes);

                            BufferedImage image = ImageIO.read(baos);


                            JPanel pane = new JPanel() {
                                protected void paintComponent(Graphics g) {
                                    super.paintComponent(g);

                                    g.drawImage(image, 0, 0, 1280, 720, null);


                                }


                            };


                            frame.add(pane);
                            frame.setVisible(true);
//                    File outputfile = new File("C:\\logs\\image.jpg");
//                    ImageIO.write(image, "jpg", outputfile);

                            //String word = in.readLine(); // ждём пока клиент что-нибудь нам напишет
                            //System.out.println(word);
                            // не долго думая отвечает клиенту
                            out.write("Привет, это Сервер! Подтверждаю, вы написали : " + "\n");
                            out.flush(); // выталкиваем все из буфера
                        }
                    }

                } finally { // в любом случае сокет будет закрыт
                    System.out.println("dfjkhgkdf");
                    clientSocket.close();
                    // потоки тоже хорошо бы закрыть
                    //in.close();
                    out.close();
                    inO.close();
                }
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
