# Use a lightweight Java image with JDK 17
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy your built jar file to the container
COPY target/LostAndFound-0.0.1-SNAPSHOT.jar app.jar

# Run the jar file when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
