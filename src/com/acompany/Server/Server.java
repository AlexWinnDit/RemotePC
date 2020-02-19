package com.acompany.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        SQLHandler.connect();
        MyServer w = new MyServer();
        // SQLHandler.disconnect();
    }
}

class MyServer {
    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>(); // Список клиентов

    public MyServer() {
        ServerSocket server = null;
        Socket s = null;
        try {
            server = new ServerSocket(8189); // Создание сервера
            System.out.println("Сервер создан. Ожидание клиентов...");
            while (true) {                                    // Цикл ожидания клиентов и создания обработчиков
                s = server.accept();
                System.out.println("Клиент подключился");
                // когда создается обработчик ему передается ссылка на сервер, на котором он работает
                ClientHandler h = new ClientHandler(s, this);
                clients.add(h);                             // после чего добавляем этот обработчик в список клиентов
                new Thread(h).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {                                              // Закрытие сервера
            try {
                server.close();
                System.out.println("Сервер закрыт");
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void remove(ClientHandler o) {    // Метод, отвечающий за удаление обработчика из списка клиентов
        clients.remove(o);
    }

    public void broadcastMsg(String msg) { // Метод, рассылающий всем клиентам сообщение
        for (ClientHandler o : clients) {           // “Бежим” по списку клиентов
            o.sendMsg(msg);                           // и каждому пишем сообщение msg
        }
    }
}

class SQLHandler {
    private static Connection conn;                   // соединение с базой данных
    private static PreparedStatement stmt;       // объект, отвечающий за отправку запросов

    public static void connect() {                        // подключение к БД
        try {
            Class.forName("org.sqlite.JDBC");    // регистрация драйвера
            conn = DriverManager.getConnection("jdbc:sqlite:ClientsDB.db"); // открытие соединения
        } catch (Exception c) {
            System.out.println("Ошибка соединения с базой данных");
        }
    }

    public static void disconnect() {                  // отключение от БД
        try {
            conn.close();                                      // закрываем соединение
        } catch (Exception c) {
            System.out.println("Ошибка соединения с базой данных");
        }
    }

    // поиск ника по логину/паролю
    public static String getNickByLoginPassword(String login, String password) {
        String w = "VZLOM";
//        try {
//// формируем запрос на выборку ника по логину/паролю
//            stmt = conn.prepareStatement("SELECT Nickname FROM main WHERE Login = ? AND Password = ?;");
//            stmt.setString(1, login);                      // указываем логин в запросе
//            stmt.setString(2, password);              // указываем пароль в запросе
//            ResultSet rs = stmt.executeQuery(); // выполняем запрос и получаем результат
//            if (rs.next())
//                w = rs.getString("Nickname");
//        } catch (SQLException e) {
//            System.out.println("SQL Query Error");
//        }
        return w;
    }
}

class ClientHandler implements Runnable {
    private MyServer owner; // ссылка на сервер, на котором работает ClientHandler
    private Socket s;
    private DataOutputStream out;
    private DataInputStream in;
    private String name;

    public ClientHandler(Socket s, MyServer owner) {
        try {
            this.s = s;
            this.owner = owner;
            out = new DataOutputStream(s.getOutputStream());
            in = new DataInputStream(s.getInputStream());
            name = "";
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String w = in.readUTF();
                if (name.isEmpty()) {
                    String[] n = w.split("\t");
                    String t = SQLHandler.getNickByLoginPassword(n[1], n[2]);
                    if (t != null) {
                        name = t;
                    } else {
                        sendMsg("Ошибка авторизации");
                        owner.remove(this);
                        break;
                    }
                    w = null;
                }
                if (w != null) {
                    owner.broadcastMsg(name + ": " + w);
                    System.out.println(name + ": " + w);
                    if (w.equalsIgnoreCase("END")) break;
                }
                Thread.sleep(100);
            }
        } catch (IOException e) {
            System.out.println("Output Error");
        } catch (InterruptedException e) {
            System.out.println("Thread sleep error");
        }
        try {
            System.out.println("Клиент отключился");
            if (!name.equals("")) owner.remove(this);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
        }
    }
}





