FROM maven:3.8.6-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline  
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Exécution
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
# Étape 3 : Exposition du port
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]


