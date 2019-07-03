package io.todos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MetadataEndpoint {

    @Autowired
    private Environment env;

    @GetMapping("/metadata")
    public List<Metadata> metadata() {
        Map<String, Object> allProperties = new HashMap<>();
        for (PropertySource<?> ps : ((AbstractEnvironment)env).getPropertySources()) {
            if (ps instanceof MapPropertySource) {
                allProperties.putAll(((MapPropertySource) ps).getSource());
            }
        }
        List<Metadata> metadata = allProperties.entrySet().stream()
                .filter(it -> !StringUtils.isEmpty(it.getValue()))
                .sorted(Map.Entry.comparingByKey())
                .map(it -> Metadata.builder()
                        .property(it.getKey())
                        .value(it.getValue().toString()).build())
                .collect(Collectors.toList());

        return metadata;
    }
}
