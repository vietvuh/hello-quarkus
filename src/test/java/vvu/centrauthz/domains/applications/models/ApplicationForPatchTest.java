package vvu.centrauthz.domains.applications.models;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationForPatchTest {

    @Test
    void testConstructor() {
        var data = ApplicationForPatch.ApplicationPatchData.builder().build();
        assertThrowsExactly(IllegalArgumentException.class,
                () -> ApplicationForPatch
                        .builder()
                        .data(data)
                        .build());
        assertThrowsExactly(IllegalArgumentException.class,
                () -> ApplicationForPatch
                        .builder()
                        .updatedFields(List.of())
                        .data(data).build());
        assertThrowsExactly(IllegalArgumentException.class,
                () -> ApplicationForPatch
                        .builder()
                        .updatedFields(List.of("name")).build());
        assertDoesNotThrow(() -> ApplicationForPatch
                .builder()
                .data(data)
                .updatedFields(List.of("name")).build());
    }

    private Application createApplication() {
        var name = UUID.randomUUID().toString().split("-")[0];
        var key = UUID.randomUUID().toString().split("-")[0];
        return Application
                .builder()
                .applicationKey(key)
                .name(name)
                .ownerId(UUID.randomUUID().toString())
                .managementGroupId(UUID.randomUUID().toString())
                .createdAt(System.currentTimeMillis())
                .createdBy(UUID.randomUUID().toString())
                .updatedAt(System.currentTimeMillis())
                .createdBy(UUID.randomUUID().toString())
                .build();
    }

    private ApplicationForPatch.ApplicationPatchData createApplicationPatchData() {
        var name = UUID.randomUUID().toString().split("-")[0];
        return ApplicationForPatch
                .ApplicationPatchData
                .builder()
                .name(name)
                .description(UUID.randomUUID().toString())
                .managementGroupId(UUID.randomUUID().toString())
                .ownerId(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void patch_withName_patchApplication() {
        var data = createApplicationPatchData();
        var app = createApplication();
        var patch = ApplicationForPatch
                .builder()
                .updatedFields(List.of("name"))
                .data(data)
                .build();

        var patchedApp = patch.patch(app);
        assertEquals(app.applicationKey(), patchedApp.applicationKey());
        assertEquals(data.name(), patchedApp.name());
        assertEquals(app.description(), patchedApp.description());
    }

    @Test
    void patch_withDescription_patchApplication() {
        var data = createApplicationPatchData();
        var app = createApplication();
        var patch = ApplicationForPatch
                .builder()
                .updatedFields(List.of("name", "description"))
                .data(data)
                .build();

        var patchedApp = patch.patch(app);
        assertEquals(app.applicationKey(), patchedApp.applicationKey());
        assertEquals(data.name(), patchedApp.name());
        assertEquals(data.description(), patchedApp.description());
    }

    @Test
    void patch_withManagementGroupId_patchApplication() {
        var data = createApplicationPatchData();
        var app = createApplication();
        var patch = ApplicationForPatch
                .builder()
                .updatedFields(List.of("managementGroupId", "ownerId"))
                .data(data)
                .build();

        var patchedApp = patch.patch(app);
        assertEquals(app.applicationKey(), patchedApp.applicationKey());
        assertEquals(app.description(), patchedApp.description());
        assertEquals(app.name(), patchedApp.name());
        assertEquals(data.ownerId(), patchedApp.ownerId());
        assertEquals(data.managementGroupId(), patchedApp.managementGroupId());
    }

    @Test
    void patch_withOwnerId_patchApplication() {
        var data = createApplicationPatchData();
        var app = createApplication();
        var patch = ApplicationForPatch
                .builder()
                .updatedFields(List.of("name", "ownerId"))
                .data(data)
                .build();

        var patchedApp = patch.patch(app);
        assertEquals(app.applicationKey(), patchedApp.applicationKey());
        assertEquals(app.description(), patchedApp.description());
        assertEquals(data.ownerId(), patchedApp.ownerId());
        assertEquals(data.name(), patchedApp.name());
    }
}