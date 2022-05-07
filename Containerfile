# Use Red Hat's ubi8 openjdk builder-image
FROM registry.redhat.io/ubi8/openjdk-11 as builder
WORKDIR app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} todos-webui.jar
RUN java -Djarmode=layertools -jar todos-webui.jar extract
# User Red Hat's ubi8 openjdk runtime-image
FROM registry.redhat.io/ubi8/openjdk-11-runtime
WORKDIR app
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]