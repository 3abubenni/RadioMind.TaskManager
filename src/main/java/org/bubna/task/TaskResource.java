package org.bubna.task;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import lombok.RequiredArgsConstructor;
import org.bubna.paging.PageOfEntities;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/task")
@ApplicationScoped
@RequiredArgsConstructor
@Tag(description = "Контроллер для управления задачами", name = "Tag Controller")
public class TaskResource {

    final TaskService taskService;

    @GET
    public PageOfEntities<Task<?, ?>> getTasks(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("10") int pageSize
    ) {
        return taskService.getTasks(page, pageSize);
    }

}
