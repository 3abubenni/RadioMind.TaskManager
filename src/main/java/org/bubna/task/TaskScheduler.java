package org.bubna.task;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class TaskScheduler {

    final TaskService taskService;

    @Transactional
    @Scheduled(cron = "0 * * * * ?")
    public void startTaskAndSendToAi() {
        log.info("Starting task and send to ai");
        taskService.sentPendingTasksToQueue();
    }


}
