# Stage 1: Build the Java application using Gradle
FROM amazoncorretto:21-alpine-jdk AS builder
WORKDIR /app

# Copy Gradle wrapper, settings, and build files
COPY gradlew gradlew.bat settings.gradle ./
COPY build.gradle ./

# Download dependencies to leverage Docker cache
# The '|| true' allows the command to fail if dependencies are not yet present, but still build the image
RUN ./gradlew dependencies --write-locks || true

# Copy the rest of the application source code
COPY . .

# Build the Spring Boot application into a JAR
RUN ./gradlew bootJar

# Stage 2: Create the production-ready image with JRE only
FROM amazoncorretto:21-alpine-jre
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the application port (commonly 8080 for Spring Boot)
EXPOSE 8080

# Command to run the JAR application
ENTRYPOINT ["java","-jar","app.jar"]

# Optional: Add a healthcheck (adjust the URL if needed, e.g., Spring Boot Actuator)
# HEALTHCHECK --interval=5m --timeout=3s CMD wget -q -O- http://localhost:8080/actuator/health | grep '{"status":"UP"}' || exit 1
