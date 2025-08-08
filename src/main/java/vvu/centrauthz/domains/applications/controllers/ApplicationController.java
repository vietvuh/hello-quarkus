package vvu.centrauthz.domains.applications.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.validator.constraints.UUID;
import vvu.centrauthz.domains.applications.models.Application;
import vvu.centrauthz.domains.applications.models.ApplicationFilter;
import vvu.centrauthz.domains.applications.models.ApplicationForPatch;
import vvu.centrauthz.domains.applications.services.ApplicationService;
import vvu.centrauthz.models.Error;
import vvu.centrauthz.utilities.Context;

/**
 * REST Controller for Application management
 * Generated from OpenAPI specification: applications.oas.yml
 * 
 * All endpoints return HTTP 503 Service Unavailable as per requirements
 */
@Path("/v0/applications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    /**
     * Create a new application
     * POST /v0/applications
     * 
     * @param application the application to create
     * @return HTTP 503 Service Unavailable
     */
    @POST
    public Response createApplications(@HeaderParam("X-Auth-Request-User-Id") @UUID String userId,
                                       Application application) {
        return Context
                .of(userId)
                .execute(context -> Response
                        .status(Response.Status.CREATED)
                        .entity(service.create(application, context))
                        .build());
    }

    /**
     * List all applications
     * GET /v0/applications
     * 
     * @return HTTP 503 Service Unavailable
     */
    @GET
    public Response listApplications(@HeaderParam("X-Auth-Request-User-Id") @UUID String userId) {
        return Context
                .of(userId)
                .execute(context -> Response
                        .status(Response.Status.CREATED)
                        .entity(service.list(ApplicationFilter.empty(), context))
                        .build());
    }

    /**
     * Get an application by key
     * GET /v0/applications/{applicationKey}
     * 
     * @param applicationKey the unique key of the application
     * @return HTTP 503 Service Unavailable
     */
    @GET
    @Path("/{applicationKey}")
    public Response getApplications(@HeaderParam("X-Auth-Request-User-Id") @UUID String userId,
                                    @PathParam("applicationKey") String applicationKey) {
        return Context
                .of(userId)
                .execute(context -> Response
                        .status(Response.Status.CREATED)
                        .entity(service.get(applicationKey, context))
                        .build());
    }

    /**
     * Update an application
     * PUT /v0/applications/{applicationKey}
     * 
     * @param applicationKey the unique key of the application
     * @param application the patch data for the application
     * @return HTTP 503 Service Unavailable
     */
    @PUT
    @Path("/{applicationKey}")
    public Response updateApplications(
            @HeaderParam("X-Auth-Request-User-Id") @UUID String userId,
            @PathParam("applicationKey") String applicationKey,
            Application application) {
        return Context
                .of(userId)
                .execute(context -> {
                    service.update(applicationKey, application, context);
                    return Response
                            .noContent()
                            .build();
                });
    }

    @PATCH
    @Path("/{applicationKey}")
    public Response updateApplications(
            @HeaderParam("X-Auth-Request-User-Id") @UUID String userId,
            @PathParam("applicationKey") String applicationKey,
            ApplicationForPatch patcher) {
        return Context
                .of(userId)
                .execute(context -> {
                    service.update(applicationKey, patcher, context);
                    return Response
                            .noContent()
                            .build();
                });
    }

    /**
     * Delete an application
     * DELETE /v0/applications/{applicationKey}
     * 
     * @param applicationKey the unique key of the application
     * @return HTTP 503 Service Unavailable
     */
    @DELETE
    @Path("/{applicationKey}")
    public Response deleteApplications(@HeaderParam("X-Auth-Request-User-Id") @UUID String userId,
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
