package vvu.centrauthz.models;

import java.util.UUID;
import lombok.Builder;

/**
 * User information container.
 *
 * @param userId the unique identifier for the user
 * @param email the user's email address
 */
@Builder(toBuilder = true)
public record User(
        UUID userId,
        String email) {
}
