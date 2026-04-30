package com.supermarketstore.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Product}.
 *
 * @author Nikita Smechik
 */
class ProductTest {
    private Product product;

    private static Product productWithImage() {
        return new Product(1, "Product", 20.0, false, null, 45, new byte[]{1, 2, 3}, "product.jpeg", "image/jpeg", 3);
    }

    @BeforeEach
    void setUp() {
        product = new Product(1, "Product", 20.0, false, null, 45, null, null, null, 0);
    }

    // --- Constructor behaviour ---
    @Test
    void defaultConstructor_initialisesEmptyProductForJackson() {
        Product emptyProduct = new Product();

        assertAll(
                () -> assertEquals(0, emptyProduct.getProductId()),
                () -> assertNull(emptyProduct.getName()),
                () -> assertEquals(0.0, emptyProduct.getPrice()),
                () -> assertFalse(emptyProduct.isOnSale()),
                () -> assertNull(emptyProduct.getDiscountPrice()),
                () -> assertEquals(0, emptyProduct.getStock()),
                () -> assertNull(emptyProduct.getProductImage()),
                () -> assertNull(emptyProduct.getFileName()),
                () -> assertNull(emptyProduct.getContentType()),
                () -> assertEquals(0, emptyProduct.getFileSize())
        );
    }

    @Test
    void constructor_withValidSaleProductAndImage_setsAllFields() {
        byte[] image = {1, 2, 3};
        Product saleProduct = new Product(
                2,
                "Sale Product",
                30.0,
                true,
                20.0,
                10,
                image,
                "sale-product.jpeg",
                "image/jpeg",
                image.length
        );

        assertAll(
                () -> assertEquals(2, saleProduct.getProductId()),
                () -> assertEquals("Sale Product", saleProduct.getName()),
                () -> assertEquals(30.0, saleProduct.getPrice()),
                () -> assertTrue(saleProduct.isOnSale()),
                () -> assertEquals(20.0, saleProduct.getDiscountPrice()),
                () -> assertEquals(10, saleProduct.getStock()),
                () -> assertArrayEquals(image, saleProduct.getProductImage()),
                () -> assertEquals("sale-product.jpeg", saleProduct.getFileName()),
                () -> assertEquals("image/jpeg", saleProduct.getContentType()),
                () -> assertEquals(3, saleProduct.getFileSize())
        );
    }

    // --- Product identity and name validation ---

    @Test
    void setProductId_withNegativeValue_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> product.setProductId(-1));
        assertEquals("productId cannot be negative", exception.getMessage());
    }

    @Test
    void setName_withValidValue_trimsAndUpdatesName() {
        product.setName("   New Product ");
        assertEquals("New Product", product.getName());
    }

    @Test
    void setName_withBlankOrNullValue_throwsIllegalArgumentException() {
        assertAll(
                () -> assertEquals(
                        "Product name must not be null or blank",
                        assertThrows(IllegalArgumentException.class, () -> product.setName("")).getMessage()
                ),
                () -> assertEquals(
                        "Product name must not be null or blank",
                        assertThrows(IllegalArgumentException.class, () -> product.setName(" ")).getMessage()
                ),
                () -> assertEquals(
                        "Product name must not be null or blank",
                        assertThrows(IllegalArgumentException.class, () -> product.setName(null)).getMessage()
                )
        );
    }

    // --- Price and sale status validation ---

    @Test
    void setPrice_withZeroOrNegativeValue_throwsIllegalArgumentException() {
        assertAll(
                () -> assertEquals(
                        "Product price must be greater than 0",
                        assertThrows(IllegalArgumentException.class, () -> product.setPrice(0)).getMessage()
                ),
                () -> assertEquals(
                        "Product price must be greater than 0",
                        assertThrows(IllegalArgumentException.class, () -> product.setPrice(-1)).getMessage()
                )
        );
    }

    @Test
    void setPrice_whenOnSaleAndDiscountRemainsValid_updatesPrice() {
        product.setOnSale(true);
        product.setDiscountPrice(10.0);
        product.setPrice(20.0);

        assertEquals(20.0, product.getPrice());
    }

    @Test
    void setPrice_whenOnSaleAndDiscountWouldBeInvalid_throwsIllegalArgumentException() {
        product.setOnSale(true);
        product.setDiscountPrice(15.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> product.setPrice(10.0));

        assertEquals("Discount price must be less than product price", exception.getMessage());
    }

    @Test
    void setOnSale_withFalse_clearsDiscountPrice() {
        product.setOnSale(true);
        product.setDiscountPrice(15.0);
        product.setOnSale(false);
        assertAll(
                () -> assertFalse(product.isOnSale()),
                () -> assertNull(product.getDiscountPrice())
        );
    }

    // --- Discount price validation ---

    @Test
    void setDiscountPrice_whenProductIsNotOnSale_throwsIllegalStateException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                product.setDiscountPrice(15.0)
        );

        assertEquals("Cannot set discount price when product is not on sale", exception.getMessage());
    }

    @Test
    void setDiscountPrice_whenOnSaleAndNull_throwsIllegalArgumentException() {
        product.setOnSale(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                product.setDiscountPrice(null)
        );

        assertEquals("Discount price is required when product is on sale", exception.getMessage());
    }

    @Test
    void setDiscountPrice_withNegativeValue_throwsIllegalArgumentException() {
        product.setOnSale(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                product.setDiscountPrice(-1.0)
        );

        assertEquals("Discount price must be 0 or greater", exception.getMessage());
    }

    @Test
    void setDiscountPrice_whenPriceIsNotSet_throwsIllegalStateException() {
        Product emptyProduct = new Product();
        emptyProduct.setOnSale(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                emptyProduct.setDiscountPrice(1.0)
        );

        assertEquals("Price must be set before discount price", exception.getMessage());
    }

    @Test
    void setDiscountPrice_whenGreaterThanOrEqualToPrice_throwsIllegalArgumentException() {
        product.setOnSale(true);

        assertAll(
                () -> assertEquals(
                        "Discount price must be less than product price",
                        assertThrows(IllegalArgumentException.class, () -> product.setDiscountPrice(20.0)).getMessage()
                ),
                () -> assertEquals(
                        "Discount price must be less than product price",
                        assertThrows(IllegalArgumentException.class, () -> product.setDiscountPrice(25.0)).getMessage()
                )
        );
    }

    @Test
    void constructor_whenProductIsNotOnSaleAndDiscountPriceProvided_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Product(1, "Product", 20.0, false, 15.0, 45, null, null, null, 0)
        );

        assertEquals("Discount price must be null when product is not on sale", exception.getMessage());
    }

    @Test
    void constructor_whenProductIsOnSaleWithoutDiscountPrice_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Product(1, "Product", 20.0, true, null, 45, null, null, null, 0)
        );

        assertEquals("Discount price is required when product is on sale", exception.getMessage());
    }

    @Test
    void constructor_whenProductIsOnSaleAndDiscountPriceIsTooHigh_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Product(1, "Product", 20.0, true, 20.0, 45, null, null, null, 0)
        );

        assertEquals("Discount price must be less than product price", exception.getMessage());
    }

    // --- Stock validation ---

    @Test
    void setStock_withNegativeValue_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> product.setStock(-1));
        assertEquals("Stock cannot be negative", exception.getMessage());
    }

    // --- Image data and file metadata validation ---
    @Test
    void setProductImage_withBytes_usesDefensiveCopies() {
        byte[] image = {1, 2, 3};

        product.setProductImage(image);
        image[0] = 99;

        byte[] returnedImage = product.getProductImage();
        returnedImage[1] = 88;

        assertArrayEquals(new byte[]{1, 2, 3}, product.getProductImage());
    }

    @Test
    void setProductImage_withNull_clearsFileMetadata() {
        Product productWithImage = productWithImage();

        productWithImage.setProductImage(null);

        assertAll(
                () -> assertNull(productWithImage.getProductImage()),
                () -> assertNull(productWithImage.getFileName()),
                () -> assertNull(productWithImage.getContentType()),
                () -> assertEquals(0, productWithImage.getFileSize())
        );
    }

    @Test
    void setFileName_whenImageIsPresentAndNameIsBlankOrNull_throwsIllegalArgumentException() {
        Product productWithImage = productWithImage();

        assertAll(
                () -> assertEquals(
                        "File name must not be null or blank",
                        assertThrows(IllegalArgumentException.class, () -> productWithImage.setFileName(null)).getMessage()
                ),
                () -> assertEquals(
                        "File name must not be null or blank",
                        assertThrows(IllegalArgumentException.class, () -> productWithImage.setFileName(" ")).getMessage()
                )
        );
    }

    @Test
    void setContentType_whenImageIsPresentAndContentTypeIsNull_throwsIllegalArgumentException() {
        Product productWithImage = productWithImage();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productWithImage.setContentType(null)
        );

        assertEquals("Content type must not be null", exception.getMessage());
    }

    // --- File size validation ---

    @Test
    void setFileSize_withNegativeValue_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                product.setFileSize(-1)
        );

        assertEquals("File size cannot be negative", exception.getMessage());
    }

    @Test
    void constructor_whenImageIsPresentAndFileNameIsMissing_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Product(1, "Product", 20.0, false, null, 45, new byte[]{1}, null, "image/jpeg", 1)
        );

        assertEquals("File name must not be null or blank", exception.getMessage());
    }

    @Test
    void constructor_whenImageIsPresentAndContentTypeIsMissing_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Product(1, "Product", 20.0, false, null, 45, new byte[]{1}, "product.jpeg", null, 1)
        );

        assertEquals("Content type must not be null", exception.getMessage());
    }

    // --- Equality and text representation ---
    @Test
    void equals_whenFieldsMatch_returnsTrueAndHashCodesMatch() {
        Product first = productWithImage();
        Product second = productWithImage();

        assertAll(
                () -> assertEquals(first, second),
                () -> assertEquals(first.hashCode(), second.hashCode())
        );
    }

    @Test
    void equals_whenImageContentDiffers_returnsFalse() {
        Product first = productWithImage();
        Product second = new Product(1, "Product", 20.0, false, null, 45, new byte[]{9, 8, 7}, "product.jpeg", "image/jpeg", 3);

        assertNotEquals(first, second);
    }

    @Test
    void equals_withNullOrDifferentType_returnsFalse() {
        assertAll(
                () -> assertNotEquals(null, product),
                () -> assertNotEquals("Product", product)
        );
    }
}
