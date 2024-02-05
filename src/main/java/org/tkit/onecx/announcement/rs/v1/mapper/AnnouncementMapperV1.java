package org.tkit.onecx.announcement.rs.v1.mapper;

import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tkit.onecx.announcement.domain.criteria.AnnouncementSearchCriteria;
import org.tkit.onecx.announcement.domain.models.Announcement;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;

import gen.org.tkit.onecx.announcement.v1.model.*;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface AnnouncementMapperV1 {

    @Mapping(target = "title", ignore = true)
    AnnouncementSearchCriteria map(AnnouncementSearchCriteriaDTOV1 dto);

    AnnouncementDTOV1 map(Announcement data);

    @Mapping(target = "removeStreamItem", ignore = true)
    AnnouncementPageResultDTOV1 mapToPageResult(PageResult<Announcement> announcementItemsPageResults);

    default RestResponse<ProblemDetailResponseDTOV1> constraint(ConstraintViolationException ex) {
        var dto = exception("CONSTRAINT_VIOLATIONS", ex.getMessage());
        dto.setInvalidParams(createErrorValidationResponse(ex.getConstraintViolations()));
        return RestResponse.status(Response.Status.BAD_REQUEST, dto);
    }

    List<ProblemDetailInvalidParamDTOV1> createErrorValidationResponse(
            Set<ConstraintViolation<?>> constraintViolation);

    @Mapping(target = "name", source = "propertyPath")
    @Mapping(target = "message", source = "message")
    public abstract ProblemDetailInvalidParamDTOV1 createError(ConstraintViolation<?> constraintViolation);

    @Mapping(target = "invalidParams", ignore = true)
    @Mapping(target = "removeInvalidParamsItem", ignore = true)
    @Mapping(target = "removeParamsItem", ignore = true)
    @Mapping(target = "params", ignore = true)
    ProblemDetailResponseDTOV1 exception(String errorCode, String detail);

    default String mapPath(Path path) {
        return path.toString();
    }
}
