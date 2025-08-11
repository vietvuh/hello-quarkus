package vvu.centrauthz.domains.applications.services;

import lombok.Builder;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import vvu.centrauthz.domains.applications.entities.ApplicationEntity;
import vvu.centrauthz.domains.applications.entities.ApplicationEntityCreator;
import vvu.centrauthz.domains.applications.mappers.ApplicationMapper;
import vvu.centrauthz.domains.applications.models.Application;
import vvu.centrauthz.domains.applications.models.ApplicationCreator;
import vvu.centrauthz.domains.applications.models.ApplicationPatcher;
import vvu.centrauthz.domains.applications.repositories.ApplicationRepo;
import vvu.centrauthz.errors.NotFoundError;
import vvu.centrauthz.models.Patcher;
import vvu.centrauthz.utilities.Context;

import java.util.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

class ApplicationServiceTest {

    @Test
    void get_hasEntity_returnDto() {
        var context = ApplicationServiceContext.builder().build();
        var service = context.toService();
        var entity = ApplicationEntityCreator.create();
        var dto = ApplicationCreator.createApplication();
        var keyCaptor = ArgumentCaptor.forClass(String.class);
        var eCaptor = ArgumentCaptor.forClass(ApplicationEntity.class);

        context.setup(ctx -> {
            Mockito.when(ctx.repo.findByKey(keyCaptor.capture())).thenReturn(Optional.of(entity));
            Mockito.when(ctx.mapper.toDto(eCaptor.capture())).thenReturn(dto);
        });

        var app = service.get(entity.getApplicationKey(), Context.of(UUID.randomUUID()));

        assertSame(app, dto);
        assertEquals(entity.getApplicationKey(), keyCaptor.getValue());
        assertSame(entity, eCaptor.getValue());

        context.verify(ctx -> {
            Mockito.verify(ctx.repo, Mockito.times(1)).findByKey(Mockito.anyString());
            Mockito.verify(ctx.mapper, Mockito.times(1)).toDto(Mockito.any(ApplicationEntity.class));
        });

    }

    @Test
    void get_hasNoEntity_NotFound() {
        var context = ApplicationServiceContext.builder().build();
        var service = context.toService();
        var entity = ApplicationEntityCreator.create();
        var dto = ApplicationCreator.createApplication();
        var keyCaptor = ArgumentCaptor.forClass(String.class);
        var eCaptor = ArgumentCaptor.forClass(ApplicationEntity.class);

        context.setup(ctx -> {
            Mockito.when(ctx.repo.findByKey(keyCaptor.capture())).thenReturn(Optional.empty());
            Mockito.when(ctx.mapper.toDto(eCaptor.capture())).thenReturn(dto);
        });

        assertThrowsExactly(NotFoundError.class, () -> service.get(entity.getApplicationKey(), Context.of(UUID.randomUUID())));

        assertEquals(entity.getApplicationKey(), keyCaptor.getValue());

        context.verify(ctx -> {
            Mockito.verify(ctx.repo, Mockito.times(1)).findByKey(Mockito.anyString());
            Mockito.verify(ctx.mapper, Mockito.never()).toDto(Mockito.any(ApplicationEntity.class));
        });

    }

    @Test
    void create() {
        var context = ApplicationServiceContext.builder().build();
        var service = context.toService();
        var entity = ApplicationEntityCreator.create();
        var userId = UUID.randomUUID();
        var dto = ApplicationCreator.createApplication();
        var dtoExpected = dto.toBuilder().updatedBy(userId).build();
        var eCaptor = ArgumentCaptor.forClass(ApplicationEntity.class);
        var dtoCaptor = ArgumentCaptor.forClass(Application.class);

        context.setup(ctx -> {
            Mockito.doNothing().when(ctx.repo).persist(eCaptor.capture());
            Mockito.when(ctx.mapper.toEntity(dtoCaptor.capture())).thenReturn(entity);
            Mockito.when(ctx.mapper.toDto(eCaptor.capture())).thenReturn(dtoExpected);
        });

        var dtoRet = service.create(dto, Context.of(userId));

        assertSame(dto, dtoCaptor.getValue());
        assertSame(dtoExpected, dtoRet);
        assertEquals(2, eCaptor.getAllValues().size());
        assertEquals(userId, eCaptor.getAllValues().getFirst().getCreatedBy());
        assertEquals(userId, eCaptor.getAllValues().getLast().getCreatedBy());

        context.verify(ctx -> {
            Mockito.verify(ctx.repo, Mockito.times(1)).persist(Mockito.any(ApplicationEntity.class));
            Mockito.verify(ctx.mapper, Mockito.times(1)).toDto(Mockito.any(ApplicationEntity.class));
            Mockito.verify(ctx.mapper, Mockito.times(1)).toEntity(Mockito.any(Application.class));
        });
    }

    @Test
    void update_whenAppExisting_updateIt() {
        var context = ApplicationServiceContext.builder().build();
        var service = context.toService();
        var force = new Random().nextBoolean();
        var userId = UUID.randomUUID();
        var entity = ApplicationEntityCreator.create();
        var dto = ApplicationCreator.createApplication();
        var keyCaptor = ArgumentCaptor.forClass(String.class);
        var dtoCaptor = ArgumentCaptor.forClass(Application.class);
        var entityCaptor = ArgumentCaptor.forClass(ApplicationEntity.class);

        context.setup(ctx -> {
            Mockito.when(ctx.repo.findByKeyWithLock(keyCaptor.capture())).thenReturn(Optional.of(entity));
            Mockito.doNothing().when(ctx.mapper).updateEntity(dtoCaptor.capture(), entityCaptor.capture());
        });

        service.update(dto.applicationKey(), dto, force, Context.of(userId));

        assertEquals(userId, entity.getUpdatedBy());
        assertSame(dto, dtoCaptor.getValue());
        assertSame(entity, entityCaptor.getValue());
        assertEquals(dto.applicationKey(), keyCaptor.getValue());

        context.verify(ctx -> {
            Mockito.verify(ctx.repo, Mockito.times(1)).findByKeyWithLock(Mockito.anyString());
            Mockito.verify(ctx.repo, Mockito.times(0)).persist(Mockito.any(ApplicationEntity.class));
        });
    }

    @Test
    void update_whenAppNotExistingNoForce_NotFound() {
        var context = ApplicationServiceContext.builder().build();
        var service = context.toService();
        var force = false;
        var userId = UUID.randomUUID();
        var entity = ApplicationEntityCreator.create();
        var dto = ApplicationCreator.createApplication();
        var keyCaptor = ArgumentCaptor.forClass(String.class);
        var dtoCaptor = ArgumentCaptor.forClass(Application.class);
        var entityCaptor = ArgumentCaptor.forClass(ApplicationEntity.class);

        context.setup(ctx -> {
            Mockito.when(ctx.repo.findByKeyWithLock(keyCaptor.capture())).thenReturn(Optional.empty());
            Mockito.doNothing().when(ctx.mapper).updateEntity(dtoCaptor.capture(), entityCaptor.capture());
        });

        assertThrowsExactly(NotFoundError.class, () -> {
            service.update(dto.applicationKey(), dto, force, Context.of(userId));
        });

        assertEquals(dto.applicationKey(), keyCaptor.getValue());

        context.verify(ctx -> {
            Mockito.verify(ctx.repo, Mockito.times(1)).findByKeyWithLock(Mockito.anyString());
            Mockito.verify(ctx.repo, Mockito.times(0)).persist(Mockito.any(ApplicationEntity.class));
            Mockito.verify(ctx.mapper, Mockito.never()).updateEntity(Mockito.any(Application.class), Mockito.any(ApplicationEntity.class));
        });
    }

    @Test
    void update_whenAppNotExistingWithForce_CreateIt() {
        var context = ApplicationServiceContext.builder().build();
        var service = context.toService();
        var force = true;
        var userId = UUID.randomUUID();
        var entity = ApplicationEntityCreator.create();
        var dto = ApplicationCreator.createApplication();
        var keyCaptor = ArgumentCaptor.forClass(String.class);
        var dtoCaptor = ArgumentCaptor.forClass(Application.class);
        var entityCaptor = ArgumentCaptor.forClass(ApplicationEntity.class);

        context.setup(ctx -> {
            Mockito.when(ctx.repo.findByKeyWithLock(keyCaptor.capture())).thenReturn(Optional.empty());
            Mockito.when(ctx.mapper.toEntity(dtoCaptor.capture())).thenReturn(entity);
            Mockito.doNothing().when(ctx.repo).persist(entityCaptor.capture());
        });

        service.update(dto.applicationKey(), dto, force, Context.of(userId));
        assertSame(dto, dtoCaptor.getValue());
        assertSame(entity, entityCaptor.getValue());
        assertEquals(dto.applicationKey(), keyCaptor.getValue());
        assertEquals(userId, entity.getCreatedBy());

        context.verify(ctx -> {
            Mockito.verify(ctx.repo, Mockito.times(1)).findByKeyWithLock(Mockito.anyString());
            Mockito.verify(ctx.repo, Mockito.times(1)).persist(Mockito.any(ApplicationEntity.class));
            Mockito.verify(ctx.mapper, Mockito.times(1)).toEntity(Mockito.any(Application.class));
            Mockito.verify(ctx.mapper, Mockito.never()).updateEntity(Mockito.any(Application.class), Mockito.any(ApplicationEntity.class));
        });
    }

    @Test
    void patch_whenAppExisting_patchIt() {
        var context = ApplicationServiceContext.builder().build();
        var service = context.toService();
        var userId = UUID.randomUUID();
        var entity = ApplicationEntityCreator.create();
        var dto = ApplicationCreator.createApplication();
        var appPatcher = ApplicationCreator.createApplicationPatcher();
        var patcher = Patcher
            .<ApplicationPatcher>builder()
            .fields(List.of("name", "description", "ownerId", "managementGroupId"))
            .data(appPatcher)
            .build();
        var keyCaptor = ArgumentCaptor.forClass(String.class);

        context.setup(ctx -> {
            Mockito.when(ctx.repo.findByKeyWithLock(keyCaptor.capture())).thenReturn(Optional.of(entity));
        });

        service.patch(dto.applicationKey(), patcher, Context.of(userId));

        assertEquals(appPatcher.name(), entity.getName());
        assertEquals(appPatcher.description(), entity.getDescription());
        assertEquals(appPatcher.ownerId(), entity.getOwnerId());
        assertEquals(appPatcher.managementGroupId(), entity.getManagementGroupId());

        context.verify( ctx -> {
            Mockito.verify(ctx.repo, Mockito.only()).findByKeyWithLock(Mockito.anyString());
        });

    }

    @Test
    void patch_whenAppNotExisting_NotFound() {
        var context = ApplicationServiceContext.builder().build();
        var service = context.toService();
        var userId = UUID.randomUUID();
        var entity = ApplicationEntityCreator.create();
        var dto = ApplicationCreator.createApplication();
        var appPatcher = ApplicationCreator.createApplicationPatcher();
        var patcher = Patcher
            .<ApplicationPatcher>builder()
            .fields(List.of("name", "description", "ownerId", "managementGroupId"))
            .data(appPatcher)
            .build();
        var keyCaptor = ArgumentCaptor.forClass(String.class);

        context.setup(ctx -> {
            Mockito.when(ctx.repo.findByKeyWithLock(keyCaptor.capture())).thenReturn(Optional.empty());
        });

        assertThrowsExactly(NotFoundError.class, () -> {
            service.patch(dto.applicationKey(), patcher, Context.of(userId));
        });

        assertNotEquals(appPatcher.name(), entity.getName());
        assertNotEquals(appPatcher.description(), entity.getDescription());
        assertNotEquals(appPatcher.ownerId(), entity.getOwnerId());
        assertNotEquals(appPatcher.managementGroupId(), entity.getManagementGroupId());

        context.verify( ctx -> {
            Mockito.verify(ctx.repo, Mockito.only()).findByKeyWithLock(Mockito.anyString());
        });

    }

    @Test
    void delete_whenAppExisting_deleteIt() {
        var context = ApplicationServiceContext.builder().build();
        var service = context.toService();
        var userId = UUID.randomUUID();
        var entity = ApplicationEntityCreator.create();
        var dto = ApplicationCreator.createApplication();
        var keyCaptor = ArgumentCaptor.forClass(String.class);
        var eCaptor = ArgumentCaptor.forClass(ApplicationEntity.class);

        context.setup(ctx -> {
            Mockito.when(ctx.repo.findByKeyWithLock(keyCaptor.capture())).thenReturn(Optional.of(entity));
            Mockito.doNothing().when(ctx.repo).delete(eCaptor.capture());
        });

        service.delete(dto.applicationKey(), Context.of(userId));
        assertSame(entity, eCaptor.getValue());

        context.verify( ctx -> {
            Mockito.verify(ctx.repo, Mockito.times(1)).findByKeyWithLock(Mockito.anyString());
            Mockito.verify(ctx.repo, Mockito.times(1)).delete(Mockito.any(ApplicationEntity.class));
        });
    }

    @Test
    void delete_whenAppNotExisting_NotFound() {
        var context = ApplicationServiceContext.builder().build();
        var service = context.toService();
        var userId = UUID.randomUUID();
        var dto = ApplicationCreator.createApplication();
        var keyCaptor = ArgumentCaptor.forClass(String.class);
        var eCaptor = ArgumentCaptor.forClass(ApplicationEntity.class);

        context.setup(ctx -> {
            Mockito.when(ctx.repo.findByKeyWithLock(keyCaptor.capture())).thenReturn(Optional.empty());
            Mockito.doNothing().when(ctx.repo).delete(eCaptor.capture());
        });

        assertThrowsExactly(NotFoundError.class, () -> {
            service.delete(dto.applicationKey(), Context.of(userId));
        });

        context.verify( ctx -> {
            Mockito.verify(ctx.repo, Mockito.only()).findByKeyWithLock(Mockito.anyString());
            Mockito.verify(ctx.repo, Mockito.times(0)).delete(Mockito.any(ApplicationEntity.class));
        });
    }

    @Builder(toBuilder = true)
    record ApplicationServiceContext(
        ApplicationRepo repo,
        ApplicationMapper mapper
    ) {

        public ApplicationServiceContext {
            if (Objects.isNull(repo)) {
                repo = Mockito.mock(ApplicationRepo.class);
            }

            if (Objects.isNull(mapper)) {
                mapper = Mockito.mock(ApplicationMapper.class);
            }
        }


        public void setup(Consumer<ApplicationServiceContext> consumer) {
            consumer.accept(this);
        }

        public void verify(Consumer<ApplicationServiceContext> consumer) {
            consumer.accept(this);
        }

        public ApplicationService toService() {
            return new ApplicationService(repo, mapper);
        }
    }
}