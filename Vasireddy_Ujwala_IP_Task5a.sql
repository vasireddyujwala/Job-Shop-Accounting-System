
-----------------------------------------------------------------------
CREATE PROCEDURE EnterNewCustomer
    @cname VARCHAR(20),
    @caddress VARCHAR(100),
    @category INT,
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN 
    BEGIN TRY
        -- Data validation
        IF LEN(@cname) > 20 
        BEGIN
            -- If the name is too long 
            SET @errorCode = 50001;
            SET @errorMessage = 'Invalid customer name. Name must be up to 20 characters.';
            RETURN;
        END

		 IF @cname LIKE '%[0-9!@#$%^&*(),.?":{}|<>]%'
        BEGIN
            -- If the name contains special characters, raise an error
            SET @errorCode = 50002;
            SET @errorMessage = 'Invalid customer name. Name must be without special characters or numbers.';
            RETURN;
        END

        -- Check if the address is null
        IF LEN(@caddress) = 0
        BEGIN
            SET @errorCode = 50003;
            SET @errorMessage = 'Invalid customer address. Address must be provided.';
            RETURN;
        END
        -- Check if the address is too long
		 IF LEN(@caddress) > 100
        BEGIN
            SET @errorCode = 50004;
            SET @errorMessage = 'Invalid customer address. Address must be up to 100 characters.';
            RETURN;
        END

        IF @category NOT BETWEEN 1 AND 10
        BEGIN
            -- If the category is in the specified range, raise an error
            SET @errorCode = 50005;
            SET @errorMessage = 'Invalid customer category. Category must be an integer between 1 and 10.';
            RETURN;
        END

        -- Data manipulation
        BEGIN
            INSERT INTO Customer (cname, caddress, category) 
            VALUES (@cname, @caddress, @category);
        END;

    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = ERROR_NUMBER();

        -- Check if the error is related to a primary key or unique constraint violation
        IF @errorCode = 2627
        BEGIN
            SET @errorMessage = 'A customer with the same name or data already exists.';
        END
        ELSE
        BEGIN
            SET @errorMessage = ERROR_MESSAGE();
        END

        RETURN;
    END CATCH;
END;
-------------------------------------------------------------------------------------

CREATE PROCEDURE EnterNewDepartment
    @department_no INT,
    @department_data VARCHAR(100),
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN 
    BEGIN TRY
        -- Data validation
        IF @department_no <= 0
        BEGIN
            -- If the department number is not positive, raise an error
            SET @errorCode = 50006;
            SET @errorMessage = 'Invalid department number. Department number must be a positive integer.';
            RETURN;
        END

        IF LEN(@department_data) > 100
        BEGIN
            -- If the department data is too long, raise an error
            SET @errorCode = 50007;
            SET @errorMessage = 'Invalid department data. Data must be up to 100 characters.';
            RETURN;
        END

        -- Data manipulation
        BEGIN
            INSERT INTO Department (department_no, department_data) 
            VALUES (@department_no, @department_data);
        END;


    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = ERROR_NUMBER();

        -- Check if the error is related to a unique key violation
        IF @errorCode = 2627
        BEGIN
            SET @errorMessage = 'Department number already exists. Please choose a unique department number.';
        END
        ELSE
        BEGIN
            SET @errorMessage = ERROR_MESSAGE();
        END

        RETURN;
    END CATCH;
END;

-----------------------------------------------


CREATE PROCEDURE EnterNewProcess
    @process_id VARCHAR(20),
    @process_data VARCHAR(100),
    @department_no INT,
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN 
    BEGIN TRY
        -- Data validation
        IF LEN(@process_data) = 0 OR LEN(@process_data) > 100
        BEGIN
            SET @errorCode = 50008;
            SET @errorMessage = 'Invaid process data. Process data must be provided upto 100 characters.';
            RETURN;
        END
     
		IF NOT EXISTS (SELECT 1 FROM Department WHERE department_no = @department_no)
        BEGIN
            SET @errorCode = 50009;
            SET @errorMessage = 'Department does not exist. Please provide a valid department number.';
            RETURN;
        END


        -- Data manipulation
        INSERT INTO Process (process_id, process_data, department_no) 
        VALUES (@process_id, @process_data, @department_no);

      
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = ERROR_NUMBER();
        SET @errorMessage = ERROR_MESSAGE();

        RETURN;
    END CATCH;
END;


-----------------------

CREATE PROCEDURE EnterNewFitProcess
    @process_id VARCHAR(20),
    @fit_type VARCHAR(20),
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN 
    BEGIN TRY
        -- Data validation
        IF LEN(@fit_type) = 0 OR LEN(@fit_type) > 20
        BEGIN
            SET @errorCode = 50010;
            SET @errorMessage = 'Invalid input. Fit Type cannot be empty and it must be up to 20 characters.';
            RETURN;
        END

        -- Data manipulation
        INSERT INTO Fit_Process (process_id, fit_type) 
        VALUES (@process_id, @fit_type);

  
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = ERROR_NUMBER();
        SET @errorMessage = ERROR_MESSAGE();
        RETURN;
    END CATCH;
END;


------------------
CREATE PROCEDURE EnterNewPaintProcess
    @process_id VARCHAR(20),
    @paint_type VARCHAR(20),
    @paint_method VARCHAR(20),
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN 
    BEGIN TRY
        -- Data validation
        IF LEN(@paint_type) = 0 OR LEN(@paint_type) > 20 
        BEGIN
            SET @errorCode = 50011;
            SET @errorMessage = 'Invalid input. Paint Type cannot be empty and must be up to 20 characters.';
            RETURN;
        END

		IF LEN(@paint_method) = 0 OR LEN(@paint_method) > 20
        BEGIN
            SET @errorCode = 50012;
            SET @errorMessage = 'Invalid input. Paint Method cannot be empty and must be up to 20 characters.';
            RETURN;
        END

        -- Data manipulation
        INSERT INTO Paint_Process (process_id, paint_type, paint_method) 
        VALUES (@process_id, @paint_type, @paint_method);

    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = ERROR_NUMBER();
        SET @errorMessage = ERROR_MESSAGE();
        RETURN;
    END CATCH;
END;


------------------


CREATE PROCEDURE EnterNewCutProcess
    @process_id VARCHAR(20),
    @cutting_type VARCHAR(20),
    @machine_type VARCHAR(20),
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN 
    BEGIN TRY
        -- Data validation
        IF LEN(@cutting_type) = 0 OR LEN(@cutting_type) > 20  
        BEGIN
            SET @errorCode = 50013;
            SET @errorMessage = 'Invalid input. Cutting Type cannot be empty and must be up to 20 characters.';
            RETURN;
        END

		 IF LEN(@machine_type) = 0 OR LEN(@machine_type) > 20
        BEGIN
            SET @errorCode = 50014;
            SET @errorMessage = 'Invalid input. Machine Type cannot be empty and must be up to 20 characters.';
            RETURN;
        END

        -- Data manipulation
        INSERT INTO Cut_Process (process_id, cutting_type, machine_type) 
        VALUES (@process_id, @cutting_type, @machine_type);

    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = ERROR_NUMBER();
        SET @errorMessage = ERROR_MESSAGE();
        RETURN;
    END CATCH;
END;


--------------
CREATE PROCEDURE EnterNewAssembly
    @assembly_id VARCHAR(20),
    @date_ordered DATE,
    @assembly_details VARCHAR(100),
    @cname VARCHAR(20),
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN 
    BEGIN TRY
        -- Data validation
        IF LEN(@assembly_details) = 0 OR LEN(@assembly_details) > 100
        BEGIN
            SET @errorCode = 50015;
            SET @errorMessage = 'Invalid assembly details. Assembly details must be provided up to 100 characters.';
            RETURN;
        END

        IF NOT EXISTS (SELECT 1 FROM Customer WHERE cname = @cname)
        BEGIN
            SET @errorCode = 50016;
            SET @errorMessage = 'Customer does not exist. Please provide a valid customer name.';
            RETURN;
        END

        -- Data manipulation
        INSERT INTO Assembly (assembly_id, date_ordered, assembly_details, cname) 
        VALUES (@assembly_id, @date_ordered, @assembly_details, @cname);

    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = ERROR_NUMBER();
        SET @errorMessage = ERROR_MESSAGE();

        RETURN;
    END CATCH;
END;

----------------



CREATE PROCEDURE EnterNewManufacture
    @assembly_id VARCHAR(20),
    @process_id VARCHAR(20),
    @job_no INT,
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN
    BEGIN TRY
        -- Check if the combination of assembly_id and process_id already exists
        IF EXISTS (SELECT 1 FROM manufacture WHERE assembly_id = @assembly_id AND process_id = @process_id)
        BEGIN
            SET @errorCode = 50017;
            SET @errorMessage = 'Combination of assembly_id and process_id already exists. Please provide a unique combination.';
            RETURN;
        END

        -- Data manipulation
        INSERT INTO manufacture (assembly_id, process_id, job_no)
        VALUES (@assembly_id, @process_id, @job_no);
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = ERROR_NUMBER();
        SET @errorMessage = ERROR_MESSAGE();
        RETURN;
    END CATCH;
END;


--------



-- Procedure for Assembly_account
CREATE PROCEDURE InsertAssemblyAccount
    @account_no INT,
    @date_of_establishment DATE,
    @details_1 NUMERIC(10,2),
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN
    BEGIN TRY
        -- Insert the record into Assembly_account table
        INSERT INTO Assembly_account (assembly_account_no, date_of_establishment, details_1)
        VALUES (@account_no, @date_of_establishment, @details_1);

        -- Success
        SET @errorCode = 0;
        SET @errorMessage = 'Assembly_account record inserted successfully.';
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = 50018; -- Custom error code for assembly account
        SET @errorMessage = ERROR_MESSAGE();
    END CATCH;
END;


-----

-- Procedure for Department_account
CREATE PROCEDURE InsertDepartmentAccount
    @account_no INT,
    @date_of_establishment DATE,
    @details_2 NUMERIC(10, 2),
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN
    BEGIN TRY
        -- Insert the record into Department_account table
        INSERT INTO Department_account (department_account_no, date_of_establishment, details_2)
        VALUES (@account_no, @date_of_establishment, @details_2);

        -- Success
        SET @errorCode = 0;
        SET @errorMessage = 'Department_account record inserted successfully.';
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = 50019; -- Custom error code for department account
        SET @errorMessage = ERROR_MESSAGE();
    END CATCH;
END;
------

-- Procedure for Process_account
CREATE PROCEDURE InsertProcessAccount
    @account_no INT,
    @date_of_establishment DATE,
    @details_3 NUMERIC(10, 2),
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN
    BEGIN TRY
        -- Insert the record into Process_account table
        INSERT INTO Process_account (process_account_no, date_of_establishment, details_3)
        VALUES (@account_no, @date_of_establishment, @details_3);

        -- Success
        SET @errorCode = 0;
        SET @errorMessage = 'Process_account record inserted successfully.';
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = 50020; -- Custom error code for process account
        SET @errorMessage = ERROR_MESSAGE();
    END CATCH;
END;

-----
-- Procedure for Assembly_expenditure
CREATE PROCEDURE InsertAssemblyExpenditure
    @assembly_account_no INT,
    @assembly_id NVARCHAR(20),
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN
    BEGIN TRY
        -- Insert the record into Assembly_expenditure table
        INSERT INTO Assembly_expenditure (assembly_account_no, assembly_id)
        VALUES (@assembly_account_no, @assembly_id);

        -- Success
        SET @errorCode = 0;
        SET @errorMessage = 'Assembly_expenditure record inserted successfully.';
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = 50021; -- Custom error code for assembly expenditure
        SET @errorMessage = ERROR_MESSAGE();
    END CATCH;
END;


------

-- Procedure for Department_expenditure
CREATE PROCEDURE InsertDepartmentExpenditure
    @department_account_no INT,
    @department_no INT,
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN
    BEGIN TRY
        -- Insert the record into Department_expenditure table
        INSERT INTO Department_expenditure (department_account_no, department_no)
        VALUES (@department_account_no, @department_no);

        -- Success
        SET @errorCode = 0;
        SET @errorMessage = 'Department_expenditure record inserted successfully.';
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = 50022; -- Custom error code for department expenditure
        SET @errorMessage = ERROR_MESSAGE();
    END CATCH;
END;


----

-- Procedure for Process_expenditure
CREATE PROCEDURE InsertProcessExpenditure
    @process_account_no INT,
    @process_id NVARCHAR(20),
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN
    BEGIN TRY
        -- Insert the record into Process_expenditure table
        INSERT INTO Process_expenditure (process_account_no, process_id)
        VALUES (@process_account_no, @process_id);

        -- Success
        SET @errorCode = 0;
        SET @errorMessage = 'Process_expenditure record inserted successfully.';
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = 50023; -- Custom error code for process expenditure
        SET @errorMessage = ERROR_MESSAGE();
    END CATCH;
END;


-----------

-- Procedure for entering a new job
CREATE PROCEDURE EnterNewJob
    @job_no INT,
    @date_of_commencement DATE,
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN
    BEGIN TRY
        -- Insert the record into the Job table
        INSERT INTO Job (job_no, date_of_commencement)
        VALUES (@job_no, @date_of_commencement);

        -- Success
        SET @errorCode = 0;
        SET @errorMessage = 'Job record inserted successfully.';
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = ERROR_NUMBER();
        SET @errorMessage = ERROR_MESSAGE();
    END CATCH;
END;


------
--Procedure for inserting details into Fit_job
CREATE PROCEDURE InsertFitJobDetails
    @job_no INT,
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN
    BEGIN TRY
        -- Insert the record into the Fit_job table
        INSERT INTO Fit_job (job_no)
        VALUES (@job_no);

        -- Success
        SET @errorCode = 0;
        SET @errorMessage = 'Fit_job details inserted successfully.';
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = ERROR_NUMBER();
        SET @errorMessage = ERROR_MESSAGE();
    END CATCH;
END;

------------

-- Procedure for inserting details into Paint_job
CREATE PROCEDURE InsertPaintJobDetails
    @job_no INT,
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN
    BEGIN TRY
        -- Insert the record into the Paint_job table
        INSERT INTO Paint_job (job_no)
        VALUES (@job_no);

        -- Success
        SET @errorCode = 0;
        SET @errorMessage = 'Paint_job details inserted successfully.';
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = ERROR_NUMBER();
        SET @errorMessage = ERROR_MESSAGE();
    END CATCH;
END;


-------


--Procedure for inserting details into Cut_job
CREATE PROCEDURE InsertCutJobDetails
    @job_no INT,
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN
    BEGIN TRY
        -- Insert the record into the Cut_job table
        INSERT INTO Cut_job (job_no)
        VALUES (@job_no);

        -- Success
        SET @errorCode = 0;
        SET @errorMessage = 'Cut_job details inserted successfully.';
    END TRY
    BEGIN CATCH
        -- Handle errors
        SET @errorCode = ERROR_NUMBER();
        SET @errorMessage = ERROR_MESSAGE();
    END CATCH;
END;


-------

CREATE PROCEDURE InsertManufactureJob (
    @jobNumber INT,
    @assemblyId VARCHAR(20),
    @processId VARCHAR(20),
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(255) OUTPUT
)
AS
BEGIN
    -- Check if the pair is legit existing in the manufacture table
    IF NOT EXISTS (
        SELECT 1
        FROM manufacture
        WHERE assembly_id = @assemblyId
            AND process_id = @processId
    )
    BEGIN
        -- Set error code and message for an invalid pair
        SET @errorCode = 1;
        SET @errorMessage = 'Invalid pair. This assembly_id and process_id combination does not exist in the manufacture table.';
    END
    ELSE
    BEGIN
        -- Update the manufacture table to associate the new job_no with the existing pair
        UPDATE manufacture
        SET job_no = @jobNumber
        WHERE assembly_id = @assemblyId
            AND process_id = @processId;

        -- Set success code and message
        SET @errorCode = 0;
        SET @errorMessage = 'Manufacture details updated successfully.';
    END
END;

-------------


-- Procedure to check if a job with the specified job number exists
CREATE PROCEDURE CheckJobExistence (
    @jobNumber INT,
    @existsFlag INT OUTPUT
)
AS
BEGIN
    -- Check if the job number exists in the Job table
    IF EXISTS (
        SELECT 1
        FROM Job
        WHERE job_no = @jobNumber
    )
    BEGIN
        -- Set existsFlag to 1 if the job exists
        SET @existsFlag = 1;
    END
    ELSE
    BEGIN
        -- Set existsFlag to 0 if the job does not exist
        SET @existsFlag = 0;
    END
END;


-----

-- Procedure to determine the type of job (Fit, Paint, or Cut) based on the job number
CREATE PROCEDURE DetermineJob (
    @jobNumber INT,
    @jobType NVARCHAR(20) OUTPUT
)
AS
BEGIN
    -- Determine the job type based on the presence in each table
    IF EXISTS (
        SELECT 1
        FROM Fit_job
        WHERE job_no = @jobNumber
    )
    BEGIN
        SET @jobType = 'Fit Job';
    END
    ELSE IF EXISTS (
        SELECT 1
        FROM Paint_job
        WHERE job_no = @jobNumber
    )
    BEGIN
        SET @jobType = 'Paint Job';
    END
    ELSE IF EXISTS (
        SELECT 1
        FROM Cut_job
        WHERE job_no = @jobNumber
    )
    BEGIN
        SET @jobType = 'Cut Job';
    END
    ELSE
    BEGIN
        -- Set a default value if the job type cannot be determined
        SET @jobType = 'Unknown Job';
    END
END;


------

-- Procedure to update Fit Job labor time
CREATE PROCEDURE UpdateFitJobLaborTime (
    @jobNumber INT,
    @fitLaborTime TIME,
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(255) OUTPUT
)
AS
BEGIN
    -- Check if the job number exists in the Fit Job table
    IF EXISTS (
        SELECT 1
        FROM Fit_job
        WHERE job_no = @jobNumber
    )
    BEGIN
        -- Update the Fit Job table with the new fit_labor_time
        UPDATE Fit_job
        SET fit_labor_time = @fitLaborTime
        WHERE job_no = @jobNumber;

        -- Set success code and message
        SET @errorCode = 0;
        SET @errorMessage = 'Fit Job labor time updated successfully.';
    END
    ELSE
    BEGIN
        -- Set error code and message for job not found
        SET @errorCode = 1;
        SET @errorMessage = 'Fit Job with the specified job number does not exist.';
    END
END;


-------
-- Procedure to update Paint Job details
CREATE PROCEDURE UpdatePaintJobDetails (
    @jobNumber INT,
    @color VARCHAR(20),
    @volume DOUBLE,
    @paintLaborTime TIME,
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(255) OUTPUT
)
AS
BEGIN
    -- Check if the job number exists in the Paint Job table
    IF EXISTS (
        SELECT 1
        FROM Paint_job
        WHERE job_no = @jobNumber
    )
    BEGIN
        -- Update Paint Job details
        UPDATE Paint_job
        SET color = @color,
            volume = @volume,
            paint_labor_time = @paintLaborTime
        WHERE job_no = @jobNumber;

        -- Set success code and message
        SET @errorCode = 0;
        SET @errorMessage = 'Paint Job details updated successfully.';
    END
    ELSE
    BEGIN
        -- Set error code and message if job number does not exist
        SET @errorCode = 1;
        SET @errorMessage = 'Error: Job with the specified job number does not exist in Paint Job table.';
    END
END;


-----

-- Procedure to update Cut Job details
CREATE PROCEDURE UpdateCutJobDetails (
    @jobNumber INT,
    @machineType VARCHAR(20),
    @machineTime TIME,
    @materialUsed VARCHAR(50),
    @cutLaborTime TIME,
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(255) OUTPUT
)
AS
BEGIN
    -- Check if the job number exists in the Cut Job table
    IF EXISTS (
        SELECT 1
        FROM Cut_job
        WHERE job_no = @jobNumber
    )
    BEGIN
        -- Update Cut Job details
        UPDATE Cut_job
        SET type_of_machine = @machineType,
            time_of_machine = @machineTime,
            material_used = @materialUsed,
            cut_labor_time = @cutLaborTime
        WHERE job_no = @jobNumber;

        -- Set success code and message
        SET @errorCode = 0;
        SET @errorMessage = 'Cut Job details updated successfully.';
    END
    ELSE
    BEGIN
        -- Set error code and message if job number does not exist
        SET @errorCode = 1;
        SET @errorMessage = 'Error: Job with the specified job number does not exist in Cut Job table.';
    END
END;

-----------

CREATE PROCEDURE CheckDateCompletionGreaterThanCommencement (
    @jobNumber INT,
    @dateCompleted DATE,
    @isDateValid BIT OUTPUT
)
AS
BEGIN
    DECLARE @dateCommenced DATE;

    -- Retrieve the date of commencement from the job table
    SELECT @dateCommenced = date_of_commencement
    FROM job
    WHERE job_no = @jobNumber;

    -- Check if the date of completion is greater than the date of commencement
    IF @dateCompleted > @dateCommenced
    BEGIN
        SET @isDateValid = 1; -- Date is valid
    END
    ELSE
    BEGIN
        SET @isDateValid = 0; -- Date is not valid
    END
END;

-----


-- Assuming your job table is named 'job'
-- and it has columns job_no, date_of_commencement, date_of_completion

CREATE PROCEDURE UpdateCompletionDate
    @jobNumber INT,
    @newCompletionDate DATE,
    @errorCode INT OUTPUT,
    @errorMessage NVARCHAR(MAX) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        -- Validate that the new completion date is greater than the date of commencement
        IF EXISTS (
                SELECT 1
                FROM job
                WHERE job_no = @jobNumber
                      AND @newCompletionDate <= date_of_commencement
            )
        BEGIN
            SET @errorCode = 1; -- You can define your error codes
            SET @errorMessage = 'Error: Date of completion must be greater than the date of commencement.';
        END
        ELSE
        BEGIN
            -- Update the date_of_completion
            UPDATE job
            SET date_of_completion = @newCompletionDate
            WHERE job_no = @jobNumber;

            SET @errorCode = 0;
            SET @errorMessage = 'Date of completion updated successfully.';
        END
    END TRY
    BEGIN CATCH
        SET @errorCode = ERROR_NUMBER();
        SET @errorMessage = ERROR_MESSAGE();
    END CATCH
END;


----

-- Sample stored procedure to update costs based on transaction parameters
CREATE PROCEDURE EnterNewTransaction
    @transaction_no INT,
    @sup_cost NUMERIC(10, 2),
    @assembly_account_no INT,
    @department_account_no INT,
    @process_account_no INT
AS
BEGIN
    -- Update Assembly_account details
    UPDATE Assembly_account
    SET details_1 = details_1 + (@sup_cost)
    WHERE assembly_account_no = @assembly_account_no;

    -- Update Department_account details
    UPDATE Department_account
    SET details_2 = details_2 + (@sup_cost)
    WHERE department_account_no = @department_account_no;

    -- Update Process_account details
    UPDATE Process_account
    SET details_3 = details_3 + (@sup_cost)
    WHERE process_account_no = @process_account_no;

    -- Insert into Cost table
    INSERT INTO Cost (transaction_no, sup_cost, assembly_account_no, department_account_no, process_account_no)
    VALUES (@transaction_no, @sup_cost, @assembly_account_no, @department_account_no, @process_account_no);
END;


--------
-- Sample stored procedure to retrieve total cost for an assembly ID
CREATE PROCEDURE GetTotalCost
    @assembly_id VARCHAR(20),
    @total_cost NUMERIC(10, 2) OUTPUT,
    @error_message NVARCHAR(MAX) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        SELECT @total_cost = ISNULL(SUM(c.sup_cost), 0)
        FROM Cost c
        WHERE c.assembly_account_no IN (
            SELECT aea.assembly_account_no
            FROM assembly_expenditure aea
            WHERE aea.assembly_id = @assembly_id
        );

        SET @error_message = NULL;
    END TRY
    BEGIN CATCH
        SET @total_cost = 0;
        SET @error_message = ERROR_MESSAGE();
    END CATCH;
END;


------------
CREATE PROCEDURE GetTotalCost
    @assembly_id VARCHAR(20),
    @total_cost NUMERIC(10, 2) OUTPUT,
    @error_message NVARCHAR(MAX) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        SELECT @total_cost = ISNULL(SUM(c.sup_cost), 0)
        FROM Cost c
        WHERE c.assembly_account_no IN (
            SELECT aea.assembly_account_no
            FROM assembly_expenditure aea
            WHERE aea.assembly_id = @assembly_id
        );

        SET @error_message = NULL;
    END TRY
    BEGIN CATCH
        SET @total_cost = 0;
        SET @error_message = ERROR_MESSAGE();
    END CATCH;
END;

------

CREATE PROCEDURE GetTotalLaborTimeInDepartment
    @department_no INT,
    @given_date DATE,
    @total_labor_time FLOAT OUTPUT,
    @error_message NVARCHAR(MAX) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        SELECT @total_labor_time = ISNULL(SUM(j.fit_labor_time + j.paint_labor_time + cj.cut_labor_time), 0)
        FROM Job j
        LEFT JOIN Fit_job fj ON j.job_no = fj.job_no
        LEFT JOIN Paint_job pj ON j.job_no = pj.job_no
        LEFT JOIN Cut_job cj ON j.job_no = cj.job_no
        WHERE j.date_of_completion = @given_date
            AND EXISTS (
                SELECT 1
                FROM manufacture m
                INNER JOIN Process p ON m.process_id = p.process_id
                WHERE m.job_no = j.job_no
                    AND p.department_no = @department_no
            );

        SET @error_message = NULL;
    END TRY
    BEGIN CATCH
        SET @total_labor_time = 0;
        SET @error_message = ERROR_MESSAGE();
    END CATCH;
END;

-------

CREATE PROCEDURE GetProcessesForAssembly
    @assembly_id VARCHAR(20),
    @process_details NVARCHAR(MAX) OUTPUT,
    @error_message NVARCHAR(MAX) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        SELECT @process_details = COALESCE(@process_details + ', ', '') +
                                  p.process_data + ' (Department: ' + CONVERT(NVARCHAR(10), p.department_no) + ')'
        FROM manufacture m
        INNER JOIN Process p ON m.process_id = p.process_id
        INNER JOIN Job j ON m.job_no = j.job_no
        WHERE m.assembly_id = @assembly_id
        ORDER BY j.date_of_completion;

        SET @error_message = NULL;
    END TRY
    BEGIN CATCH
        SET @process_details = NULL;
        SET @error_message = ERROR_MESSAGE();
    END CATCH;
END;


----------------

CREATE PROCEDURE GetCustomersInRange
    @min_category INT,
    @max_category INT,
    @customer_details NVARCHAR(MAX) OUTPUT,
    @error_message NVARCHAR(MAX) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        SELECT @customer_details = STRING_AGG(cname, ', ')
            FROM Customer
            WHERE category BETWEEN @min_category AND @max_category
            ORDER BY cname;

        SET @error_message = NULL;
    END TRY
    BEGIN CATCH
        SET @customer_details = NULL;
        SET @error_message = ERROR_MESSAGE();
    END CATCH;
END;

----------------
CREATE PROCEDURE DeleteCutJobsInRange
    @min_job_no INT,
    @max_job_no INT,
    @error_message NVARCHAR(MAX) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        DELETE FROM Cut_job
        WHERE job_no BETWEEN @min_job_no AND @max_job_no;

        SET @error_message = NULL;
    END TRY
    BEGIN CATCH
        SET @error_message = ERROR_MESSAGE();
    END CATCH;
END;


---------

CREATE PROCEDURE ChangePaintJobColor
    @job_number INT,
    @new_color VARCHAR(20),
    @error_message NVARCHAR(MAX) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        UPDATE Paint_job
        SET color = @new_color
        WHERE job_no = @job_number;

        SET @error_message = NULL;
    END TRY
    BEGIN CATCH
        SET @error_message = ERROR_MESSAGE();
    END CATCH;
END;





