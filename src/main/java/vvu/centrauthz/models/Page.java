package vvu.centrauthz.models;

import lombok.Builder;

import java.util.List;
import java.util.Objects;

@Builder(toBuilder = true)
public record Page<T,N>(List<T> data, N next) {
    public Page {
        if (Objects.isNull(data)) {
            throw new IllegalArgumentException("data is required");
        }
    }
}
