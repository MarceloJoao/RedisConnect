# Estágio 1: Build (Compila o projeto usando Maven e Java 21)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Execução (Cria a imagem final leve com Java 21)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Copia o jar gerado no estágio de build
COPY --from=build /app/target/*.jar app.jar
# Expõe a porta que o Spring Boot usa
EXPOSE 8080
# Comando para iniciar
ENTRYPOINT ["java","-jar","app.jar"]