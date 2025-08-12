package vvu.centrauthz.domains.applications.models;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import vvu.centrauthz.models.Patcher;

/**
 * Data transfer object for patching application entities.
 *
 * <p>This record contains the partial data used to update specific fields
 * of an application. Only non-null fields will be applied during the patch
 * operation, allowing for selective updates without affecting unchanged fields.</p>
 *
 * @param name the new name for the application (optional)
 * @param description the new description for the application (optional)
 * @param ownerId the new owner UUID for the application (optional)
 * @param managementGroupId the new management group UUID for the application (optional)
 *
 * @since 1.0
 */
@Builder(toBuilder = true)
public record ApplicationPatcher(
        String name,
        String description,
        UUID ownerId,
        UUID managementGroupId
) {

    /**
     * Applies the patch data to an existing application entity.
     *
     * <p>This method selectively updates only the fields that are not null
     * in this patcher object. The original application is converted to a builder,
     * modified with the patch data, and then rebuilt.</p>
     *
     * @param application the application entity to patch (must not be null)
     * @param patcher the patch data to apply (must not be null)
     * @return a new Application instance with the patched fields applied
     * @throws NullPointerException if application or patcher is null
     */
    public static Application patch(
            Application application,
            Patcher<ApplicationPatcher> patcher) {

        var builder = application.toBuilder();

        var fields = patcher.fields();
        var data = patcher.data();

        if (fields.contains("name")) {
            builder.name(data.name);
        }

        if (fields.contains("description")) {
            builder.description(data.description);
        }

        if (fields.contains("ownerId")) {
            builder.ownerId(data.ownerId);
        }

        if (fields.contains("managementGroupId")) {
            builder.managementGroupId(data.managementGroupId);
        }

        return builder.build();
    }
}
