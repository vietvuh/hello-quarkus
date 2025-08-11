package vvu.centrauthz.domains.applications.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vvu.centrauthz.domains.common.entities.AuditEntity;

/**
 * Application Entity represents the core application data model.
 * This entity extends AuditEntity to include standard auditing fields
 * such as creation and modification timestamps.
 */
@Setter
@Getter
@Entity
@Table(name = "application")
public class ApplicationEntity extends AuditEntity {

    // Getters and Setters
    @Id
    @Column(name = "application_key", nullable = false, length = 255)
    @NotBlank(message = "Application key cannot be blank")
    private String applicationKey;

    @Column(name = "name", nullable = false, length = 255)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "owner_id", nullable = false, columnDefinition = "UUID")
    @NotNull(message = "Owner ID cannot be null")
    private UUID ownerId;

    @Column(name = "management_group_id", columnDefinition = "UUID")
    private UUID managementGroupId;

    // Audit fields are now inherited from AuditEntity

    // Constructors
    public ApplicationEntity() {
        super();
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationEntity that = (ApplicationEntity) o;
        return Objects.equals(applicationKey, that.applicationKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationKey);
    }

    // toString
    @SuppressWarnings("checkstyle:OperatorWrap")
    @Override
    public String toString() {
        return "ApplicationEntity{" +
            "applicationKey='" + applicationKey + '\'' +
            ", name='" + name + '\'' +
            ", ownerId=" + ownerId +
            ", managementGroupId=" + managementGroupId +
            '}';
    }
}
