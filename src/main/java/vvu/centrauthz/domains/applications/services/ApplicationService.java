package vvu.centrauthz.domains.applications.services;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Supplier;
import vvu.centrauthz.domains.applications.entities.ApplicationEntity;
import vvu.centrauthz.domains.applications.mappers.ApplicationMapper;
import vvu.centrauthz.domains.applications.models.Application;
import vvu.centrauthz.domains.applications.models.ApplicationFilter;
import vvu.centrauthz.domains.applications.models.ApplicationPatcher;
import vvu.centrauthz.domains.applications.repositories.ApplicationRepo;
import vvu.centrauthz.errors.ErrorUtils;
import vvu.centrauthz.errors.NotImplementedError;
import vvu.centrauthz.models.Page;
import vvu.centrauthz.models.Patcher;
import vvu.centrauthz.utilities.Context;

/**
 * Service layer for managing application entities and business logic.
 *
 * <p>This service provides comprehensive CRUD operations for applications,
 * including listing with filtering, creation, updates, patches, and deletion.
 * All operations are performed within the security context and include
 * proper auditing of changes.</p>
 *
 * <p>The service handles entity locking for concurrent access safety and
 * provides both strict and force update modes. It automatically manages
 * audit fields like createdBy, updatedBy, and updatedAt.</p>
 *
 * @since 1.0
 */
@Singleton
public class ApplicationService {

    private final ApplicationRepo repo;
    private final ApplicationMapper mapper;

    /**
     * Constructs a new ApplicationService with the required dependencies.
     *
     * @param repo the application repository for data access operations
     * @param mapper the mapper for converting between entities and DTOs
     */
    public ApplicationService(ApplicationRepo repo, ApplicationMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    /**
     * Retrieves a paginated list of applications based on filter criteria.
     *
     * @param filter the filtering, sorting, and pagination criteria
     * @param context the execution context containing user information
     * @return a paginated result containing matching applications
     * @throws NotImplementedError as this feature is not yet implemented
     */
    public Page<Application, String> list(ApplicationFilter filter, Context context) {
        throw new NotImplementedError("Feature not implemented");
    }

    /**
     * Retrieves a single application by its unique key.
     *
     * @param applicationKey the unique identifier of the application
     * @param context the execution context containing user information
     * @return the application DTO if found
     * @throws RuntimeException if the application is not found
     */
    public Application get(String applicationKey, Context context) {
        var e = this.repo
                .findByKey(applicationKey)
                .orElseThrow(() -> ErrorUtils.createNotFoundError(applicationKey + "is not found"));
        return mapper.toDto(e);
    }

    private void lockKey(String applicationKey,
                         Consumer<ApplicationEntity> consumer) {
        lockKey(applicationKey, consumer, () -> {
            throw ErrorUtils.createNotFoundError(applicationKey + " is not found");
        });
    }

    private void lockKey(String applicationKey,
                         Consumer<ApplicationEntity> consumer,
                         Supplier<Void> whenNotFound) {
        this.repo.findByKeyWithLock(applicationKey).ifPresentOrElse(consumer, whenNotFound::get);
    }

    /**
     * Creates a new application in the system.
     *
     * <p>This method persists a new application entity with audit information
     * automatically populated from the execution context.</p>
     *
     * @param application the application data to create
     * @param context the execution context containing user information
     * @return the created application DTO with generated fields populated
     */
    @Transactional
    public Application create(Application application, Context context) {
        var e = mapper.toEntity(application);
        e.setCreatedBy(context.user().userId());
        repo.persist(e);
        return mapper.toDto(e);
    }

    /**
     * Updates an existing application with new data.
     *
     * <p>This method performs a full update of the application entity.
     * If force mode is enabled and the application doesn't exist, it will
     * be created instead of throwing an error.</p>
     *
     * @param applicationKey the unique identifier of the application to update
     * @param application the new application data
     * @param force whether to create the application if it doesn't exist
     * @param context the execution context containing user information
     * @throws RuntimeException if the application is not found and force is false
     */
    @Transactional
    public void update(String applicationKey,
                       Application application,
                       Boolean force,
                       Context context) {
        lockKey(applicationKey, (e) -> {
            mapper.updateEntity(application, e);
            e.setUpdatedBy(context.user().userId());
            e.setUpdatedAt(Instant.ofEpochMilli(System.currentTimeMillis()));
        }, () -> {
            if (!Boolean.TRUE.equals(force)) {
                throw ErrorUtils.createNotFoundError(applicationKey + " is not found");
            }
            this.create(application, context);
            return null;
        });
    }

    /**
     * Applies partial updates to an existing application.
     *
     * <p>This method allows selective updating of application fields
     * based on the provided patcher object. Only fields present in the
     * patcher will be updated, leaving other fields unchanged.</p>
     *
     * @param applicationKey the unique identifier of the application to patch
     * @param patcher the patcher containing the fields to update
     * @param context the execution context containing user information
     * @throws RuntimeException if the application is not found
     */
    @Transactional
    public void patch(String applicationKey, Patcher<ApplicationPatcher> patcher, Context context) {
        lockKey(applicationKey, (e) -> {
            e.setUpdatedBy(context.user().userId());
            patcher
                    .having("name", d -> e.setName(d.name()))
                    .having("description", d -> e.setDescription(d.description()))
                    .having("ownerId", d -> e.setOwnerId(d.ownerId()))
                    .having("managementGroupId",
                        d -> e.setManagementGroupId(d.managementGroupId()));
        });
    }

    /**
     * Deletes an application from the system.
     *
     * <p>This method permanently removes the application entity from
     * the database after acquiring an exclusive lock.</p>
     *
     * @param applicationKey the unique identifier of the application to delete
     * @param context the execution context containing user information
     * @throws RuntimeException if the application is not found
     */
    @Transactional
    public void delete(String applicationKey, Context context) {
        lockKey(applicationKey, repo::delete);
    }

}
