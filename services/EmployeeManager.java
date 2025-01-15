package services;

import models.Employee;

public class EmployeeManager {
    private Employee currentEmployee;

    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }
}
