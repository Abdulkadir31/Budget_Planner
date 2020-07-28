package com.example.abdul.oep_database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

    public Connection jconnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://192.168.182.2:3306/test", "abdulkadir", "miracle");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
}
