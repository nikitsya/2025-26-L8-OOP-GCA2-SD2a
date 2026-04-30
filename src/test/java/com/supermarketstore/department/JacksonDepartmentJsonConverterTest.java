package com.supermarketstore.department;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link JacksonDepartmentJsonConverter}.
 */
class JacksonDepartmentJsonConverterTest {

    private final JacksonDepartmentJsonConverter converter = new JacksonDepartmentJsonConverter();

    private final Department department = new Department(1, "Bakery", 0, 2, 12000.0, 5, false, "bakery.jpg", "image/jpeg", 3, new byte[]{1, 2, 3});
    private final List<Department> departments = List.of(
            department,
            new Department(2, "Frozen Foods", 1, 5, 20000.0, 7, true, "frozen-foods.jpg", "image/jpeg", 4, new byte[]{4, 5, 6, 7})
    );

    private final String departmentJson =
            "{\"department_id\":1,\"name\":\"Bakery\",\"floor\":0,\"zone\":2,\"budget\":12000.0,\"employee_count\":5,\"file_name\":\"bakery.jpg\",\"content_type\":\"image/jpeg\",\"file_size\":3,\"department_image\":\"AQID\",\"is_refrigerated\":false}";
    private final String departmentsJson =
            "[" +
                    departmentJson +
                    ",{\"department_id\":2,\"name\":\"Frozen Foods\",\"floor\":1,\"zone\":5,\"budget\":20000.0,\"employee_count\":7,\"file_name\":\"frozen-foods.jpg\",\"content_type\":\"image/jpeg\",\"file_size\":4,\"department_image\":\"BAUGBw==\",\"is_refrigerated\":true}" +
                    "]";

    @Test
    void departmentToJson_returnsExpectedJson() {
        assertEquals(departmentJson, converter.departmentToJson(department));
    }

    @Test
    void departmentFromJson_returnsExpectedDepartment() {
        assertEquals(department, converter.departmentFromJson(departmentJson));
    }

    @Test
    void departmentListToJson_returnsExpectedJsonArray() {
        assertEquals(departmentsJson, converter.departmentListToJson(departments));
    }

    @Test
    void departmentListFromJson_returnsExpectedDepartments() {
        assertEquals(departments, converter.departmentListFromJson(departmentsJson));
    }

    @Test
    void departmentListFromJson_whenJsonIsBlank_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> converter.departmentListFromJson(" ")
        );

        assertEquals("JSON must not be null or blank", exception.getMessage());
    }

    @Test
    void departmentListFromJson_whenJsonIsEmptyArray_returnsEmptyList() {
        assertTrue(converter.departmentListFromJson("[]").isEmpty());
    }

    @Test
    void departmentListFromJson_whenJsonIsMalformed_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> converter.departmentListFromJson("[{bad json}]")
        );

        assertEquals("Failed to deserialize department list from JSON", exception.getMessage());
    }
}
