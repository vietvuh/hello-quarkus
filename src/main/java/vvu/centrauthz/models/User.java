package vvu.centrauthz.models;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record User(
    UUID userId,
    String email) {
}
