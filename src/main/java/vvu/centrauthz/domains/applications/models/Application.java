package vvu.centrauthz.domains.applications.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

/**
 * Application record representing the main application entity.
 * Generated from OpenAPI schema: #/components/schemas/Application
 */
@Builder(toBuilder = true)
public record Application(
        @NotNull
        @NotBlank
        @Size(min = 3, max = 128)
        String applicationKey,

        @NotNull
        @NotBlank
        @Size(min = 3, max = 255)
        String name,

        String description,

        @NotNull
        UUID ownerId,

        UUID managementGroupId,

        Long createdAt,

        UUID createdBy,

        Long updatedAt,

        UUID updatedBy
) {
}
