package org.bubna.data;

import com.fasterxml.jackson.annotation.JsonView;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bubna.data.content.Content;
import org.bubna.util.View;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class DataEntity extends PanacheEntity {

    @JsonView(View.GET.class)
    @Getter
    private long id;

    @NotNull
    private String sourceUrl;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @Valid
    private List<Content> content;

    @Column(updatable = false, insertable = false)
    @ColumnDefault("NOW()")
    @JsonView(View.GET.class)
    private OffsetDateTime created = OffsetDateTime.now();

}
