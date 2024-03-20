-- Create Customer table 
CREATE TABLE Customer ( 
cname VARCHAR(20) PRIMARY KEY,
caddress VARCHAR(100),
category INT, 
CONSTRAINT category_ck CHECK (category BETWEEN 1 AND 10), 
);

-- Create Assembly table 
CREATE TABLE Assembly ( 
assembly_id VARCHAR(20) PRIMARY KEY, 
date_ordered DATE, 
assembly_details VARCHAR(100),
cname VARCHAR(20) FOREIGN KEY REFERENCES Customer(cname) 
);

--Create Department Table 
CREATE TABLE Department ( 
department_no INT PRIMARY KEY,
department_data VARCHAR(100), 
);

--Create Process Table 
CREATE TABLE Process (
process_id VARCHAR(20) PRIMARY KEY, 
process_data VARCHAR(100),
department_no INT,
FOREIGN KEY (department_no) REFERENCES Department(department_no) 
);

--Create Fit_process Table
CREATE TABLE Fit_process (
process_id VARCHAR(20) FOREIGN KEY REFERENCES Process(process_id),
fit_type VARCHAR(20), 
PRIMARY KEY (process_id)
);

--Create Paint_process Table
CREATE TABLE Paint_process (
process_id VARCHAR(20) FOREIGN KEY REFERENCES Process(process_id),
paint_type VARCHAR(20), 
paint_method VARCHAR(20),
PRIMARY KEY (process_id) 
);


--Create Cut_process Table 
CREATE TABLE Cut_process (
process_id VARCHAR(20) FOREIGN KEY REFERENCES Process(process_id),
cutting_type VARCHAR(20),
machine_type VARCHAR(20), 
PRIMARY KEY (process_id)
);


--Create Job Table 
CREATE TABLE Job (
 job_no INT PRIMARY KEY, 
date_of_commencement DATE, 
date_of_completion DATE
);

--Create Fit_job Table 
CREATE TABLE Fit_job (
 job_no INT FOREIGN KEY REFERENCES Job(job_no), 
fit_labor_time TIME,
PRIMARY KEY (job_no) 
);

--Create Paint_job Table 
CREATE TABLE Paint_job (
 job_no INT FOREIGN KEY REFERENCES Job(job_no), 
color VARCHAR(20), 
volume FLOAT, 
paint_labor_time TIME,
PRIMARY KEY (job_no) 
);

--Create Cut_job Table 
CREATE TABLE Cut_job (
 job_no INT FOREIGN KEY REFERENCES Job(job_no), 
job_machine_type VARCHAR(20),
machine_time TIME, 
material_used VARCHAR(20), 
cut_labor_time TIME,
PRIMARY KEY (job_no) 
);

--Create manufacture Table
CREATE TABLE manufacture (
assembly_id VARCHAR(20) FOREIGN KEY REFERENCES Assembly(assembly_id),
process_id VARCHAR(20) FOREIGN KEY REFERENCES Process(process_id),
job_no INT FOREIGN KEY REFERENCES Job(job_no),
PRIMARY KEY (assembly_id, process_id)
);

--Create Assembly Account Table 
CREATE TABLE Assembly_account ( 
assembly_account_no INT PRIMARY KEY, 
date_of_establishment DATE, 
details_1 NUMERIC (10, 2)
);



--Create Department Account Table
CREATE TABLE Department_account ( 
department_account_no INT PRIMARY KEY, 
date_of_establishment DATE, 
details_2 NUMERIC (10, 2) NOT NULL
);

--Create Process Account Table
CREATE TABLE Process_account ( 
process_account_no INT PRIMARY KEY, 
date_of_establishment DATE,
details_3 NUMERIC (10, 2) 
);


-- Create Assembly Expenditure Table
CREATE TABLE assembly_expenditure (
    assembly_account_no INT,
    assembly_id VARCHAR(20),
    PRIMARY KEY (assembly_account_no),
    FOREIGN KEY (assembly_account_no) REFERENCES Assembly_account(assembly_account_no),
    FOREIGN KEY (assembly_id) REFERENCES Assembly(assembly_id)
);

-- Create Department Expenditure Table
CREATE TABLE department_expenditure (
    department_account_no INT,
    department_no INT, 
    PRIMARY KEY (department_account_no),
    FOREIGN KEY (department_account_no) REFERENCES Department_account(department_account_no),
    FOREIGN KEY (department_no) REFERENCES Department(department_no)
);


-- Create Process Expenditure Table
CREATE TABLE process_expenditure (
    process_account_no INT,
    process_id VARCHAR(20),
    PRIMARY KEY (process_account_no),
    FOREIGN KEY (process_account_no) REFERENCES Process_account(process_account_no),
    FOREIGN KEY (process_id) REFERENCES Process(process_id)
);


--Create Cost Table 
-- Create Cost Table
CREATE TABLE Cost (
    transaction_no INT PRIMARY KEY, 
    sup_cost NUMERIC(10, 2),
    assembly_account_no INT,
    department_account_no INT,
    process_account_no INT,
    FOREIGN KEY (assembly_account_no) REFERENCES Assembly_account(assembly_account_no),
    FOREIGN KEY (department_account_no) REFERENCES Department_account(department_account_no),
    FOREIGN KEY (process_account_no) REFERENCES Process_account(process_account_no)
);


--Create proceed Table
CREATE TABLE proceed (
transaction_no INT FOREIGN KEY REFERENCES Cost(transaction_no),
job_no INT FOREIGN KEY REFERENCES Job(job_no),
PRIMARY KEY (transaction_no)
);

CREATE INDEX customer_category ON Customer(category);
CREATE INDEX process_department_no ON Process(department_no);
CREATE INDEX job_date_of_completion ON Job(date_of_completion);
CREATE INDEX assembly_expenditure_assembly_id ON assembly_expenditure(assembly_id);
CREATE INDEX manufacture_job_no ON manufacture(job_no);
CREATE INDEX manufacture_assembly_id ON manufacture(assembly_id);
