package vvu.centrauthz.domains.applications.controllers;

import jakarta.ws.rs.core.Response;
import lombok.Builder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import vvu.centrauthz.domains.applications.models.*;
import vvu.centrauthz.domains.applications.services.ApplicationService;
import vvu.centrauthz.errors.BadRequestError;
import vvu.centrauthz.models.Patcher;
import vvu.centrauthz.utilities.Context;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

class ApplicationControllerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCreateApplication() {
        var userId = UUID.randomUUID();
        var app = ApplicationCreator.createApplication();
        var appRet = app.toBuilder()
                .createdAt(System.currentTimeMillis())
                .createdBy(userId).build();
        var contextCaptor = ArgumentCaptor.forClass(Context.class);
        var appCaptor = ArgumentCaptor.forClass(Application.class);

        var appContext = ApplicationControllerContext.builder().build();
        var controller = appContext.toController();

        appContext.setup(service -> {
            Mockito.when(service.create(appCaptor.capture(), contextCaptor.capture())).thenReturn(appRet);
        });

        try (var response = controller.createApplication(userId, app)) {
            Assertions.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            Assertions.assertSame(appRet, response.getEntity());

            appContext.verify(service -> {
                Mockito.verify(service, Mockito.only()).create(Mockito.any(Application.class), Mockito.any(Context.class));
                Assertions.assertEquals(userId, contextCaptor.getValue().user().userId());
                Assertions.assertSame(app, appCaptor.getValue());
            });
        }

    }

    @Test
    void testListApplication() {
        var userId = UUID.randomUUID();
        var page = ApplicationCreator.createAppPage();
        var contextCaptor = ArgumentCaptor.forClass(Context.class);
        var filterCaptor = ArgumentCaptor.forClass(ApplicationFilter.class);

        var appContext = ApplicationControllerContext.builder().build();
        var controller = appContext.toController();
        var pageSize = new Random().nextInt();
        var pageToken = UUID.randomUUID().toString();
        var ownerId = UUID.randomUUID();
        var managementGroupId = UUID.randomUUID();
        var name = UUID.randomUUID().toString().split("-")[0];

        appContext.setup(service -> {
            Mockito.when(service.list(filterCaptor.capture(), contextCaptor.capture())).thenReturn(page);
        });

        try (var response = controller
                .listApplications(
                        userId,
                        pageSize,
                        pageToken,
                        ownerId,
                        managementGroupId,
                        name)) {
            Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assertions.assertSame(page, response.getEntity());

            appContext.verify(service -> {
                Mockito
                        .verify(service, Mockito.only())
                        .list(Mockito.any(ApplicationFilter.class), Mockito.any(Context.class));
                var f = filterCaptor.getValue();
                Assertions.assertEquals(userId, contextCaptor.getValue().user().userId());
                Assertions.assertEquals(pageSize, f.pageSize());
                Assertions.assertEquals(pageToken, f.pageToken());
                Assertions.assertEquals(ownerId, f.ownerId());
                Assertions.assertEquals(managementGroupId, f.managementGroupId());
                Assertions.assertEquals(name, f.name());

            });
        }
    }

    @Test
    void getApplication() {
        var userId = UUID.randomUUID();
        var app = ApplicationCreator.createApplication();
        var appRet = app.toBuilder()
                .createdAt(System.currentTimeMillis())
                .createdBy(userId).build();
        var contextCaptor = ArgumentCaptor.forClass(Context.class);
        var appKeyCaptor = ArgumentCaptor.forClass(String.class);

        var appContext = ApplicationControllerContext.builder().build();
        var controller = appContext.toController();

        appContext.setup(service -> {
            Mockito.when(service.get(appKeyCaptor.capture(), contextCaptor.capture())).thenReturn(appRet);
        });

        try (var response = controller.getApplication(userId, app.applicationKey())) {
            Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assertions.assertSame(appRet, response.getEntity());

            appContext.verify(service -> {
                Mockito.verify(service, Mockito.only()).get(Mockito.anyString(), Mockito.any(Context.class));
                Assertions.assertEquals(userId, contextCaptor.getValue().user().userId());
                Assertions.assertSame(app.applicationKey(), appKeyCaptor.getValue());
            });
        }
    }

    @Test
    void updateApplications_keyMatch_noContent() {
        var userId = UUID.randomUUID();
        var app = ApplicationCreator.createApplication();

        var contextCaptor = ArgumentCaptor.forClass(Context.class);
        var appKeyCaptor = ArgumentCaptor.forClass(String.class);
        var appCaptor = ArgumentCaptor.forClass(Application.class);

        var appContext = ApplicationControllerContext.builder().build();
        var controller = appContext.toController();

        appContext.setup(service -> {
            Mockito.doNothing().when(service).update(appKeyCaptor.capture(), appCaptor.capture(), contextCaptor.capture());
        });

        try (var response = controller.updateApplication(userId, app.applicationKey(), app)) {
            Assertions.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            appContext.verify(service -> {
                Mockito.verify(service, Mockito.only()).update(Mockito.anyString(), Mockito.any(Application.class), Mockito.any(Context.class));
                Assertions.assertEquals(userId, contextCaptor.getValue().user().userId());
                Assertions.assertSame(app.applicationKey(), appKeyCaptor.getValue());
            });
        }
    }

    @Test
    void testUpdateApplications_keyMismatch_BadRequest() {
        var userId = UUID.randomUUID();
        var app = ApplicationCreator.createApplication();
        var appKey = UUID.randomUUID().toString();

        var contextCaptor = ArgumentCaptor.forClass(Context.class);
        var appKeyCaptor = ArgumentCaptor.forClass(String.class);
        var appCaptor = ArgumentCaptor.forClass(Application.class);

        var appContext = ApplicationControllerContext.builder().build();
        var controller = appContext.toController();

        appContext.setup(service -> {
            Mockito.doNothing().when(service).update(appKeyCaptor.capture(), appCaptor.capture(), contextCaptor.capture());
        });


        Assertions.assertThrows(
                BadRequestError.class,
                () -> controller.updateApplication(userId, appKey, app));

        appContext.verify(service -> {
            Mockito.verify(service, Mockito.never()).update(Mockito.anyString(), Mockito.any(Application.class), Mockito.any(Context.class));
        });
    }

    @Test
    void testPatchApplication() {
        var userId = UUID.randomUUID();
        var key = UUID.randomUUID().toString().split("-")[0];

        var patcher = Patcher
                .<ApplicationPatcher>builder()
                .fields(List.of("name"))
                .data(ApplicationCreator.createApplicationPatcher())
                .build();

        var contextCaptor = ArgumentCaptor.forClass(Context.class);
        var appKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Patcher<ApplicationPatcher>> patcherCaptor = ArgumentCaptor.forClass(Patcher.class);

        var appContext = ApplicationControllerContext.builder().build();
        var controller = appContext.toController();

        appContext.setup(service -> {
            Mockito.doNothing().when(service).update(appKeyCaptor.capture(), patcherCaptor.capture(), contextCaptor.capture());
        });

        try (var response = controller.patchApplication(userId, key, patcher)) {
            Assertions.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            appContext.verify(service -> {
                Mockito.verify(service, Mockito.only()).update(Mockito.anyString(), Mockito.any(Patcher.class), Mockito.any(Context.class));
                Assertions.assertEquals(userId, contextCaptor.getValue().user().userId());
                Assertions.assertSame(key, appKeyCaptor.getValue());
                Assertions.assertSame(patcher, patcherCaptor.getValue());
            });
        }
    }

    @Test
    void deleteApplication() {
        var userId = UUID.randomUUID();
        var app = ApplicationCreator.createApplication();
        var appRet = app.toBuilder()
                .createdAt(System.currentTimeMillis())
                .createdBy(userId).build();
        var contextCaptor = ArgumentCaptor.forClass(Context.class);
        var appKeyCaptor = ArgumentCaptor.forClass(String.class);

        var appContext = ApplicationControllerContext.builder().build();
        var controller = appContext.toController();

        appContext.setup(service -> {
            Mockito.doNothing().when(service).delete(appKeyCaptor.capture(), contextCaptor.capture());
        });

        try (var response = controller.deleteApplication(userId, app.applicationKey())) {
            Assertions.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            appContext.verify(service -> {
                Mockito.verify(service, Mockito.only()).delete(Mockito.anyString(), Mockito.any(Context.class));
                Assertions.assertEquals(userId, contextCaptor.getValue().user().userId());
                Assertions.assertSame(app.applicationKey(), appKeyCaptor.getValue());
            });
        }
    }

    @Builder(toBuilder = true)
    record ApplicationControllerContext(ApplicationService service) {

        public ApplicationControllerContext {
            if (Objects.isNull(service)) {
                service = Mockito.mock(ApplicationService.class);
            }
        }


        void setup(Consumer<ApplicationService> consumer) {
            consumer.accept(service);
        }

        void verify(Consumer<ApplicationService> consumer) {
            consumer.accept(service);
        }

        ApplicationController toController() {
            return new ApplicationController(service);
        }
    }
}