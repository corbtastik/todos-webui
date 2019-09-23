package io.todos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

@RestController
public class WebAPI {

    private static final Logger LOG = LoggerFactory.getLogger(WebAPI.class);

    private final Environment env;

    private final BuildProperties buildProperties;

    private final TodosProperties properties;

    @Autowired
    public WebAPI(Environment environment, BuildProperties buildProperties, TodosProperties properties) {
        this.env = environment;
        this.buildProperties = buildProperties;
        this.properties = properties;
    }

    @GetMapping("/metadata")
    public List<Metadata> metadata() {
        Map<String, Object> allProperties = new HashMap<>();
        for(PropertySource<?> ps : ((AbstractEnvironment)env).getPropertySources()) {
            if(ps instanceof MapPropertySource) {
                allProperties.putAll(((MapPropertySource)ps).getSource());
            }
        }
        return allProperties.entrySet().stream()
            .filter(it -> !StringUtils.isEmpty(it.getValue()))
            .sorted(Map.Entry.comparingByKey())
            .map(it -> Metadata.builder()
                    .property(it.getKey())
                    .value(it.getValue().toString()).build())
            .collect(Collectors.toList());
    }

    @GetMapping("/about")
    public BuildProperties buildInfo() {
        return buildProperties;
    }

    @GetMapping("/logs")
    public String logs() {
        return "Online";
    }

    @PostMapping("/logs")
    public void consoleLog(@RequestBody ConsoleLog consoleLog) {
        if("ERROR".equalsIgnoreCase(consoleLog.getLevel())) {
            LOG.error(consoleLog.getLine());
        } else if("WARN".equalsIgnoreCase(consoleLog.getLevel())) {
            LOG.warn(consoleLog.getLine());
        } else if("INFO".equalsIgnoreCase(consoleLog.getLevel())) {
            LOG.info(consoleLog.getLine());
        } else if("DEBUG".equalsIgnoreCase(consoleLog.getLevel())) {
            LOG.debug(consoleLog.getLine());
        } else if("TRACE".equalsIgnoreCase(consoleLog.getLevel())) {
            LOG.trace(consoleLog.getLine());
        }
    }

    private final Map<String, Todo> todos = Collections.synchronizedMap(new LinkedHashMap<>());

    @PostMapping("/todos")
    public Mono<Todo> create(@RequestBody Mono<Todo> todo) {
        if(todos.size() < this.properties.getApi().getLimit()) {
            return todo.map(it -> {
                if(properties.getIds().getTinyId()) {
                    it.setId(UUID.randomUUID().toString().substring(0, 8));
                } else {
                    it.setId(UUID.randomUUID().toString());
                }
                todos.put(it.getId(), it);
                return todos.get(it.getId());
            });
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                format("todos.api.limit=%d, todos.size()=%d", this.properties.getApi().getLimit(), todos.size()));
        }
    }

    @GetMapping("/todos")
    public Flux<Todo> retrieveAll() {
        return Flux.fromIterable(todos.values());
    }

    @GetMapping("/todos/{id}")
    public Mono<Todo> retrieve(@PathVariable String id) {
        if(!todos.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("todo.id=%s", id));
        }
        return Mono.just(todos.get(id));
    }

    @PatchMapping("/todos/{id}")
    public Mono<Todo> update(@PathVariable String id, @RequestBody Mono<Todo> todo) {
        if(!todos.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("todo.id=%s", id));
        }

        return todo.map(it -> {
            if(!ObjectUtils.isEmpty(it.getComplete())) {
                todos.get(id).setComplete(it.getComplete());
            }
            if(!StringUtils.isEmpty(it.getTitle())){
                todos.get(id).setTitle(it.getTitle());
            }
            return todos.get(id);
        });
    }

    @DeleteMapping("/todos")
    public void deleteAll() {
        todos.clear();
    }

    @DeleteMapping("/todos/{id}")
    public Mono<Todo> delete(@PathVariable String id) {
        if(!todos.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("todo.id=%s", id));
        }
        return Mono.just(todos.remove(id));
    }
}
