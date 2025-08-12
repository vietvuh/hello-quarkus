package vvu.centrauthz.errors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConflictError Tests")
class ConflictErrorTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create ConflictError with custom code and message")
        void shouldCreateConflictErrorWithCustomCodeAndMessage() {
            // Given
            String code = "RESOURCE_EXISTS";
            String message = "Resource already exists with this identifier";

            // When
            ConflictError error = new ConflictError(code, message);

            // Then
            assertNotNull(error);
            assertInstanceOf(AppError.class, error);
            assertEquals(code, error.getError().code());
            assertEquals(message, error.getError().message());
            assertEquals(message, error.getMessage());
        }

        @Test
        @DisplayName("Should create ConflictError with default code")
        void shouldCreateConflictErrorWithDefaultCode() {
            // Given
            String message = "Duplicate entry detected";

            // When
            ConflictError error = new ConflictError(message);

            // Then
            assertNotNull(error);
            assertEquals("CONFLICT", error.getError().code());
            assertEquals(message, error.getError().message());
            assertEquals(message, error.getMessage());
        }

        @Test
        @DisplayName("Should create ConflictError with no parameters")
        void shouldCreateConflictErrorWithNoParameters() {
            // When
            ConflictError error = new ConflictError();

            // Then
            assertNotNull(error);
            assertEquals("CONFLICT", error.getError().code());
            assertEquals("CONFLICT", error.getMessage());
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
            ConflictError error = new ConflictError("Test conflict message");

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
            AppError appError = new ConflictError("Polymorphic conflict test");

            // Then
            assertInstanceOf(ConflictError.class, appError);
            assertEquals("CONFLICT", appError.getError().code());
            assertEquals("Polymorphic conflict test", appError.getMessage());
        }

        @Test
        @DisplayName("Should be distinguishable from other error types")
        void shouldBeDistinguishableFromOtherErrorTypes() {
            // Given
            ConflictError conflictError = new ConflictError("Conflict message");
            BadRequestError badRequestError = new BadRequestError("Bad request message");

            // Then
            assertNotEquals(conflictError.getClass(), badRequestError.getClass());
            assertInstanceOf(ConflictError.class, conflictError);
            assertInstanceOf(BadRequestError.class, badRequestError);
            //assertFalse(badRequestError instanceof ConflictError);
            //assertFalse(conflictError instanceof BadRequestError);
        }
    }

    @Nested
    @DisplayName("Behavior Tests")
    class BehaviorTests {

        @Test
        @DisplayName("Should maintain proper toString format")
        void shouldMaintainProperToStringFormat() {
            // Given
            ConflictError error = new ConflictError("DUPLICATE_KEY", "Primary key constraint violation");

            // When
            String toString = error.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.startsWith("{ error="));
            assertTrue(toString.contains("DUPLICATE_KEY"));
            assertTrue(toString.contains("Primary key constraint violation"));
        }

        @Test
        @DisplayName("Should work in exception handling")
        void shouldWorkInExceptionHandling() {
            // Given
            ConflictError error = new ConflictError("Test conflict");

            // When & Then
            assertThrows(ConflictError.class, () -> {
                throw error;
            });

            try {
                throw error;
            } catch (ConflictError caught) {
                assertEquals("CONFLICT", caught.getError().code());
                assertEquals("Test conflict", caught.getError().message());
            } catch (AppError caught) {
                fail("Should catch ConflictError specifically");
            }
        }

        @Test
        @DisplayName("Should support exception chaining")
        void shouldSupportExceptionChaining() {
            // Given
            ConflictError error = new ConflictError("Conflict with details");

            // When
            ConflictError chainedError = new ConflictError("CHAINED_CONFLICT", "Chained conflict message");
            chainedError.initCause(error);

            // Then
            assertEquals(error, chainedError.getCause());
            assertEquals("CHAINED_CONFLICT", chainedError.getError().code());
            assertEquals("Chained conflict message", chainedError.getError().message());
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null message")
        void shouldHandleNullMessage() {
            // When
            ConflictError error = new ConflictError((String) null);

            // Then
            assertNotNull(error);
            assertEquals("CONFLICT", error.getError().code());
            assertNull(error.getError().message());
            assertNull(error.getMessage());
        }

        @Test
        @DisplayName("Should handle empty string message")
        void shouldHandleEmptyStringMessage() {
            // Given
            String emptyMessage = "";

            // When
            ConflictError error = new ConflictError(emptyMessage);

            // Then
            assertNotNull(error);
            assertEquals("CONFLICT", error.getError().code());
            assertEquals(emptyMessage, error.getError().message());
            assertEquals(emptyMessage, error.getMessage());
        }

        @Test
        @DisplayName("Should handle null custom code")
        void shouldHandleNullCustomCode() {
            // Given
            String message = "Valid message";

            // When
            ConflictError error = new ConflictError(null, message);

            // Then
            assertNotNull(error);
            assertNull(error.getError().code());
            assertEquals(message, error.getError().message());
        }

        @Test
        @DisplayName("Should handle whitespace-only strings")
        void shouldHandleWhitespaceOnlyStrings() {
            // Given
            String code = "   ";
            String message = "\t\n\r ";

            // When
            ConflictError error = new ConflictError(code, message);

            // Then
            assertNotNull(error);
            assertEquals(code, error.getError().code());
            assertEquals(message, error.getError().message());
        }

        @Test
        @DisplayName("Should handle very long strings")
        void shouldHandleVeryLongStrings() {
            // Given
            String longCode = "CONFLICT_" + "X".repeat(500);
            String longMessage = "A very long conflict message: " + "Y".repeat(2000);

            // When
            ConflictError error = new ConflictError(longCode, longMessage);

            // Then
            assertNotNull(error);
            assertEquals(longCode, error.getError().code());
            assertEquals(longMessage, error.getError().message());
            assertTrue(error.toString().contains(longCode.substring(0, 50))); // At least part of it
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should be appropriate for HTTP 409 scenarios")
        void shouldBeAppropriateForHttp409Scenarios() {
            // Given - typical conflict scenarios
            ConflictError duplicateUser = new ConflictError("Username already exists");
            ConflictError concurrentModification = new ConflictError("CONCURRENT_MODIFICATION", "Resource was modified by another user");
            ConflictError resourceExists = new ConflictError("RESOURCE_EXISTS", "Application with this key already exists");

            // Then
            assertEquals("CONFLICT", duplicateUser.getError().code());
            assertEquals("CONCURRENT_MODIFICATION", concurrentModification.getError().code());
            assertEquals("RESOURCE_EXISTS", resourceExists.getError().code());

            // All should be catchable as ConflictError
            assertInstanceOf(ConflictError.class, duplicateUser);
            assertInstanceOf(ConflictError.class, concurrentModification);
            assertInstanceOf(ConflictError.class, resourceExists);
        }

        @Test
        @DisplayName("Should support different conflict types")
        void shouldSupportDifferentConflictTypes() {
            // Given - different types of conflicts
            ConflictError[] conflicts = {
                new ConflictError("UNIQUE_CONSTRAINT", "Email address already in use"),
                new ConflictError("VERSION_CONFLICT", "Entity version mismatch"),
                new ConflictError("STATE_CONFLICT", "Operation not allowed in current state"),
                new ConflictError("RESOURCE_LOCKED", "Resource is currently locked by another process")
            };

            // Then
            for (ConflictError conflict : conflicts) {
                assertNotNull(conflict);
                assertNotNull(conflict.getError().code());
                assertNotNull(conflict.getError().message());
                assertInstanceOf(ConflictError.class, conflict);
            }
        }

        @Test
        @DisplayName("Should support business rule violations")
        void shouldSupportBusinessRuleViolations() {
            // Given
            ConflictError businessRuleViolation = new ConflictError(
                "BUSINESS_RULE_VIOLATION", 
                "Cannot delete application that has active dependencies"
            );

            // Then
            assertEquals("BUSINESS_RULE_VIOLATION", businessRuleViolation.getError().code());
            assertTrue(businessRuleViolation.getError().message().contains("active dependencies"));
            assertInstanceOf(ConflictError.class, businessRuleViolation);
        }
    }
}
