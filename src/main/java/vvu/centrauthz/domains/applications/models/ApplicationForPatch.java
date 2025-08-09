package vvu.centrauthz.domains.applications.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * ApplicationForPatch record for partial updates of applications.
 * Generated from OpenAPI schema: #/components/schemas/ApplicationForPatch
 */
@Builder(toBuilder = true)
public record ApplicationForPatch(

        @NotEmpty
        List<String> updatedFields,

        @Valid @NotNull
        ApplicationPatchData data
) {

    public ApplicationForPatch {
        if (Objects.isNull(updatedFields) || updatedFields.isEmpty()) {
            throw new IllegalArgumentException("updatedFields is required");
        }

        if (Objects.isNull(data)) {
            throw new IllegalArgumentException("data is required");
        }
    }

    public Application patch(Application application) {
        var builder = application.toBuilder();

        if (updatedFields.contains("name")) {
            builder.name(this.data.name);
        }

        if (updatedFields.contains("description")) {
            builder.description(this.data.description);
        }

        if (updatedFields.contains("ownerId")) {
            builder.ownerId(this.data.ownerId);
        }

        if (updatedFields.contains("managementGroupId")) {
            builder.managementGroupId(this.data.managementGroupId);
        }

        return builder.build();
    }

    /**
     * Nested record representing the data fields that can be updated.
     */
    @Builder(toBuilder = true)
    public record ApplicationPatchData(
            String name,

            String description,

            UUID ownerId,

            UUID managementGroupId
    ) {
    }
}
