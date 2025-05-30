FROM ghcr.io/graalvm/jdk-community:17.0.9
COPY event-service/target/event.jar /app/app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]