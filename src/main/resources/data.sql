-- Insert initial posts
INSERT INTO posts (title, text)
VALUES ('First Post', 'This is the content of the first post.'),
       ('Second Post', 'This is the content of the second post.'),
       ('Third Post', 'This is the content of the third post.');

-- Insert initial tags
INSERT INTO tags (name)
VALUES ('Spring Boot'),
       ('Java'),
       ('Database'),
       ('Testing');

-- Link posts with tags
INSERT INTO post_tags (post_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 4),
       (3, 1);