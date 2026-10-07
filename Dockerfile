# Etapa de construccion (Build)
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copiar archivos de Gradle
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Dar permisos
RUN chmod +x gradlew

# Copiar el codigo fuente
COPY src src

# Compilar
RUN ./gradlew build -x test --no-daemon

# Etapa de ejecucion (Run)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/AgroFlow-0.0.1-SNAPSHOT.jar app.jar

# Las fotos se guardan en /home (persistente en Azure App Service
# cuando WEBSITES_ENABLE_APP_SERVICE_STORAGE=true)
ENV UPLOAD_DIR=/home/uploads
RUN mkdir -p /home/uploads

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
