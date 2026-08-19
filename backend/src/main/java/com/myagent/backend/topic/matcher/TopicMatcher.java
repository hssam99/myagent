package com.myagent.backend.topic.matcher;

import com.myagent.backend.topic.entity.Topic;
import com.myagent.backend.topic.entity.TopicAlias;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class TopicMatcher {
    public Set<Topic> match(String text, List<TopicAlias> aliases) {
        Set<Topic> matchedTopics = new HashSet<>();
        if (text == null || text.isBlank()) { return matchedTopics; }
        for (TopicAlias alias : aliases) {
            if (matches(text, alias)) matchedTopics.add(alias.getTopic());
        }
        return matchedTopics;
    }

    private boolean matches(String text, TopicAlias alias) {
        return switch (alias.getMatchType()){
            case CONTAINS ->
                    text.toLowerCase(Locale.ROOT)
                            .contains(alias.getAlias().toLowerCase(Locale.ROOT));
            case WORD_BOUNDARY ->
                    Pattern.compile("\\b" + Pattern.quote(alias.getAlias())
                            + "\\b", Pattern.CASE_INSENSITIVE)
                            .matcher(text)
                            .find();
        };
    }
}

