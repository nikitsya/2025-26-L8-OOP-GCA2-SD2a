package com.supermarketstore;

import com.supermarketstore.department.Department;
import com.supermarketstore.department.JdbcDepartmentDao;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        JdbcDepartmentDao dao =
                new JdbcDepartmentDao(
                        "jdbc:mysql://localhost:3306/supermarket_store_system",
                        "root",
                        ""
                );

        List<Department> departments = dao.getAllDepartments();

        for (Department d : departments) {
            System.out.println(d);
        }
    }
}