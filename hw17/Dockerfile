FROM ghcr.io/graalvm/jdk-community:17.0.9
COPY /target/hw17-Docker.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]