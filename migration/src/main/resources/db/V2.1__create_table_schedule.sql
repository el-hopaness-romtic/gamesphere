CREATE TABLE schedule (
  schedule_id BIGSERIAL PRIMARY KEY,
  scheduled_time TIMESTAMP,
  room VARCHAR(10),
  course_id INT REFERENCES course(course_id)
)
