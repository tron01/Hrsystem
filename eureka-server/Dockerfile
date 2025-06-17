# Use a slim JDK 21 base image
FROM eclipse-temurin:21-jdk-alpine AS base

# Set working directory inside container
WORKDIR /app

# Copy the built jar into the image
COPY target/*.jar app.jar

# Expose the application port (adjust if needed)
EXPOSE 8761

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
