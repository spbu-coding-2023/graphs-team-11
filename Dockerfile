# Use a base image with JDK 21
FROM eclipse-temurin:21-jdk

# Set the working directory
WORKDIR /app

# Copy the project files to the working directory
COPY . .

# Install necessary dependencies (Gradle)
RUN ./gradlew --version

# Set environment variables for headless mode
ENV _JAVA_OPTIONS="-Djava.awt.headless=false"

# Install additional necessary libraries
RUN apt-get update && apt-get install -y \
    libxrender1 \
    libxtst6 \
    libxi6

# Define the command to run the tests
CMD ["./gradlew", "test"]
