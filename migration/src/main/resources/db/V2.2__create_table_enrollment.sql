CREATE TABLE enrollment (
  enrollment_id BIGSERIAL PRIMARY KEY,
  student_id INT REFERENCES student(student_id),
  course_id BIGINT REFERENCES course(course_id)
)
