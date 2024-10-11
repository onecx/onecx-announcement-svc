package org.tkit.onecx.announcement.domain.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.hibernate.annotations.TenantId;
import org.tkit.quarkus.jpa.models.TraceableEntity;

import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "ANNOUNCEMENT", indexes = {
        @Index(name = "START_STATUS_IDX", columnList = "STARTDATE, STATUS, TENANT_ID"),
        @Index(name = "PRODUCT_IDX", columnList = "PRODUCT_NAME, TENANT_ID"),
        @Index(name = "WORKSPACE_IDX", columnList = "WORKSPACENAME, TENANT_ID")
})
@SuppressWarnings("java:S2160")
public class Announcement extends TraceableEntity {

    @TenantId
    @Column(name = "TENANT_ID")
    private String tenantId;

    @Column(name = "title")
    private String title;

    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "appearance", columnDefinition = "varchar(255) default 'ALL'")
    @Enumerated(EnumType.STRING)
    private Appearance appearance = Appearance.ALL;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "startDate")
    private LocalDateTime startDate;

    @Column(name = "endDate")
    private LocalDateTime endDate;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "workspaceName")
    private String workspaceName;

    public enum Type {
        EVENT,
        INFO,
        SYSTEM_MAINTENANCE;
    }

    public enum Priority {
        IMPORTANT,
        NORMAL,
        LOW;
    }

    public enum Status {
        ACTIVE,
        INACTIVE;
    }

    public enum Appearance {
        BANNER,
        WELCOME,
        ALL;
    }

}
