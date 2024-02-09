# Use the specified OpenJDK 19 base image
FROM openjdk:19

# Set the JAR file and properties file arguments
ARG JAR_FILE=build/libs/*.jar
ARG PROPERTIES

# Copy the JAR file into the image
COPY ${JAR_FILE} app.jar

# Copy the application.yml properties file into the image
COPY ${PROPERTIES} application.yml

# Set the entry point for the container
ENTRYPOINT ["nohup", "java", "-jar", "/app.jar", ">" , "/server.log"]
