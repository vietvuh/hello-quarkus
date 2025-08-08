package vvu.centrauthz.models;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

@Builder(toBuilder = true)
public record User(
    @UUID String userId,
    String email) {
}
