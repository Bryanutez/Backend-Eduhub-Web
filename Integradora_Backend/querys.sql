INSERT INTO course 
(
    course_id, 
    banner_path, 
    category, 
    status, 
    description, 
    end_date, 
    price, 
    size, 
    start_date, 
    title, 
    instructor_id
) 
VALUES 
(
    UNHEX(REPLACE('44b26cf7-1784-45f6-aa3f-03330cb68e73', '-', '')), 
    'ruta/del/banner.jpg', 
    'Programación', 
    'PUBLISHED', 
    'Curso sobre desarrollo web utilizando tecnologías modernas.',  
    '2025-12-31 23:59:59', 
    99.99, 
    30,
    '2025-01-23 08:00:00', 
    'Curso de Desarrollo Web', 
 UNHEX(REPLACE('335167f7-5c5d-4026-bd7f-7a78c154e651', '-', ''))
 );
 
INSERT INTO registration 
(
    registration_id, 
    status, 
    course_id, 
    student_id
) 
VALUES 
(
    UNHEX(REPLACE('d9b8c92f-d6e4-475f-b8f3-4f6944a9d02f', '-', '')),  -- registration_id (UUID convertido a binary(16))
    'REGISTERED',                                                -- status
    UNHEX(REPLACE('44b26cf7-1784-45f6-aa3f-03330cb68e73', '-', '')),  -- course_id (UUID convertido a binary(16))
    UNHEX(REPLACE('7b4bcb61-aa31-41e3-9038-0e8b8ba7f37b', '-', ''))   -- student_id (UUID convertido a binary(16))
);

SELECT BIN_TO_UUID(category_id) FROM category;
SELECT BIN_TO_UUID(course_id) curso, BIN_TO_UUID(instructor_id) docente FROM course;

select c.title, ct.name
from course c
inner join course_category cc
on cc.course_id = c.course_id
inner join category ct
on ct.category_id = cc.category_id;