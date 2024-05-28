FROM eclipse-temurin:21-jdk
WORKDIR /app

RUN apt-get update && apt-get install -y \
    libxrender1 \
    libxtst6 \
    libxi6 \
    xvfb

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
RUN ./gradlew --version

COPY . .
ENV _JAVA_OPTIONS="-Djava.awt.headless=false"
RUN echo "Docker setup complete"

CMD ["sh", "-c", "xvfb-run -a ./gradlew test --stacktrace ' && tail -f /dev/null"]
