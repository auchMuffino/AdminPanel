package needs.importantclasses;

import java.sql.*;

public class DBConnection {
    private static Connection connection;

    public static void openConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:MySQL://localhost:3306/GoodsAndPromotions","root","1234567890123");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void closeConnection(){
        try {
            if(!connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void statementExecute(String query){
        try {
            if (!connection.isClosed()) {
                (connection.createStatement()).executeUpdate(query);
            }
            else{
                System.out.println("No connection");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ResultSet statementExecuteQuery(String query){
        try {
            if (!connection.isClosed()) {
                return (connection.createStatement()).executeQuery(query);
            }
            else{
                System.out.println("No connection");
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/GoodsAndPromotions","root","1234567890123");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}