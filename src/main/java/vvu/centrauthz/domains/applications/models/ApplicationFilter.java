package vvu.centrauthz.domains.applications.models;

import java.util.UUID;

import lombok.Builder;

@Builder(toBuilder = true)
public record ApplicationFilter(
    Integer pageSize,
    String pageToken,    
    UUID ownerId,
    UUID managementGroupId,
    String name) {
}
