package org.bubna.task;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Builder
@Getter
public class TaskHistory {

    @Builder.Default
    private OffsetDateTime changeTime = OffsetDateTime.now();

    private TaskStatus from;

    private TaskStatus to;

}
