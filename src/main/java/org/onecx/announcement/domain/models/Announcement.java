package org.onecx.announcement.domain.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import org.tkit.quarkus.jpa.models.TraceableEntity;

import gen.io.github.onecx.announcement.rs.internal.model.AnnouncementPriorityTypeDTO;
import gen.io.github.onecx.announcement.rs.internal.model.AnnouncementStatusDTO;
import gen.io.github.onecx.announcement.rs.internal.model.AnnouncementTypeDTO;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "announcement")
@SuppressWarnings("java:S2160")
public class Announcement extends TraceableEntity {

    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private AnnouncementTypeDTO type;
    @Enumerated(EnumType.STRING)
    private AnnouncementPriorityTypeDTO priority;
    @Enumerated(EnumType.STRING)
    private AnnouncementStatusDTO status;
    @Column(name = "startDate")
    private LocalDateTime startDate;
    @Column(name = "endDate")
    private LocalDateTime endDate;
    @Column(name = "appId")
    private String appId;
}
