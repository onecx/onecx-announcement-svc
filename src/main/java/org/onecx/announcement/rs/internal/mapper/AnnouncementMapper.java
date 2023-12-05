package org.onecx.announcement.rs.internal.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.onecx.announcement.domain.models.Announcement;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.jpa.exceptions.ConstraintException;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;

import gen.io.github.onecx.announcement.rs.internal.model.*;
import gen.io.github.onecx.announcement.v1.model.AnnouncementDTOV1;
import gen.io.github.onecx.announcement.v1.model.SearchAnnouncementRequestDTOV1;

@Mapper(uses = OffsetDateTimeMapper.class)
public abstract class AnnouncementMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "controlTraceabilityManual", ignore = true)
    @Mapping(target = "modificationCount", ignore = true)
    @Mapping(target = "persisted", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    @Mapping(target = "modificationUser", ignore = true)
    public abstract Announcement create(CreateAnnouncementRequestDTO dto);

    public abstract SearchAnnouncementRequestDTOV1 mapCriteria(SearchAnnouncementRequestDTO searchAnnouncementRequestDTO);

    @Mapping(target = "version", ignore = true)
    public abstract AnnouncementDTO map(Announcement announcement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    @Mapping(target = "modificationUser", ignore = true)
    @Mapping(target = "controlTraceabilityManual", ignore = true)
    @Mapping(target = "modificationCount", ignore = true)
    @Mapping(target = "persisted", ignore = true)
    public abstract Announcement update(@MappingTarget Announcement announcement, UpdateAnnouncementRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    @Mapping(target = "modificationUser", ignore = true)
    @Mapping(target = "controlTraceabilityManual", ignore = true)
    @Mapping(target = "modificationCount", ignore = true)
    @Mapping(target = "persisted", ignore = true)
    public abstract Announcement map(AnnouncementDTOV1 dto);

    @Mapping(target = "removeStreamItem", ignore = true)
    //  @Mapping(target = "stream.version", ignore = true)
    public abstract AnnouncementPageResultDTO mapToPageResult(PageResult<Announcement> announcementItemsPageResults);

    @Mapping(target = "params", ignore = true)
    @Mapping(target = "removeParamsItem", ignore = true)
    @Mapping(target = "invalidParams", ignore = true)
    @Mapping(target = "removeInvalidParamsItem", ignore = true)
    public abstract ProblemDetailResponseDTO exception(String errorCode, String detail);

    public RestResponse<ProblemDetailResponseDTO> constraint(ConstraintViolationException ex) {
        var dto = exception("CONSTRAINT_VIOLATIONS", ex.getMessage());
        dto.setInvalidParams(createErrorValidationResponse(ex.getConstraintViolations()));
        return RestResponse.status(Response.Status.BAD_REQUEST, dto);
    }

    public RestResponse<ProblemDetailResponseDTO> exception(ConstraintException ex) {
        var dto = exception(ex.getMessageKey().name(), ex.getConstraints());
        dto.setParams(map(ex.namedParameters));
        return RestResponse.status(Response.Status.BAD_REQUEST, dto);
    }

    public List<ProblemDetailParamDTO> map(Map<String, Object> params) {
        if (params == null) {
            return List.of();
        }
        return params.entrySet().stream().map(e -> {
            var item = new ProblemDetailParamDTO();
            item.setKey(e.getKey());
            if (e.getValue() != null) {
                item.setValue(e.getValue().toString());
            }
            return item;
        }).toList();
    }

    public abstract List<ProblemDetailInvalidParamDTO> createErrorValidationResponse(
            Set<ConstraintViolation<?>> constraintViolation);

    @Mapping(target = "name", source = "propertyPath")
    @Mapping(target = "message", source = "message")
    public abstract ProblemDetailInvalidParamDTO createError(ConstraintViolation<?> constraintViolation);

    public String mapPath(Path path) {
        return path.toString();
    }
}
