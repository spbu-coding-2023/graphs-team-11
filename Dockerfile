# Use a base image with JDK 21
FROM eclipse-temurin:21-jdk

# Set the working directory
WORKDIR /app

# Install necessary system dependencies first to take advantage of docker layer caching
RUN apt-get update && apt-get install -y \
    libxrender1 \
    libxtst6 \
    libxi6 \
    xvfb

# Install Gradle and check version, could be cached unless gradle files change
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
RUN ./gradlew --version

# Copy the rest of the project files
COPY . .

# Set environment variables for headless mode
ENV _JAVA_OPTIONS="-Djava.awt.headless=false"

# Add logging for debugging
RUN echo "Docker setup complete"

# Define the command to run the tests
CMD ["sh", "-c", "xvfb-run -a ./gradlew test --stackwatch --stacktrace ' && tail -f /dev/null"]
