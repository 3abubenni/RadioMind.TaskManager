package org.bubna.data;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import lombok.RequiredArgsConstructor;
import org.bubna.paging.PageOfEntities;
import org.bubna.util.View;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/data")
@ApplicationScoped
@RequiredArgsConstructor
@Tag(description = "Контроллер для управления данными", name = "Data Controller")
public class DataResource {

    private final DataService dataService;

    @Operation(description = "Добавить данные на обработку")
    @POST
    @JsonView(View.GET.class)
    public DataEntity saveData(@JsonView(View.POST.class) @Valid DataEntity entity) {
        return dataService.saveData(entity);
    }

    @Operation(description = "Получить собранные данные")
    @GET
    @JsonView(View.GET.class)
    public PageOfEntities<DataEntity> getData(
            @QueryParam(value = "page") @DefaultValue(value = "0") int page,
            @QueryParam(value = "pageSize") @DefaultValue(value = "10") int pageSize
    ) {
        return dataService.getData(page, pageSize);
    }

}
