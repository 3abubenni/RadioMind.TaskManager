package org.bubna.task;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bubna.ai.AiClassification;
import org.bubna.data.DataEntity;
import org.bubna.paging.PageOfEntities;
import org.bubna.task.rabbitmq.TaskRabbitMqProducer;

import java.util.List;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    final TaskRabbitMqProducer rabbitMqProducer;

    @Transactional
    public Task<DataEntity, AiClassification> createClassificationTask(DataEntity data) {
        log.info("Creating Classification Task");

        Task<DataEntity, AiClassification> task = Task.<DataEntity, AiClassification>builder()
                .inputData(data)
                .taskType(TaskType.CLASSIFICATION)
                .referencedData(data)
                .build();

        task.setStatus(TaskStatus.PENDING);
        task.persist();

        log.info("Classification Task created with id '{}'", task.getId());

        return task;
    }

    public PageOfEntities<Task<?, ?>> getTasks(int page, int pageSize) {
        log.info("Getting Page Tasks by page '{}' with pageSize '{}'", page, pageSize);

        PanacheQuery<Task<?, ?>> taskPanacheQuery = Task.find("order by id desc").page(page, pageSize);

        return new PageOfEntities<>(
                taskPanacheQuery.list(),
                page,
                pageSize,
                taskPanacheQuery.pageCount()
        );
    }

    public void sentPendingTasksToQueue() {
        log.info("Start Sending Pending Tasks to Queue");
        List<Task<?, ?>> tasks = Task.find("status", TaskStatus.PENDING).list();
        log.info("{} Tasks found", tasks.size());
        int sentTasks = tasks.stream()
                .map(this::sendPendingTaskToQueue)
                .mapToInt(taskSent -> taskSent ? 1 : 0)
                .sum();

        log.info("{} Tasks sent", sentTasks);
    }

    @Transactional
    protected boolean sendPendingTaskToQueue(Task<?, ?> task) {
        log.info("Sending Task with id '{}' to queue", task.getId());
        try {
            task.setStatus(TaskStatus.SENT);
            task.persist();
            rabbitMqProducer.sendTask(task);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

}
