package urujdas.social.impl.vk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VkUserCity {
    private final Long id;
    private final String title;

    @JsonCreator
    public VkUserCity(@JsonProperty("id") Long id,
                         @JsonProperty("title") String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
