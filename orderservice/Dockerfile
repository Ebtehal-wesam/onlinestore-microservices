FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /onlinestore
COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:17-slim
WORKDIR /onlinestore
COPY --from=build /onlinestore/target/*.jar orderservice.jar
CMD ["java", "-jar", "orderservice.jar"]