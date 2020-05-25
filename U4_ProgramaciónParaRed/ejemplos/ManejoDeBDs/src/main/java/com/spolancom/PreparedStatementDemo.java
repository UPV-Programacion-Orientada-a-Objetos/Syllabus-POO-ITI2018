package com.spolancom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreparedStatementDemo {

    public static void main(String[] args) {

        List<Person> persons = selectPersonsByFirstName("Said");
        System.out.println(persons.size());

    }

    private static List<Person> selectPersonsByFirstName(String searchName) {
        List<Person> list = new ArrayList<>();
        Connection conn = App.connectionWithPSG();
        String sql = "select id, first_name, last_name, dob from person where first_name = ?";
        try (conn; PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, searchName);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Person(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("dob").toLocalDate()));
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
