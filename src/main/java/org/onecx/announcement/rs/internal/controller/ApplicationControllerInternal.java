package org.onecx.announcement.rs.internal.controller;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.onecx.announcement.domain.dao.AnnouncementDAO;
import org.onecx.announcement.domain.models.Announcement;
import org.onecx.announcement.rs.internal.mapper.AnnouncementMapper;
import org.onecx.announcement.rs.v1.controller.ErrorKeys;
import org.tkit.quarkus.jpa.exceptions.ConstraintException;
import org.tkit.quarkus.jpa.exceptions.DAOException;

import gen.io.github.onecx.announcement.rs.internal.AnnouncementInternalApi;
import gen.io.github.onecx.announcement.rs.internal.model.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Path("/internal/announcements")
public class ApplicationControllerInternal implements AnnouncementInternalApi {
    @Inject
    AnnouncementMapper announcementMapper;
    @Inject
    AnnouncementDAO announcementDAO;

    @Context
    UriInfo uriInfo;

    @Override
    public Response addAnnouncement(CreateAnnouncementRequestDTO createAnnouncementRequestDTO) {
        Announcement announcementItem = announcementMapper.create(createAnnouncementRequestDTO);
        Announcement responseAnnouncementItem = announcementDAO.create(announcementItem);
        return Response
                .created(uriInfo.getAbsolutePathBuilder().path(responseAnnouncementItem.getId()).build())
                .entity(announcementMapper.map(responseAnnouncementItem))
                .build();
    }

    @Override
    public Response deleteAnnouncementById(String id) {
        try {
            Announcement announcement = announcementDAO.findById(id);
            if (announcement != null) {
                announcementDAO.deleteQueryById(id);
                return Response.noContent().build();
            }
        } catch (DAOException e) {
            e.printStackTrace();
            return Response.status(BAD_REQUEST)
                    .entity(announcementMapper.exception(ErrorKeys.ERROR_DELETE_ANNOUNCEMENT_BY_CRITERIA.name(),
                            "Failed to delete announcements. Error: " + e.getMessage()))
                    .build();
        }
        return Response.status(NOT_FOUND).build();
    }

    @Override

    public Response getAllAppsWithAnnouncements() {
        List<String> resultList = announcementDAO.findApplicationsWithAnnouncements();
        return Response.ok(resultList).build();
    }

    @Override
    public Response getAnnouncementById(String id) {
        Announcement fetchedAnnouncement = announcementDAO.findById(id);
        if (fetchedAnnouncement == null) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.ok(announcementMapper.map(fetchedAnnouncement)).build();
    }

    @Override
    public Response getAnnouncements(SearchAnnouncementRequestDTO searchAnnouncementRequestDTO) {
        log.info("INSIDE SEARCH SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
        var results = announcementDAO.loadAnnouncementByCriteria(announcementMapper.mapCriteria(searchAnnouncementRequestDTO));
        if (results == null) {
            return Response.status(NOT_FOUND).build();
        }
        AnnouncementPageResultDTO announcementPageResultDTO = announcementMapper.mapToPageResult(results);
        return Response.ok().entity(announcementPageResultDTO).build();
    }

    @Override
    public Response updateAnnouncementById(String id, UpdateAnnouncementRequestDTO updateAnnouncementRequestDTO) {
        Announcement acItem = announcementDAO.findById(id);
        if (acItem != null) {
            announcementDAO.update(announcementMapper.update(acItem, updateAnnouncementRequestDTO));
            Announcement updatedAnnouncement = announcementDAO.findById(id);
            return Response.ok(updatedAnnouncement).build();
        }
        return Response.status(NOT_FOUND).build();

    }

    @ServerExceptionMapper
    public RestResponse<ProblemDetailResponseDTO> exception(ConstraintException ex) {
        return announcementMapper.exception(ex);
    }

    @ServerExceptionMapper
    public RestResponse<ProblemDetailResponseDTO> constraint(ConstraintViolationException ex) {
        return announcementMapper.constraint(ex);
    }
}
