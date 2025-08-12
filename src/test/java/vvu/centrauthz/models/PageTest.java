package vvu.centrauthz.models;

import org.junit.jupiter.api.Test;
import vvu.centrauthz.domains.applications.models.Application;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PageTest {

    @Test
    void testConstructor() {
        assertThrowsExactly(IllegalArgumentException.class,
                () -> Page
                .<Application, String>builder()
                .next(UUID.randomUUID().toString())
                .build());
    }
}