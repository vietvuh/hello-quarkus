package vvu.centrauthz.domains.applications.models;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import vvu.centrauthz.domains.common.models.Sort;

/**
 * Filter criteria for querying applications with pagination and sorting support.
 *
 * <p>This record encapsulates all the filtering parameters used to search and
 * paginate through applications in the system. It includes ownership filtering,
 * name-based searching, and configurable sorting options.</p>
 *
 * <p>Default values are automatically applied in the compact constructor:
 * pageSize defaults to 10 if null or less than 1, and sortOrder defaults
 * to an empty list if null.</p>
 *
 * @param pageSize the number of results per page (defaults to 10, minimum 1)
 * @param pageToken the pagination token for retrieving the next page of results
 * @param ownerId the UUID of the application owner to filter by
 * @param managementGroupId the UUID of the management group to filter by
 * @param name the application name to search for (partial matching may be supported)
 * @param sortOrder the list of sort criteria to apply to the results
 *
 * @since 1.0
 */
@Builder(toBuilder = true)
public record ApplicationFilter(
        Integer pageSize,
        String pageToken,
        UUID ownerId,
        UUID managementGroupId,
        String name,
        List<Sort> sortOrder) {

    /**
     * Compact constructor that applies default values and validation.
     *
     * <p>Ensures pageSize is at least 1 (defaults to 10) and sortOrder
     * is never null (defaults to empty list).</p>
     */
    public ApplicationFilter {
        if (Objects.isNull(sortOrder)) {
            sortOrder = List.of();
        }

        if (Objects.isNull(pageSize) || pageSize < 1) {
            pageSize = 10;
        }
    }
}
