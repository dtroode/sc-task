FROM openjdk:18-alpine

RUN addgroup -S sc && adduser -S sc -G sc
USER sc:sc

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT [ "java", "-jar", "/app.jar" ]