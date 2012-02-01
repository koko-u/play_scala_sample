# Post schema

# --- !Ups
CREATE TABLE Post (
       id INTEGER NOT NULL AUTO_INCREMENT,
       title VARCHAR NOT NULL,
       content TEXT NOT NULL,
       postedAt DATE NOT NULL,
       author_id INTEGER NOT NULL,
       FOREIGN KEY (author_id) REFERENCES User(id),
       PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE Post;