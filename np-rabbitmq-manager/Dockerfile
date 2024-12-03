FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Copy the source code
COPY src ./src

# Copy the application properties
COPY src/main/resources/application.yml ./application.yml

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# Build the application
RUN mvn clean package -DskipTests

# Expose the application port
EXPOSE 9090

# Set environment variables for email configuration
# Note: These should be passed at runtime or set in docker-compose
ENV EMAIL_USERNAME=your-email@example.com
ENV EMAIL_PASSWORD=your-email-password

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/target/spring-rabbitmq-test-0.0.1-SNAPSHOT.jar"]