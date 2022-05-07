# Stage 1: build
FROM docker.io/library/eclipse-temurin:11-jdk-focal as builder
WORKDIR app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} todos-webui.jar
RUN java -Djarmode=layertools -jar todos-webui.jar extract
# Stage 2: run
FROM docker.io/library/eclipse-temurin:11-jre-focal
WORKDIR app
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]