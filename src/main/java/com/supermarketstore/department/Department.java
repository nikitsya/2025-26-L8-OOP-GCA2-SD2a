package com.supermarketstore.department;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

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
    private String fileName;
    private String contentType;
    private int fileSize;
    private byte[] departmentImage;

    /**
     * Creates an empty department instance for Jackson deserialisation.
     */
    public Department() {
    }

    /**
     * Creates a department without attached image metadata.
     *
     * @param departmentId   the department identifier
     * @param name           the department name
     * @param floor          the floor where the department is located
     * @param zone           the department zone on the floor
     * @param budget         the allocated department budget
     * @param employeeCount  the number of employees assigned to the department
     * @param isRefrigerated whether the department requires refrigeration
     * @throws IllegalArgumentException when any core department value is invalid
     */
    public Department(int departmentId, String name, int floor, int zone, double budget, int employeeCount, boolean isRefrigerated) {
        setDepartmentId(departmentId);
        setName(name);
        setFloor(floor);
        setZone(zone);
        setBudget(budget);
        setEmployeeCount(employeeCount);
        setRefrigerated(isRefrigerated);
        setFileName("");
        setContentType("");
        setFileSize(0);
        setDepartmentImage(null);
    }

    /**
     * Creates a department with optional attached image metadata.
     *
     * @param departmentId    the department identifier
     * @param name            the department name
     * @param floor           the floor where the department is located
     * @param zone            the department zone on the floor
     * @param budget          the allocated department budget
     * @param employeeCount   the number of employees assigned to the department
     * @param isRefrigerated  whether the department requires refrigeration
     * @param fileName        the attached image file name when image data is present
     * @param contentType     the attached image content type when image data is present
     * @param fileSize        the attached image size in bytes when image data is present
     * @param departmentImage the optional attached image bytes
     * @throws IllegalArgumentException when any core department value is invalid
     */
    public Department(int departmentId, String name, int floor, int zone, double budget, int employeeCount, boolean isRefrigerated, String fileName, String contentType, int fileSize, byte[] departmentImage) {
        this(departmentId, name, floor, zone, budget, employeeCount, isRefrigerated);
        setFileName(fileName);
        setContentType(contentType);
        setFileSize(fileSize);
        setDepartmentImage(departmentImage);
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

    @JsonProperty("file_name")
    public String getFileName() {
        return fileName;
    }

    @JsonProperty("file_name")
    public void setFileName(String f) {
        this.fileName = (f == null) ? "" : f.trim();
    }

    @JsonProperty("content_type")
    public String getContentType() {
        return contentType;
    }

    @JsonProperty("content_type")
    public void setContentType(String ct) {
        this.contentType = (ct == null) ? "" : ct.trim();
    }

    @JsonProperty("file_size")
    public int getFileSize() {
        return fileSize;
    }

    @JsonProperty("file_size")
    public void setFileSize(int size) {
        this.fileSize = Math.max(0, size);
    }

    @JsonProperty("department_image")
    public byte[] getDepartmentImage() {
        return departmentImage;
    }

    @JsonProperty("department_image")
    public void setDepartmentImage(byte[] data) {
        this.departmentImage = data;
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
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", fileSize=" + fileSize +
                ", departmentImage=" + (departmentImage != null ? departmentImage.length : 0) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Department department = (Department) o;
        return departmentId == department.departmentId
                && floor == department.floor
                && zone == department.zone
                && Double.compare(budget, department.budget) == 0
                && employeeCount == department.employeeCount
                && isRefrigerated == department.isRefrigerated
                && Objects.equals(name, department.name)
                && Objects.equals(fileName, department.fileName)
                && Objects.equals(contentType, department.contentType)
                && fileSize == department.fileSize
                && Arrays.equals(departmentImage, department.departmentImage);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(departmentId, name, floor, zone, budget, employeeCount, isRefrigerated, fileName, contentType, fileSize);
        result = 31 * result + java.util.Arrays.hashCode(departmentImage);
        return result;
    }
}
