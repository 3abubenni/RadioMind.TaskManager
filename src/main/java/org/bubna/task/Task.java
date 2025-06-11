package org.bubna.task;

import com.fasterxml.jackson.annotation.JsonView;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bubna.data.DataEntity;
import org.bubna.util.View;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Task<F, T> extends PanacheEntity {

    @JsonView(View.GET.class)
    private long id;

    @NotNull
    @Type(JsonType.class)
    @Column(columnDefinition = "JSON")
    private F inputData;

    @JsonView(View.GET.class)
    @Type(JsonType.class)
    @Column(columnDefinition = "JSON")
    private T outputData;

    @NotNull
    @JsonView(View.GET.class)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @NotNull
    @Type(JsonType.class)
    @Column(columnDefinition = "JSON")
    private List<TaskHistory> taskHistory;

    @JsonView(View.GET.class)
    @ColumnDefault("NOW()")
    @Column(updatable = false, nullable = false)
    @Builder.Default
    private OffsetDateTime created = OffsetDateTime.now();

    @JsonView(View.GET.class)
    @ManyToOne
    @NotNull
    private DataEntity referencedData;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    public Task(TaskType type) {
        this.taskType = type;
    }

    public void setStatus(TaskStatus status) {
        if (taskHistory == null) {
            taskHistory = new ArrayList<>();
        }

        TaskHistory history = TaskHistory.builder()
                .from(this.status)
                .to(status)
                .build();

        taskHistory.add(history);
        this.status = status;
    }

}
