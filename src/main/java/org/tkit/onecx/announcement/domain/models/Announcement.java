package org.tkit.onecx.announcement.domain.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.hibernate.annotations.TenantId;
import org.tkit.quarkus.jpa.models.TraceableEntity;

import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "announcement", indexes = {
        @Index(name = "start_status_idx", columnList = "startDate, status, TENANT_ID"),
        @Index(name = "product_idx", columnList = "PRODUCT_NAME, TENANT_ID"),
        @Index(name = "workspace_idx", columnList = "workspaceName, TENANT_ID")
})
@SuppressWarnings("java:S2160")
public class Announcement extends TraceableEntity {

    @TenantId
    @Column(name = "TENANT_ID")
    private String tenantId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

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

}
