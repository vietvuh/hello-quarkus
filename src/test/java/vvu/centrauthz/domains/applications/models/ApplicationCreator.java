package vvu.centrauthz.domains.applications.models;

import vvu.centrauthz.models.Page;

import java.util.List;
import java.util.UUID;

public class ApplicationCreator {
    public static Application createApplication() {
        var name = UUID.randomUUID().toString().split("-")[0];
        var key = UUID.randomUUID().toString().split("-")[0];
        return Application
            .builder()
            .applicationKey(key)
            .name(name)
            .ownerId(UUID.randomUUID())
            .managementGroupId(UUID.randomUUID())
            .createdAt(System.currentTimeMillis())
            .createdBy(UUID.randomUUID())
            .updatedAt(System.currentTimeMillis())
            .createdBy(UUID.randomUUID())
            .build();
    }

    public static ApplicationForPatch.ApplicationPatchData createApplicationPatchData() {
        var name = UUID.randomUUID().toString().split("-")[0];
        return ApplicationForPatch
            .ApplicationPatchData
            .builder()
            .name(name)
            .description(UUID.randomUUID().toString())
            .managementGroupId(UUID.randomUUID())
            .ownerId(UUID.randomUUID())
            .build();
    }

    public static ApplicationForPatch createApplicationPatcher(ApplicationForPatch.ApplicationPatchData data) {
        return ApplicationForPatch.builder()
                .updatedFields(List.of("name"))
                .data(data)
                .build();
    }

    public static ApplicationForPatch createApplicationForPatch() {
        return createApplicationPatcher(createApplicationPatchData());
    }

    public static Page<Application, String> createAppPage() {
        var list = List.of(createApplication(), createApplication());
        return Page.<Application, String>builder()
            .data(list)
            .next(UUID.randomUUID().toString())
            .build();
    }

    public static ApplicationPatcher createApplicationPatcher() {
        var name = UUID.randomUUID().toString().split("-")[0];
        return ApplicationPatcher
                .builder()
                .name(name)
                .description(UUID.randomUUID().toString())
                .managementGroupId(UUID.randomUUID())
                .ownerId(UUID.randomUUID())
                .build();
    }

}
