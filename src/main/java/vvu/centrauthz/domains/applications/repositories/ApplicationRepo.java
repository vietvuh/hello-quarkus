package vvu.centrauthz.domains.applications.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;
import vvu.centrauthz.domains.applications.entities.ApplicationEntity;
import vvu.centrauthz.domains.applications.models.ApplicationFilter;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing ApplicationEntity persistence operations.
 */
@ApplicationScoped
public class ApplicationRepo implements PanacheRepository<ApplicationEntity> {

    private static final String APPLICATION_KEY = "applicationKey";


    public List<ApplicationEntity> query(ApplicationFilter filter) {
        return List.of();
    }


    /**
     * Find an application by its key.
     *
     * @param applicationKey the application key to search for
     * @return an Optional containing the found application, or empty if not found
     */
    public Optional<ApplicationEntity> findByKey(String applicationKey) {
        return find(APPLICATION_KEY, applicationKey).firstResultOptional();
    }

    /**
     * Find an application by its key, locking the record for write
     * to prevent concurrent modifications.
     *
     * @param applicationKey the application key to search for
     * @return an Optional containing the found application, or empty if not found
     */
    public Optional<ApplicationEntity> findByKeyWithLock(String applicationKey) {
        return find(APPLICATION_KEY, applicationKey)
                .withLock(LockModeType.PESSIMISTIC_WRITE)
                .firstResultOptional();
    }

    /**
     * Check if an application with the given key exists.
     *
     * @param applicationKey the application key to check
     * @return true if an application with the key exists, false otherwise
     */
    public boolean existsByKey(String applicationKey) {
        return count(APPLICATION_KEY, applicationKey) > 0;
    }

    /**
     * Delete an application by its key.
     *
     * @param applicationKey the application key to delete
     * @return true if the application was deleted, false if not found
     */
    public boolean deleteByKey(String applicationKey) {
        return delete(APPLICATION_KEY, applicationKey) > 0;
    }

    /**
     * Check if an application with the given name exists (case-insensitive).
     *
     * @param name the name to check
     * @return true if an application with the name exists, false otherwise
     */
    public boolean existsByNameIgnoreCase(String name) {
        return count("LOWER(name) = LOWER(?1)", name) > 0;
    }

}
