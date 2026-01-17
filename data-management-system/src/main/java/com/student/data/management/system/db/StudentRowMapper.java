package com.student.data.management.system.db;

import org.springframework.jdbc.core.RowMapper;

import com.student.data.management.system.model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {
  @Override
  public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new Student(
        rs.getLong("student_id"),
        rs.getString("first_name"),
        rs.getString("last_name"),
        rs.getDate("dob").toLocalDate(),
        rs.getString("class_name"),
        rs.getInt("score")
    );
  }
}
