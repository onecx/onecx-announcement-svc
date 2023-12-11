package io.github.onecx.announcement.rs.v1.log;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

import org.tkit.quarkus.log.cdi.LogParam;

import gen.io.github.onecx.announcement.v1.model.AnnouncementSearchCriteriaDTOV1;

@ApplicationScoped
public class ExternalLogParamV1 implements LogParam {

    @Override
    public List<Item> getClasses() {
        return List.of(
                item(10, AnnouncementSearchCriteriaDTOV1.class, x -> {
                    AnnouncementSearchCriteriaDTOV1 d = (AnnouncementSearchCriteriaDTOV1) x;
                    return AnnouncementSearchCriteriaDTOV1.class.getSimpleName() + "[" + d.getPageNumber() + ","
                            + d.getPageSize()
                            + "]";
                }));
    }
}
