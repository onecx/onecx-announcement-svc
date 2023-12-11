package io.github.onecx.announcement.rs.v1.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.tkit.quarkus.log.cdi.LogService;

import gen.io.github.onecx.announcement.v1.AnnouncementV1Api;
import gen.io.github.onecx.announcement.v1.model.AnnouncementSearchCriteriaDTOV1;
import gen.io.github.onecx.announcement.v1.model.ProblemDetailResponseDTOV1;
import io.github.onecx.announcement.domain.daos.AnnouncementDAO;
import io.github.onecx.announcement.rs.v1.mapper.AnnouncementMapperV1;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LogService
@ApplicationScoped
public class AnnouncementControllerV1 implements AnnouncementV1Api {

    @Inject
    AnnouncementMapperV1 mapper;
    @Inject
    AnnouncementDAO dao;

    @Override
    public Response getAnnouncementsByCriteria(AnnouncementSearchCriteriaDTOV1 announcementSearchCriteriaDTOV1) {
        var criteria = mapper.map(announcementSearchCriteriaDTOV1);
        var results = dao.loadAnnouncementByCriteria(criteria);
        return Response.ok().entity(mapper.mapToPageResult(results)).build();
    }

    @ServerExceptionMapper
    public RestResponse<ProblemDetailResponseDTOV1> constraint(ConstraintViolationException ex) {
        return mapper.constraint(ex);
    }
}
