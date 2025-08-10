package vvu.centrauthz.domains.applications.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vvu.centrauthz.domains.applications.entities.ApplicationEntity;
import vvu.centrauthz.domains.applications.models.Application;

import java.util.List;
import java.util.stream.Collectors;

import java.time.Instant;

/**
 * Mapper for converting between Application and ApplicationEntity.
 */
@Mapper(componentModel = "cdi",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ApplicationMapper {

    /**
     * Converts an ApplicationEntity to an Application DTO.
     *
     * @param entity the entity to convert
     * @return the converted DTO
     */
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toEpochMilli")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toEpochMilli")
    Application toDto(ApplicationEntity entity);

    /**
     * Converts a list of ApplicationEntity to a list of Application DTOs.
     *
     * @param entities the list of entities to convert
     * @return a list of converted DTOs
     */
    default List<Application> toDtoList(List<ApplicationEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts an Application DTO to an ApplicationEntity.
     *
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toInstant")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toInstant")
    @Mapping(target = "id", ignore = true) // Ignore ID as it's managed by the entity
    ApplicationEntity toEntity(Application dto);

    /**
     * Updates an existing entity with values from a DTO.
     *
     * @param dto    the DTO with updated values
     * @param entity the entity to update
     */
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toInstant")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toInstant")
    @Mapping(target = "id", ignore = true) // Ignore ID as it's managed by the entity
    @Mapping(target = "applicationKey", ignore = true) // Application key should not be updated
    void updateEntity(Application dto, @MappingTarget ApplicationEntity entity);

    /**
     * Converts Instant to epoch milliseconds.
     *
     * @param instant the Instant to convert, can be null
     * @return the epoch milliseconds, or null if the input is null
     */
    @Named("toEpochMilli")
    default Long toEpochMilli(Instant instant) {
        return instant != null ? instant.toEpochMilli() : null;
    }

    /**
     * Converts epoch milliseconds to Instant.
     *
     * @param epochMilli the epoch milliseconds to convert, can be null
     * @return the Instant, or null if the input is null
     */
    @Named("toInstant")
    default Instant toInstant(Long epochMilli) {
        return epochMilli != null ? Instant.ofEpochMilli(epochMilli) : null;
    }
}
