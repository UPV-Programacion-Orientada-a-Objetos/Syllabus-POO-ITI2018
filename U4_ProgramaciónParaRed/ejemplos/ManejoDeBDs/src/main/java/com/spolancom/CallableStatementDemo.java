package com.spolancom;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class CallableStatementDemo {

    public static void main(String[] args) {
        String result = replace("That is original text", "original text", "the result");

        System.out.println(result);
    }

    private static String replace(String origText, String substr1, String substr2) {
        String result = "";
        String sql = "{ ? = call replace(?, ?, ?) }";
        Connection conn = App.connectionWithPSG();
        try (conn; CallableStatement st = conn.prepareCall(sql)) {
            st.registerOutParameter(1, Types.VARCHAR);
            st.setString(2, origText);
            st.setString(3, substr1);
            st.setString(4, substr2);
            st.execute();
            result = st.getString(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }
}
