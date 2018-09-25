CREATE TABLE user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  email varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  role varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE project (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE project_members (
  project_id bigint(20) NOT NULL,
  members_id bigint(20) NOT NULL,
  PRIMARY KEY (project_id,members_id),
  FOREIGN KEY (members_id) REFERENCES user(id)
);

CREATE TABLE issue (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  description varchar(255) DEFAULT NULL,
  status varchar(255) DEFAULT NULL,
  title varchar(255) DEFAULT NULL,
  owner_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (owner_id) REFERENCES user(id)
);

INSERT INTO user(name, email, password, role) VALUES('Admin', 'admin@test.com', 'password', 'ADMIN');
INSERT INTO issue(title, description, owner_id, status) VALUES('Wrong menu color', 'The menu shows the wrong background color.', 1, 'OPEN');
INSERT INTO issue(title, description, owner_id, status) VALUES('Add header', 'The website requires a header.', 1, 'CLOSED');
