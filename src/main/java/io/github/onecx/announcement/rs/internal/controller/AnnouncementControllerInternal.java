package io.github.onecx.announcement.rs.internal.controller;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.tkit.quarkus.log.cdi.LogService;

import gen.io.github.onecx.announcement.rs.internal.AnnouncementInternalApi;
import gen.io.github.onecx.announcement.rs.internal.model.*;
import io.github.onecx.announcement.domain.daos.AnnouncementDAO;
import io.github.onecx.announcement.rs.internal.mapper.AnnouncementMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LogService
@ApplicationScoped
public class AnnouncementControllerInternal implements AnnouncementInternalApi {
    @Inject
    AnnouncementMapper mapper;
    @Inject
    AnnouncementDAO dao;

    @Context
    UriInfo uriInfo;

    @Override
    public Response createAnnouncement(CreateAnnouncementRequestDTO createAnnouncementRequestDTO) {
        var item = mapper.create(createAnnouncementRequestDTO);
        item = dao.create(item);
        return Response
                .created(uriInfo.getAbsolutePathBuilder().path(item.getId()).build())
                .entity(mapper.map(item))
                .build();
    }

    @Override
    public Response deleteAnnouncementById(String id) {
        dao.deleteQueryById(id);
        return Response.noContent().build();
    }

    @Override

    public Response getAllAppsWithAnnouncements() {
        var items = dao.findApplicationsWithAnnouncements();
        var results = mapper.map(items);
        return Response.ok(results).build();
    }

    @Override
    public Response getAnnouncementById(String id) {
        var item = dao.findById(id);
        if (item == null) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.ok(mapper.map(item)).build();
    }

    @Override
    public Response getAnnouncements(AnnouncementSearchCriteriaDTO announcementSearchCriteriaDTO) {
        var criteria = mapper.map(announcementSearchCriteriaDTO);
        var page = dao.loadAnnouncementByCriteria(criteria);
        var results = mapper.mapToPageResult(page);
        return Response.ok().entity(results).build();
    }

    @Override
    public Response updateAnnouncementById(String id, UpdateAnnouncementRequestDTO updateAnnouncementRequestDTO) {
        var item = dao.findById(id);
        if (item == null) {
            return Response.status(NOT_FOUND).build();
        }

        mapper.update(item, updateAnnouncementRequestDTO);
        item = dao.update(item);
        return Response.ok(item).build();
    }

    @ServerExceptionMapper
    public RestResponse<ProblemDetailResponseDTO> constraint(ConstraintViolationException ex) {
        return mapper.constraint(ex);
    }

}
