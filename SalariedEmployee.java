/**
 * CMSC 256 Spring 2018 Section 001
 * 
 * This class inherits from the Employee abstract class and implements the 
 * compute pay method. It is intended to store employees that are salaried
 * 
 * @author Rasha Abuhantash
 * @version 02/24/2018
 */
public class SalariedEmployee extends Employee
{

    /**
     * Constructs the salaried employee class
     * @param firstName of the salaried employee
     * @param lastName of the salaried employee
     * @param salary of the salaried employee
     */
    public SalariedEmployee(String firstName, String lastName, double salary)
    { 
        super(firstName, lastName, (salary /= 2080));
        
    }
    
    /**
     * Gets the annual salary of the salaried employee
     * @return the annual salary
     */
    public double getAnnualSalary()
    {
        return getWage() * 2080;
    }
    
    /**
     * Sets the annual salary of the salaried employee
     * @param salary the new salary that will be set
     */
    public void setSalary(int salary)
    {
        setWage(salary / 2080);
    }

    
    /**
     * Computes the weekly salary employee. If they work over 40 hours then the
     * salaried employee only gets paid for 40 hours
     * @param hours number of hours the employee worked
     * @return the salaried employees weekly salary.
     */
    @Override
    public double computePay(double hours)
    {

        double computePay = getWage() * 40; //1/52 of the pay;
        return computePay;
    }
    
    /**
     * Returns a formatted string of the salaried employees information which 
     * includes their last name, first name, and their annual salary
     * @return the formatted annual salary employee string
     */
    @Override
    public String toString()
    {
        double annualSalary = getAnnualSalary();
        String yearlyString = String.format("%.2f", annualSalary);
        yearlyString = "$" + yearlyString + "/year";
        String retString = super.getLastName() + ", " + super.getFirstName();
        for (int i = retString.length(); i < 40; i++)
        {
            if (yearlyString.length() + i == 40)
            {
                retString += yearlyString;
                break;
            }
            retString += " ";
        }

        return retString;
    }

}
