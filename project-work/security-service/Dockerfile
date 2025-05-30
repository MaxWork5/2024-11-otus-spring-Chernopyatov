FROM ghcr.io/graalvm/jdk-community:17.0.9
COPY security-service/target/security.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]