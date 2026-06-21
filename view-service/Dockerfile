FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN ./mvnw -pl view-service -am package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/view-service/target/view-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java","-jar","app.jar"]