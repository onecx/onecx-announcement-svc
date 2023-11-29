package org.onecx.announcement.domain.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import org.onecx.announcement.domain.enums.AnnouncementPriorityType;
import org.onecx.announcement.domain.enums.AnnouncementStatus;
import org.onecx.announcement.domain.enums.AnnouncementType;
import org.tkit.quarkus.jpa.models.TraceableEntity;

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
    private AnnouncementType type;
    @Enumerated(EnumType.STRING)
    private AnnouncementPriorityType priority;
    @Enumerated(EnumType.STRING)
    private AnnouncementStatus status;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "app_id")
    private String appId;
}
