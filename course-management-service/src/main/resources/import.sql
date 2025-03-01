-- Insert dummy course data only if the course_id does not already exist
INSERT INTO course_table (course_id, course_name, description, image_url, is_enrollment_enabled, course_fee, discount, published_at)
SELECT '550e8400-e29b-41d4-a716-446655440002',
       'System Design Preparation',
       'The System Design Preparation course equips software engineers with essential skills to excel in system design interviews. Covering core concepts like scalability, load balancing, and database design, this course combines theory with practical exercises, helping you confidently tackle real-world challenges.',
       'file-system-storage/dummy-system-design-course-banner.jpg',
       true,
       10000,
       50,
       '2024-04-09 18:00:00'
WHERE NOT EXISTS (SELECT 1 FROM course_table WHERE course_id = '550e8400-e29b-41d4-a716-446655440002');

INSERT INTO course_table (course_id, course_name, description, image_url, is_enrollment_enabled, course_fee, discount, published_at)
SELECT '550e8400-e29b-41d4-a716-446655440001',
       'Java & Spring Boot',
       'Master Java and Spring Boot with this comprehensive course. Gain an in depth understanding of Java programming and the powerful Spring Boot framework. Whether you''re a beginner or looking to level up, this course has you covered.',
       'file-system-storage/dummy-java-course-banner.jpg',
       true,
       10000,
       0,
       '2024-04-12 18:00:00'
WHERE NOT EXISTS (SELECT 1 FROM course_table WHERE course_id = '550e8400-e29b-41d4-a716-446655440001');

INSERT INTO course_table (course_id, course_name, description, image_url, is_enrollment_enabled, course_fee, discount, published_at)
SELECT '550e8400-e29b-41d4-a716-446655440000',
       'Introduction to Programming',
       'Kickstart your coding journey with this easy to follow, beginner friendly course. Learn the fundamentals of programming through intuitive lessons designed to build a strong foundation. Perfect for CS students and anyone eager to dive into the world of coding.',
       'file-system-storage/dummy-programming-course-banner.jpg',
       true,
       10000,
       50,
       '2024-04-14 18:00:00'
WHERE NOT EXISTS (SELECT 1 FROM course_table WHERE course_id = '550e8400-e29b-41d4-a716-446655440000');

-- Insert dummy course content data only if the content_id does not already exist
INSERT INTO course_content_table (content_id, content_sequence, content_type, content_url, course_id, content_title)
SELECT 'afd83ea1-9a4d-4f7b-95d1-e030e6249ae9',
       2,
       'MP4',
       'file-system-storage/dummy-video-1.mp4',
       '550e8400-e29b-41d4-a716-446655440002',
       'Dummy Content Title 3'
WHERE NOT EXISTS (SELECT 1 FROM course_content_table WHERE content_id = 'afd83ea1-9a4d-4f7b-95d1-e030e6249ae9');

INSERT INTO course_content_table (content_id, content_sequence, content_type, content_url, course_id, content_title)
SELECT 'cb8bf5aa-1030-439c-b78d-ad60e4f46d28',
       1,
       'MP4',
       'file-system-storage/dummy-video-1.mp4',
       '550e8400-e29b-41d4-a716-446655440002',
       'Dummy Content Title 1'
WHERE NOT EXISTS (SELECT 1 FROM course_content_table WHERE content_id = 'cb8bf5aa-1030-439c-b78d-ad60e4f46d28');

INSERT INTO course_content_table (content_id, content_sequence, content_type, content_url, course_id, content_title)
SELECT '9030e2f7-8300-4021-9236-8c50246dd5c7',
       3,
       'MP4',
       'file-system-storage/dummy-video-1.mp4',
       '550e8400-e29b-41d4-a716-446655440002',
       'Dummy Content Title 2'
WHERE NOT EXISTS (SELECT 1 FROM course_content_table WHERE content_id = '9030e2f7-8300-4021-9236-8c50246dd5c7');
