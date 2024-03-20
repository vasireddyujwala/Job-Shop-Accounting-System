import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;

public class problem {
    
	// Database credentials
    final static String HOSTNAME = "vasi0011-sql-server.database.windows.net";
    final static String DBNAME = "cs-dsa-4513-sql-db";
    final static String USERNAME = "vasi0011";
    final static String PASSWORD = "********";
	
    // Database connection string
	final static String URL = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",HOSTNAME, DBNAME, USERNAME, PASSWORD);
	
	// Query templates
	final static String ENTER_NEW_CUSTOMER = "{CALL EnterNewCustomer(?, ?, ?, ?, ?)}";

	final static String ENTER_NEW_DEPARTMENT = "{CALL EnterNewDepartment(?, ?, ?, ?)}";
	
	final static String ENTER_NEW_PROCESS = "{CALL EnterNewProcess(?, ?, ?, ?, ?)}";
	
	final static String ENTER_NEW_FIT_PROCESS = "{CALL EnterNewFitProcess(?, ?, ?, ?)}";
	
	final static String ENTER_NEW_PAINT_PROCESS = "{CALL EnterNewPaintProcess(?, ?, ?, ?, ?)}";
	
	final static String ENTER_NEW_CUT_PROCESS = "{CALL EnterNewCutProcess(?, ?, ?, ?, ?)}";
	
	final static String ENTER_NEW_ASSEMBLY = "{CALL EnterNewAssembly(?, ?, ?, ?, ?, ?)}";
	
	final static String ENTER_NEW_MANUFACTURE = "{CALL EnterNewManufacture(?, ?, ?, ?, ?)}";
	
	final static String ENTER_NEW_JOB = "{CALL EnterNewJob(?, ?, ?, ?)}";
	
	final static String INSERT_FIT = "{call InsertFitJobDetails(?, ?, ?)}";
	
	final static String INSERT_PAINT = "{call InsertPaintJobDetails(?, ?, ?)}";
	
	final static String INSERT_CUT = "{call InsertCutJobDetails(?, ?, ?)}";
	
	final static String INSERT_MANUFACTURE_JOB = "{call InsertManufactureJob(?, ?, ?, ?, ?)}";
	
	final static String UPDATE_JOB = "{call UpdateJob(?, ?, ?, ?, ?)}";

	final static String DETERMINE_JOB_TYPE = "{call DetermineJob(?, ?)}";
	
	final static String UPDATE_JOB_COMPLETION = "{CALL UpdateJobCompletion(?, ?, ?, ?)}";
	
	final static String INSERT_DEPARTMENT_ACCOUNT = "{CALL InsertDepartmentAccount(?, ?, ?, ?, ?)}";

	final static String INSERT_ASSEMBLY_ACCOUNT = "{CALL InsertAssemblyAccount(?, ?, ?, ?, ?)}";

	final static String INSERT_PROCESS_ACCOUNT = "{CALL InsertProcessAccount(?, ?, ?, ?, ?)}";
	
	final static String INSERT_DEPARTMENT_EXPENDITURE = "{CALL InsertDepartmentExpenditure(?, ?, ?, ?)}";

	final static String INSERT_ASSEMBLY_EXPENDITURE = "{CALL InsertAssemblyExpenditure(?, ?, ?, ?)}";

	final static String INSERT_PROCESS_EXPENDITURE = "{CALL InsertProcessExpenditure(?, ?, ?, ?)}";
	
	final static String DETERMINE_ACCOUNT_TYPE = "{CALL DetermineAccountType(?, ?)}";
	
	final static String CHECK_JOB_EXISTENCE = "{CALL CheckJobExistence(?, ?)}";
	
	final static String UPDATE_FIT_JOB_LABOR_TIME = "{CALL UpdateFitJobLaborTime(?, ?, ?, ?)}";
	
	final static String UPDATE_PAINT_JOB_DETAILS = "{CALL UpdatePaintJobDetails(?, ?, ?, ?, ?, ?)}";
	
	final static String UPDATE_CUT_JOB_DETAILS = "{CALL UpdateCutJobDetails(?, ?, ?, ?, ?, ?, ?)}";
	
	final static String UPDATE_COMPLETION_DATE = "{CALL UpdateCompletionDate(?, ?, ?, ?)}";
	
	final static String ENTER_NEW_TRANSACTION = "{CALL EnterNewTransaction(?, ?, ?, ?, ?)}";
	
	final static String GET_TOTAL_COST = "{CALL GetTotalCost(?, ?, ?)}";
	
	final static String CHECK_DATE_COMPLETION_GREATER_THAN_COMMENCEMENT = "{CALL CheckDateCompletionGreaterThanCommencement(?, ?, ?)}";
	
	final static String CHANGE_PAINT_JOB_COLOR = "{call ChangePaintJobColor(?, ?, ?)}";

	final static String DELETE_CUT_JOBS_IN_RANGE = "{call DeleteCutJobsInRange(?, ?, ?)}";

	final static String GET_CUSTOMERS_IN_RANGE = "{call GetCustomersInRange(?, ?, ?, ?)}";
	
	final static String GET_PROCESSES_FOR_ASSEMBLY = "{call GetProcessesForAssembly(?, ?, ?)}";

    final static String GET_TOTAL_LABOR_TIME_IN_DEPARTMENT = "{call GetTotalLaborTimeInDepartmen(?, ?, ?, ?)}";
	
	
	

	
	private static boolean isValidDateFormat(String date) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        return date.matches(regex);
    }
	// User input prompt
	final static String PROMPT =
	"\nPlease select one of the options below: \n" +
	"(1) Enter a new Customer: \n" +
	"(2) Enter a new Department: \n" +
	"(3) Enter a new Process and information related to the type: \n" +
	"(4) Enter a new Assembly and associate it with one or more processes: \n" +
	"(5) Enter a new Account and associate it with the one which it is applicable: \n" +
	"(6) Enter a new Job: \n" +
	"(7) Enter the date of completion for a job and information of its type: \n" +
	"(8) Enter a new Cost Transaction and update all the affected accounts: \n" +
	"(9) Retrieve the total cost incurred on an assembly-id: \n" +
	"(10) Retrieve the total labor time within a department for jobs completed in the department during a given date: \n" +
	"(11) Retrieve the processes through which a given assembly-id has passed so far and the department responsible for each process: \n" +
	"(12) Retrieve the customers whose category is in a given range: \n" +
	"(13) Delete all cut-jobs whose job-no is in a given range: \n" +
	"(14) Change the color of a given paint job: \n" +
	"(15) Import: enter new customers from a data file until the file is empty: \n" +
	"(16) Export: Retrieve the customers whose category is in a given range and output them to a data file: \n" +
	"(17) Quit";
	
	private static String getUserInputWithFormat(String prompt, String format) {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);

        while (true) {
            System.out.println(prompt);
            String userInput = sc.nextLine();

            try {
                // Attempt to parse the input as a date
                Date parsedDate = (Date) dateFormat.parse(userInput);
                // Return the parsed date as a formatted string
                return dateFormat.format(parsedDate);
            } catch (ParseException e) {
                // If an exception occurs, the input is not a valid date
                System.out.println("Invalid date format. Please enter the date in " + format + " format.");
            }
        }
    }


    public static void main(String[] args) throws SQLException {

        System.out.println("WELCOME TO THE JOB-SHOP ACCOUNTING DATABASE SYSTEM\n");

        final Scanner sc = new Scanner(System.in); // Scanner is used to collect the user input
        String option = ""; // Initialize user option selection as nothing
        while (!option.equals("17")) { // As user for options until option 17 is selected
            System.out.println(PROMPT); // Print the available options
            option = sc.next(); // Read in the user option selection
            switch (option) { // Switch between different options
         
         // Case "1" represents the user's choice to add a new customer.
         case "1":
             System.out.println("Please enter customer name:");
             // Consume any remaining newline characters
             sc.nextLine();
             // Read customer name
             final String cname = sc.nextLine();

             System.out.println("Please enter customer address:");
             // Read customer address
             final String caddress = sc.nextLine();

             System.out.println("Please enter customer category:");
             final int category;
             try {
                 // Read and parse category as an integer
                 category = Integer.parseInt(sc.nextLine().trim());
             } catch (NumberFormatException e) {
                 // Handle invalid input for category
                 System.out.println("Error Message: Invalid input for category. Please enter a valid integer.");
                 break;  // Terminate the loop or take appropriate action
             }

             System.out.println("Connecting to the database...");
             try (Connection connection = DriverManager.getConnection(URL)) {
                 try (CallableStatement statement = connection.prepareCall(ENTER_NEW_CUSTOMER)) {
                     // Set parameters for the stored procedure
                     statement.setString(1, cname);
                     statement.setString(2, caddress);
                     statement.setInt(3, category);

                     // Register output parameters for error code and message
                     statement.registerOutParameter(4, Types.INTEGER);
                     statement.registerOutParameter(5, Types.NVARCHAR);

                     System.out.println("Dispatching the stored procedure...");
                     // Execute the stored procedure
                     statement.execute();

                     // Retrieve error code and message from the stored procedure
                     int aerrorCode = statement.getInt(4);
                     String aerrorMessage = statement.getString(5);

                     if (aerrorCode != 0) {
                         // Error occurred, display error message
                         System.out.println("Error Code: " + aerrorCode);
                         System.out.println("Error Message: " + aerrorMessage);
                         System.out.println("Failed to add customer. Returning to the main menu.");
                     } else {
                         // No error, success
                         System.out.println("Done. Customer added.");
                     }
                 } catch (SQLException e) {
                     // Handle SQL exceptions during stored procedure execution
                     System.out.println("Error: " + e.getMessage());
                     System.out.println("Failed to add customer. Returning to the main menu.");
                 }
             } catch (SQLException e) {
                 // Handle connection errors
                 System.out.println("Connection error: " + e.getMessage());
             }
             break;

          // Case "2" represents the user's choice to add a new department.
         case "2":
             // Prompt the user to enter the department number.
             System.out.println("Please enter department number:");
             // Read the entered department number.
             int department_no = sc.nextInt();

             // Consume the newline character left by previous sc.nextInt().
             sc.nextLine();

             // Prompt the user to enter department data.
             System.out.println("Please enter department data:");
             // Read the entered department data.
             String department_data = sc.nextLine();

             // Inform the user about the database connection process.
             System.out.println("Connecting to the database...");

             try (Connection connection = DriverManager.getConnection(URL)) {
                 // Prepare the stored procedure call to add a new department.
                 try (CallableStatement statement = connection.prepareCall(ENTER_NEW_DEPARTMENT)) {
                     // Set the input parameters for the stored procedure.
                     statement.setInt(1, department_no);
                     statement.setString(2, department_data);

                     // Register output parameters for error code and message.
                     statement.registerOutParameter(3, Types.INTEGER);
                     statement.registerOutParameter(4, Types.NVARCHAR);

                     // Inform the user about dispatching the stored procedure.
                     System.out.println("Dispatching the stored procedure...");
                     // Execute the stored procedure.
                     statement.execute();

                     // Retrieve error code and message from the stored procedure.
                     int berrorCode = statement.getInt(3);
                     String berrorMessage = statement.getString(4);

                     if (berrorCode != 0) {
                         // Error occurred, display error message.
                         System.out.println("Error Code: " + berrorCode);
                         System.out.println("Error Message: " + berrorMessage);
                         System.out.println("Failed to add department. Returning to the main menu.");
                     } else {
                         // No error, success.
                         System.out.println("Done. Department added.");
                     }
                 } catch (SQLException e) {
                     // Handle SQL exceptions.
                     System.out.println("Error: " + e.getMessage());
                     System.out.println("Failed to add department. Returning to the main menu.");
                 }
             } catch (SQLException e) {
                 // Handle connection-related exceptions.
                 System.out.println("Connection error: " + e.getMessage());
             }
             // Exit the case statement for adding a department.
             break;
             
         // Case "3" represents the user's choice to add a new process.
         case "3":
        	    System.out.println("Please enter process ID:");
        	    sc.nextLine();
        	    // read process id
        	    String process_id = sc.nextLine();

        	    System.out.println("Please enter process data:");
        	    // read process data
        	    String process_data = sc.nextLine();

        	    System.out.println("Please enter department number:");

        	    final int department_noc;
        	    try {
        	        department_noc = Integer.parseInt(sc.nextLine().trim());
        	    } catch (NumberFormatException e) {
        	        System.out.println("Error Message: Invalid input for department number. Please enter a valid integer.");
        	        break; // Terminate the loop or take appropriate action
        	    }

        	    System.out.println("Connecting to the database...");

        	    try (Connection connection = DriverManager.getConnection(URL)) {
        	        try (CallableStatement statement = connection.prepareCall(ENTER_NEW_PROCESS)) {
        	            // Set parameters for the main process
        	            statement.setString(1, process_id);
        	            statement.setString(2, process_data);
        	            statement.setInt(3, department_noc);

        	            // Register output parameters for error code and message
        	            statement.registerOutParameter(4, Types.INTEGER);
        	            statement.registerOutParameter(5, Types.NVARCHAR);

        	            System.out.println("Dispatching the stored procedure for Process...");
        	            statement.execute();

        	            // Retrieve error code and message from the stored procedure
        	            int cerrorCode = statement.getInt(4);
        	            String cerrorMessage = statement.getString(5);

        	            if (cerrorCode != 0) {
        	                // Error occurred, display error message
        	                System.out.println("Error Code: " + cerrorCode);
        	                System.out.println("Error Message: " + cerrorMessage);
        	                System.out.println("Failed to add process. Returning to the main menu.");
        	            } else {
        	                // No error, success
        	                System.out.println("Done. Process added.");

        	                boolean processDetailsEnteredSuccessfully = false;

        	                while (!processDetailsEnteredSuccessfully) {
        	                    // Ask the user to choose the type of process to add
        	                    System.out.println("Choose the type of process to add:");
        	                    System.out.println("1. Fit Process");
        	                    System.out.println("2. Paint Process");
        	                    System.out.println("3. Cut Process");

        	                    int processTypeChoice = sc.nextInt();

        	                    switch (processTypeChoice) {
        	                        case 1:
        	                            // Now, let's handle the Fit Process
        	                            System.out.println("Please enter Fit Type:");
        	                            sc.nextLine(); // Consume the newline character left by previous sc.nextInt()
        	                            String fit_type = sc.nextLine();

        	                            try (CallableStatement fitStatement = connection.prepareCall(ENTER_NEW_FIT_PROCESS)) {
        	                                // Set parameters for the Fit Process
        	                                fitStatement.setString(1, process_id);
        	                                fitStatement.setString(2, fit_type);

        	                                // Register output parameters for error code and message
        	                                fitStatement.registerOutParameter(3, Types.INTEGER);
        	                                fitStatement.registerOutParameter(4, Types.NVARCHAR);

        	                                System.out.println("Dispatching the stored procedure for Fit Process...");
        	                                fitStatement.execute();

        	                                // Retrieve error code and message from the stored procedure
        	                                int errorCodeFitProcess = fitStatement.getInt(3);
        	                                String errorMessageFitProcess = fitStatement.getString(4);

        	                                if (errorCodeFitProcess != 0) {
        	                                    // Error occurred, display error message
        	                                    System.out.println("Error Code: " + errorCodeFitProcess);
        	                                    System.out.println("Error Message: " + errorMessageFitProcess);
        	                                    System.out.println("Failed to add Fit Process. Please try again.");
        	                                } else {
        	                                    // No error, success
        	                                    System.out.println("Done. Fit Process added.");
        	                                    processDetailsEnteredSuccessfully = true;
        	                                }
        	                            } catch (SQLException e) {
        	                                // Handle SQL exceptions
        	                                System.out.println("Error: " + e.getMessage());
        	                                System.out.println("Failed to add Fit Process. Returning to process type selection.");
        	                            }
        	                            break;

        	                        case 2:
        	                            // Now, let's handle the Paint Process
        	                            System.out.println("Please enter Paint Type:");
        	                            sc.nextLine(); // Consume the newline character left by previous sc.nextInt()
        	                            String paint_type = sc.nextLine();

        	                            System.out.println("Please enter Paint Method:");
        	                            String paint_method = sc.nextLine();

        	                            try (CallableStatement paintStatement = connection.prepareCall(ENTER_NEW_PAINT_PROCESS)) {
        	                                // Set parameters for the Paint Process
        	                                paintStatement.setString(1, process_id);
        	                                paintStatement.setString(2, paint_type);
        	                                paintStatement.setString(3, paint_method);

        	                                // Register output parameters for error code and message
        	                                paintStatement.registerOutParameter(4, Types.INTEGER);
        	                                paintStatement.registerOutParameter(5, Types.NVARCHAR);

        	                                System.out.println("Dispatching the stored procedure for Paint Process...");
        	                                paintStatement.execute();

        	                                // Retrieve error code and message from the stored procedure
        	                                int errorCodePaintProcess = paintStatement.getInt(4);
        	                                String errorMessagePaintProcess = paintStatement.getString(5);

        	                                if (errorCodePaintProcess != 0) {
        	                                    // Error occurred, display error message
        	                                    System.out.println("Error Code: " + errorCodePaintProcess);
        	                                    System.out.println("Error Message: " + errorMessagePaintProcess);
        	                                    System.out.println("Failed to add Paint Process. Please try again.");
        	                                } else {
        	                                    // No error, success
        	                                    System.out.println("Done. Paint Process added.");
        	                                    processDetailsEnteredSuccessfully = true;
        	                                }
        	                            } catch (SQLException e) {
        	                                // Handle SQL exceptions
        	                                System.out.println("Error: " + e.getMessage());
        	                                System.out.println("Failed to add Paint Process. Returning to process type selection.");
        	                            }
        	                            break;

        	                        case 3:
        	                            // Now, let's handle the Cut Process
        	                            System.out.println("Please enter Cutting Type:");
        	                            sc.nextLine(); // Consume the newline character left by previous sc.nextInt()
        	                            String cuttingType = sc.nextLine();

        	                            System.out.println("Please enter Machine Type:");
        	                            String machineType = sc.nextLine();

        	                            try (CallableStatement cutStatement = connection.prepareCall(ENTER_NEW_CUT_PROCESS)) {
        	                                // Set parameters for the Cut Process
        	                                cutStatement.setString(1, process_id);
        	                                cutStatement.setString(2, cuttingType);
        	                                cutStatement.setString(3, machineType);

        	                                // Register output parameters for error code and message
        	                                cutStatement.registerOutParameter(4, Types.INTEGER);
        	                                cutStatement.registerOutParameter(5, Types.NVARCHAR);

        	                                System.out.println("Dispatching the stored procedure for Cut Process...");
        	                                cutStatement.execute();

        	                                // Retrieve error code and message from the stored procedure
        	                                int errorCodeCutProcess = cutStatement.getInt(4);
        	                                String errorMessageCutProcess = cutStatement.getString(5);

        	                                if (errorCodeCutProcess != 0) {
        	                                    // Error occurred, display error message
        	                                    System.out.println("Error Code: " + errorCodeCutProcess);
        	                                    System.out.println("Error Message: " + errorMessageCutProcess);
        	                                    System.out.println("Failed to add Cut Process. Please try again.");
        	                                } else {
        	                                    // No error, success
        	                                    System.out.println("Done. Cut Process added.");
        	                                    processDetailsEnteredSuccessfully = true;
        	                                }
        	                            } catch (SQLException e) {
        	                                // Handle SQL exceptions
        	                                System.out.println("Error: " + e.getMessage());
        	                                System.out.println("Failed to add Cut Process. Returning to process type selection.");
        	                            }
        	                            break;

        	                        default:
        	                            System.out.println("Invalid choice. Returning to the main menu.");
        	                            break;
        	                    }
        	                }
        	            }
        	        } catch (SQLException e) {
        	            // Handle SQL exceptions
        	            System.out.println("Error: " + e.getMessage());
        	            System.out.println("Failed to add process. Returning to the main menu.");
        	        }
        	    } catch (SQLException e) {
        	        System.out.println("Connection error: " + e.getMessage());
        	    }
        	    break;
         // Case "4" represents the user's choice to add a new assembly and manufacture record.
         case "4":
        	    // Get user input for Assembly table
        	    System.out.println("Please enter assembly ID:");
        	    sc.nextLine();
        	    String assemblyId = sc.nextLine();
                // read date ordered
        	    String dateOrdered = getUserInputWithFormat("Please enter date ordered (YYYY-MM-DD):", "yyyy-MM-dd");
                // read assembly details
        	    System.out.println("Please enter assembly details:");
        	    String assemblyDetails = sc.nextLine();
                // read customer name
        	    System.out.println("Please enter customer name:");
        	    String assemblyName = sc.nextLine();

        	    // Connecting to the database and executing stored procedure for Assembly
        	    try (Connection connection = DriverManager.getConnection(URL)) {
        	        try (CallableStatement assemblyStatement = connection.prepareCall(ENTER_NEW_ASSEMBLY)) {
        	            assemblyStatement.setString(1, assemblyId);
        	            assemblyStatement.setString(2, dateOrdered);
        	            assemblyStatement.setString(3, assemblyDetails);
        	            assemblyStatement.setString(4, assemblyName);

        	            // Register output parameters for error code and message
        	            assemblyStatement.registerOutParameter(5, Types.INTEGER);
        	            assemblyStatement.registerOutParameter(6, Types.NVARCHAR);

        	            System.out.println("Dispatching the stored procedure for Assembly...");
        	            assemblyStatement.execute();

        	            // Retrieve error code and message from the stored procedure
        	            int derrorCode = assemblyStatement.getInt(5);
        	            String derrorMessage = assemblyStatement.getString(6);

        	            if (derrorCode != 0) {
        	                // Error occurred, display error message
        	                System.out.println("Error Code: " + derrorCode);
        	                System.out.println("Error Message: " + derrorMessage);
        	                System.out.println("Failed to add Assembly record. Returning to the main menu.");
        	                break; // Terminate the loop or take appropriate action
        	            } else {
        	                // No error, success
        	                System.out.println("Done. Assembly record added.");
        	            }
        	        } catch (SQLException e) {
        	            // Handle SQL exceptions
        	            System.out.println("Error: " + e.getMessage());
        	            System.out.println("Failed to add Assembly record. Returning to the main menu.");
        	            break; // Terminate the loop or take appropriate action
        	        }

        	        // Ask the user if they want to enter a process for the specific assembly
        	        String userChoice = "yes";

        	        while (userChoice.equalsIgnoreCase("yes")) {
        	            System.out.println("Do you want to enter process for this assembly? (yes/no):");
        	            userChoice = sc.nextLine();

        	            if (userChoice.equalsIgnoreCase("yes")) {
        	                // Get user input for Manufacture table
        	                System.out.println("Please enter process ID for Manufacture:");
        	                String processIdManufacture = sc.nextLine();

        	                boolean isValidProcessId = false;

        	                while (!isValidProcessId) {
        	                    try (CallableStatement manufactureStatement = connection.prepareCall(ENTER_NEW_MANUFACTURE)) {
        	                        manufactureStatement.setString(1, assemblyId);
        	                        manufactureStatement.setString(2, processIdManufacture);
        	                        manufactureStatement.setNull(3, Types.INTEGER); // Placeholder for job_no (null)

        	                        // Register output parameters for error code and message
        	                        manufactureStatement.registerOutParameter(4, Types.INTEGER);
        	                        manufactureStatement.registerOutParameter(5, Types.NVARCHAR);

        	                        System.out.println("Dispatching the stored procedure for Manufacture...");
        	                        manufactureStatement.execute();

        	                        // Retrieve error code and message from the stored procedure
        	                        int dderrorCode = manufactureStatement.getInt(4);
        	                        String dderrorMessage = manufactureStatement.getString(5);

        	                        if (dderrorCode != 0) {
        	                            // Error occurred, display error message
        	                            System.out.println("Error Code: " + dderrorCode);
        	                            System.out.println("Error Message: " + dderrorMessage);
        	                            System.out.println("Failed to add Manufacture record. Please enter process ID again:");

        	                            // Prompt user for a new process ID
        	                            processIdManufacture = sc.next();
        	                        } else {
        	                            // No error, success
        	                            System.out.println("Done. Manufacture record added.");
        	                            isValidProcessId = true; // Exit the loop
        	                        }
        	                    } catch (SQLException e) {
        	                        // Handle SQL exceptions
        	                        System.out.println("Error: " + e.getMessage());
        	                        System.out.println("Failed to add Manufacture record. Please enter process ID again:");

        	                        // Prompt user for a new process ID
        	                        processIdManufacture = sc.next();
        	                    }
        	                }
        	            }
        	        }
        	    } catch (SQLException e) {
        	        System.out.println("Connection error: " + e.getMessage());
        	    }
        	    break;


	     // CASE "5": represents the user's choice to add a new account and expenditure
         case "5":
        	    System.out.println("Enter account number:");
        	    int accountNumber = sc.nextInt();

        	    String dateEstablished = getUserInputWithFormat("Enter date of establishment (YYYY-MM-DD):", "yyyy-MM-dd");

        	    System.out.println("Choose one of the following types of account:");
        	    System.out.println("1. Assembly Account");
        	    System.out.println("2. Department Account");
        	    System.out.println("3. Process Account");

        	    int accountTypeChoice = sc.nextInt();

        	    System.out.println("Enter account details:");
        	    sc.nextLine(); // Consume the newline character
        	    BigDecimal accountDetails = new BigDecimal(sc.nextLine()); // Use BigDecimal for NUMERIC(10,2)

        	    int eErrorCode;
        	    String eErrorMessage;

        	    try (Connection connection = DriverManager.getConnection(URL)) {
        	        switch (accountTypeChoice) {
        	            case 1:
        	                try (CallableStatement assemblyAccountStatement = connection.prepareCall(INSERT_ASSEMBLY_ACCOUNT)) {
        	                    assemblyAccountStatement.setInt(1, accountNumber);
        	                    assemblyAccountStatement.setString(2, dateEstablished);
        	                    assemblyAccountStatement.setBigDecimal(3, accountDetails);

        	                    assemblyAccountStatement.registerOutParameter(4, Types.INTEGER);
        	                    assemblyAccountStatement.registerOutParameter(5, Types.NVARCHAR);

        	                    System.out.println("Dispatching the stored procedure for Assembly Account...");
        	                    assemblyAccountStatement.execute();

        	                    eErrorCode = assemblyAccountStatement.getInt(4);
        	                    eErrorMessage = assemblyAccountStatement.getString(5);

        	                    // Check for errors in the assembly account addition
        	                    if (eErrorCode != 0) {
        	                        // Error occurred, display error message
        	                        System.out.println("Error Code: " + eErrorCode);
        	                        System.out.println("Error Message: " + eErrorMessage);
        	                        System.out.println("Failed to add Assembly Account record. Returning to the main menu.");
        	                        break; // Terminate the loop or take appropriate action
        	                    }

        	                    // Now, prompt the user for expenditure details
        	                    System.out.println("Enter Assembly ID for Expenditure:");
        	                    String eassemblyId = sc.nextLine();

        	                    // Insert expenditure details into the assembly_expenditure table
        	                    try (CallableStatement assemblyExpenditureStatement = connection.prepareCall(INSERT_ASSEMBLY_EXPENDITURE)) {
        	                        assemblyExpenditureStatement.setInt(1, accountNumber);
        	                        assemblyExpenditureStatement.setString(2, eassemblyId);

        	                        assemblyExpenditureStatement.registerOutParameter(3, Types.INTEGER);
        	                        assemblyExpenditureStatement.registerOutParameter(4, Types.NVARCHAR);

        	                        assemblyExpenditureStatement.execute();

        	                        int aeErrorCode = assemblyExpenditureStatement.getInt(3);
        	                        String aeErrorMessage = assemblyExpenditureStatement.getString(4);

        	                        // Check for errors in the assembly expenditure addition
        	                        if (aeErrorCode != 0) {
        	                            // Error occurred, display error message
        	                            System.out.println("Error Code: " + aeErrorCode);
        	                            System.out.println("Error Message: " + aeErrorMessage);
        	                            System.out.println("Failed to add Assembly Expenditure record. Returning to the main menu.");
        	                            break; // Terminate the loop or take appropriate action
        	                        }
        	                    } catch (SQLException e) {
        	                        // Handle SQL exceptions for assembly expenditure statement
        	                        eErrorCode = 50021; // Custom error code for assembly expenditure
        	                        eErrorMessage = e.getMessage();

        	                        // Display error message
        	                        System.out.println("Error Code: " + eErrorCode);
        	                        System.out.println("Error Message: " + eErrorMessage);
        	                        System.out.println("Failed to add Assembly Expenditure record. Returning to the main menu.");
        	                        break; // Terminate the loop or take appropriate action
        	                    }

        	                    System.out.println("Assembly Account and Expenditure records added successfully.");
        	                } catch (SQLException e) {
        	                    // Handle SQL exceptions for assembly account statement
        	                    eErrorCode = 50018; // Custom error code for assembly account
        	                    eErrorMessage = e.getMessage();

        	                    // Display error message
        	                    System.out.println("Error Code: " + eErrorCode);
        	                    System.out.println("Error Message: " + eErrorMessage);
        	                    System.out.println("Failed to add Assembly Account record. Returning to the main menu.");
        	                    break; // Terminate the loop or take appropriate action
        	                }
        	                break;

        	            case 2:
        	                try (CallableStatement departmentAccountStatement = connection.prepareCall(INSERT_DEPARTMENT_ACCOUNT)) {
        	                    departmentAccountStatement.setInt(1, accountNumber);
        	                    departmentAccountStatement.setString(2, dateEstablished);
        	                    departmentAccountStatement.setBigDecimal(3, accountDetails);

        	                    departmentAccountStatement.registerOutParameter(4, Types.INTEGER); // Assuming this is an INTEGER output parameter
        	                    departmentAccountStatement.registerOutParameter(5, Types.NVARCHAR);

        	                    System.out.println("Dispatching the stored procedure for Department Account...");
        	                    departmentAccountStatement.execute();

        	                    eErrorCode = departmentAccountStatement.getInt(4);
        	                    eErrorMessage = departmentAccountStatement.getString(5);

        	                    // Check for errors in the department account addition
        	                    if (eErrorCode != 0) {
        	                        // Error occurred, display error message
        	                        System.out.println("Error Code: " + eErrorCode);
        	                        System.out.println("Error Message: " + eErrorMessage);
        	                        System.out.println("Failed to add Department Account record. Returning to the main menu.");
        	                        break; // Terminate the loop or take appropriate action
        	                    }

        	                    // Now, prompt the user for expenditure details
        	                    System.out.println("Enter Department Number for Expenditure:");
        	                    int departmentNumber = sc.nextInt();

        	                    // Insert expenditure details into the department_expenditure table
        	                    try (CallableStatement departmentExpenditureStatement = connection.prepareCall(INSERT_DEPARTMENT_EXPENDITURE)) {
        	                        departmentExpenditureStatement.setInt(1, accountNumber);
        	                        departmentExpenditureStatement.setInt(2, departmentNumber);

        	                        departmentExpenditureStatement.registerOutParameter(3, Types.INTEGER);
        	                        departmentExpenditureStatement.registerOutParameter(4, Types.NVARCHAR);

        	                        departmentExpenditureStatement.execute();

        	                        int deErrorCode = departmentExpenditureStatement.getInt(3);
        	                        String deErrorMessage = departmentExpenditureStatement.getString(4);

        	                        // Check for errors in the department expenditure addition
        	                        if (deErrorCode != 0) {
        	                            // Error occurred, display error message
        	                            System.out.println("Error Code: " + deErrorCode);
        	                            System.out.println("Error Message: " + deErrorMessage);
        	                            System.out.println("Failed to add Department Expenditure record. Returning to the main menu.");
        	                            break; // Terminate the loop or take appropriate action
        	                        }
        	                    } catch (SQLException e) {
        	                        // Handle SQL exceptions for department expenditure statement
        	                        eErrorCode = 50022; // Custom error code for department expenditure
        	                        eErrorMessage = e.getMessage();

        	                        // Display error message
        	                        System.out.println("Error Code: " + eErrorCode);
        	                        System.out.println("Error Message: " + eErrorMessage);
        	                        System.out.println("Failed to add Department Expenditure record. Returning to the main menu.");
        	                        break; // Terminate the loop or take appropriate action
        	                    }

        	                    System.out.println("Department Account and Expenditure records added successfully.");
        	                } catch (SQLException e) {
        	                    // Handle SQL exceptions for department account statement
        	                    eErrorCode = 50019; // Custom error code for department account
        	                    eErrorMessage = e.getMessage();

        	                    // Display error message
        	                    System.out.println("Error Code: " + eErrorCode);
        	                    System.out.println("Error Message: " + eErrorMessage);
        	                    System.out.println("Failed to add Department Account record. Returning to the main menu.");
        	                    break; // Terminate the loop or take appropriate action
        	                }
        	                break;

        	            case 3:
        	                try (CallableStatement processAccountStatement = connection.prepareCall(INSERT_PROCESS_ACCOUNT)) {
        	                    processAccountStatement.setInt(1, accountNumber);
        	                    processAccountStatement.setString(2, dateEstablished);
        	                    processAccountStatement.setBigDecimal(3, accountDetails);

        	                    processAccountStatement.registerOutParameter(4, Types.INTEGER);
        	                    processAccountStatement.registerOutParameter(5, Types.NVARCHAR);

        	                    System.out.println("Dispatching the stored procedure for Process Account...");
        	                    processAccountStatement.execute();

        	                    eErrorCode = processAccountStatement.getInt(4);
        	                    eErrorMessage = processAccountStatement.getString(5);

        	                    // Check for errors in the process account addition
        	                    if (eErrorCode != 0) {
        	                        // Error occurred, display error message
        	                        System.out.println("Error Code: " + eErrorCode);
        	                        System.out.println("Error Message: " + eErrorMessage);
        	                        System.out.println("Failed to add Process Account record. Returning to the main menu.");
        	                        break; // Terminate the loop or take appropriate action
        	                    }

        	                    // Now, prompt the user for expenditure details
        	                    System.out.println("Enter Process ID for Expenditure:");
        	                    String processId = sc.nextLine();

        	                    // Insert expenditure details into the process_expenditure table
        	                    try (CallableStatement processExpenditureStatement = connection.prepareCall(INSERT_PROCESS_EXPENDITURE)) {
        	                        processExpenditureStatement.setInt(1, accountNumber);
        	                        processExpenditureStatement.setString(2, processId);

        	                        processExpenditureStatement.registerOutParameter(3, Types.INTEGER);
        	                        processExpenditureStatement.registerOutParameter(4, Types.NVARCHAR);

        	                        processExpenditureStatement.execute();

        	                        int peErrorCode = processExpenditureStatement.getInt(3);
        	                        String peErrorMessage = processExpenditureStatement.getString(4);

        	                        // Check for errors in the process expenditure addition
        	                        if (peErrorCode != 0) {
        	                            // Error occurred, display error message
        	                            System.out.println("Error Code: " + peErrorCode);
        	                            System.out.println("Error Message: " + peErrorMessage);
        	                            System.out.println("Failed to add Process Expenditure record. Returning to the main menu.");
        	                            break; // Terminate the loop or take appropriate action
        	                        }
        	                    } catch (SQLException e) {
        	                        // Handle SQL exceptions for process expenditure statement
        	                        eErrorCode = 50023; // Custom error code for process expenditure
        	                        eErrorMessage = e.getMessage();

        	                        // Display error message
        	                        System.out.println("Error Code: " + eErrorCode);
        	                        System.out.println("Error Message: " + eErrorMessage);
        	                        System.out.println("Failed to add Process Expenditure record. Returning to the main menu.");
        	                        break; // Terminate the loop or take appropriate action
        	                    }

        	                    System.out.println("Process Account and Expenditure records added successfully.");
        	                } catch (SQLException e) {
        	                    // Handle SQL exceptions for process account statement
        	                    eErrorCode = 50020; // Custom error code for process account
        	                    eErrorMessage = e.getMessage();

        	                    // Display error message
        	                    System.out.println("Error Code: " + eErrorCode);
        	                    System.out.println("Error Message: " + eErrorMessage);
        	                    System.out.println("Failed to add Process Account record. Returning to the main menu.");
        	                    break; // Terminate the loop or take appropriate action
        	                }
        	                break;

        	            default:
        	                System.out.println("Invalid choice. Returning to the main menu.");
        	                return;
        	        }

        	        if (eErrorCode != 0) {
        	            // Display error message
        	            System.out.println("Error Code: " + eErrorCode);
        	            System.out.println("Error Message: " + eErrorMessage);
        	            System.out.println("Failed to add account. Returning to the main menu.");
        	        } else {
        	            System.out.println("Account record added successfully.");
        	        }
        	    } catch (SQLException e) {
        	        // Handle SQL exceptions for connection
        	        System.out.println("Connection error: " + e.getMessage());
        	    }
        	    break;
        	    
        	    
         // case "6": represents the user's choice to add a new job
         case "6":
        	    // Insert into Job
        	    System.out.println("Please enter job number:");
        	    int jobNumber = sc.nextInt();

        	    // Ask the user to enter the date of commencement
        	    String dateCommenced = getUserInputWithFormat("Please enter date the job commenced (YYYY-MM-DD):", "yyyy-MM-dd");

        	    try (Connection connection = DriverManager.getConnection(URL)) {
        	        try (CallableStatement jobStatement = connection.prepareCall(ENTER_NEW_JOB)) {
        	            jobStatement.setInt(1, jobNumber);
        	            jobStatement.setString(2, dateCommenced);

        	            // Register output parameters for error code and message
        	            jobStatement.registerOutParameter(3, Types.INTEGER);
        	            jobStatement.registerOutParameter(4, Types.NVARCHAR);

        	            System.out.println("Dispatching the stored procedure for Job...");
        	            jobStatement.execute();

        	            // Retrieve error code and message from the stored procedure
        	            int ferrorCode = jobStatement.getInt(3);
        	            String ferrorMessage= jobStatement.getString(4);

        	            if (ferrorCode != 0) {
        	                // Error occurred, display error message
        	                System.out.println("Error Code: " + ferrorCode);
        	                System.out.println("Error Message: " + ferrorMessage);
        	                System.out.println("Failed to add Job record. Returning to the main menu.");
        	            } else {
        	                System.out.println("Job record added successfully.");

        	                // Ask the user to choose the type of job
        	                System.out.println("Choose the type of job:");
        	                System.out.println("1. Fit Job");
        	                System.out.println("2. Paint Job");
        	                System.out.println("3. Cut Job");
        	                int jobTypeChoice = sc.nextInt();
        	                sc.nextLine(); // Consume the newline character

        	                // Call the procedure to insert details based on the user's choice
        	                switch (jobTypeChoice) {
        	             // Inside the main switch statement
        	                case 1:
        	                    // User's choice for Fit Job
        	                    // Insert into Fit Job
        	                    try (Connection fconnection = DriverManager.getConnection(URL)) {
        	                        try (CallableStatement fitJobStatement = fconnection.prepareCall(INSERT_FIT)) {
        	                            fitJobStatement.setInt(1, jobNumber);

        	                            // Register output parameters for error code and message
        	                            fitJobStatement.registerOutParameter(2, Types.INTEGER);
        	                            fitJobStatement.registerOutParameter(3, Types.NVARCHAR);

        	                            System.out.println("Dispatching the stored procedure for Fit Job...");
        	                            fitJobStatement.execute();

        	                            // Retrieve error code and message from the stored procedure
        	                            int errorCodeFitJob = fitJobStatement.getInt(2);
        	                            String errorMessageFitJob = fitJobStatement.getString(3);

        	                            if (errorCodeFitJob != 0) {
        	                                // Error occurred, display error message
        	                                System.out.println("Error Code: " + errorCodeFitJob);
        	                                System.out.println("Error Message: " + errorMessageFitJob);
        	                                System.out.println("Failed to add Fit Job details. Returning to the main menu.");
        	                            } else {
        	                                System.out.println("Fit Job details added successfully.");

        	                                // Ask the user if they want to insert Manufacture details
        	                                System.out.println("Do you want to insert assembly_id and process_id pairs for this job? (yes/no):");
        	                                String userInputManufacture = sc.next().toLowerCase();

        	                                if (userInputManufacture.equals("yes")) {
        	                                    boolean insertMoreManufacture = true;

        	                                    while (insertMoreManufacture) {
        	                                        // Ask the user to enter the assembly_id and process_id pair
        	                                        System.out.println("Please enter assembly_id:");
        	                                        String fassemblyId = sc.next();

        	                                        System.out.println("Please enter process_id:");
        	                                        String processId = sc.next();

        	                                        // Call the procedure to insert details into the Manufacture table
        	                                        try (CallableStatement insertManufactureStatement = connection.prepareCall(INSERT_MANUFACTURE_JOB)) {
        	                                            insertManufactureStatement.setInt(1, jobNumber);
        	                                            insertManufactureStatement.setString(2, fassemblyId);
        	                                            insertManufactureStatement.setString(3, processId);

        	                                            // Register output parameters for error code and message
        	                                            insertManufactureStatement.registerOutParameter(4, Types.INTEGER);
        	                                            insertManufactureStatement.registerOutParameter(5, Types.NVARCHAR);

        	                                            System.out.println("Dispatching the stored procedure for Manufacture...");
        	                                            insertManufactureStatement.execute();

        	                                            // Retrieve error code and message from the stored procedure
        	                                            int errorCodeManufacture = insertManufactureStatement.getInt(4);
        	                                            String errorMessageManufacture = insertManufactureStatement.getString(5);

        	                                            if (errorCodeManufacture != 0) {
        	                                                // Error occurred, display error message
        	                                                System.out.println("Error Code: " + errorCodeManufacture);
        	                                                System.out.println("Error Message: " + errorMessageManufacture);
        	                                                System.out.println("Failed to add Manufacture details.");

        	                                                // Ask the user if they want to try entering assembly_id and process_id again
        	                                                System.out.println("Do you want to try entering assembly_id and process_id again? (yes/no):");
        	                                                String userInputTryAgain = sc.next().toLowerCase();

        	                                                if (userInputTryAgain.equals("no")) {
        	                                                    insertMoreManufacture = false; // Exit the loop
        	                                                }
        	                                            } else {
        	                                                System.out.println("Manufacture details added successfully.");

        	                                                // Ask the user if they want to insert more assembly_id and process_id pairs
        	                                                System.out.println("Do you want to insert more assembly_id and process_id pairs for this job? (yes/no):");
        	                                                String userInputMorePairs = sc.next().toLowerCase();

        	                                                if (userInputMorePairs.equals("no")) {
        	                                                    insertMoreManufacture = false; // Exit the loop
        	                                                }
        	                                            }
        	                                        } catch (SQLException e) {
        	                                            // Handle SQL exceptions
        	                                            System.out.println("Error: " + e.getMessage());
        	                                            System.out.println("Failed to add Manufacture details. Returning to the main menu.");
        	                                            insertMoreManufacture = false; // Exit the loop on error
        	                                        }
        	                                    }
        	                                }
        	                            }
        	                        } catch (SQLException e) {
        	                            // Handle SQL exceptions
        	                            System.out.println("Error: " + e.getMessage());
        	                            System.out.println("Failed to add Fit Job details. Returning to the main menu.");
        	                        }}
        	                        break;

        	                case 2:
        	                    // User's choice for Paint Job
        	                    // Insert into Paint Job
        	                    try (Connection fconnection = DriverManager.getConnection(URL)) {
        	                        try (CallableStatement paintJobStatement = fconnection.prepareCall(INSERT_PAINT)) {
        	                            paintJobStatement.setInt(1, jobNumber);

        	                            // Register output parameters for error code and message
        	                            paintJobStatement.registerOutParameter(2, Types.INTEGER);
        	                            paintJobStatement.registerOutParameter(3, Types.NVARCHAR);

        	                            System.out.println("Dispatching the stored procedure for Paint Job...");
        	                            paintJobStatement.execute();

        	                            // Retrieve error code and message from the stored procedure
        	                            int errorCodePaintJob = paintJobStatement.getInt(2);
        	                            String errorMessagePaintJob = paintJobStatement.getString(3);

        	                            if (errorCodePaintJob != 0) {
        	                                // Error occurred, display error message
        	                                System.out.println("Error Code: " + errorCodePaintJob);
        	                                System.out.println("Error Message: " + errorMessagePaintJob);
        	                                System.out.println("Failed to add Paint Job details. Returning to the main menu.");
        	                            } else {
        	                                System.out.println("Paint Job details added successfully.");

        	                                // Ask the user if they want to insert Manufacture details
        	                                System.out.println("Do you want to insert assembly_id and process_id pairs for this job? (yes/no):");
        	                                String userInputManufacture = sc.next().toLowerCase();

        	                                if (userInputManufacture.equals("yes")) {
        	                                    boolean insertMoreManufacture = true;

        	                                    while (insertMoreManufacture) {
        	                                        // Ask the user to enter the assembly_id and process_id pair
        	                                        System.out.println("Please enter assembly_id:");
        	                                        String fassemblyId = sc.next();

        	                                        System.out.println("Please enter process_id:");
        	                                        String processId = sc.next();

        	                                        // Call the procedure to insert details into the Manufacture table
        	                                        try (CallableStatement insertManufactureStatement = connection.prepareCall(INSERT_MANUFACTURE_JOB)) {
        	                                            insertManufactureStatement.setInt(1, jobNumber);
        	                                            insertManufactureStatement.setString(2, fassemblyId);
        	                                            insertManufactureStatement.setString(3, processId);

        	                                            // Register output parameters for error code and message
        	                                            insertManufactureStatement.registerOutParameter(4, Types.INTEGER);
        	                                            insertManufactureStatement.registerOutParameter(5, Types.NVARCHAR);

        	                                            System.out.println("Dispatching the stored procedure for Manufacture...");
        	                                            insertManufactureStatement.execute();

        	                                            // Retrieve error code and message from the stored procedure
        	                                            int errorCodeManufacture = insertManufactureStatement.getInt(4);
        	                                            String errorMessageManufacture = insertManufactureStatement.getString(5);

        	                                            if (errorCodeManufacture != 0) {
        	                                                // Error occurred, display error message
        	                                                System.out.println("Error Code: " + errorCodeManufacture);
        	                                                System.out.println("Error Message: " + errorMessageManufacture);
        	                                                System.out.println("Failed to add Manufacture details.");

        	                                                // Ask the user if they want to try entering assembly_id and process_id again
        	                                                System.out.println("Do you want to try entering assembly_id and process_id again? (yes/no):");
        	                                                String userInputTryAgain = sc.next().toLowerCase();

        	                                                if (userInputTryAgain.equals("no")) {
        	                                                    insertMoreManufacture = false; // Exit the loop
        	                                                }
        	                                            } else {
        	                                                System.out.println("Manufacture details added successfully.");

        	                                                // Ask the user if they want to insert more assembly_id and process_id pairs
        	                                                System.out.println("Do you want to insert more assembly_id and process_id pairs for this job? (yes/no):");
        	                                                String userInputMorePairs = sc.next().toLowerCase();

        	                                                if (userInputMorePairs.equals("no")) {
        	                                                    insertMoreManufacture = false; // Exit the loop
        	                                                }
        	                                            }
        	                                        } catch (SQLException e) {
        	                                            // Handle SQL exceptions
        	                                            System.out.println("Error: " + e.getMessage());
        	                                            System.out.println("Failed to add Manufacture details. Returning to the main menu.");
        	                                            insertMoreManufacture = false; // Exit the loop on error
        	                                        }
        	                                    }
        	                                }
        	                            }
        	                        } catch (SQLException e) {
        	                            // Handle SQL exceptions
        	                            System.out.println("Error: " + e.getMessage());
        	                            System.out.println("Failed to add Paint Job details. Returning to the main menu.");
        	                        }
        	                    }
        	                    break;


        	                case 3:
        	                    // User's choice for Cut Job
        	                    // Insert into Cut Job
        	                    try (Connection ffconnection = DriverManager.getConnection(URL)) {
        	                        try (CallableStatement cutJobStatement = ffconnection.prepareCall(INSERT_CUT)) {
        	                            cutJobStatement.setInt(1, jobNumber);

        	                            // Register output parameters for error code and message
        	                            cutJobStatement.registerOutParameter(2, Types.INTEGER);
        	                            cutJobStatement.registerOutParameter(3, Types.NVARCHAR);

        	                            System.out.println("Dispatching the stored procedure for Cut Job...");
        	                            cutJobStatement.execute();

        	                            // Retrieve error code and message from the stored procedure
        	                            int errorCodeCutJob = cutJobStatement.getInt(2);
        	                            String errorMessageCutJob = cutJobStatement.getString(3);

        	                            if (errorCodeCutJob != 0) {
        	                                // Error occurred, display error message
        	                                System.out.println("Error Code: " + errorCodeCutJob);
        	                                System.out.println("Error Message: " + errorMessageCutJob);
        	                                System.out.println("Failed to add Cut Job details. Returning to the main menu.");
        	                            } else {
        	                                System.out.println("Cut Job details added successfully.");

        	                                // Ask the user if they want to insert Manufacture details
        	                                System.out.println("Do you want to insert assembly_id and process_id pairs for this job? (yes/no):");
        	                                String userInputManufacture = sc.next().toLowerCase();

        	                                if (userInputManufacture.equals("yes")) {
        	                                    boolean insertMoreManufacture = true;

        	                                    while (insertMoreManufacture) {
        	                                        // Ask the user to enter the assembly_id and process_id pair
        	                                        System.out.println("Please enter assembly_id:");
        	                                        String fffassemblyId = sc.next();

        	                                        System.out.println("Please enter process_id:");
        	                                        String processId = sc.next();

        	                                        // Call the procedure to insert details into the Manufacture table
        	                                        try (CallableStatement insertManufactureStatement = connection.prepareCall(INSERT_MANUFACTURE_JOB)) {
        	                                            insertManufactureStatement.setInt(1, jobNumber);
        	                                            insertManufactureStatement.setString(2, fffassemblyId);
        	                                            insertManufactureStatement.setString(3, processId);

        	                                            // Register output parameters for error code and message
        	                                            insertManufactureStatement.registerOutParameter(4, Types.INTEGER);
        	                                            insertManufactureStatement.registerOutParameter(5, Types.NVARCHAR);

        	                                            System.out.println("Dispatching the stored procedure for Manufacture...");
        	                                            insertManufactureStatement.execute();

        	                                            // Retrieve error code and message from the stored procedure
        	                                            int errorCodeManufacture = insertManufactureStatement.getInt(4);
        	                                            String errorMessageManufacture = insertManufactureStatement.getString(5);

        	                                            if (errorCodeManufacture != 0) {
        	                                                // Error occurred, display error message
        	                                                System.out.println("Error Code: " + errorCodeManufacture);
        	                                                System.out.println("Error Message: " + errorMessageManufacture);
        	                                                System.out.println("Failed to add Manufacture details.");

        	                                                // Ask the user if they want to try entering assembly_id and process_id again
        	                                                System.out.println("Do you want to try entering assembly_id and process_id again? (yes/no):");
        	                                                String userInputTryAgain = sc.next().toLowerCase();

        	                                                if (userInputTryAgain.equals("no")) {
        	                                                    insertMoreManufacture = false; // Exit the loop
        	                                                }
        	                                            } else {
        	                                                System.out.println("Manufacture details added successfully.");

        	                                                // Ask the user if they want to insert more assembly_id and process_id pairs
        	                                                System.out.println("Do you want to insert more assembly_id and process_id pairs for this job? (yes/no):");
        	                                                String userInputMorePairs = sc.next().toLowerCase();

        	                                                if (userInputMorePairs.equals("no")) {
        	                                                    insertMoreManufacture = false; // Exit the loop
        	                                                }
        	                                            }
        	                                        } catch (SQLException e) {
        	                                            // Handle SQL exceptions
        	                                            System.out.println("Error: " + e.getMessage());
        	                                            System.out.println("Failed to add Manufacture details. Returning to the main menu.");
        	                                            insertMoreManufacture = false; // Exit the loop on error
        	                                        }
        	                                    }
        	                                }
        	                            }
        	                        } catch (SQLException e) {
        	                            // Handle SQL exceptions
        	                            System.out.println("Error: " + e.getMessage());
        	                            System.out.println("Failed to add Cut Job details. Returning to the main menu.");
        	                        }
        	                    }
        	                    break;



        	                            default:
        	                                System.out.println("Invalid job type choice. Returning to the main menu.");
        	                                break;
        	                        }
        	                    }
        	                }
        	            } catch (SQLException e) {
        	                // Handle SQL exceptions
        	                System.out.println("Error: " + e.getMessage());
        	                System.out.println("Failed to add job. Returning to the main menu.");
        	            }
        	            break;
         case "7":
        	    // Ask the user to enter the job number
        	    System.out.println("Please enter the job number:");
        	    int updateJobNumber = sc.nextInt();

        	    // Loop to handle errors and ask for the correct date of completion
        	    while (true) {
        	        // Check if the job number exists and determine the job type
        	        try (Connection checkConnection = DriverManager.getConnection(URL)) {
        	            try (CallableStatement checkStatement = checkConnection.prepareCall(CHECK_JOB_EXISTENCE)) {
        	                checkStatement.setInt(1, updateJobNumber);
        	                checkStatement.registerOutParameter(2, Types.INTEGER);

        	                // Execute the stored procedure to check job existence
        	                checkStatement.execute();

        	                // Retrieve the existsFlag from the stored procedure
        	                int existsFlag = checkStatement.getInt(2);

        	                if (existsFlag != 0) {
        	                    boolean isValidDate = false;
        	                    String dateCompleted = null;

        	                    while (!isValidDate) {
        	                        // Job exists, ask for date of completion
        	                        dateCompleted = getUserInputWithFormat("Please enter date the job completed (YYYY-MM-DD):", "yyyy-MM-dd");

        	                        // Update the completion date for the job
        	                        try (CallableStatement updateCompletionDateStatement = checkConnection.prepareCall(UPDATE_COMPLETION_DATE)) {
        	                            updateCompletionDateStatement.setInt(1, updateJobNumber);
        	                            updateCompletionDateStatement.setString(2, dateCompleted);
        	                            updateCompletionDateStatement.registerOutParameter(3, Types.INTEGER);
        	                            updateCompletionDateStatement.registerOutParameter(4, Types.NVARCHAR);

        	                            // Execute the stored procedure to update the completion date
        	                            updateCompletionDateStatement.execute();

        	                            // Retrieve error code and message from the stored procedure
        	                            int errorCodeUpdateCompletionDate = updateCompletionDateStatement.getInt(3);
        	                            String errorMessageUpdateCompletionDate = updateCompletionDateStatement.getString(4);

        	                            if (errorCodeUpdateCompletionDate != 0) {
        	                                // Error occurred, display error message
        	                                System.out.println("Error Code: " + errorCodeUpdateCompletionDate);
        	                                System.out.println("Error Message: " + errorMessageUpdateCompletionDate);
        	                                System.out.println("Failed to update completion date.");

        	                                // Prompt user for a correct date
        	                                isValidDate = false;
        	                            } else {
        	                                System.out.println("Completion date updated successfully.");
        	                                isValidDate = true; // Exit the loop as the update was successful
        	                            }
        	                        } catch (SQLException e) {
        	                            // Handle SQL exceptions
        	                            System.out.println("Error: " + e.getMessage());
        	                            System.out.println("Failed to update completion date.");
        	                            isValidDate = false; // Prompt user for a correct date
        	                        }
        	                    }

        	                    // Determine the type of job (Fit, Paint, or Cut)
        	                    String jobType;
        	                    try (CallableStatement determineTypeStatement = checkConnection.prepareCall(DETERMINE_JOB_TYPE)) {
        	                        determineTypeStatement.setInt(1, updateJobNumber);
        	                        determineTypeStatement.registerOutParameter(2, Types.NVARCHAR);

        	                        // Execute the stored procedure to determine job type
        	                        determineTypeStatement.execute();

        	                        // Retrieve the job type from the stored procedure
        	                        jobType = determineTypeStatement.getString(2);

        	                        // Additional logic based on job type
        	                        switch (jobType) {
        	                            case "Fit Job":
        	                                // Ask the user to enter the new fit_labor_time
        	                                System.out.println("This is a Fit Job:");
        	                                System.out.println("Please enter the new fit labor time (HH:mm:ss):");
        	                                String fitLaborTimeString = sc.next();
        	                                LocalTime fitLaborTime = LocalTime.parse(fitLaborTimeString);

        	                                // Call the stored procedure to update Fit Job labor time
        	                                try (CallableStatement updateFitJobStatement = checkConnection.prepareCall(UPDATE_FIT_JOB_LABOR_TIME)) {
        	                                    updateFitJobStatement.setInt(1, updateJobNumber);
        	                                    updateFitJobStatement.setTime(2, Time.valueOf(fitLaborTime));
        	                                    updateFitJobStatement.registerOutParameter(3, Types.INTEGER);
        	                                    updateFitJobStatement.registerOutParameter(4, Types.NVARCHAR);

        	                                    // Execute the stored procedure to update Fit Job labor time
        	                                    updateFitJobStatement.execute();

        	                                    // Retrieve error code and message from the stored procedure
        	                                    int errorCodeUpdateFitJob = updateFitJobStatement.getInt(3);
        	                                    String errorMessageUpdateFitJob = updateFitJobStatement.getString(4);

        	                                    if (errorCodeUpdateFitJob != 0) {
        	                                        // Error occurred, display error message
        	                                        System.out.println("Error Code: " + errorCodeUpdateFitJob);
        	                                        System.out.println("Error Message: " + errorMessageUpdateFitJob);
        	                                        System.out.println("Failed to update Fit Job labor time.");
        	                                    } else {
        	                                        System.out.println("Fit Job labor time updated successfully.");
        	                                    }
        	                                } catch (SQLException e) {
        	                                    // Handle SQL exceptions
        	                                    System.out.println("Error: " + e.getMessage());
        	                                    System.out.println("Failed to update Fit Job labor time.");
        	                                }
        	                                break;

        	                            case "Paint Job":
        	                                // Ask the user to enter the new details for Paint Job
        	                                System.out.println("This is a Paint Job:");
        	                                System.out.println("Please enter the new color:");
        	                                String paintColor = sc.next();

        	                                System.out.println("Please enter the new volume:");
        	                                double paintVolume = sc.nextDouble();

        	                                System.out.println("Please enter the new paint labor time (HH:mm:ss):");
        	                                String paintLaborTimeString = sc.next();
        	                                LocalTime paintLaborTime = LocalTime.parse(paintLaborTimeString);

        	                                // Call the stored procedure to update Paint Job details
        	                                try (CallableStatement updatePaintJobStatement = checkConnection.prepareCall(UPDATE_PAINT_JOB_DETAILS)) {
        	                                    updatePaintJobStatement.setInt(1, updateJobNumber);
        	                                    updatePaintJobStatement.setString(2, paintColor);
        	                                    updatePaintJobStatement.setDouble(3, paintVolume);
        	                                    updatePaintJobStatement.setTime(4, Time.valueOf(paintLaborTime));
        	                                    updatePaintJobStatement.registerOutParameter(5, Types.INTEGER);
        	                                    updatePaintJobStatement.registerOutParameter(6, Types.NVARCHAR);

        	                                    // Execute the stored procedure to update Paint Job details
        	                                    updatePaintJobStatement.execute();

        	                                    // Retrieve error code and message from the stored procedure
        	                                    int errorCodeUpdatePaintJob = updatePaintJobStatement.getInt(5);
        	                                    String errorMessageUpdatePaintJob = updatePaintJobStatement.getString(6);

        	                                    if (errorCodeUpdatePaintJob != 0) {
        	                                        // Error occurred, display error message
        	                                        System.out.println("Error Code: " + errorCodeUpdatePaintJob);
        	                                        System.out.println("Error Message: " + errorMessageUpdatePaintJob);
        	                                        System.out.println("Failed to update Paint Job details.");
        	                                    } else {
        	                                        System.out.println("Paint Job details updated successfully.");
        	                                    }
        	                                } catch (SQLException e) {
        	                                    // Handle SQL exceptions
        	                                    System.out.println("Error: " + e.getMessage());
        	                                    System.out.println("Failed to update Paint Job details.");
        	                                }
        	                                break;

        	                            case "Cut Job":
        	                                // Ask the user to enter the new details for Cut Job
        	                                System.out.println("This is a Cut Job:");
        	                                System.out.println("Please enter the new type of machine:");
        	                                String machineType = sc.next();

        	                                System.out.println("Please enter the new time of machine (HH:mm:ss):");
        	                                String machineTimeString = sc.next();
        	                                LocalTime machineTime = LocalTime.parse(machineTimeString);

        	                                System.out.println("Please enter the new material used:");
        	                                String machineUsed = sc.next();

        	                                System.out.println("Please enter the new cut labor time (HH:mm:ss):");
        	                                String cutLaborTimeString = sc.next();
        	                                LocalTime cutLaborTime = LocalTime.parse(cutLaborTimeString);

        	                                // Call the stored procedure to update Cut Job details
        	                                try (CallableStatement updateCutJobStatement = checkConnection.prepareCall(UPDATE_CUT_JOB_DETAILS)) {
        	                                    updateCutJobStatement.setInt(1, updateJobNumber);
        	                                    updateCutJobStatement.setString(2, machineType);
        	                                    updateCutJobStatement.setTime(3, Time.valueOf(machineTime));
        	                                    updateCutJobStatement.setString(4, machineUsed);
        	                                    updateCutJobStatement.setTime(5, Time.valueOf(cutLaborTime));
        	                                    updateCutJobStatement.registerOutParameter(6, Types.INTEGER);
        	                                    updateCutJobStatement.registerOutParameter(7, Types.NVARCHAR);

        	                                    // Execute the stored procedure to update Cut Job details
        	                                    updateCutJobStatement.execute();

        	                                    // Retrieve error code and message from the stored procedure
        	                                    int errorCodeUpdateCutJob = updateCutJobStatement.getInt(6);
        	                                    String errorMessageUpdateCutJob = updateCutJobStatement.getString(7);

        	                                    if (errorCodeUpdateCutJob != 0) {
        	                                        // Error occurred, display error message
        	                                        System.out.println("Error Code: " + errorCodeUpdateCutJob);
        	                                        System.out.println("Error Message: " + errorMessageUpdateCutJob);
        	                                        System.out.println("Failed to update Cut Job details.");
        	                                    } else {
        	                                        System.out.println("Cut Job details updated successfully.");
        	                                    }
        	                                } catch (SQLException e) {
        	                                    // Handle SQL exceptions
        	                                    System.out.println("Error: " + e.getMessage());
        	                                    System.out.println("Failed to update Cut Job details.");
        	                                }
        	                                break;

        	                            default:
        	                                System.out.println("Invalid job type. Returning to the main menu.");
        	                                break;
        	                        }
        	                    }

        	                    break; // Exit the loop as the update was successful
        	                } else {
        	                    // Error: Job does not exist
        	                    System.out.println("Error: Job with the specified job number does not exist. Returning to the main menu.");
        	                    break; // Exit the loop as the job does not exist
        	                }
        	            } catch (SQLException e) {
        	                // Handle SQL exceptions
        	                System.out.println("Error: " + e.getMessage());
        	                System.out.println("Failed to update job details. Returning to the main menu.");
        	                break; // Exit the loop in case of a SQL exception
        	            }
        	        }
        	    }
        	    break;
        	 // Prompt the user to enter transaction details for a specific case (case "8")
         case "8":
             System.out.println("Please enter transaction-no:");
             sc.nextLine();
             // read transaction-no
             int transaction_no = sc.nextInt();

             System.out.println("Please enter sup-cost:");
             // read sup-cost
             double sup_cost = sc.nextDouble();

             System.out.println("Do you want to enter assembly account? (yes/no):");
             String enterAssembly = sc.next().toLowerCase();
             int assembly_account_no = 0;

             // Check if the user wants to enter an assembly account
             if (enterAssembly.equals("yes")) {
                 System.out.println("Please enter assembly account no:");
                 assembly_account_no = sc.nextInt();
             }

             System.out.println("Do you want to enter department account? (yes/no):");
             String enterDepartment = sc.next().toLowerCase();
             int department_account_no = 0;

             // Check if the user wants to enter a department account
             if (enterDepartment.equals("yes")) {
                 System.out.println("Please enter department account no:");
                 department_account_no = sc.nextInt();
             }

             System.out.println("Do you want to enter process account? (yes/no):");
             String enterProcess = sc.next().toLowerCase();
             int process_account_no = 0;

             // Check if the user wants to enter a process account
             if (enterProcess.equals("yes")) {
                 System.out.println("Please enter process account no:");
                 process_account_no = sc.nextInt();
             }

             System.out.println("Connecting to the database...");

             int accountsUpdatedCount = 0;

             // Establish a database connection
             try (Connection connection = DriverManager.getConnection(URL)) {
                 try (CallableStatement statement = connection.prepareCall(ENTER_NEW_TRANSACTION)) {
                     // Set parameters for the transaction
                     statement.setInt(1, transaction_no);
                     statement.setDouble(2, sup_cost);

                     // Check and set assembly account if provided
                     if (assembly_account_no != 0) {
                         statement.setInt(3, assembly_account_no);
                         accountsUpdatedCount++;
                         System.out.println("Updating assembly account.");
                     } else {
                         statement.setNull(3, Types.INTEGER);
                     }

                     // Check and set department account if provided
                     if (department_account_no != 0) {
                         statement.setInt(4, department_account_no);
                         accountsUpdatedCount++;
                         System.out.println("Updating department account.");
                     } else {
                         statement.setNull(4, Types.INTEGER);
                     }

                     // Check and set process account if provided
                     if (process_account_no != 0) {
                         statement.setInt(5, process_account_no);
                         accountsUpdatedCount++;
                         System.out.println("Updating process account.");
                     } else {
                         statement.setNull(5, Types.INTEGER);
                     }

                     System.out.println("Dispatching the stored procedure for Transaction...");

                     // Execute the stored procedure for the transaction
                     boolean accountsUpdated = statement.execute();

                     // Display the number of accounts updated
                     if (accountsUpdatedCount > 0) {
                         System.out.println("Cost updated for " + accountsUpdatedCount + " account(s).");
                     } else {
                         System.out.println("No cost update.");
                     }
                 } catch (SQLException e) {
                     // Handle SQL exceptions
                     System.out.println("Error: " + e.getMessage());
                     System.out.println("Failed to update costs. Returning to the main menu.");
                 }
             } catch (SQLException e) {
                 System.out.println("Connection error: " + e.getMessage());
             }
             break;

        	            	    
        	    
          // Prompt the user to enter an assembly id to retrieve total cost (case "9")
         case "9":
             System.out.println("Enter an assembly id to retrieve total cost:");
             String assemblyIdi = sc.next();

             // Establish a database connection
             try (Connection connection = DriverManager.getConnection(URL)) {
                 // Retrieve total cost from the Cost table using a stored procedure
                 try (CallableStatement costStatement = connection.prepareCall(GET_TOTAL_COST)) {
                     costStatement.setString(1, assemblyIdi);
                     costStatement.registerOutParameter(2, Types.DOUBLE);
                     costStatement.registerOutParameter(3, Types.NVARCHAR);

                     // Execute the stored procedure to get total cost
                     costStatement.execute();

                     // Retrieve total cost and error message from the stored procedure
                     double totalCost = costStatement.getDouble(2);
                     String errorMessage = costStatement.getString(3);

                     // Display the total cost or an error message, if any
                     if (errorMessage != null) {
                         System.out.println("Error Message: " + errorMessage);
                     } else {
                         System.out.println("Total cost incurred on assembly_id " + assemblyIdi + ": " + totalCost);
                     }
                 } catch (SQLException e) {
                     // Handle SQL exceptions for the cost statement
                     int errorCode23 = 50025; // Custom error code
                     String errorMessage23 = e.getMessage();
                     System.out.println("Error Code: " + errorCode23);
                     System.out.println("Error Message: " + errorMessage23);
                     System.out.println("Failed to retrieve total cost. Returning to the main menu.");
                 }
             } catch (SQLException e) {
                 // Handle SQL exceptions for the database connection
                 System.out.println("Connection error: " + e.getMessage());
             }
             break;



          // Prompt the user to enter department number and date to retrieve total labor time (case "10")
         case "10":
             System.out.println("Enter department number:");
             String departmentNumberCase10 = sc.next();

             System.out.println("Enter the date (YYYY-MM-DD):");
             String givenDateCase10 = sc.next();

             // Establish a database connection
             try (Connection connection = DriverManager.getConnection(URL)) {
                 // Retrieve total labor time from Process, Manufacture, and Job tables using a stored procedure
                 try (CallableStatement totalLaborTimeStatement = connection.prepareCall(GET_TOTAL_LABOR_TIME_IN_DEPARTMENT)) {
                     totalLaborTimeStatement.setString(1, departmentNumberCase10);
                     totalLaborTimeStatement.setString(2, givenDateCase10);
                     totalLaborTimeStatement.registerOutParameter(3, Types.FLOAT);
                     totalLaborTimeStatement.registerOutParameter(4, Types.NVARCHAR);

                     // Execute the stored procedure to get total labor time
                     totalLaborTimeStatement.execute();

                     // Retrieve total labor time and error message from the stored procedure
                     float totalLaborTime = totalLaborTimeStatement.getFloat(3);
                     String errorMessageCase10 = totalLaborTimeStatement.getString(4);

                     // Display the total labor time or an error message, if any
                     if (errorMessageCase10 != null) {
                         System.out.println("Error Message: " + errorMessageCase10);
                     } else {
                         System.out.println("Total labor time in department " + departmentNumberCase10 +
                                 " for jobs completed on " + givenDateCase10 + ": " + totalLaborTime + " minutes");
                     }
                 } catch (SQLException e) {
                     // Handle SQL exceptions for the total labor time statement
                     int errorCodeCase10 = 50020; // Custom error code
                     String errorMessageCase10 = e.getMessage();
                     System.out.println("Error Code: " + errorCodeCase10);
                     System.out.println("Error Message: " + errorMessageCase10);
                     System.out.println("Failed to retrieve total labor time. Returning to the main menu.");
                 }
             } catch (SQLException e) {
                 // Handle SQL exceptions for the database connection
                 System.out.println("Connection error: " + e.getMessage());
             }
             break;


  // Case 11
          // Prompt the user to enter assembly_id to retrieve processes (case "11")
         case "11":
             System.out.println("Enter assembly_id to retrieve processes:");
             String asssemblyId = sc.next();

             // Establish a database connection
             try (Connection connection = DriverManager.getConnection(URL)) {
                 // Retrieve processes for the given assembly_id using a stored procedure
                 try (CallableStatement processesStatement = connection.prepareCall(GET_PROCESSES_FOR_ASSEMBLY)) {
                     processesStatement.setString(1, asssemblyId);
                     processesStatement.registerOutParameter(2, Types.NVARCHAR);
                     processesStatement.registerOutParameter(3, Types.NVARCHAR);

                     // Execute the stored procedure to get processes
                     processesStatement.execute();

                     // Retrieve process details and error message from the stored procedure
                     String processDetails = processesStatement.getString(2);
                     String errorMessageCase11 = processesStatement.getString(3);

                     // Display the processes or an error message, if any
                     if (errorMessageCase11 != null) {
                         System.out.println("Error Message: " + errorMessageCase11);
                     } else {
                         System.out.println("Processes for assembly_id " + asssemblyId + ":\n" + processDetails);
                     }
                 } catch (SQLException e) {
                     // Handle SQL exceptions for the processes statement
                     int errorCodeCase11 = 50020; // Custom error code
                     String errorMessageCase11 = e.getMessage();
                     System.out.println("Error Code: " + errorCodeCase11);
                     System.out.println("Error Message: " + errorMessageCase11);
                     System.out.println("Failed to retrieve processes. Returning to the main menu.");
                 }
             } catch (SQLException e) {
                 // Handle SQL exceptions for the database connection
                 System.out.println("Connection error: " + e.getMessage());
             }
             break;

     
  // Case 12
          // Prompt the user to enter the minimum and maximum category to retrieve customers (case "12")
         case "12":
             System.out.println("Enter the minimum category:");
             int minCategory = sc.nextInt();

             System.out.println("Enter the maximum category:");
             int maxCategory = sc.nextInt();

             // Establish a database connection
             try (Connection connection = DriverManager.getConnection(URL)) {
                 // Retrieve customers in the given category range using a stored procedure
                 try (CallableStatement customersStatement = connection.prepareCall(GET_CUSTOMERS_IN_RANGE)) {
                     customersStatement.setInt(1, minCategory);
                     customersStatement.setInt(2, maxCategory);
                     customersStatement.registerOutParameter(3, Types.NVARCHAR);
                     customersStatement.registerOutParameter(4, Types.NVARCHAR);

                     // Execute the stored procedure to get customers
                     customersStatement.execute();

                     // Retrieve customer details and error message from the stored procedure
                     String customerDetails = customersStatement.getString(3);
                     String errorMessageCase12 = customersStatement.getString(4);

                     // Display the customers or an error message, if any
                     if (errorMessageCase12 != null) {
                         System.out.println("Error Message: " + errorMessageCase12);
                     } else {
                         System.out.println("Customers in the category range " + minCategory + " to " + maxCategory + ":\n" + customerDetails);
                     }
                 } catch (SQLException e) {
                     // Handle SQL exceptions for the customers statement
                     int errorCodeCase12 = 50021; // Custom error code
                     String errorMessageCase12 = e.getMessage();
                     System.out.println("Error Code: " + errorCodeCase12);
                     System.out.println("Error Message: " + errorMessageCase12);
                     System.out.println("Failed to retrieve customers. Returning to the main menu.");
                 }
             } catch (SQLException e) {
                 // Handle SQL exceptions for the database connection
                 System.out.println("Connection error: " + e.getMessage());
             }
             break;

     
  // Case 13
          // Prompt the user to enter the range for job numbers to delete cut-jobs (case "13")
         case "13":
             System.out.println("Enter the range for job numbers (minJobNo and maxJobNo):");
             int minJobNo = sc.nextInt();
             int maxJobNo = sc.nextInt();

             // Establish a database connection
             try (Connection connection = DriverManager.getConnection(URL)) {
                 // Delete cut-jobs within the specified job number range using a stored procedure
                 try (CallableStatement deleteCutJobsStatement = connection.prepareCall(DELETE_CUT_JOBS_IN_RANGE)) {
                     deleteCutJobsStatement.setInt(1, minJobNo);
                     deleteCutJobsStatement.setInt(2, maxJobNo);
                     deleteCutJobsStatement.registerOutParameter(3, Types.NVARCHAR);

                     // Execute the stored procedure to delete cut-jobs
                     deleteCutJobsStatement.execute();

                     // Retrieve error message from the stored procedure
                     String errorMessageCase13 = deleteCutJobsStatement.getString(3);

                     // Display success message or an error message, if any
                     if (errorMessageCase13 != null) {
                         System.out.println("Error Message: " + errorMessageCase13);
                     } else {
                         System.out.println("Cut-jobs within the specified range have been deleted.");
                     }
                 } catch (SQLException e) {
                     // Handle SQL exceptions for delete cut-jobs statement
                     int errorCodeCase13 = 50023; // Custom error code
                     String errorMessageCase13 = e.getMessage();
                     System.out.println("Error Code: " + errorCodeCase13);
                     System.out.println("Error Message: " + errorMessageCase13);
                     System.out.println("Failed to delete cut-jobs. Returning to the main menu.");
                 }
             } catch (SQLException e) {
                 // Handle SQL exceptions for the database connection
                 System.out.println("Connection error: " + e.getMessage());
             }
             break;

  // Case 14
          // Prompt the user to enter job number and new color for a paint job (case "14")
         case "14":
             System.out.println("Enter job number for paint job:");
             int jobNumberw = sc.nextInt();

             System.out.println("Enter new color:");
             String newColor = sc.next();

             // Establish a database connection
             try (Connection connection = DriverManager.getConnection(URL)) {
                 // Change the color of the given paint job using a stored procedure
                 try (CallableStatement changeColorStatement = connection.prepareCall(CHANGE_PAINT_JOB_COLOR)) {
                     changeColorStatement.setInt(1, jobNumberw);
                     changeColorStatement.setString(2, newColor);
                     changeColorStatement.registerOutParameter(3, Types.NVARCHAR);

                     // Execute the stored procedure to change the color
                     changeColorStatement.execute();

                     // Retrieve error message from the stored procedure
                     String errorMessageCase14 = changeColorStatement.getString(3);

                     // Display success message or an error message, if any
                     if (errorMessageCase14 != null) {
                         System.out.println("Error Message: " + errorMessageCase14);
                     } else {
                         System.out.println("Color changed successfully for paint job " + jobNumberw);
                     }
                 } catch (SQLException e) {
                     // Handle SQL exceptions for changing color statement
                     int errorCodeCase14 = 50022; // Custom error code
                     String errorMessageCase14 = e.getMessage();
                     System.out.println("Error Code: " + errorCodeCase14);
                     System.out.println("Error Message: " + errorMessageCase14);
                     System.out.println("Failed to change color. Returning to the main menu.");
                 }
             } catch (SQLException e) {
                 // Handle SQL exceptions for the database connection
                 System.out.println("Connection error: " + e.getMessage());
             }
             break;

  // ...

          // Prompt the user to enter the input file name for adding customers (case "15")
         case "15":
             System.out.println("Please enter the input file name:");
             String inputFileName = sc.next();

             // Print current working directory for debugging
             System.out.println("Current Directory: " + System.getProperty("user.dir"));

             // Open the file and read customer data until the file is empty
             try (Scanner fileScanner = new Scanner(new File(inputFileName))) {
                 while (fileScanner.hasNext()) {
                     String cname15 = fileScanner.nextLine();
                     String caddress15 = fileScanner.nextLine();
                     int category15;

                     try {
                         // Attempt to parse category as an integer
                         category15 = Integer.parseInt(fileScanner.nextLine().trim());
                     } catch (NumberFormatException e) {
                         // Handle invalid input for category
                         System.out.println("Error Message: Invalid input for category. Please enter a valid integer.");
                         continue; // Move to the next iteration
                     }

                     // Establish a database connection
                     try (Connection connection = DriverManager.getConnection(URL)) {
                         // Add a new customer from the file using a stored procedure
                         try (CallableStatement statement = connection.prepareCall(ENTER_NEW_CUSTOMER)) {
                             statement.setString(1, cname15);
                             statement.setString(2, caddress15);
                             statement.setInt(3, category15);
                             statement.registerOutParameter(4, Types.INTEGER);
                             statement.registerOutParameter(5, Types.NVARCHAR);

                             // Execute the stored procedure to add the customer
                             statement.execute();

                             // Retrieve error code and message from the stored procedure
                             int errorCode = statement.getInt(4);
                             String errorMessage = statement.getString(5);

                             // Display success message or an error message, if any
                             if (errorCode != 0) {
                                 System.out.println("Error Code: " + errorCode);
                                 System.out.println("Error Message: " + errorMessage);
                                 System.out.println("Failed to add customer from file.");
                             } else {
                                 System.out.println("Customer added from file: " + cname15);
                             }
                         } catch (SQLException e) {
                             System.out.println("Error: " + e.getMessage());
                             System.out.println("Failed to add customer from file.");
                         }
                     } catch (SQLException e) {
                         System.out.println("Connection error: " + e.getMessage());
                     }
                 }
             } catch (FileNotFoundException e) {
                 System.out.println("File not found: " + inputFileName);
             }
             break;


  

// Case 16: Export customers to a data file based on category range
          // Prompt the user to enter the output file name and category range for exporting customers (case "16")
         case "16":
             System.out.println("Please enter the output file name:");
             String outputFileName = sc.next();
             System.out.println("Please enter the category range (e.g., 1-5):");
             String categoryRange = sc.next();

             // Parse the category range
             String[] range = categoryRange.split("-");
             int minCategoryy = Integer.parseInt(range[0]);
             int maxCategoryy = Integer.parseInt(range[1]);

             // Establish a database connection and export customers to a file
             try (Connection connection = DriverManager.getConnection(URL);
                  Statement statement = connection.createStatement();
                  ResultSet resultSet = statement.executeQuery("SELECT cname, caddress, category FROM Customer " +
                          "WHERE category BETWEEN " + minCategoryy + " AND " + maxCategoryy + " ORDER BY cname");
                  PrintWriter writer = new PrintWriter(outputFileName)) {

                 while (resultSet.next()) {
                     String cname16 = resultSet.getString("cname");
                     String caddress16 = resultSet.getString("caddress");
                     int category16 = resultSet.getInt("category");

                     // Write customer details to the output file
                     writer.println(cname16);
                     writer.println(caddress16);
                     writer.println(category16);

                     System.out.println("Exported customer: " + cname16);
                 }
                 System.out.println("Customers exported to file: " + outputFileName);
             } catch (SQLException | FileNotFoundException e) {
                 // Handle SQL and file-related exceptions
                 System.out.println("Error: " + e.getMessage());
                 System.out.println("Failed to export customers to file.");
             }
             break;



          // Handle case "17" - Do nothing, the while loop will terminate upon the next iteration
         case "17":
             System.out.println("Exiting!"); // Display exit message
             break;

         // Handle default case - Unrecognized option, re-prompt the user for the correct one
         default:
             System.out.println(String.format(
                 "Unrecognized option: %s\n" +
                 "Please try again!", option));
             break;

			}
		}
		sc.close(); // Close the scanner before exiting the application
	}
}