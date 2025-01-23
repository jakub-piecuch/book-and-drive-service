# Use a base image with Java runtime environment
FROM openjdk:21-jdk-slim

# Set a working directory inside the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/book-and-drive-service-0.0.1-SNAPSHOT.jar book-and-drive.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "book-and-drive.jar"]
