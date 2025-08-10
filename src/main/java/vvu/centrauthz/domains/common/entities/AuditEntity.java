package vvu.centrauthz.domains.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Base entity class that includes audit fields (createdAt, createdBy, updatedAt, updatedBy).
 * All entities that need audit tracking should extend this class.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AuditEntity {
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private Instant createdAt;
    
    @Column(name = "created_by", columnDefinition = "UUID")
    private UUID createdBy;
    
    @Column(name = "updated_at")
    private Instant updatedAt;
    
    @Column(name = "updated_by", columnDefinition = "UUID")
    private UUID updatedBy;
    
    /**
     * Sets the creation timestamp before persisting the entity.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        // Set updatedAt on create as well
        this.updatedAt = this.createdAt;
    }
    
    /**
     * Updates the update timestamp before updating the entity.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
    
    /**
     * Sets the user who created the entity.
     * This method should be called when creating a new entity.
     * 
     * @param userId the ID of the user who created the entity
     */
    public void setCreatedByUser(UUID userId) {
        this.createdBy = userId;
        // For new entities, also set updatedBy
        if (this.updatedBy == null) {
            this.updatedBy = userId;
        }
    }
    
    /**
     * Sets the user who last updated the entity.
     * This method should be called when updating an existing entity.
     * 
     * @param userId the ID of the user who updated the entity
     */
    public void setUpdatedByUser(UUID userId) {
        this.updatedBy = userId;
    }
}
