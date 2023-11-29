package org.onecx.announcement.rs.internal.controller;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.onecx.announcement.domain.dao.AnnouncementDAO;
import org.onecx.announcement.domain.models.Announcement;
import org.onecx.announcement.rs.internal.mapper.AnnouncementMapper;
import org.onecx.announcement.rs.v1.controller.AnnouncementControllerV1;

import gen.io.github.onecx.announcement.rs.internal.AnnouncementInternalApi;
import gen.io.github.onecx.announcement.rs.internal.model.AnnouncementPageResultDTO;
import gen.io.github.onecx.announcement.rs.internal.model.CreateAnnouncementRequestDTO;
import gen.io.github.onecx.announcement.rs.internal.model.SearchAnnouncementRequestDTO;
import gen.io.github.onecx.announcement.rs.internal.model.UpdateAnnouncementRequestDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Path("/internal/announcements")
public class ApplicationControllerInternal implements AnnouncementInternalApi {
    public static final String ANNOUNCEMENT_WITH_GIVEN_ID_NOT_FOUND = "Announcement with given ID not found";
    @Inject
    AnnouncementMapper announcementMapper;
    @Inject
    AnnouncementDAO announcementDAO;

    @Context
    UriInfo uriInfo;

    @Override
    public Response addAnnouncement(@NotNull CreateAnnouncementRequestDTO createAnnouncementRequestDTO) {
        Announcement announcementItem = announcementMapper.create(createAnnouncementRequestDTO);
        Announcement responseAnnouncementItem = announcementDAO.create(announcementItem);
        return Response
                .created(uriInfo.getAbsolutePathBuilder().path(responseAnnouncementItem.getId()).build())
                .entity(announcementMapper.map(responseAnnouncementItem))
                .build();
    }

    @Override
    public Response deleteAnnouncementById(String id) {
        Announcement acItem = announcementDAO.findById(id);
        if (acItem != null) {
            announcementDAO.delete(acItem);
            return Response.noContent().build();
        }
        return Response.status(NOT_FOUND)
                .entity(announcementMapper.exception(
                        AnnouncementControllerV1.ErrorKeys.ERROR_NO_ANNOUNCEMENTS_FOUND_WITH_GIVEN_ID.toString(),
                        ANNOUNCEMENT_WITH_GIVEN_ID_NOT_FOUND))
                .build();
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
            return Response.status(NOT_FOUND)
                    .entity(announcementMapper.exception(
                            AnnouncementControllerV1.ErrorKeys.ERROR_NO_ANNOUNCEMENTS_FOUND_WITH_GIVEN_ID.toString(),
                            ANNOUNCEMENT_WITH_GIVEN_ID_NOT_FOUND))
                    .build();
        }
        return Response.ok(announcementMapper.map(fetchedAnnouncement)).build();
    }

    @Override
    public Response getAnnouncements(@NotNull SearchAnnouncementRequestDTO searchAnnouncementRequestDTO) {
        var results = announcementDAO.loadAnnouncementByCriteria(announcementMapper.mapCriteria(searchAnnouncementRequestDTO));
        if (results == null) {
            return Response.status(NOT_FOUND)
                    .entity(announcementMapper.exception(
                            AnnouncementControllerV1.ErrorKeys.ERROR_NO_ANNOUNCEMENTS_FOUND_WITH_GIVEN_CRITERIA.toString(),
                            "Announcement with given criteria not found"))
                    .build();
        }
        AnnouncementPageResultDTO announcementPageResultDTO = announcementMapper.mapToPageResult(results);
        return Response.ok().entity(announcementPageResultDTO).build();
    }

    @Override
    public Response updateAnnouncementById(String id, UpdateAnnouncementRequestDTO updateAnnouncementRequestDTO) {
        Announcement acItem = announcementDAO.findById(id);
        if (acItem != null) {
            announcementMapper.update(acItem, updateAnnouncementRequestDTO);
            Announcement updatedAnnouncement = announcementDAO.findById(id);
            return Response.ok(updatedAnnouncement).build();
        }
        return Response.status(NOT_FOUND)
                .entity(announcementMapper.exception(
                        AnnouncementControllerV1.ErrorKeys.ERROR_NO_ANNOUNCEMENTS_FOUND_WITH_GIVEN_ID.toString(),
                        ANNOUNCEMENT_WITH_GIVEN_ID_NOT_FOUND))
                .build();

    }
}
