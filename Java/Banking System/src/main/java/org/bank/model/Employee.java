package org.bank.model;

/**
 * Represents an employee within the banking system.
 * An employee has a salary and a specific role within the organization.
 */
public class Employee extends User {
    private int salary;
    private String role;

    /**
     * Constructs an Employee instance with specified details.
     *
     * @param id          the unique identifier of the employee
     * @param firstName   the first name of the employee
     * @param lastName    the last name of the employee
     * @param email       the email address of the employee
     * @param phoneNumber the phone number of the employee
     * @param password    the hashed password for employee authentication
     * @param salary      the salary of the employee
     * @param role        the job role or title of the employee
     */
    public Employee(int id, String firstName, String lastName, String email, String phoneNumber, String password, int salary, String role) {
        super(id, firstName, lastName, email, phoneNumber, password);
        this.salary = salary;
        this.role = role;
    }

    /**
     * Retrieves the salary of the employee.
     *
     * @return the salary of the employee
     */
    public int getSalary() {
        return salary;
    }

    /**
     * Sets the salary of the employee.
     *
     * @param salary the salary to be set for the employee
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * Retrieves the role of the employee within the organization.
     *
     * @return the role of the employee
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the employee within the organization.
     *
     * @param role the job role or title to be set for the employee
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns a string representation of the employee, including personal details, salary, and role.
     *
     * @return a string representation of the employee
     */
    @Override
    public String toString() {
        return String.format("Employee | %s | salary=%d | role='%s'",
                super.toString(), salary, role);
    }

}
