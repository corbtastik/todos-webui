#--------------------------------------------------------------------------------------------------
# Builder image
#--------------------------------------------------------------------------------------------------
FROM docker.io/library/maven:3.8-eclipse-temurin-11-alpine as builder

WORKDIR /workspace/app
COPY pom.xml .
COPY src src

ARG APP_VERSION=1.0.0

RUN mvn versions:set -DnewVersion=$APP_VERSION && \
    mvn install -DskipTests && \
    mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
#--------------------------------------------------------------------------------------------------
# Application image
#--------------------------------------------------------------------------------------------------
FROM docker.io/library/eclipse-temurin:11-jre-alpine

VOLUME /tmp

ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","io.todos.WebUI"]