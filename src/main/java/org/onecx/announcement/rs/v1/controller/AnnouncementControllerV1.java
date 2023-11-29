package org.onecx.announcement.rs.v1.controller;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.onecx.announcement.domain.dao.AnnouncementDAO;
import org.onecx.announcement.domain.models.Announcement;
import org.onecx.announcement.rs.v1.mapper.AnnouncementMapperV1;

import gen.io.github.onecx.announcement.v1.AnnouncementV1Api;
import gen.io.github.onecx.announcement.v1.model.AnnouncementDTOV1;
import gen.io.github.onecx.announcement.v1.model.AnnouncementPageResultDTOV1;
import gen.io.github.onecx.announcement.v1.model.SearchAnnouncementRequestDTOV1;
import lombok.extern.slf4j.Slf4j;

@Path("/v1/applications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
@Tag(name = "AnnouncementV1")
public class AnnouncementControllerV1 implements AnnouncementV1Api {

    @Inject
    AnnouncementMapperV1 announcementMapper;
    @Inject
    AnnouncementDAO announcementDAO;

    @Override
    public Response getAnnouncementByIdV1(String id) {
        Announcement announcement = announcementDAO.findById(id);
        if (announcement == null) {
            return Response.status(NOT_FOUND)
                    .entity(announcementMapper.exception(ErrorKeys.ERROR_NO_ANNOUNCEMENTS_FOUND_WITH_GIVEN_CRITERIA.toString(),
                            "Could not find announcement with ID = '" + id + "'"))
                    .build();
        }
        AnnouncementDTOV1 announcementDto = announcementMapper.map(announcement);
        return Response.ok().entity(announcementDto).build();
    }

    @Override
    public Response getAnnouncementsByCriteriaV1(@NotNull SearchAnnouncementRequestDTOV1 searchAnnouncementRequestDTOV1) {
        var results = announcementDAO.loadAnnouncementByCriteria(searchAnnouncementRequestDTOV1);
        if (results == null) {
            return Response.status(NOT_FOUND)
                    .entity(announcementMapper.exception(ErrorKeys.ERROR_NO_ANNOUNCEMENTS_FOUND_WITH_GIVEN_CRITERIA.toString(),
                            "Announcement with given criteria not found"))
                    .build();
        }
        AnnouncementPageResultDTOV1 announcementPageResultDTOV1 = announcementMapper.mapToPageResult(results);
        return Response.ok().entity(announcementPageResultDTOV1).build();
    }

    public enum ErrorKeys {
        ERROR_NO_ANNOUNCEMENTS_FOUND_WITH_GIVEN_CRITERIA,

        ERROR_NO_ANNOUNCEMENTS_FOUND_WITH_GIVEN_ID;
    }
}
