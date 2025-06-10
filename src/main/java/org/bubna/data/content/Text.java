package org.bubna.data.content;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Text extends Content {

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String text;

    public Text() {
        super(ContentType.TEXT);
    }

}
