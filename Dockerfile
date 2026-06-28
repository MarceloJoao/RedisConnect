# Estágio 1: Build (Compila o projeto usando Maven)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Execução (Cria a imagem final leve)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copia o jar gerado no estágio de build
COPY --from=build /app/target/*.jar app.jar
# Expõe a porta que o Spring Boot usa
EXPOSE 8080
# Comando para iniciar
ENTRYPOINT ["java","-jar","app.jar"]