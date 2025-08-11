package vvu.centrauthz.domains.applications.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import vvu.centrauthz.domains.applications.entities.ApplicationEntity;
import vvu.centrauthz.domains.applications.entities.ApplicationEntityCreator;
import vvu.centrauthz.domains.applications.models.Application;
import vvu.centrauthz.domains.applications.models.ApplicationCreator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationMapperTest {

    private final ApplicationMapper mapper = Mappers.getMapper(ApplicationMapper.class);

    void assertEqualsEscapeKey(Application application, ApplicationEntity entity) { // No key changed
        Assertions.assertEquals(application.name(), entity.getName());
        Assertions.assertEquals(application.description(), entity.getDescription());
        Assertions.assertEquals(application.ownerId(), entity.getOwnerId());
        Assertions.assertEquals(application.managementGroupId(), entity.getManagementGroupId());
        Assertions.assertEquals(application.createdBy(), entity.getCreatedBy());
        Assertions.assertEquals(application.updatedBy(), entity.getUpdatedBy());
        Assertions.assertEquals(Instant.ofEpochMilli(application.updatedAt()), entity.getUpdatedAt());
        Assertions.assertEquals(Instant.ofEpochMilli(application.createdAt()), entity.getCreatedAt());
    }

    void mapperAssertEquals(Application application, ApplicationEntity entity) {
        Assertions.assertEquals(application.applicationKey(), entity.getApplicationKey());
        assertEqualsEscapeKey(application, entity);
    }

    @Test
    void toDto() {
        var entity = ApplicationEntityCreator.create();
        var application = mapper.toDto(entity);
        mapperAssertEquals(application, entity);

    }

    @Test
    void toDtoList() {
        var entities = List.of(ApplicationEntityCreator.create(), ApplicationEntityCreator.create(), ApplicationEntityCreator.create());
        var dtos = mapper.toDtoList(entities);

        for (int i = 0; i < entities.size(); i++) {
            mapperAssertEquals(dtos.get(i), entities.get(i));
        }
    }

    @Test
    void toEntity() {
        var application = ApplicationCreator.createApplication();
        var entity = mapper.toEntity(application);
        mapperAssertEquals(application, entity);
    }

    @Test
    void updateEntity() {
        var application = ApplicationCreator.createApplication();
        var entity = ApplicationEntityCreator.create();
        var key = entity.getApplicationKey();
        mapper.updateEntity(application, entity);

        Assertions.assertEquals(key, entity.getApplicationKey()); // No key changed
        assertEqualsEscapeKey(application, entity);
    }

    @Test
    void toEpochMilli() {
        var datetime = LocalDateTime.of(2025, 8, 11, 14, 30, 0);
        Instant instant = datetime.atZone(ZoneId.of("UTC")).toInstant();
        Long epochMillis = instant.toEpochMilli();
        Assertions.assertEquals(epochMillis, mapper.toEpochMilli(instant));
        assertNull(mapper.toInstant(null));
    }

    @Test
    void toInstant() {
        var datetime = LocalDateTime.of(2025, 8, 11, 14, 30, 0);
        Instant instant = datetime.atZone(ZoneId.of("UTC")).toInstant();
        long epochMillis = instant.toEpochMilli();
        Instant result = mapper.toInstant(epochMillis);
        Assertions.assertEquals(instant, result);
        assertNull(mapper.toInstant(null));
    }
}