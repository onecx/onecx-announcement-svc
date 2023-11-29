package org.onecx.announcement.rs.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.onecx.announcement.domain.models.Announcement;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;

import gen.io.github.onecx.announcement.rs.internal.model.*;
import gen.io.github.onecx.announcement.v1.model.AnnouncementDTOV1;
import gen.io.github.onecx.announcement.v1.model.ProblemDetailResponseDTOV1;
import gen.io.github.onecx.announcement.v1.model.SearchAnnouncementRequestDTOV1;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface AnnouncementMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "controlTraceabilityManual", ignore = true)
    @Mapping(target = "modificationCount", ignore = true)
    @Mapping(target = "persisted", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    @Mapping(target = "modificationUser", ignore = true)
    Announcement create(CreateAnnouncementRequestDTO dto);

    SearchAnnouncementRequestDTOV1 mapCriteria(SearchAnnouncementRequestDTO searchAnnouncementRequestDTO);

    @Mapping(target = "version", ignore = true)
    AnnouncementDTO map(Announcement announcement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    @Mapping(target = "modificationUser", ignore = true)
    @Mapping(target = "controlTraceabilityManual", ignore = true)
    @Mapping(target = "modificationCount", ignore = true)
    @Mapping(target = "persisted", ignore = true)
    void update(@MappingTarget Announcement announcement, UpdateAnnouncementRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    @Mapping(target = "modificationUser", ignore = true)
    @Mapping(target = "controlTraceabilityManual", ignore = true)
    @Mapping(target = "modificationCount", ignore = true)
    @Mapping(target = "persisted", ignore = true)
    Announcement map(AnnouncementDTOV1 dto);

    @Mapping(target = "removeStreamItem", ignore = true)
    //  @Mapping(target = "stream.version", ignore = true)
    AnnouncementPageResultDTO mapToPageResult(PageResult<Announcement> announcementItemsPageResults);

    @Mapping(target = "params", ignore = true)
    @Mapping(target = "removeParamsItem", ignore = true)
    @Mapping(target = "invalidParams", ignore = true)
    @Mapping(target = "removeInvalidParamsItem", ignore = true)
    ProblemDetailResponseDTOV1 exception(String errorCode, String detail);

}
