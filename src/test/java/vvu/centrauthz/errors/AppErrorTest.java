package vvu.centrauthz.errors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import vvu.centrauthz.models.Error;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AppError Tests")
class AppErrorTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create AppError with Error object")
        void shouldCreateAppErrorWithErrorObject() {
            // Given
            Error error = Error.builder()
                    .code("TEST_CODE")
                    .message("Test message")
                    .details(Map.of("field", "value"))
                    .build();

            // When
            AppError appError = new AppError(error);

            // Then
            assertNotNull(appError);
            assertEquals(error, appError.getError());
            assertEquals("Test message", appError.getMessage());
            assertEquals("TEST_CODE", appError.getError().code());
            assertEquals("Test message", appError.getError().message());
            assertEquals(Map.of("field", "value"), appError.getError().details());
        }

        @Test
        @DisplayName("Should create AppError with Error object having null message")
        void shouldCreateAppErrorWithErrorObjectHavingNullMessage() {
            // Given
            Error error = Error.builder()
                    .code("TEST_CODE")
                    .message(null)
                    .build();

            // When
            AppError appError = new AppError(error);

            // Then
            assertNotNull(appError);
            assertEquals(error, appError.getError());
            assertNull(appError.getMessage());
            assertEquals("TEST_CODE", appError.getError().code());
            assertNull(appError.getError().message());
        }

        @Test
        @DisplayName("Should create AppError with code only")
        void shouldCreateAppErrorWithCodeOnly() {
            // Given
            String code = "SIMPLE_ERROR";

            // When
            AppError appError = new AppError(code);

            // Then
            assertNotNull(appError);
            assertNotNull(appError.getError());
            assertEquals(code, appError.getMessage());
            assertEquals(code, appError.getError().code());
            assertNull(appError.getError().message());
            assertNull(appError.getError().details());
        }

        @Test
        @DisplayName("Should create AppError with code and message")
        void shouldCreateAppErrorWithCodeAndMessage() {
            // Given
            String code = "VALIDATION_ERROR";
            String message = "Validation failed for input";

            // When
            AppError appError = new AppError(code, message);

            // Then
            assertNotNull(appError);
            assertNotNull(appError.getError());
            assertEquals(message, appError.getMessage());
            assertEquals(code, appError.getError().code());
            assertEquals(message, appError.getError().message());
            assertNull(appError.getError().details());
        }

        @Test
        @DisplayName("Should create AppError with code and throwable")
        void shouldCreateAppErrorWithCodeAndThrowable() {
            // Given
            String code = "INTERNAL_ERROR";
            RuntimeException cause = new RuntimeException("Database connection failed");

            // When
            AppError appError = new AppError(code, cause);

            // Then
            assertNotNull(appError);
            assertNotNull(appError.getError());
            assertEquals(cause, appError.getCause());
            assertEquals(code, appError.getError().code());
            assertEquals("Database connection failed", appError.getError().message());
            assertNull(appError.getError().details());
        }

        @Test
        @DisplayName("Should handle null throwable message")
        void shouldHandleNullThrowableMessage() {
            // Given
            String code = "NULL_CAUSE_ERROR";
            RuntimeException cause = new RuntimeException((String) null);

            // When
            AppError appError = new AppError(code, cause);

            // Then
            assertNotNull(appError);
            assertEquals(cause, appError.getCause());
            assertEquals(code, appError.getError().code());
            assertNull(appError.getError().message());
        }
    }

    @Nested
    @DisplayName("Behavior Tests")
    class BehaviorTests {

        @Test
        @DisplayName("Should implement toString correctly")
        void shouldImplementToStringCorrectly() {
            // Given
            Error error = Error.builder()
                    .code("TO_STRING_TEST")
                    .message("Test message")
                    .build();
            AppError appError = new AppError(error);

            // When
            String toString = appError.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.startsWith("{ error="));
            assertTrue(toString.contains("TO_STRING_TEST"));
            assertTrue(toString.contains("Test message"));
        }

        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            // Given
            AppError appError = new AppError("TEST_CODE");

            // Then
            assertInstanceOf(RuntimeException.class, appError);
            assertInstanceOf(Exception.class, appError);
            assertInstanceOf(Throwable.class, appError);
        }

        @Test
        @DisplayName("Should maintain immutable error reference")
        void shouldMaintainImmutableErrorReference() {
            // Given
            Error originalError = Error.builder()
                    .code("IMMUTABLE_TEST")
                    .message("Original message")
                    .build();
            AppError appError = new AppError(originalError);

            // When
            Error retrievedError1 = appError.getError();
            Error retrievedError2 = appError.getError();

            // Then
            assertSame(retrievedError1, retrievedError2);
            assertEquals(originalError, retrievedError1);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty string code")
        void shouldHandleEmptyStringCode() {
            // Given
            String emptyCode = "";

            // When
            AppError appError = new AppError(emptyCode);

            // Then
            assertNotNull(appError);
            assertEquals(emptyCode, appError.getError().code());
            assertEquals(emptyCode, appError.getMessage());
        }

        @Test
        @DisplayName("Should handle empty string message")
        void shouldHandleEmptyStringMessage() {
            // Given
            String code = "EMPTY_MESSAGE_TEST";
            String emptyMessage = "";

            // When
            AppError appError = new AppError(code, emptyMessage);

            // Then
            assertNotNull(appError);
            assertEquals(code, appError.getError().code());
            assertEquals(emptyMessage, appError.getError().message());
            assertEquals(emptyMessage, appError.getMessage());
        }

        @Test
        @DisplayName("Should handle whitespace-only strings")
        void shouldHandleWhitespaceOnlyStrings() {
            // Given
            String code = "   ";
            String message = "\t\n\r ";

            // When
            AppError appError = new AppError(code, message);

            // Then
            assertNotNull(appError);
            assertEquals(code, appError.getError().code());
            assertEquals(message, appError.getError().message());
        }

        @Test
        @DisplayName("Should handle very long strings")
        void shouldHandleVeryLongStrings() {
            // Given
            String longCode = "CODE_" + "X".repeat(1000);
            String longMessage = "MESSAGE_" + "Y".repeat(5000);

            // When
            AppError appError = new AppError(longCode, longMessage);

            // Then
            assertNotNull(appError);
            assertEquals(longCode, appError.getError().code());
            assertEquals(longMessage, appError.getError().message());
            assertTrue(appError.toString().contains(longCode));
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work correctly in try-catch blocks")
        void shouldWorkCorrectlyInTryCatchBlocks() {
            // Given
            AppError appError = new AppError("CATCH_TEST", "Test exception");

            // When & Then
            assertThrows(AppError.class, () -> {
                throw appError;
            });

            try {
                throw appError;
            } catch (AppError caught) {
                assertEquals("CATCH_TEST", caught.getError().code());
                assertEquals("Test exception", caught.getError().message());
            }
        }

        @Test
        @DisplayName("Should support exception chaining")
        void shouldSupportExceptionChaining() {
            // Given
            RuntimeException rootCause = new RuntimeException("Root cause");
            AppError appError = new AppError("CHAIN_TEST", rootCause);

            // Then
            assertEquals(rootCause, appError.getCause());
            assertEquals("CHAIN_TEST", appError.getError().code());
            assertEquals("Root cause", appError.getError().message());
        }

        @Test
        @DisplayName("Should maintain stack trace")
        void shouldMaintainStackTrace() {
            // Given
            AppError appError = new AppError("STACK_TEST");

            // When
            StackTraceElement[] stackTrace = appError.getStackTrace();

            // Then
            assertNotNull(stackTrace);
            assertTrue(stackTrace.length > 0);
            assertEquals("shouldMaintainStackTrace", stackTrace[0].getMethodName());
        }
    }
}
