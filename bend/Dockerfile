# Étape 1 : Build avec Java 23 et Maven
FROM maven:3.9-eclipse-temurin-23 AS build

WORKDIR /app

# Copier uniquement les fichiers nécessaires pour précharger les deps
COPY pom.xml ./
COPY api/pom.xml ./api/
COPY bll/pom.xml ./bll/
COPY dal/pom.xml ./dal/
COPY dl/pom.xml ./dl/
COPY il/pom.xml ./il/

# Copier les sources
COPY . .

# Construire le projet
RUN mvn clean install -pl api -am -DskipTests

# Étape 2 : Runtime avec Java 23 seulement
FROM eclipse-temurin:23-jre

WORKDIR /app

COPY --from=build /app/api/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
