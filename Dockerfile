# build
FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# run
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /build/target/teebay-api-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
