CREATE TABLE IF NOT EXISTS course_table (
    course_id UUID PRIMARY KEY,
    course_name VARCHAR(255) NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    is_enrollment_enabled BOOLEAN,
    course_fee INTEGER,
    discount INTEGER,
    published_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS course_content_table (
    content_id UUID PRIMARY KEY,
    course_id UUID NOT NULL,
    content_title VARCHAR(255),
    content_type VARCHAR(50),
    content_url VARCHAR(255),
    content_sequence INTEGER
);

CREATE TABLE IF NOT EXISTS enrollment_info_table (
    enrollment_id UUID PRIMARY KEY,
    course_id UUID NOT NULL,
    user_id UUID NOT NULL,
    status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS payment_info_table (
    payment_id UUID PRIMARY KEY,
    course_id UUID NOT NULL,
    user_id UUID NOT NULL,
    payment_vendor VARCHAR(100),
    sender_account VARCHAR(100),
    amount INTEGER,
    trx_id VARCHAR(100),
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50)
);