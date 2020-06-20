package ru.gb.chat.server.core;

import java.sql.*;

public class SqlClient {

    private static Connection connection;
    private static Statement statement;
    private static String loginCl;

    synchronized static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:chat-server/chat-db.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized static void disconnect() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    synchronized static String getNickname(String login, String password) {
        try {
            ResultSet rs = statement.executeQuery(
                    String.format("select nickname from users where login = '%s' and password = '%s'",
                            login, password));
            loginCl = login;
            if (rs.next()) {
                String nick = rs.getString("nickname");
                return nick;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    synchronized static String getNickname() {
        try {
            ResultSet rs = statement.executeQuery(
                    String.format("select nickname from users where login = '%s'",
                            loginCl));
            if (rs.next()) {
                String nick = rs.getString("nickname");
                return nick;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    synchronized static String setNickname(String nickNew) {
        System.out.println("setNickname");
        System.out.println(nickNew);
        System.out.println(loginCl);
        try {
            int rs = statement.executeUpdate(
                    String.format("update users set nickname = '%s' where login = '%s'"  ,
                            nickNew, loginCl));
            ResultSet res = statement.executeQuery(
                    String.format("select nickname from users where login = '%s'",
                            loginCl));
            if (res.next() && rs > 0 ) {
                String nick = res.getString("nickname");
                return nick;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
