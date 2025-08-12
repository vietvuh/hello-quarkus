package vvu.centrauthz.domains.applications.models;

import org.junit.jupiter.api.Test;
import vvu.centrauthz.models.Patcher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationPatcherTest {

    @Test
    void patch() {
        var app = ApplicationCreator.createApplication();
        var appPatch = ApplicationCreator.createApplicationPatcher();
        var patcher = Patcher.<ApplicationPatcher>builder()
                .fields(List.of("name"))
                .data(appPatch)
                .build();

        var patchedApp = ApplicationPatcher.patch(app, patcher);
        assertEquals(app.applicationKey(), patchedApp.applicationKey());
        assertEquals(appPatch.name(), patchedApp.name());
        assertEquals(app.description(), patchedApp.description());

        patcher = patcher.toBuilder().fields(List.of("name", "description")).build();
        patchedApp = ApplicationPatcher.patch(app, patcher);
        assertEquals(appPatch.name(), patchedApp.name());
        assertEquals(appPatch.description(), patchedApp.description());

        patcher = patcher.toBuilder().fields(List.of("managementGroupId", "description", "ownerId")).build();
        patchedApp = ApplicationPatcher.patch(app, patcher);
        assertEquals(app.name(), patchedApp.name());
        assertEquals(appPatch.description(), patchedApp.description());
        assertEquals(appPatch.managementGroupId(), patchedApp.managementGroupId());
        assertEquals(appPatch.ownerId(), patchedApp.ownerId());
    }
}