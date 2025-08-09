package vvu.centrauthz.models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatcherTest {

    @Test
    void builder() {
        assertThrowsExactly(IllegalArgumentException.class,
                () -> Patcher
                        .<String>builder()
                        .fields(List.of("name")).build());
        assertThrowsExactly(IllegalArgumentException.class,
                () -> Patcher.<String>builder().data("test").build());
        assertDoesNotThrow(() -> Patcher
                .<String>builder()
                .fields(List.of("name"))
                .data("test").build());
    }
}