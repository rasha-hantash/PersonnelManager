/**
 * CMSC 256 Spring 2018 Section 001
 * 
 * This class inherits from the Employee abstract class and implements the 
 * compute pay method. It is intended to store employees that are hourly
 * 
 * @author Rasha Abuhantash
 * @version 2/24/2018
 */
public class HourlyEmployee extends Employee
{

    /**
     * Constructs the hourly employee class
     * @param firstName of the hourly employee
     * @param lastName of the hourly employee
     * @param hourlyWage of the hourly employee
     */
    public HourlyEmployee(String firstName, String lastName, double hourlyWage)
    {
        super(firstName, lastName, hourlyWage);

    }

    
    /**
     * Returns a formatted string of the hourly employees information which 
     * includes their last name, first name, and their hourly wage
     * @return the formatted hourly employee string
     */
    @Override
    public String toString()
    {
        double hourlyWage = super.getWage();
        String hourlyWageString = String.format("%.2f", hourlyWage);

        hourlyWageString = "$" + hourlyWageString + "/hour";

        String retString = getLastName() + ", " + getFirstName();

        for (int i = retString.length(); i < 40; i++)
        {
            if (hourlyWageString.length() + i == 40)
            {
                retString += hourlyWageString;
                break;
            }
            retString += " ";
        }

        return retString;
    }

    
    /**
     * Computes the weekly salary employee. If they work over 40 hours then they
     * get paid a time and a half
     * @param hours number of hours the employee worked
     * @return the hourly employees weekly salary.
     */
    @Override
    public double computePay(double hours)
    {
        double hourlyWage = super.getWage();

        //pull this information form the HoursWorked.dat file
        if (hours > 40)
        {
            double overTime = (hours - 40) * (hourlyWage * 1.5);
            double regularHours = 40 * hourlyWage;

            return overTime + regularHours; //look at jones case supposed to return 1220.63
        }

        return hours * hourlyWage;
    }

}
