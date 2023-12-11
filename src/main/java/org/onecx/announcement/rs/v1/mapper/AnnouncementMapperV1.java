package org.onecx.announcement.rs.v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.onecx.announcement.domain.models.Announcement;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;

import gen.io.github.onecx.announcement.v1.model.AnnouncementDTOV1;
import gen.io.github.onecx.announcement.v1.model.AnnouncementPageResultDTOV1;
import gen.io.github.onecx.announcement.v1.model.ProblemDetailResponseDTOV1;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface AnnouncementMapperV1 {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    @Mapping(target = "modificationUser", ignore = true)
    @Mapping(target = "controlTraceabilityManual", ignore = true)
    @Mapping(target = "modificationCount", ignore = true)
    @Mapping(target = "persisted", ignore = true)
    Announcement map(AnnouncementDTOV1 dto);

    @Mapping(target = "version", ignore = true)
    AnnouncementDTOV1 map(Announcement data);

    @Mapping(target = "removeStreamItem", ignore = true)
    AnnouncementPageResultDTOV1 mapToPageResult(PageResult<Announcement> announcementItemsPageResults);

    @Mapping(target = "params", ignore = true)
    @Mapping(target = "removeParamsItem", ignore = true)
    @Mapping(target = "invalidParams", ignore = true)
    @Mapping(target = "removeInvalidParamsItem", ignore = true)
    ProblemDetailResponseDTOV1 exception(String errorCode, String detail);

}
