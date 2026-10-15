# ---- Etapa 1: compilar el JAR ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Primero los archivos de Gradle para aprovechar la cache de capas
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --no-daemon --version

COPY src ./src
RUN ./gradlew --no-daemon bootJar -x test

# ---- Etapa 2: imagen final, solo JRE ----
FROM eclipse-temurin:25-jre
WORKDIR /app

RUN useradd --system --no-create-home appuser
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar
USER appuser

# La app usa server.port=8967 (application.properties). Las plataformas en la
# nube inyectan PORT; si existe se usa, si no se queda el 8967.
EXPOSE 8967
ENTRYPOINT ["sh", "-c", "exec java -XX:MaxRAMPercentage=75 -Dserver.port=${PORT:-8967} -jar app.jar"]
