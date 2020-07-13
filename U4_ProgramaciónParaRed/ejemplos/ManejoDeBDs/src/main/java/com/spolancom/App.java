package com.spolancom;

import org.postgresql.ds.PGConnectionPoolDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.PooledConnection;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // Connection myc = connectionWithDM();
        // Connection myc = connectionWithPSG();
        // connectionPool();

        executeCommands();
        // executeQueryCommands();
        // executeUpdateCommands();

        // System.out.println(myc);
    }

    public static void executeCommands() {
        cleanTablePerson();
        insertExecute();
        selectExecute();
        updateExecute();
        deleteExecute();
    }

    public static void executeQueryCommands() {
        cleanTablePerson();
        insertExecuteQuery();
        selectExecuteQuery();
        updateExecuteQuery();
        deleteExecuteQuery();
    }

    public static void executeUpdateCommands() {
        cleanTablePerson();
        insertExecuteUpdate();
        selectExecuteUpdate();
        updateExecuteUpdate();
        deleteExecuteUpdate();
    }

    private static void insertExecuteUpdate() {
        System.out.println("\ninsertExecuteUpdate()");

        String sql = "insert into person (first_name, last_name, dob) values ('Bill', 'Grey', '1980-01-27')";
        Connection conn = connectionWithPSG();
        try (conn; Statement st = conn.createStatement()) {
            System.out.println(st.executeUpdate(sql));
            System.out.println(st.getResultSet() == null);
            System.out.println(st.getUpdateCount());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println(selectAllFirstNames());
    }

    private static void selectExecuteUpdate() {
        System.out.println("\nselectExecuteUpdate()");

        String sql = "select first_name from person";
        Connection conn = connectionWithPSG();
        try (conn;Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
            // no tiene sentido poner otra instrucción porque lanza una excepción
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void updateExecuteUpdate() {
        System.out.println("\nupdateExecuteUpdate()");
        String sql = "update person set first_name = 'Adam' where first_name = 'Bill'";
        Connection conn = connectionWithPSG();
        try (conn; Statement st = conn.createStatement()) {
            System.out.println(st.executeUpdate(sql));
            System.out.println(st.getResultSet() == null);
            System.out.println(st.getUpdateCount());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(selectAllFirstNames());
    }

    private static void deleteExecuteUpdate() {
        System.out.println("\ndeleteExecuteUpdate():");
        String sql = "delete from person where first_name = 'Adam'";
        Connection conn = connectionWithPSG();
        try (conn; Statement st = conn.createStatement()) {
            System.out.println(st.executeUpdate(sql));
            System.out.println(st.getResultSet() == null);
            System.out.println(st.getUpdateCount());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(selectAllFirstNames());
    }

    private static void insertExecuteQuery() {
        System.out.println("\ninsertExecuteQuery()");

        String sql = "insert into person (first_name, last_name, dob) values ('Bill', 'Grey', '1980-01-27')";

        Connection conn = connectionWithPSG();
        try (conn; Statement st = conn.createStatement()) {
            st.executeQuery(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(selectAllFirstNames());
    }

    private static void selectExecuteQuery() {
        System.out.println("\nSelectExecuteQuery()");

        String sql = "select first_name from person";
        Connection conn = connectionWithPSG();
        try (conn; Statement st = conn.createStatement()) {
            ResultSet rs1 = st.executeQuery(sql);
            System.out.println(rs1 == null);
            ResultSet rs2 = st.getResultSet();
            System.out.println(rs2 == null);
            System.out.println(st.getUpdateCount());

//            System.out.println(rs1 == rs2);
//            System.out.println(rs1.equals(rs2));
//            System.out.println(rs1 + " == " + rs2);

            while (rs1.next()) {
                System.out.println(rs1.getString(1));
            }

            while (rs2.next()) {
                System.out.println(rs2.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void updateExecuteQuery() {
        System.out.println("\nupdateExecuteQuery()");

        String sql = "update person set first_name = 'Adam' where first_name = 'Bill'";
        Connection conn = connectionWithPSG();
        try (conn; Statement st = conn.createStatement()) {
            st.executeQuery(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println(selectAllFirstNames());
    }

    private static void deleteExecuteQuery() {
        System.out.println("\ndeleteExecuteQuery()");

        String sql = "delete from person where first_name = 'Adam'";
        Connection conn = connectionWithPSG();
        try (conn; Statement st = conn.createStatement()) {
            st.executeQuery(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println(selectAllFirstNames());
    }

    private static void cleanTablePerson() {
        System.out.println("\ncleaning Table Person...");
        try (Connection conn = connectionWithPSG(); Statement st = conn.createStatement()) {
            st.execute("delete from person");
            System.out.println("done...");
        }
        catch (SQLException e) {
            System.out.println("An Exception has occurred");
        }
    }

    private static void insertExecute() {
        System.out.println("\ninsertExecute():");
        String sql = "insert into person (first_name, last_name, dob) values ('Bill', 'Gray', '1980-01-27')";
        Connection conn = connectionWithPSG();
        try (conn; Statement st = conn.createStatement()) {
            System.out.println(st.execute(sql));
            System.out.println(st.getResultSet() == null);
            System.out.println(st.getUpdateCount());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(selectAllFirstNames());
    }

    private static void selectExecute() {
        System.out.println("\nselectExecute(): ");
        String sql = "SELECT first_name FROM person";
        Connection conn = connectionWithPSG();
        try(conn; Statement st = conn.createStatement()) {
            System.out.println(st.execute(sql));
            ResultSet rs = st.getResultSet();
            System.out.println(rs == null);
            System.out.println(st.getUpdateCount());

            while (rs.next()) {
                System.out.println(rs.getString(1) + " ");
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateExecute() {
        System.out.println("\nupdateExecute():");

        String sql = "update person set first_name = 'Adam' where first_name = 'Bill'";
        Connection conn = connectionWithPSG();
        try (conn; Statement st = conn.createStatement()) {
            System.out.println(st.execute(sql));
            System.out.println(st.getResultSet() == null);
            System.out.println(st.getUpdateCount());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(selectAllFirstNames());
    }

    private static void deleteExecute() {
        System.out.println("\ndeleteExecute()");
        String sql = "delete from person where first_name = 'Adam'";
        Connection conn = connectionWithPSG();
        try(conn; Statement st = conn.createStatement()) {
            System.out.println(st.execute(sql));
            System.out.println(st.getResultSet() == null);
            System.out.println(st.getUpdateCount());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(selectAllFirstNames());
    }

    public static String selectAllFirstNames() {
        String result = "";

        Connection conn = connectionWithPSG();
        try (conn; Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT first_name FROM person");
            while (rs.next()) {
                // result += rs.getString(1) + " ";
                result += rs.getString("first_name") + " ";
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Connection connectionWithDM() {
        String URL = "jdbc:postgresql://localhost/poo_db_test";

        Properties prop = new Properties();
        prop.put("user", "poo_student");

        try {
            return DriverManager.getConnection(URL, prop);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Connection connectionWithPSG() {
        PGSimpleDataSource source = new PGSimpleDataSource();
        String[] serversNames = {"localhost"};

        source.setServerNames(serversNames);
        source.setDatabaseName("poo_db_test");
        source.setUser("poo_student");
        // source.setPassword("xxx");
        source.setLoginTimeout(10);

        Connection conn = null;

        try {
            return source.getConnection();
            // System.out.println(conn);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return  null;
    }

    private static void connectionPool() {
        PGConnectionPoolDataSource source = new PGConnectionPoolDataSource();
        String[] serversNames = {"localhost"};

        source.setServerNames(serversNames);
        source.setDatabaseName("poo_db_test");
        source.setUser("poo_students");
        // source.setPassword("xxx");
        source.setLoginTimeout(10);

        try {
            PooledConnection conn = source.getPooledConnection();

            Set<Connection> pool = new HashSet<>();

            for (int i=0; i < 10; i++) {
                pool.add(conn.getConnection());
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
