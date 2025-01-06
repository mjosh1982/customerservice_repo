# Use the official Eclipse Temurin image as a parent image
FROM eclipse-temurin:21-jre

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file into the container
COPY target/customer-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]