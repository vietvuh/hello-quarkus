package vvu.centrauthz.domains.applications.models;

import lombok.Builder;

@Builder(toBuilder = true)
public record ApplicationFilter(String nextToken) {
    public static ApplicationFilter empty() {
        return ApplicationFilter.builder().build();
    }
}
