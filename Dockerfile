# Use a base image with JDK 21
FROM eclipse-temurin:21-jdk

# Set the working directory
WORKDIR /app

# Copy the project files to the working directory
COPY . .

# Install necessary dependencies (Gradle and others)
RUN ./gradlew --version

# Install additional necessary libraries for GUI operations
RUN apt-get update && apt-get install -y \
    libxrender1 \
    libxtst6 \
    libxi6 \
    xvfb

# Set environment variables for headless mode
ENV _JAVA_OPTIONS="-Djava.awt.headless=false"

# Add logging for debugging
RUN echo "Docker setup complete"

# Define the command to run the tests
CMD ["sh", "-c", "echo 'Starting tests...' && xvfb-run -a ./gradlew test --stacktrace && echo 'Tests completed' && tail -f /dev/null"]
