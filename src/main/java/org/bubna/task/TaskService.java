package org.bubna.task;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bubna.ai.AiClassification;
import org.bubna.data.DataEntity;
import org.bubna.paging.PageOfEntities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

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

}
