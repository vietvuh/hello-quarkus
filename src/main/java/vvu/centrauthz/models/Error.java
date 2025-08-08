package vvu.centrauthz.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Map;

/**
 * Error record representing API error responses.
 * Generated from OpenAPI schema: #/components/schemas/Error
 */
@Builder(toBuilder = true)
public record Error(
    @NotNull
    @NotBlank
    String code,
    
    String message,
    
    Map<String, String> details
) {
}
