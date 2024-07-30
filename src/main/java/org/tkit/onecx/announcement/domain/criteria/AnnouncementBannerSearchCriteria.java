package org.tkit.onecx.announcement.domain.criteria;

import java.time.OffsetDateTime;

import jakarta.validation.Valid;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class AnnouncementBannerSearchCriteria {

    private @Valid OffsetDateTime currentDate;

    private @Valid String productName;

    private @Valid String workspaceName;

    private @Valid Integer pageNumber = 0;

    private @Valid Integer pageSize = 100;

}
