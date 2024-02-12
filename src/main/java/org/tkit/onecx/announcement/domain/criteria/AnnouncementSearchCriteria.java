package org.tkit.onecx.announcement.domain.criteria;

import java.time.OffsetDateTime;

import jakarta.validation.Valid;

import org.tkit.onecx.announcement.domain.models.Announcement;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class AnnouncementSearchCriteria {

    private Announcement.Type type;

    private Announcement.Priority priority;

    private Announcement.Status status;

    private @Valid OffsetDateTime startDateFrom;

    private @Valid OffsetDateTime startDateTo;

    private @Valid OffsetDateTime endDateFrom;

    private @Valid OffsetDateTime endDateTo;

    private @Valid String appId;

    private @Valid Integer pageNumber = 0;

    private @Valid Integer pageSize = 100;

    private @Valid String workspaceName;
}
