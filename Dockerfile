#FROM ubuntu:latest
#LABEL authors="anuka"
#
#ENTRYPOINT ["top", "-b"]
# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:resolve
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]