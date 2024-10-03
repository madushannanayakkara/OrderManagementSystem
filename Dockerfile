# Use OpenJDK 17 as the base image
FROM openjdk:17-alpine
# Set the working directory inside the container
WORKDIR /opt
# Set the environment variable for the application port
ENV PORT 8080
# Expose port 8080 to allow external access to the app
EXPOSE 8080
# Copy the pre-built JAR file to the container
COPY target/*.jar /opt/app.jar
# Run the application using the JAR file
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
