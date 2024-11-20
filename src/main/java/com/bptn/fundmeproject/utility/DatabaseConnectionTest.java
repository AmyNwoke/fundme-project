/*package com.bptn.fundmeproject.utility;


import java.sql.Connection;
import java.sql.SQLException;
	

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnectionF.getConnection();
            if (connection != null) {
                System.out.println("Connected to the database!");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
*/