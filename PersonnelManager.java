
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * CMSC 256 Spring 2018 Section 001
 * 
 * This class parses the information from the EmployeesIn.dat, Updates.dat as
 * well as the WeeklyPayroll.txt files and uses the information to update
 * EmployeesOut.dat file. It prints out the updates information onto the console
 * Errors in parsing the said files are also displayed onto the console. It also
 * maintains an array of salaried and hourly Employees that have been added from
 * parsing the files.
 *
 * @author Rasha Abuhantash
 * @version 02/24/2018
 */
public class PersonnelManager
{

    private Employee[] employees;
    private static int numberOfEntries;
    private static final int MAX_CAPACITY = 50;
    public static final int DEFAULT_CAPACITY = 1;
    private Scanner scanner;
    private String employeesIn = "";
    private String employeesOut = "";
    private String weeklyPayroll;
    private String error = "";
    private boolean initialized = false;

    /**
     * This is the default constructor for Personnel Manager and gives the
     * Employees array a default capacity of 1.
     */
    public PersonnelManager()
    {
        this(DEFAULT_CAPACITY);
    }

    /**
     * This is the parameterized constructor that allows the user to insert
     * their own initial capacity for employees array
     *
     * @param capacity is the initial capacity for the Employees array
     */
    public PersonnelManager(int capacity)
    {
        if (capacity < MAX_CAPACITY)
        {
            employees = new Employee[capacity];
            numberOfEntries = 0;
            initialized = true;

        } else
        {
            throw new IllegalStateException("Attempt to create a bag "
                    + "whose capacity exceeds " + "allowed maximum.");
        }

    }

    /**
     * This method reads in the files and send them to their directed method if
     * the file name matches the conditional statement. If the file is empty the
     * console will prompt the user to insert a different file.
     *
     * @param fileName the name of the file that is being passed into the method
     */
    public void readFile(String fileName)
    {
        try
        {

            scanner = new Scanner(new FileReader(fileName));

            if (fileName.equals("EmployeesIn.dat"))
            {
                checkIfFileIsEmpty(fileName);
                employeesIn();
            }
            if (fileName.equals("Updates.dat"))
            {
                checkIfFileIsEmpty(fileName);
                update();
            }

            if (fileName.equals("HoursWorked.dat"))
            {
                checkIfFileIsEmpty(fileName);
                hoursWorked();
            }
        } catch (FileNotFoundException e)
        {
            System.out.println("File not found " + fileName);
        }

    }

    /**
     * This method checks if the file is empty. It will repeatedly ask for
     * another file until the user provides a file that is not empty
     *
     * @param fileName the name of the File that is bring passed into the method
     */
    private void checkIfFileIsEmpty(String fileName)
    {

        while (!scanner.hasNext())
        {
            Scanner scan = new Scanner(System.in);
            System.out.print("Please supply a different " + fileName + " file: ");
            String newfile = scan.next();
            try
            {

                scanner = new Scanner(new FileReader(newfile));
            } catch (FileNotFoundException ex)
            {
                System.out.println("File not found");
            }
        }
    }

    /**
     * This method parses the Updates.dat file. if the first command is N or n
     * then it adds the new Employee. If the first command is R or r then it
     * raises the existing employees wages. If the command is D or d then it
     * deletes the employee. If the command is unrecognized then it prints the
     * whole line the the unrecognized command.
     */
    public void update()
    {

        String firstName = "";
        String lastName = "";
        String command = "";
        String wageOfEmployee = "";
        double wage = 0;

        while (scanner.hasNext())
        {
            command = scanner.next();
            if (command.equals("n") || command.equals("N") || command.equals("r")
                    || command.equals("R") || command.equals("d") || command.equals("D"))
            {
                if (command.equals("n") || command.equals("N"))
                {

                    lastName = scanner.next();
                    firstName = scanner.next();
                    command = scanner.next();
                    wageOfEmployee = scanner.next();
                    try
                    {
                        wage = Double.parseDouble(wageOfEmployee);
                    } catch (NumberFormatException nfe)
                    {
                        error += "<n" + lastName + firstName + wageOfEmployee + ">\n\t\t\t    ";
                    }

                    if (command.equals("s") || command.equals("S") || command.equals("h") || command.equals("H"))
                    {
                        checkInitialization();

                        if (isArrayFull())
                        {
                            doubleCapacity();
                        }

                        if ((command.equals("s") || command.equals("S")) && wage >= 0)
                        {

                            lastName = lastName.substring(0, lastName.length() - 1);

                            employees[numberOfEntries] = new SalariedEmployee(firstName, lastName, wage);
                            if (getFrequencyOf(employees[numberOfEntries]) < 1)
                            {

                                employeesIn += "New Employee added   " + employees[numberOfEntries].getLastName()
                                        + ", " + employees[numberOfEntries].getFirstName() + "\n";

                            }
                            addEmployee(employees[numberOfEntries], command);

                        }

                        if ((command.equals("h") || command.equals("H")) && wage >= 0)
                        {

                            lastName = lastName.substring(0, lastName.length() - 1);

                            employees[numberOfEntries] = new HourlyEmployee(firstName, lastName, wage);
                            if (getFrequencyOf(employees[numberOfEntries]) < 1)
                            {

                                employeesIn += "New Employee added   " + employees[numberOfEntries].getLastName()
                                        + ", " + employees[numberOfEntries].getFirstName() + "\n";

                            }
                            addEmployee(employees[numberOfEntries], command);

                        }
                    } else
                    {
                        error += "<n" + lastName + firstName + command + wageOfEmployee + ">\n\t\t\t    "; //how do i access this
                    }
                }

                if (command.equals("r") || command.equals("R"))
                {
                    String percentRaise = scanner.next();
                    try
                    {

                        double wageRaise = Double.parseDouble(percentRaise);
                        employeesIn += ("New wages:\n");
                        for (int i = 0; i < numberOfEntries; i++)
                        {
                            employees[i].raiseWages(wageRaise);
                            employeesIn += ("\t" + employees[i].toString()) + "\n";
                        }
                    } catch (NumberFormatException nfe)
                    {
                        error += "<" + command + percentRaise + ">\n\t\t\t    ";
                    }

                }

                if (command.equals("d") || command.equals("D"))
                {

                    String nameOfEmployee = scanner.next();

                    if (!deleteEmployee(nameOfEmployee))
                    {
                        error += "<" + command + nameOfEmployee + " Employee not found>\n\t\t\t    ";

                    }
                }
            } else
            {

                error += "<" + command;
                int counter = 0;

                String infix = scanner.nextLine();
                infix = infix.replace("\t", "");
                infix = infix.replace(" ", "");
                error += infix;
                counter++;

                error += ">\n\t\t\t    ";

            }

        }

        printToFile("EmployeesOut.dat", employeesIn);
        //employeesOut();
    }


    /**
     * Parses through the EmployeesIn.dat file and stores the employees in an 
     * employees array. If they are a salaried worker the they are stored as
     * a salary employee and if they are an hourly worker than they are stored 
     * as an hourly employee. 
     */
    public void employeesIn()
    {
        String firstName;
        String lastName;
        String command;
        double wage = 0;

        //create if statement to check if array is larger than number of entries (use a method)
        while (scanner.hasNext())
        {

            lastName = scanner.next();

            firstName = scanner.next();

            command = scanner.next();

            String wageOfEmployee = scanner.next();

            checkInitialization();

            if (isArrayFull())
            {
                doubleCapacity();
            }
            if (command.equals("s") || command.equals("S")
                    || command.equals("h") || command.equals("H"))
            {
                if ((command.equals("s") || command.equals("S")))
                {
                    boolean incorrectWage = true;
                    try
                    {
                        wage = Double.parseDouble(wageOfEmployee);
                        if (wage <= 0)
                        {
                            error += "<" + lastName + firstName + command + wageOfEmployee + ">\n\t\t\t    ";
                            scanner.nextLine();

                        } else
                        {
                            incorrectWage = false;
                        }
                    } catch (NumberFormatException nfe)
                    {
                        error += "<" + lastName + firstName + command + wageOfEmployee + ">\n\t\t\t    ";
                        scanner.nextLine();
                    }
                    if (!incorrectWage)
                    {
                        lastName = lastName.substring(0, lastName.length() - 1);
                        employees[numberOfEntries] = new SalariedEmployee(firstName, lastName, wage);
                        addEmployee(employees[numberOfEntries], command);
                    }

                }
                if (command.equals("h") || command.equals("H"))
                {
                    boolean incorrectWage = true;
                    try
                    {
                        wage = Double.parseDouble(wageOfEmployee);
                        if (wage <= 0)
                        {
                            error += "<" + lastName + firstName + command + wageOfEmployee + ">\n\t\t\t    ";
                            scanner.nextLine();

                        } else
                        {
                            incorrectWage = false;
                        }
                    } catch (NumberFormatException nfe)
                    {
                        error += "<" + lastName + firstName + command + wageOfEmployee + ">\n\t\t\t    ";
                        scanner.nextLine();
                    }
                    if (!incorrectWage)
                    {
                        lastName = lastName.substring(0, lastName.length() - 1);
                        employees[numberOfEntries] = new HourlyEmployee(firstName, lastName, wage);
                        addEmployee(employees[numberOfEntries], command);
                    }

                }
            } else
            {

                error += "<" + lastName + firstName + command + wageOfEmployee + ">\n\t\t\t    ";

            }

        }

        
    }

    /**
     * This method takes in the hours that an employee works and then calculates
     * the salary that they earned for the week. It does this calculation by calling 
     * the computePay() method
     */
    public void hoursWorked()
    {
        double total = 0;
        //how do you want it to be formatted?
        //check to see if scanner is null
        weeklyPayroll = "Paycheck amount:\n";
        while (scanner.hasNext())
        {
            String nameOfEmployee = scanner.next(); //what do we do if we have megan and don draper

            for (int i = 0; i < numberOfEntries; i++)
            {

                if (employees[i].getFirstName().equals(nameOfEmployee)
                        || employees[i].getLastName().equals(nameOfEmployee))

                {
                    String workHours = scanner.next();
                    try
                    {
                        double hoursWorked = Double.parseDouble(workHours);
                        total += employees[i].computePay(hoursWorked);
                        String formatted = ("\t" + employees[i].getLastName() + ", "
                                + employees[i].getFirstName() + "  $"
                                + String.format("%.2f", employees[i].computePay(hoursWorked)) + "\n");

                        employees[i].computePay(hoursWorked);
                        weeklyPayroll += formatStringForPayRoll(formatted);
                    } catch (NumberFormatException nfe)
                    {
                        error += "<" + nameOfEmployee + workHours + ">\n\t\t\t    ";
                    }
                    //call delete method
                }
            }

        }

        weeklyPayroll += "\t\t\t\t     ---------\n";

        weeklyPayroll += formatStringForPayRoll("\tTotal $" + String.format("%.2f", total));

        printToFile("WeeklyPayroll.txt", weeklyPayroll);

    }

    /**
     * This method formats the string for the weekly payroll 
     * @param string the string that is formatted to display the weekly payroll
     * onto the console
     * @return a formatted string for the weekly payroll
     */
    private String formatStringForPayRoll(String string)
    {

        StringBuilder sb = new StringBuilder(string);
 
        for (int i = 0; i < sb.length(); i++)
        {
            if (sb.charAt(i) == '$')
            {
                if (sb.charAt(1) == 'T') // why do i need this code
                {
                    while (sb.length() < 39)
                    {
                        sb.insert(i - 1, ' ');
                    }
                } else
                {
                    while (sb.length() < 40)
                    {
                        sb.insert(i - 1, ' ');
                    }
                }

            }

        }
        String result = sb.toString();

        return result;

    }

    /**
     * This method takes in the the parsed information from a file and displays 
     * it onto the console. If the file does not exist then a file not found 
     * exception  will be thrown
     * @param fileName the name of the file being passed into the method
     * @param fileInfo the parsed information from the file
     */
    private void printToFile(String fileName, String fileInfo)
    {
        Scanner scan = new Scanner(fileInfo);

        PrintWriter printWriter;
        try
        {
            printWriter = new PrintWriter(new File(fileName));
            printWriter.print(fileInfo);
            System.out.println(fileInfo); //why is this not updating to the text file??
            printWriter.close();

            if (!error.equals(""))
            {
                System.out.println("Command was not recognized; " + this.error);
                error = "";
            }

        } catch (FileNotFoundException ex)
        {
            System.out.println("File not found");
        }

    }

    /**
     * Adds a new employee into the employees array. It also makes sure that the
     * employee that is added is not a duplicate
     * @param employee the employee that is going to be checked to see if there 
     * is already more than one of that employee
     * @param command checks to see if the employee is  hourly or salaried
     */
    public void addEmployee(Employee employee, String command)
    {

        if (command.equals("h") || command.equals("H") || command.equals("s") || command.equals("S"))
        {
            numberOfEntries++;
            getFrequencyOf(employees[numberOfEntries - 1]);
            
        }
    }

    /**
     * Returns true if the employee array is empty
     * @return true if there are 0 entries in the employee array
     */
    public boolean isEmpty()
    {
        return numberOfEntries == 0;
    }

    /**
     * Returns the current size of the employees array
     * @return the size of the employees array
     */
    public int getCurrentSize()
    {
        return numberOfEntries;
    }

    /**
     * Checks to see if an the employees array contains a particular employee
     * @param employee the employee that is searched for in the array
     * @return true if the index is greater than -1
     */
    public boolean contains(Employee employee)
    {
        return getIndexOf(employee) > -1;
    } // end contains

    /**
     * Deletes all employees into the array
     */
    public void clear()
    {
        checkInitialization();
        int index = 0;
        while (!isEmpty())
        {
            employees[index] = employees[numberOfEntries - 1];
            employees[numberOfEntries - 1] = null;
            numberOfEntries -= 1;
            index++;
            
        }

    }

    /**
     * Removes the last employee in the array
     * @return the last employee in the array
     */
    public Employee remove()
    {
        checkInitialization();
        Employee result = removeEntry(numberOfEntries - 1);
        return result;
    }

    /**
     * Removes a specific employee in the array
     * @param anEntry the employee that is to be removed
     * @return true if the employee that is deleted is the same employee that
     * the user wanted to delete
     */
    public boolean remove(Employee anEntry)
    {
        checkInitialization();
        int index = getIndexOf(anEntry);
        Employee result = removeEntry(index);
        employees[index] = employees[numberOfEntries - 1];
        return result.equals(anEntry);

    }

    /**
     * Removes an employee at a specific index
     * @param index the index that the user wants to remove
     * @return the removed employee
     */
    private Employee removeEntry(int index)
    {
        checkInitialization();
        Employee result = null;
        if (!isEmpty() && (index >= 0))
        {
            result = employees[index];
            employees[index] = employees[numberOfEntries - 1];
            employees[numberOfEntries - 1] = null;
            numberOfEntries--;

        } 
        return result;
    } 

    /**
     * This method passes in the name of an employee and if there is a match 
     * in the employees array with either the first name or the last name then
     * that employee is deleted from the array
     * @param nameOfEmployee the name of the employee that we want to delete
     * @return true if an employee was deleted
     */
    public boolean deleteEmployee(String nameOfEmployee)
    {

        for (int i = 0; i < numberOfEntries; i++)
        {

            if (employees[i].getFirstName().equals(nameOfEmployee) || employees[i].getLastName().equals(nameOfEmployee))

            {

                employeesIn = employeesIn.replace("\t" + employees[i].toString() + "\n", ""); //what to do
                employeesIn += ("Deleted Employee: " + employees[i].getLastName() + ", " + employees[i].getFirstName()) + "\n";

                removeEntry(i);
                return true;
                
            }
        }
        return false;
    }

    /**
     * Gets the frequency of an employee in an array. If the frequency is more
     * than one than that employee is deleted from the array
     * @param employee the employee that is searched for in the array
     * @return the number of frequency an employee appeared in the employees array
     */
    public int getFrequencyOf(Employee employee)
    {
        checkInitialization();
        int counter = 0;

        for (int index = 0; index < numberOfEntries; index++)
        {
            if (employees[index].getFirstName().equals(employee.getFirstName())
                    && employees[index].getLastName().equals(employee.getLastName()))
            {
                counter++;
                if (counter == 2)
                {
                    removeEntry(index);
                }

            } 
        } 
        return counter;
    } 

    /**
     * Checks to see if the employee array has been correctly initialized
     */
    private void checkInitialization()
    {
        if (!initialized)
        {
            throw new SecurityException("ArrayBag object is not initialized "
                    + "properly.");
        }
    }

    /**
     * Checks to see if the the array is full
     * @return true if the array is full
     */
    public boolean isArrayFull()
    {

        return numberOfEntries >= employees.length;
    }

    /**
     * Doubles the capcity of the array
     */
    public void doubleCapacity()
    {
        checkInitialization();
        int newLength = 2 * employees.length;
        checkCapacity(newLength);
        Employee[] temp = new Employee[newLength];
        for (int i = 0; i < employees.length; i++)
        {
            temp[i] = employees[i];
        }

        employees = new Employee[newLength];

        for (int i = 0; i < employees.length; i++)
        {
            employees[i] = temp[i];
        }

    }

    /**
     * Checks the capacity of the array, if the array is larger than the max
     * capacity than an illegal argument exception is thrown
     * @param capacity the capacity of the array
     */
    private void checkCapacity(int capacity)
    {

        if (capacity > MAX_CAPACITY)
        {
            throw new IllegalArgumentException("(\"Attempt to create a bag whose \" +\n"
                    + " // end checkCapacity\n"
                    + "\"capacity exeeds allowed \" + \"maximum of \" + MAX_CAPACITY)");
        }

    }

    /**
     * Gets the index of an employee in the employees array
     * @param anEntry the employee that we want to get the index of 
     * @return -1 if the employee does not exist
     */
    private int getIndexOf(Employee anEntry)
    {
        int where = -1;
        boolean found = false;
        int index = 0;
        while (!found && (index < numberOfEntries))
        {
            if (anEntry.equals(employees[index]))
            {
                found = true;
                where = index;
            }

            index++;
        }
        return where;
    }

    public Employee[] toArray()
    {

        Employee[] result = new Employee[numberOfEntries];

        for (int index = 0; index < numberOfEntries; index++)
        {
            result[index] = employees[index];

        }

        return result;

    }
}
//fork youuu-u-u-u-u-u-u-oooo