package vvu.centrauthz.domains.common.models;

import java.util.Arrays;
import java.util.List;
import lombok.Builder;

/**
 * Sorting criteria with field name and direction.
 *
 * @param field the field to sort by
 * @param direction the sort direction (ASC or DESC)
 */
@Builder(toBuilder = true)
public record Sort(String field, SortDirection direction) {

    /**
     * Parses comma-separated sort specifications into a list.
     *
     * @param values comma-separated sort strings (e.g., "name:ASC,id:DESC")
     * @return list of parsed Sort objects
     * @throws IllegalArgumentException if any specification is invalid
     */
    public static List<Sort> list(String values) {
        return Arrays
            .stream(values.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(Sort::from).toList();
    }

    /**
     * Parses a single sort specification from string.
     *
     * @param value sort specification (e.g., "name:ASC" or just "name")
     * @return parsed Sort object (defaults to ASC if no direction specified)
     * @throws IllegalArgumentException if value is invalid or field is empty
     */
    public static Sort from(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Sort value cannot be null or empty");
        }

        String[] parts = value.split(":", 2); // Limit to 2 parts to handle edge cases
        String field = parts[0].trim();

        if (field.isEmpty()) {
            throw new IllegalArgumentException("Field name cannot be empty");
        }

        SortDirection direction = SortDirection.ASC; // Default value

        if (parts.length > 1) {
            String directionStr = parts[1].trim();
            if (!directionStr.isEmpty()) {
                try {
                    direction = SortDirection.valueOf(directionStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid sort direction: "
                            + directionStr
                            + ". Must be ASC or DESC (case insensitive)");
                }
            }
        }

        return new Sort(field, direction);
    }
}
