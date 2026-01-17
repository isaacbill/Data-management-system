package com.student.data.management.system.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.student.data.management.system.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class StudentSearchRepository {

  private final JdbcTemplate jdbcTemplate;
  private final StudentRowMapper mapper = new StudentRowMapper();

  public StudentSearchRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Map<String, Object> search(Long studentId, String className, int page, int size) {
    if (page < 0) page = 0;
    if (size < 1) size = 10;

    String base = " FROM students WHERE 1=1 ";
    List<Object> params = new ArrayList<>();

    if (studentId != null) {
      base += " AND student_id = ? ";
      params.add(studentId);
    }
    if (className != null && !className.isBlank()) {
      base += " AND class_name = ? ";
      params.add(className.trim());
    }

    long total = jdbcTemplate.queryForObject("SELECT COUNT(*) " + base, params.toArray(), Long.class);

    int offset = page * size;
    String dataSql = "SELECT student_id, first_name, last_name, dob, class_name, score "
        + base
        + " ORDER BY student_id ASC LIMIT ? OFFSET ?";

    List<Object> dataParams = new ArrayList<>(params);
    dataParams.add(size);
    dataParams.add(offset);

    List<Student> rows = jdbcTemplate.query(dataSql, dataParams.toArray(), mapper);

    return Map.of(
        "content", rows,
        "page", page,
        "size", size,
        "totalElements", total,
        "totalPages", (int) Math.ceil(total / (double) size)
    );
  }

  public List<Student> listAll(Long studentId, String className) {
    String base = " FROM students WHERE 1=1 ";
    List<Object> params = new ArrayList<>();

    if (studentId != null) {
      base += " AND student_id = ? ";
      params.add(studentId);
    }
    if (className != null && !className.isBlank()) {
      base += " AND class_name = ? ";
      params.add(className.trim());
    }

    String sql = "SELECT student_id, first_name, last_name, dob, class_name, score "
        + base
        + " ORDER BY student_id ASC";

    return jdbcTemplate.query(sql, params.toArray(), mapper);
  }
}