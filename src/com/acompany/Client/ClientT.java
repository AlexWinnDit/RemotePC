package com.acompany.Client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientT {

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader; // нам нужен ридер читающий с консоли, иначе как
    // мы узнаем что хочет сказать клиент?
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    private static ObjectOutputStream outO;


    public static void main(String[] args) {
        try {
            try {
                // адрес - локальный хост, порт - 4004, такой же как у сервера
                clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем
                //  у сервера доступ на соединение
                reader = new BufferedReader(new InputStreamReader(System.in));
                // читать соообщения с сервера
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // писать туда же

                outO = new ObjectOutputStream(clientSocket.getOutputStream());
                while (true) {
                    for (int i = 0; i < 25; i++) {


                        byte[] screen = grabScreen();
                        outO.writeInt(screen.length);
                        outO.flush();
                        System.out.println("flush length " + screen.length);
                        String serverWord = in.readLine(); // ждём, что скажет сервер
                        System.out.println(serverWord); // получив - выводим на экран
                        outO.write(screen);
                        outO.flush();
                        //out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                        System.out.println("flush image");
                        // если соединение произошло и потоки успешно созданы - мы можем
                        //  работать дальше и предложить клиенту что то ввести
                        // если нет - вылетит исключение

                        // не напишет в консоль
                        //out.write(word + "\n"); // отправляем сообщение на сервер
                        //out.flush();
                        serverWord = in.readLine(); // ждём, что скажет сервер
                        System.out.println(serverWord); // получив - выводим на экран
                        //TimeUnit.MILLISECONDS.sleep(40);
                    }
                }



            } finally { // в любом случае необходимо закрыть сокет и потоки
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                //out.close();
                outO.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
    private static byte[] grabScreen() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (SecurityException e) {
        } catch (AWTException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

