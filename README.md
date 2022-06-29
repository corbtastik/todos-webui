# Todo(s) WebUI

Todo(s) frontend Web app using [Spring Boot](https://spring.io/projects/spring-boot) and [Vue.js](https://vuejs.org/).

* Inspired by [TodoMVC Vue App](http://todomvc.com/examples/vue/), with enhancements:
  * Updated style
  * App is vendored as a Spring Boot app
  * App calls a backing API endpoint (`/todos`) if offline data is temporal
  * App captures user interactions

## Build

```bash
git clone https://github.com/corbtastik/todos-webui.git && cd todos-webui
# build spring boot jar
mvn clean package
# build OCI image via podman plugin (bound to install phase)
mvn clean install
# or build OCI image via podman cli
podman build -t todos-webui .
```

## Run via Java

```bash
java -jar ./target/todos-webui-1.0.0.jar \
  --server.port=8000 \
  --spring.security.user.name="Sponge Bob" \
  --todos.webui.placeholder="Make bacon pancakes"
```

### Or

## Run via Podman

```bash
# run
podman run --name todos-webui -d -p 8000:8000 \
  -e "SERVER_PORT=8000" \
  -e "SPRING_SECURITY_USER_NAME=Podman" \
  -e "TODOS_WEBUI_PLACEHOLDER=Make bacon pancakes" \
  todos-webui
# follow logs
podman logs -f todos-webui
```

## Grok

Open: [http://localhost:8000](http://localhost:8080)

## Next Steps

This application assumes the `/todos` endpoint is exposed from the same "origin". Because of this it's best to use this application behind the [Todos-Edge](https://github.com/corbtastik/todos-edge)which will serve as a gateway and single origin to the client for both the WebUI and for proxying API calls to `/todos`.

Another option would be to pair [Todos-WebUI](https://github.com/corbtastik/todos-webui) with a backend like [Todos-MySQL](https://github.com/corbtastik/todos-mysql), for example by running both in a podman pod and publishing and mapping port 8000 on the host.
