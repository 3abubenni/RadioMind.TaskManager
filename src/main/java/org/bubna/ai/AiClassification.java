package org.bubna.ai;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Builder
@Getter
public class AiClassification {

    private String[] tags;

    private String classification;

    private String model;

    private String version;

    private OffsetDateTime begin;

    private OffsetDateTime end;

}
