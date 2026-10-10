# ==========================================
# ETAPA 1: Construcción (Build)
# ==========================================
# Uso una imagen oficial de Maven con Java 21 para compilar
FROM maven:3.9.6-eclipse-temurin-21 AS builder
# Establezco el directorio de trabajo dentro del contenedor
WORKDIR /app
# OPTIMIZACIÓN CRÍTICA: Copia solo el pom.xml primero.
# Esto permite a Docker "cachear" las dependencias de Internet.
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# ETAPA 2: Ejecución (Run)
# ==========================================
# Usamos una imagen mucho más ligera que solo tiene el entorno de ejecución (JRE) de Java 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
#Se copia el archivo .jar generado en la Etapa 1 a esta nueva imagen limpia
COPY --from=builder /app/target/*.jar app.jar
# Exponemos el puerto
EXPOSE 8081
# Comando que se ejecutará al arrancar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]