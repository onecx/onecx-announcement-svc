package org.tkit.onecx.announcement.rs.internal.log;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

import org.tkit.quarkus.log.cdi.LogParam;

import gen.org.tkit.onecx.announcement.rs.internal.model.AnnouncementSearchCriteriaDTO;
import gen.org.tkit.onecx.announcement.rs.internal.model.CreateAnnouncementRequestDTO;
import gen.org.tkit.onecx.announcement.rs.internal.model.UpdateAnnouncementRequestDTO;

@ApplicationScoped
public class InternalLogParam implements LogParam {

    @Override
    public List<Item> getClasses() {
        return List.of(
                item(10, CreateAnnouncementRequestDTO.class, x -> {
                    CreateAnnouncementRequestDTO d = (CreateAnnouncementRequestDTO) x;
                    return CreateAnnouncementRequestDTO.class.getSimpleName() + "[ productName:" + d.getProductName() + "]";
                }),
                item(10, UpdateAnnouncementRequestDTO.class, x -> {
                    UpdateAnnouncementRequestDTO d = (UpdateAnnouncementRequestDTO) x;
                    return UpdateAnnouncementRequestDTO.class.getSimpleName() + "[ productName:" + d.getProductName() + "]";
                }),
                item(10, AnnouncementSearchCriteriaDTO.class, x -> {
                    AnnouncementSearchCriteriaDTO d = (AnnouncementSearchCriteriaDTO) x;
                    return AnnouncementSearchCriteriaDTO.class.getSimpleName() + "[" + d.getPageNumber() + ","
                            + d.getPageSize()
                            + "]";
                }));
    }
}
