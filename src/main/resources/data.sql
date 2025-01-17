-- Insert initial users if they do not exist
INSERT INTO users (username, password, display_name)
SELECT 'user1', '$2a$10$wJ.IUpJkiuYD1T4ZHUEIwOjecvHDxPrSzC5mEcoCK9Hbn/pS7TBUC', 'User One'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user1');

INSERT INTO users (username, password, display_name)
SELECT 'user2', '$2a$10$wJ.IUpJkiuYD1T4ZHUEIwOjecvHDxPrSzC5mEcoCK9Hbn/pS7TBUC', 'User Two'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user2');

-- Insert initial posts if they do not exist
INSERT INTO posts (title, text, user_id)
SELECT 'First Post', 'This is the content of the first post.', 1
    WHERE NOT EXISTS (SELECT 1 FROM posts WHERE title = 'First Post');

INSERT INTO posts (title, text, user_id)
SELECT 'Second Post', 'This is the content of the second post.', 1
    WHERE NOT EXISTS (SELECT 1 FROM posts WHERE title = 'Second Post');

INSERT INTO posts (title, text, user_id)
SELECT 'Third Post', 'This is the content of the third post.', 2
    WHERE NOT EXISTS (SELECT 1 FROM posts WHERE title = 'Third Post');

-- Insert initial tags if they do not exist
INSERT INTO tags (name)
SELECT 'Spring Boot'
    WHERE NOT EXISTS (SELECT 1 FROM tags WHERE name = 'Spring Boot');

INSERT INTO tags (name)
SELECT 'Java'
    WHERE NOT EXISTS (SELECT 1 FROM tags WHERE name = 'Java');

INSERT INTO tags (name)
SELECT 'Database'
    WHERE NOT EXISTS (SELECT 1 FROM tags WHERE name = 'Database');

INSERT INTO tags (name)
SELECT 'Testing'
    WHERE NOT EXISTS (SELECT 1 FROM tags WHERE name = 'Testing');

-- Link posts with tags if they do not exist
INSERT INTO post_tags (post_id, tag_id)
SELECT 1, 1
    WHERE NOT EXISTS (SELECT 1 FROM post_tags WHERE post_id = 1 AND tag_id = 1);

INSERT INTO post_tags (post_id, tag_id)
SELECT 1, 2
    WHERE NOT EXISTS (SELECT 1 FROM post_tags WHERE post_id = 1 AND tag_id = 2);

INSERT INTO post_tags (post_id, tag_id)
SELECT 2, 3
    WHERE NOT EXISTS (SELECT 1 FROM post_tags WHERE post_id = 2 AND tag_id = 3);

INSERT INTO post_tags (post_id, tag_id)
SELECT 3, 4
    WHERE NOT EXISTS (SELECT 1 FROM post_tags WHERE post_id = 3 AND tag_id = 4);

INSERT INTO post_tags (post_id, tag_id)
SELECT 3, 1
    WHERE NOT EXISTS (SELECT 1 FROM post_tags WHERE post_id = 3 AND tag_id = 1);