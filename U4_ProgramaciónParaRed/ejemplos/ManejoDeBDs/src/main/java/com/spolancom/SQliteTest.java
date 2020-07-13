package com.spolancom;



import java.sql.*;

public class SQliteTest {

    public static void main(String[] args) {
        Connection conn =  connect();
        createNewTable(conn);

         insert(conn, "Raw Materials", 3000);
         insert(conn, "Semifinished Goods", 4000);
         insert(conn, "Finished Goods", 5000);

        selectAll(conn);

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public static Connection connect() {
        try {
            // db parameters
            //String url = "jdbc:sqlite:/tmp/my_sqlite.db";
            String url = "jdbc:sqlite::memory:";
            // create a connection to the database
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean createNewTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	capacity real\n"
                + ");";

        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);

            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void insert(Connection conn, String name, double capacity) {
        String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, capacity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void selectAll(Connection conn){
        String sql = "SELECT id, name, capacity FROM warehouses";

        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getDouble("capacity"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
