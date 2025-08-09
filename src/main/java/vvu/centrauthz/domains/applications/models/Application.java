package vvu.centrauthz.domains.applications.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        String applicationKey,

        @NotNull
        @NotBlank
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
