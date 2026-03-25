package com.supermarketstore.protocol;

/**
 * Enumerates the supported JSON protocol request types.
 *
 * @author Nikita Smiichyk
 */
public enum RequestType {
    GET_ALL_DEPARTMENTS,
    GET_DEPARTMENT_BY_ID,
    ADD_DEPARTMENT,
    GET_ALL_PRODUCTS,
    GET_PRODUCT_BY_ID,
    ADD_PRODUCT
}