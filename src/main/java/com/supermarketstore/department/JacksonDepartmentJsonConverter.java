package com.supermarketstore.department;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JacksonDepartmentJsonConverter implements DepartmentJsonConverter {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Override
    public String departmentToJson(Department entity) {
        return null;
    }

    @Override
    public Department departmentFromJson(String json) {
        return null;
    }

    @Override
    public String departmentListToJson(List<Department> list) {
        return null;
    }
}