package vvu.centrauthz.domains.applications.models;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.Builder;
import vvu.centrauthz.domains.common.models.Sort;

@Builder(toBuilder = true)
public record ApplicationFilter (
        Integer pageSize,
        String pageToken,
        UUID ownerId,
        UUID managementGroupId,
        String name,
        List<Sort> sortOrder) {
    public ApplicationFilter {
        if (Objects.isNull(sortOrder)) {
            sortOrder = List.of();
        }

        if (Objects.isNull(pageSize) || pageSize < 1) {
            pageSize = 10;
        }
    }
}
