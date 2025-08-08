package vvu.centrauthz.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Error Record Tests")
class ErrorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Valid Error Creation Tests")
    class ValidErrorCreationTests {

        @Test
        @DisplayName("Should create Error with all fields using builder")
        void shouldCreateErrorWithAllFields() {
            // Given
            Map<String, String> details = Map.of("field", "value", "reason", "invalid");

            // When
            Error error = Error.builder()
                    .code("VALIDATION_ERROR")
                    .message("Validation failed")
                    .details(details)
                    .build();

            // Then
            assertNotNull(error);
            assertEquals("VALIDATION_ERROR", error.code());
            assertEquals("Validation failed", error.message());
            assertEquals(details, error.details());
            assertEquals(2, error.details().size());
        }

        @Test
        @DisplayName("Should create Error with required fields only")
        void shouldCreateErrorWithRequiredFieldsOnly() {
            // When
            Error error = Error.builder()
                    .code("NOT_FOUND")
                    .build();

            // Then
            assertNotNull(error);
            assertEquals("NOT_FOUND", error.code());
            assertNull(error.message());
            assertNull(error.details());
        }

        @Test
        @DisplayName("Should create Error with code and message")
        void shouldCreateErrorWithCodeAndMessage() {
            // When
            Error error = Error.builder()
                    .code("BAD_REQUEST")
                    .message("Invalid input provided")
                    .build();

            // Then
            assertNotNull(error);
            assertEquals("BAD_REQUEST", error.code());
            assertEquals("Invalid input provided", error.message());
            assertNull(error.details());
        }

        @Test
        @DisplayName("Should create Error with empty details map")
        void shouldCreateErrorWithEmptyDetails() {
            // When
            Error error = Error.builder()
                    .code("SERVER_ERROR")
                    .message("Internal server error")
                    .details(Map.of())
                    .build();

            // Then
            assertNotNull(error);
            assertEquals("SERVER_ERROR", error.code());
            assertEquals("Internal server error", error.message());
            assertNotNull(error.details());
            assertTrue(error.details().isEmpty());
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should pass validation with valid Error")
        void shouldPassValidationWithValidError() {
            // Given
            Error error = Error.builder()
                    .code("VALID_CODE")
                    .message("Valid message")
                    .build();

            // When
            Set<ConstraintViolation<Error>> violations = validator.validate(error);

            // Then
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should fail validation when code is null")
        void shouldFailValidationWhenCodeIsNull() {
            // Given
            Error error = Error.builder()
                    .code(null)
                    .message("Valid message")
                    .build();

            // When
            Set<ConstraintViolation<Error>> violations = validator.validate(error);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("code")));
        }

        @Test
        @DisplayName("Should fail validation when code is empty")
        void shouldFailValidationWhenCodeIsEmpty() {
            // Given
            Error error = Error.builder()
                    .code("")
                    .message("Valid message")
                    .build();

            // When
            Set<ConstraintViolation<Error>> violations = validator.validate(error);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("code")));
        }

        @Test
        @DisplayName("Should fail validation when code is blank")
        void shouldFailValidationWhenCodeIsBlank() {
            // Given
            Error error = Error.builder()
                    .code("   ")
                    .message("Valid message")
                    .build();

            // When
            Set<ConstraintViolation<Error>> violations = validator.validate(error);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("code")));
        }

        @Test
        @DisplayName("Should pass validation when message is null")
        void shouldPassValidationWhenMessageIsNull() {
            // Given
            Error error = Error.builder()
                    .code("VALID_CODE")
                    .message(null)
                    .build();

            // When
            Set<ConstraintViolation<Error>> violations = validator.validate(error);

            // Then
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderPatternTests {

        @Test
        @DisplayName("Should support toBuilder for modification")
        void shouldSupportToBuilderForModification() {
            // Given
            Error originalError = Error.builder()
                    .code("ORIGINAL")
                    .message("Original message")
                    .build();

            // When
            Error modifiedError = originalError.toBuilder()
                    .code("MODIFIED")
                    .build();

            // Then
            assertEquals("ORIGINAL", originalError.code());
            assertEquals("MODIFIED", modifiedError.code());
            assertEquals("Original message", modifiedError.message());
        }

        @Test
        @DisplayName("Should create new instance with toBuilder")
        void shouldCreateNewInstanceWithToBuilder() {
            // Given
            Error originalError = Error.builder()
                    .code("TEST")
                    .build();

            // When
            Error copiedError = originalError.toBuilder().build();

            // Then
            assertNotSame(originalError, copiedError);
            assertEquals(originalError.code(), copiedError.code());
            assertEquals(originalError.message(), copiedError.message());
            assertEquals(originalError.details(), copiedError.details());
        }
    }

    @Nested
    @DisplayName("Record Behavior Tests")
    class RecordBehaviorTests {

        @Test
        @DisplayName("Should implement equals correctly")
        void shouldImplementEqualsCorrectly() {
            // Given
            Error error1 = Error.builder()
                    .code("TEST")
                    .message("Test message")
                    .details(Map.of("key", "value"))
                    .build();

            Error error2 = Error.builder()
                    .code("TEST")
                    .message("Test message")
                    .details(Map.of("key", "value"))
                    .build();

            // Then
            assertEquals(error1, error2);
        }

        @Test
        @DisplayName("Should implement hashCode correctly")
        void shouldImplementHashCodeCorrectly() {
            // Given
            Error error1 = Error.builder()
                    .code("TEST")
                    .message("Test message")
                    .build();

            Error error2 = Error.builder()
                    .code("TEST")
                    .message("Test message")
                    .build();

            // Then
            assertEquals(error1.hashCode(), error2.hashCode());
        }

        @Test
        @DisplayName("Should implement toString correctly")
        void shouldImplementToStringCorrectly() {
            // Given
            Error error = Error.builder()
                    .code("TEST_CODE")
                    .message("Test message")
                    .build();

            // When
            String toString = error.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("TEST_CODE"));
            assertTrue(toString.contains("Test message"));
        }
    }
}
