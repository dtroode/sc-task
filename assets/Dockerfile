FROM openjdk:18-alpine

WORKDIR /app
COPY . /app
RUN ./mvnw package -Dmaven.test.skip

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]