package vvu.centrauthz.domains.applications.models;

import lombok.Builder;
import vvu.centrauthz.models.Patcher;

import java.util.UUID;

@Builder(toBuilder = true)
public record ApplicationPatcher(
        String name,
        String description,
        UUID ownerId,
        UUID managementGroupId
) {
    public static Application patch(Application application, Patcher<ApplicationPatcher> patcher) {

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
