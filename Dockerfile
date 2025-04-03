# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Create upload directories
RUN mkdir -p uploads/documents uploads/statements

# Expose the application port
EXPOSE 8080
EXPOSE 5005

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 