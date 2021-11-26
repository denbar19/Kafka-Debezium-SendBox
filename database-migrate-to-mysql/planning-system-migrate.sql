USE planning_system;
CREATE DATABASE planning_system;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     user_name VARCHAR(100) NOT NULL,
                                     surname VARCHAR(100) NOT NULL,
                                     email VARCHAR(100) NOT NULL UNIQUE,
                                     dateAdded TIMESTAMP NOT NULL,
                                     user_role SMALLINT NOT NULL ,
                                     deleted BOOLEAN NOT NULL DEFAULT FALSE
                                     );


CREATE TABLE IF NOT EXISTS tasks (
									id BIGINT AUTO_INCREMENT PRIMARY KEY,
									task_name VARCHAR(100) NOT NULL,
									task_description VARCHAR(500),
									dateAdded TIMESTAMP NOT NULL,
									task_status SMALLINT NOT NULL,
									priority SMALLINT NOT NULL,
                                    user_id BIGINT,
                                    deleted BOOLEAN NOT NULL DEFAULT FALSE
);
ALTER TABLE tasks ADD FOREIGN KEY (user_id) REFERENCES users (id);


CREATE TABLE IF NOT EXISTS task_subscriber (
											task_id BIGINT,
											subscriber_id BIGINT                                            
);

ALTER TABLE task_subscriber ADD FOREIGN KEY (task_id) REFERENCES tasks (id);
ALTER TABLE task_subscriber ADD FOREIGN KEY (subscriber_id) REFERENCES users (id);