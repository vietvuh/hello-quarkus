package vvu.centrauthz.domains.applications.controllers;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Objects;
import java.util.UUID;
import vvu.centrauthz.domains.applications.models.Application;
import vvu.centrauthz.domains.applications.models.ApplicationFilter;
import vvu.centrauthz.domains.applications.models.ApplicationPatcher;
import vvu.centrauthz.domains.applications.services.ApplicationService;
import vvu.centrauthz.domains.common.models.Sort;
import vvu.centrauthz.errors.ErrorUtils;
import vvu.centrauthz.models.Patcher;
import vvu.centrauthz.utilities.Context;

/**
 * REST Controller for Application management.
 * Generated from OpenAPI specification: applications.oas.yml
 */
@Path("/v0/applications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApplicationController {

    private final ApplicationService service;

    /**
     * Application Controller.
     *
     * @param service Application Service.
     *
     */
    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    /**
     * Create a new application.
     * POST /v0/applications
     *
     * @param userId the ID of the user making the request
     * @param application the application data to be created
     * @return HTTP 201 Created
     */
    @POST
    public Response createApplication(
            @Valid @HeaderParam("X-Auth-Request-User-Id") java.util.UUID userId,
            @Valid Application application) {
        return Context
                .of(userId)
                .execute(context -> Response
                        .status(Response.Status.CREATED)
                        .entity(service.create(application, context))
                        .build());
    }


    /**
     * List applications with optional filtering parameters.
     * GET /v0/applications
     *
     * @param userId the ID of the user making the request
     * @param pageSize the number of applications to return per page
     * @param pageToken the token for the next page of results
     * @param ownerId filter applications by the owner's UUID
     * @param managementGroupId filter applications by the management group's UUID
     * @param name filter applications by application name
     * @param sortOrder sort direction
     * @return HTTP 200 OK with a list of applications
     */
    @GET
    public Response listApplications(
            @HeaderParam("X-Auth-Request-User-Id") UUID userId,
            @QueryParam("pageSize") Integer pageSize,
            @QueryParam("pageToken") String pageToken,
            @QueryParam("ownerId") UUID ownerId,
            @QueryParam("managementGroupId") UUID managementGroupId,
            @QueryParam("name") String name,
            @QueryParam("sort") String sortOrder) {

        var builder = ApplicationFilter
                .builder()
                .pageSize(pageSize)
                .pageToken(pageToken)
                .ownerId(ownerId)
                .name(name)
                .managementGroupId(managementGroupId);

        if (Objects.nonNull(sortOrder)) {
            builder.sortOrder(Sort.list(sortOrder));
        }

        var filter = builder.build();

        return Context
                .of(userId)
                .execute(context -> Response
                        .ok()
                        .entity(service.list(filter, context))
                        .build());
    }

    /**
     * Retrieve an application by its key.
     * GET /v0/applications/{applicationKey}
     *
     * @param userId the ID of the user making the request
     * @param applicationKey the unique key of the application
     * @return HTTP 200 OK with the application data
     */
    @GET
    @Path("/{applicationKey}")
    public Response getApplication(
            @HeaderParam("X-Auth-Request-User-Id") @Valid UUID userId,
            @PathParam("applicationKey") String applicationKey) {

        return Context
                .of(userId)
                .execute(context -> Response
                        .ok()
                        .entity(service.get(applicationKey, context))
                        .build());
    }

    /**
     * Update an application by key.
     * PUT /v0/applications/{applicationKey}
     *
     * @param userId the ID of the user making the request
     * @param applicationKey the unique key of the application
     * @param application the new application data
     * @return HTTP 200 OK
     */
    @PUT
    @Path("/{applicationKey}")
    public Response updateApplication(
            @HeaderParam("X-Auth-Request-User-Id") UUID userId,
            @PathParam("applicationKey") String applicationKey,
            @QueryParam("force") Boolean force,
            @Valid Application application) {

        if (!Objects.equals(applicationKey, application.applicationKey())) {
            throw ErrorUtils.createBadRequestError("Application Key Mismatch");
        }

        return Context
                .of(userId)
                .execute(context -> {
                    service.update(applicationKey, application, force, context);
                    return Response
                            .noContent()
                            .build();
                });
    }

    /**
     * Update an application by key.
     * PATCH /v0/applications/{applicationKey}
     *
     * @param userId the ID of the user making the request
     * @param applicationKey the unique key of the application
     * @param patcher the set of fields and values to update
     * @return HTTP 204 OK
     */
    @PATCH
    @Path("/{applicationKey}")
    public Response patchApplication(
            @HeaderParam("X-Auth-Request-User-Id") java.util.UUID userId,
            @PathParam("applicationKey") String applicationKey,
            @Valid Patcher<ApplicationPatcher> patcher) {
        return Context
                .of(userId)
                .execute(context -> {
                    service.patch(applicationKey, patcher, context);
                    return Response
                            .noContent()
                            .build();
                });
    }


    /**
     * Delete an application by key.
     * DELETE /v0/applications/{applicationKey}
     *
     * @param userId the ID of the user making the request
     * @param applicationKey the unique key of the application
     * @return HTTP 200 OK
     */
    @DELETE
    @Path("/{applicationKey}")
    public Response deleteApplication(
            @HeaderParam("X-Auth-Request-User-Id") UUID userId,
            @PathParam("applicationKey") String applicationKey) {
        return Context
                .of(userId)
                .execute(context -> {
                    service.delete(applicationKey, context);
                    return Response
                            .noContent()
                            .build();
                });
    }
}
