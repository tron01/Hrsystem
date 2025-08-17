# Use a slim JDK 21 base image
FROM eclipse-temurin:21-jre-alpine
# Set the working directory
WORKDIR /app

# Copy any JAR from target/
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 9090

# Set the active Spring profile to docker
ENV SPRING_PROFILES_ACTIVE=docker

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
