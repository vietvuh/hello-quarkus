package vvu.centrauthz.domains.applications.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

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
        @UUID
        String ownerId,

        @UUID
        String managementGroupId,

        Long createdAt,

        @UUID
        String createdBy,

        Long updatedAt,

        @UUID
        String updatedBy
) {
}
