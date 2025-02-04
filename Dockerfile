# Use a base image with Java
FROM openjdk:17-jdk-alpine

# Copy the application's JAR file into the container
COPY target/ecommerce-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar", "--logging.level.org.springframework=INFO"]


