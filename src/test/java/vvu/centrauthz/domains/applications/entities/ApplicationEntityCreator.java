package vvu.centrauthz.domains.applications.entities;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ApplicationEntityCreator {

    ApplicationEntityCreator() {
        throw new IllegalStateException();
    }

    public static ApplicationEntity create() {
        var entity = new ApplicationEntity();

        entity.setApplicationKey(UUID.randomUUID().toString().split("-")[0]);
        entity.setDescription(UUID.randomUUID().toString());
        entity.setName(UUID.randomUUID().toString().split("-")[0]);
        entity.setOwnerId(UUID.randomUUID());
        entity.setManagementGroupId(UUID.randomUUID());
        entity.setCreatedAt(Instant.ofEpochMilli(System.currentTimeMillis()).minus(1, ChronoUnit.DAYS));
        entity.setCreatedBy(UUID.randomUUID());
        entity.setUpdatedAt(Instant.ofEpochMilli(System.currentTimeMillis()));
        entity.setUpdatedBy(UUID.randomUUID());

        return entity;
    }


}
