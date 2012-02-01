# Comment schema

# --- !Ups
CREATE TABLE Comment (
       id INTEGER NOT NULL AUTO_INCREMENT,
       author VARCHAR NOT NULL,
       content TEXT NOT NULL,
       postedAt DATE NOT NULL,
       post_id INTEGER NOT NULL,
       FOREIGN KEY(post_id) REFERENCES Post(id) ON DELETE CASCADE,
       PRIMARY KEY(id)
);

# --- !Downs
DROP TABLE Comment;
