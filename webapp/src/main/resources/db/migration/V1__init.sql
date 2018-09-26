CREATE TABLE user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  email varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
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
  description text DEFAULT NULL,
  status varchar(255) NOT NULL,
  title varchar(255) NOT NULL,
  reporter_id bigint(20) NOT NULL,
  owner_id bigint(20) DEFAULT NULL,
  project_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (reporter_id) REFERENCES user(id),
  FOREIGN KEY (owner_id) REFERENCES user(id),
  FOREIGN KEY (project_id) REFERENCES project(id)
);

INSERT INTO user(name, email, password, role) VALUES('Marcus', 'marcus@vaadin.com', 'password1', 'ADMIN');
INSERT INTO user(name, email, password, role) VALUES('Alejandro', 'alejandro@vaadin.com', 'password2', 'DEVELOPER');
INSERT INTO user(name, email, password, role) VALUES('Sven', 'sven@vaadin.com', 'password3', 'DEVELOPER');
INSERT INTO user(name, email, password, role) VALUES('john', 'john@vaadin.com', 'password4', 'USER');

INSERT INTO project(name) VALUES ('Website');
INSERT INTO project(name) VALUES ('Business app');

INSERT INTO project_members(project_id, members_id) VALUES (1, 1);
INSERT INTO project_members(project_id, members_id) VALUES (1, 2);
INSERT INTO project_members(project_id, members_id) VALUES (1, 3);
INSERT INTO project_members(project_id, members_id) VALUES (1, 4);
INSERT INTO project_members(project_id, members_id) VALUES (2, 2);
INSERT INTO project_members(project_id, members_id) VALUES (2, 3);
INSERT INTO project_members(project_id, members_id) VALUES (2, 4);

INSERT INTO issue(title, description, reporter_id, status, project_id) VALUES('Wrong menu color', 'The menu has a wrong background color. It should be darker.', 1, 'OPEN', 1);
INSERT INTO issue(title, description, reporter_id, status, project_id) VALUES('Error updating users', 'An error occurs anytime the update button is clicked in the edit user view.', 4, 'OPEN', 1);
INSERT INTO issue(title, description, reporter_id, status, project_id) VALUES('Add header', 'The website requires a header with the company logo.', 4, 'CLOSED', 1);
INSERT INTO issue(title, description, reporter_id, status, project_id) VALUES('Implement web service', 'Functionality in services should be available to external systems through a REST WS.', 2, 'OPEN', 2);
INSERT INTO issue(title, description, reporter_id, status, project_id) VALUES('Add remember me option', 'A remember me option is needed in the login view.', 4, 'OPEN', 2);
INSERT INTO issue(title, description, reporter_id, status, project_id) VALUES('Error saving issues', 'An error occurs when saving a new issue.', 4, 'OPEN', 2);
