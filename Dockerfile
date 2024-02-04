# Use Ubuntu as base image
FROM ubuntu:latest

# Install necessary packages
RUN apt-get update && apt-get install -y openjdk-17-jdk

# Set environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$PATH:$JAVA_HOME/bin

# Copy the compiled JAR file
COPY target/crudOperations.jar /app/crudOperations.jar

# Expose the port your application runs on
EXPOSE 9191

# Command to run your application
ENTRYPOINT ["java","-jar","/app/crudOperations.jar"]