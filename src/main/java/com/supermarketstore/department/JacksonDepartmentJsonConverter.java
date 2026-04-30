package com.supermarketstore.department;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Converts Department objects to and from JSON using Jackson.
 */
public class JacksonDepartmentJsonConverter implements DepartmentJsonConverter {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Override
    public String departmentToJson(Department entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Department must not be null");
        }

        try {
            return JSON_MAPPER.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize department to JSON", e);
        }
    }


    @Override
    public Department departmentFromJson(String json) {
        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException("JSON must not be null or blank");
        }

        try {
            return JSON_MAPPER.readValue(json, Department.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to deserialize department from JSON", e);
        }
    }

    @Override
    public String departmentListToJson(List<Department> list) {
        if (list == null) {
            throw new IllegalArgumentException("Department list must not be null");
        }

        try {
            return JSON_MAPPER.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize department list to JSON", e);
        }
    }

    @Override
    public List<Department> departmentListFromJson(String json) {
        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException("JSON must not be null or blank");
        }

        try {
            return JSON_MAPPER.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to deserialize department list from JSON", e);
        }
    }
}
