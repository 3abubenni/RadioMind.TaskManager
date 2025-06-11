package org.bubna.task.rabbitmq;

import lombok.Builder;
import lombok.Getter;
import org.bubna.task.TaskStatus;
import org.bubna.task.TaskType;

@Builder
@Getter
public class TaskRabbitMqMessage<T, F> {

    private long id;

    private T inputData;

    private F outputData;

    private TaskType type;

    private TaskStatus status;

}
