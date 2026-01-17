CREATE TABLE IF NOT EXISTS students (
  student_id  BIGINT PRIMARY KEY,
  first_name  VARCHAR(8) NOT NULL,
  last_name   VARCHAR(8) NOT NULL,
  dob         DATE NOT NULL,
  class_name  VARCHAR(10) NOT NULL,
  score       INT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_students_class ON students(class_name);
