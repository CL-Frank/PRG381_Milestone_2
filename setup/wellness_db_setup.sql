-- Drop tables if they exist
DROP TABLE feedback;
DROP TABLE appointments;
DROP TABLE counselors;

-- Create tables
CREATE TABLE counselors (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50),
    specialization VARCHAR(50),
    availability VARCHAR(20)
);

CREATE TABLE appointments (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    student VARCHAR(50),
    counselor_id INT,
    date DATE,
    time TIME,
    status VARCHAR(20),
    FOREIGN KEY (counselor_id) REFERENCES counselors(id)
);

CREATE TABLE feedback (
    id INT PRIMARY KEY GENERATED AaLWAYS AS IDENTITY,
    student VARCHAR(50),
    rating INT,
    comments VARCHAR(255)
);

-- Insert sample data
INSERT INTO counselors (name, specialization, availability)
VALUES 
  ('Dr. Smith', 'Anxiety', 'Mon-Wed'),
  ('Dr. Patel', 'Depression', 'Tue-Thu'),
  ('Dr. Mokoena', 'Academic Stress', 'Mon-Fri');

INSERT INTO appointments (student, counselor_id, date, time, status)
VALUES 
  ('Alice Johnson', 1, '2025-07-15', '10:00:00', 'Scheduled'),
  ('Brian Lee', 2, '2025-07-16', '14:30:00', 'Scheduled'),
  ('Chipo Moyo', 3, '2025-07-17', '09:00:00', 'Completed');

INSERT INTO feedback (student, rating, comments)
VALUES 
  ('Alice Johnson', 5, 'Very helpful session!'),
  ('Brian Lee', 4, 'Great advice, thank you.'),
  ('Chipo Moyo', 3, 'Helpful but rushed.');

