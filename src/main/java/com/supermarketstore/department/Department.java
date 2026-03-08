package com.supermarketstore.department;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a department in the supermarket.
 * Contains information about its location, budget, staffing, and refrigeration requirements.
 *
 * @author Hanna Bokariuk
 */

public class Department {
    private int departmentId;
    private String name;
    private int floor;
    private int zone;
    private double budget;
    private int employeeCount;
    private boolean isRefrigerated;

    public Department() {
    }

    public Department(int departmentId, String name, int floor, int zone, double budget, int employeeCount, boolean isRefrigerated) {
        setDepartmentId(departmentId);
        setName(name);
        setFloor(floor);
        setZone(zone);
        setBudget(budget);
        setEmployeeCount(employeeCount);
        setRefrigerated(isRefrigerated);
    }


    @JsonProperty("department_id")
    public int getDepartmentId() {
        return departmentId;
    }

    @JsonProperty("department_id")
    public void setDepartmentId(int departmentId) {
        if (departmentId < 0) {
            throw new IllegalArgumentException("departmentId cannot be negative");
        }
        this.departmentId = departmentId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Department name must not be null or blank");
        }
        this.name = name.trim();
    }

    @JsonProperty("floor")
    public int getFloor() {
        return floor;
    }

    @JsonProperty("floor")
    public void setFloor(int floor) {
        if (floor < 0) {
            throw new IllegalArgumentException("Floor cannot be negative");
        }
        this.floor = floor;
    }

    @JsonProperty("zone")
    public int getZone() {
        return zone;
    }

    @JsonProperty("zone")
    public void setZone(int zone) {
        if (zone <= 0) {
            throw new IllegalArgumentException("Zone must be greater than 0");
        }
        this.zone = zone;
    }

    @JsonProperty("budget")
    public double getBudget() {
        return budget;
    }

    @JsonProperty("budget")
    public void setBudget(double budget) {
        if (budget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }
        this.budget = budget;
    }

    @JsonProperty("employee_count")
    public int getEmployeeCount() {
        return employeeCount;
    }

    @JsonProperty("employee_count")
    public void setEmployeeCount(int employeeCount) {
        if (employeeCount < 0) {
            throw new IllegalArgumentException("Employee count cannot be negative");
        }
        this.employeeCount = employeeCount;
    }

    @JsonProperty("is_refrigerated")
    public boolean isRefrigerated() {
        return isRefrigerated;
    }

    @JsonProperty("is_refrigerated")
    public void setRefrigerated(boolean refrigerated) {
        this.isRefrigerated = refrigerated;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", name='" + name + '\'' +
                ", floor=" + floor +
                ", zone=" + zone +
                ", budget=" + budget +
                ", employeeCount=" + employeeCount +
                ", isRefrigerated=" + isRefrigerated +
                '}';
    }

}
