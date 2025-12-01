# Build stage
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# Copy gradle files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Download dependencies
RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build application
RUN gradle clean build -x test --no-daemon

# Runtime stage
FROM gradle:8.5-jdk17

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]