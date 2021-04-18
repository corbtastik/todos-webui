FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN --mount=type=cache,target=/root/.m2 ./mvnw install -DskipTests
WORKDIR /workspace/app/target/dependency
RUN jar -xf ../*.jar

FROM openjdk:8-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
VOLUME /tmp
USER spring
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-noverify","-cp","app:app/lib/*","-Dspring.main.lazy-initialization=true","io.todos.WebUI"]
