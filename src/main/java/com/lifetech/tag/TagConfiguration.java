package com.lifetech.tag;

import com.lifetech.tag.db.Tag;
import com.lifetech.tag.db.TagRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.function.Function;

@Configuration
public class TagConfiguration {

    @Bean
    public Function<Tag, Tag> saveTag(TagRepository tagRespository) {
        return tagRespository::save;
    }

    @Bean
    public Function<Long, Optional<Tag>> findTagById(TagRepository tagRespository) {
        return tagRespository::findById;
    }

    @Bean
    public Function<String, Tag> findTagByName(TagRepository tagRespository) {
        return tagRespository::findByName;
    }

}
