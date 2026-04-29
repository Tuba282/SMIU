CREATE DATABASE IF NOT EXISTS MyCompany; -- create database 
use MyCompany; -- this command is important to link tables to database


-- 1. Employee Table
CREATE TABLE EMPLOYEE (
    Fname VARCHAR(50),
    Lname VARCHAR(50),
    Ssn CHAR(9) PRIMARY KEY,
    Salary DECIMAL(10, 2)
);

-- 2. Department Table
CREATE TABLE DEPARTMENT (
    Dname VARCHAR(50),
    Dnumber INT PRIMARY KEY,
    Mgr_ssn CHAR(9)
);

-- 3. Project Table
CREATE TABLE PROJECT (
    Pname VARCHAR(50),
    Pnumber INT PRIMARY KEY,
    Plocation VARCHAR(50)
);

-- 4. Works_On Table (Linking table)
CREATE TABLE WORKS_ON (
    Essn CHAR(9),
    Pno INT,
    Hours DECIMAL(3, 1),
    PRIMARY KEY (Essn, Pno)
);



USE CompanyDB;

INSERT INTO EMPLOYEE (Fname, Lname, Ssn, Salary) VALUES 
('John', 'Smith', '123456789', 30000),
('Franklin', 'Wong', '333445555', 40000),
('Alicia', 'Zelaya', '999887777', 25000),
('Jennifer', 'Wallace', '987654321', 43000),
('Ramesh', 'Narayan', '666884444', 38000),
('Joyce', 'English', '453453453', 25000);


INSERT INTO DEPARTMENT (Dname, Dnumber, Mgr_ssn) VALUES 
('Research', 5, '333445555'),
('Administration', 4, '987654321'),
('Headquarters', 1, '888665555'),
('Sales', 6, '123456789'),
('IT', 7, '666884444'),
('HR', 8, '999887777');


INSERT INTO PROJECT (Pname, Pnumber, Plocation) VALUES 
('ProductX', 1, 'Bellaire'),
('ProductY', 2, 'Sugarland'),
('ProductZ', 3, 'Houston'),
('Computerization', 10, 'Stafford'),
('Reorganization', 20, 'Houston'),
('Newbenefits', 30, 'Stafford');

INSERT INTO WORKS_ON (Essn, Pno, Hours) VALUES 
('123456789', 1, 32.5),
('123456789', 2, 7.5),
('666884444', 3, 40.0),
('453453453', 1, 20.0),
('453453453', 2, 20.0),
('333445555', 10, 10.0);


-- linking tables to each other using Forigon keys

USE CompanyDB;

-- WORKS_ON table ko EMPLOYEE se link karna
ALTER TABLE WORKS_ON
ADD CONSTRAINT fk_employee_works
FOREIGN KEY (Essn) REFERENCES EMPLOYEE(Ssn);

-- WORKS_ON table ko PROJECT se link karna
ALTER TABLE WORKS_ON
ADD CONSTRAINT fk_project_works
FOREIGN KEY (Pno) REFERENCES PROJECT(Pnumber);

-- DEPARTMENT table ko EMPLOYEE (Manager) se link karna
ALTER TABLE DEPARTMENT
ADD CONSTRAINT fk_dept_manager
FOREIGN KEY (Mgr_ssn) REFERENCES EMPLOYEE(Ssn);

