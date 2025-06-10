package org.bubna.paging;

import java.util.Collection;

public record PageOfEntities<T>(
        Collection<T> entities,
        int page,
        int pageSize,
        int totalPages
) { }
