# Users schema

# --- !Ups
CREATE TABLE User (
  id INTEGER NOT NULL AUTO_INCREMENT,
  email VARCHAR NOT NULL,
  password VARCHAR NOT NULL,
  fullname VARCHAR NOT NULL,
  isAdmin BOOLEAN NOT NULL,
  PRIMARY KEY(id)
);

# --- !Downs
DROP TABlE User;
