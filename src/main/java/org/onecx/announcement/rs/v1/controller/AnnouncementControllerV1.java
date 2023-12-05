package org.onecx.announcement.rs.v1.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status.*;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.onecx.announcement.domain.dao.AnnouncementDAO;
import org.onecx.announcement.domain.models.Announcement;
import org.onecx.announcement.rs.v1.mapper.AnnouncementMapperV1;
import org.tkit.quarkus.jpa.exceptions.DAOException;

import gen.io.github.onecx.announcement.v1.AnnouncementV1Api;
import gen.io.github.onecx.announcement.v1.model.AnnouncementDTOV1;
import gen.io.github.onecx.announcement.v1.model.AnnouncementPageResultDTOV1;
import gen.io.github.onecx.announcement.v1.model.DeleteAnnouncementRequestDTOV1;
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
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        AnnouncementDTOV1 announcementDto = announcementMapper.map(announcement);
        return Response.ok().entity(announcementDto).build();
    }

    @Override
    public Response getAnnouncementsByCriteriaV1(SearchAnnouncementRequestDTOV1 searchAnnouncementRequestDTOV1) {
        var results = announcementDAO.loadAnnouncementByCriteria(searchAnnouncementRequestDTOV1);
        if (results == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        AnnouncementPageResultDTOV1 announcementPageResultDTOV1 = announcementMapper.mapToPageResult(results);
        return Response.ok().entity(announcementPageResultDTOV1).build();
    }

    @Override
    @Transactional
    public Response deleteAnnouncementByCriteriaV1(DeleteAnnouncementRequestDTOV1 deleteAnnouncementRequestDTOV1) {
        try {
            for (String appId : deleteAnnouncementRequestDTOV1.getAppIds()) {
                announcementDAO.deleteByAppId(appId);
            }
            return Response.noContent().build();
        } catch (DAOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(announcementMapper.exception(ErrorKeys.ERROR_DELETE_ANNOUNCEMENT_BY_CRITERIA.name(),
                            "Failed to delete announcements. Error: " + e.getMessage()))
                    .build();
        }
    }
}
