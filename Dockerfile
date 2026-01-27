FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY ./gradlew .

COPY ./gradlew.bat .

COPY ./gradle ./gradle

COPY ./build.gradle.kts .

COPY ./settings.gradle.kts .

COPY ./src ./src

RUN chmod +x gradlew

RUN ./gradlew playwrightInstall

RUN ./gradlew build -x test

FROM eclipse-temurin:21-jdk

WORKDIR /app

RUN apt-get update && apt-get install -y \
    wget \
    ca-certificates \
    fonts-liberation \
    libnss3 \
    libatk-bridge2.0-0 \
    libgtk-3-0 \
    libx11-xcb1 \
    libxcomposite1 \
    libxdamage1 \
    libxrandr2 \
    libgbm1 \
    libasound2t64 \
    libdrm2 \
    libxshmfence1 \
    libu2f-udev \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/build/libs/*.jar app.jar

COPY --from=builder /root/.cache/ms-playwright /root/.cache/ms-playwright

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
