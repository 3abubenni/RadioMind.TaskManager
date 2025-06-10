package org.bubna.data;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bubna.paging.PageOfEntities;
import org.bubna.task.TaskService;

@Singleton
@Slf4j
@RequiredArgsConstructor
public class DataService {

    private final TaskService taskService;

    @Transactional
    public DataEntity saveData(DataEntity data) {
        log.info("saving data");
        data.persist();
        log.info("saved data by id {}", data.getId());

        taskService.createClassificationTask(data);
        return data;
    }

    public PageOfEntities<DataEntity> getData(int page, int pageSize) {
        log.info("Trying to get data from page {} and page size {}", page, pageSize);

        PanacheQuery<DataEntity> dataEntityPanacheQuery = DataEntity
                .find("order by id desc")
                .page(page, pageSize);

        return new PageOfEntities<>(
                dataEntityPanacheQuery.list(),
                page,
                pageSize,
                dataEntityPanacheQuery.pageCount()
        );
    }

}
