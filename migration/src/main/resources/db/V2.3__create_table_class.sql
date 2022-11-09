CREATE TABLE class (
  class_id BIGSERIAL PRIMARY KEY,
  has_attended BOOLEAN,
  passed_homework BOOLEAN,
  student_id INT REFERENCES student(student_id),
  schedule_id BIGINT REFERENCES schedule(schedule_id)
)
