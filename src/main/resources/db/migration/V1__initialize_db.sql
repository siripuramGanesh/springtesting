CREATE TABLE employee(
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
email VARCHAR(225),
full_name VARCHAR(255),
salary BIGINT,
CONSTRAINT pk_employee PRIMARY KEY (id));

ALTER TABLE employee ADD CONSTRAINT uc_employee_email UNIQUE (email);