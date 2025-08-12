package vvu.centrauthz.errors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import vvu.centrauthz.models.Error;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BadRequestError Tests")
class BadRequestErrorTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create BadRequestError with custom code and message")
        void shouldCreateBadRequestErrorWithCustomCodeAndMessage() {
            // Given
            String code = "VALIDATION_FAILED";
            String message = "Required field is missing";

            // When
            BadRequestError error = new BadRequestError(code, message);

            // Then
            assertNotNull(error);
            assertInstanceOf(AppError.class, error);
            assertEquals(code, error.getError().code());
            assertEquals(message, error.getError().message());
            assertEquals(message, error.getMessage());
        }

        @Test
        @DisplayName("Should create BadRequestError with default code")
        void shouldCreateBadRequestErrorWithDefaultCode() {
            // Given
            String message = "Invalid request format";

            // When
            BadRequestError error = new BadRequestError(message);

            // Then
            assertNotNull(error);
            assertEquals("BAD_REQUEST", error.getError().code());
            assertEquals(message, error.getError().message());
            assertEquals(message, error.getMessage());
        }

        @Test
        @DisplayName("Should create BadRequestError with Error object")
        void shouldCreateBadRequestErrorWithErrorObject() {
            // Given
            Error errorObj = Error.builder()
                    .code("CUSTOM_BAD_REQUEST")
                    .message("Custom bad request message")
                    .details(Map.of("field", "username", "issue", "invalid format"))
                    .build();

            // When
            BadRequestError error = new BadRequestError(errorObj);

            // Then
            assertNotNull(error);
            assertEquals(errorObj, error.getError());
            assertEquals("Custom bad request message", error.getMessage());
            assertEquals("CUSTOM_BAD_REQUEST", error.getError().code());
            assertEquals(Map.of("field", "username", "issue", "invalid format"), error.getError().details());
        }

        @Test
        @DisplayName("Should create BadRequestError with no parameters")
        void shouldCreateBadRequestErrorWithNoParameters() {
            // When
            BadRequestError error = new BadRequestError();

            // Then
            assertNotNull(error);
            assertEquals("BAD_REQUEST", error.getError().code());
            assertEquals("BAD_REQUEST", error.getMessage());
            assertNull(error.getError().message());
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should inherit from AppError")
        void shouldInheritFromAppError() {
            // Given
            BadRequestError error = new BadRequestError("Test message");

            // Then
            assertInstanceOf(AppError.class, error);
            assertInstanceOf(RuntimeException.class, error);
            assertInstanceOf(Exception.class, error);
            assertInstanceOf(Throwable.class, error);
        }

        @Test
        @DisplayName("Should support polymorphic behavior")
        void shouldSupportPolymorphicBehavior() {
            // Given
            AppError appError = new BadRequestError("Polymorphic test");

            // Then
            assertInstanceOf(BadRequestError.class, appError);
            assertEquals("BAD_REQUEST", appError.getError().code());
            assertEquals("Polymorphic test", appError.getMessage());
        }
    }

    @Nested
    @DisplayName("Behavior Tests")
    class BehaviorTests {

        @Test
        @DisplayName("Should maintain proper toString format")
        void shouldMaintainProperToStringFormat() {
            // Given
            BadRequestError error = new BadRequestError("INVALID_INPUT", "Input validation failed");

            // When
            String toString = error.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.startsWith("{ error="));
            assertTrue(toString.contains("INVALID_INPUT"));
            assertTrue(toString.contains("Input validation failed"));
        }

        @Test
        @DisplayName("Should work in exception handling")
        void shouldWorkInExceptionHandling() {
            // Given
            BadRequestError error = new BadRequestError("Test bad request");

            // When & Then
            assertThrows(BadRequestError.class, () -> {
                throw error;
            });

            try {
                throw error;
            } catch (BadRequestError caught) {
                assertEquals("BAD_REQUEST", caught.getError().code());
                assertEquals("Test bad request", caught.getError().message());
            } catch (AppError caught) {
                fail("Should catch BadRequestError specifically");
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null message")
        void shouldHandleNullMessage() {
            // When
            BadRequestError error = new BadRequestError((String) null);

            // Then
            assertNotNull(error);
            assertEquals("BAD_REQUEST", error.getError().code());
            assertNull(error.getError().message());
            assertNull(error.getMessage());
        }

        @Test
        @DisplayName("Should handle empty string message")
        void shouldHandleEmptyStringMessage() {
            // Given
            String emptyMessage = "";

            // When
            BadRequestError error = new BadRequestError(emptyMessage);

            // Then
            assertNotNull(error);
            assertEquals("BAD_REQUEST", error.getError().code());
            assertEquals(emptyMessage, error.getError().message());
            assertEquals(emptyMessage, error.getMessage());
        }

        @Test
        @DisplayName("Should handle null Error object")
        void shouldHandleNullErrorObject() {
            // When & Then
            assertThrows(NullPointerException.class, () -> {
                new BadRequestError((Error) null);
            });
        }

        @Test
        @DisplayName("Should handle special characters in message")
        void shouldHandleSpecialCharactersInMessage() {
            // Given
            String specialMessage = "Bad request: 中文测试 🚫 @#$%^&*()";

            // When
            BadRequestError error = new BadRequestError(specialMessage);

            // Then
            assertNotNull(error);
            assertEquals("BAD_REQUEST", error.getError().code());
            assertEquals(specialMessage, error.getError().message());
            assertTrue(error.toString().contains(specialMessage));
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should be appropriate for HTTP 400 scenarios")
        void shouldBeAppropriateForHttp400Scenarios() {
            // Given - typical bad request scenarios
            BadRequestError missingField = new BadRequestError("Missing required field");
            BadRequestError invalidFormat = new BadRequestError("INVALID_FORMAT", "Email format is invalid");
            BadRequestError outOfRange = new BadRequestError("VALUE_OUT_OF_RANGE", "Age must be between 0 and 150");

            // Then
            assertEquals("BAD_REQUEST", missingField.getError().code());
            assertEquals("INVALID_FORMAT", invalidFormat.getError().code());
            assertEquals("VALUE_OUT_OF_RANGE", outOfRange.getError().code());

            // All should be catchable as BadRequestError
            assertInstanceOf(BadRequestError.class, missingField);
            assertInstanceOf(BadRequestError.class, invalidFormat);
            assertInstanceOf(BadRequestError.class, outOfRange);
        }

        @Test
        @DisplayName("Should support detailed error information")
        void shouldSupportDetailedErrorInformation() {
            // Given
            Error detailedError = Error.builder()
                    .code("VALIDATION_ERROR")
                    .message("Multiple validation failures")
                    .details(Map.of(
                        "field1", "required",
                        "field2", "invalid format",
                        "field3", "too long"
                    ))
                    .build();

            // When
            BadRequestError error = new BadRequestError(detailedError);

            // Then
            assertEquals(3, error.getError().details().size());
            assertTrue(error.getError().details().containsKey("field1"));
            assertTrue(error.getError().details().containsKey("field2"));
            assertTrue(error.getError().details().containsKey("field3"));
        }
    }
}
