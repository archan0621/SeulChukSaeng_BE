# Use the specified OpenJDK 19 base image
FROM openjdk:19

# Set the JAR file and properties file arguments
ARG JAR_FILE=build/libs/*.jar
ARG PROPERTIES

ARG PINPOINTLICENSE
ENV URL $PINPOINTLICENSE

# Download and extract the pinpoint agent
RUN curl -o agent.tar.gz https://ncloud-pinpoint.com/agent.tar.gz \
    && tar xvf agent.tar.gz -C / \
    && mv /pinpoint-agent-2.2.3-NCP-RC1 /pinpoint-agent \
    && echo "'"$PINPOINTLICENSE"'" > /pinpoint-agent/pinpoint.license \
    && export pinpointPath=/pinpoint-agent-2.2.3-NCP-RC1

# Copy the JAR file into the image
COPY ${JAR_FILE} app.jar

# Copy the application.yml properties file into the image
COPY ${PROPERTIES} application.yml

# Set the entry point for the container

ENTRYPOINT ["java", "-jar", "-javaagent:${pinpointPath}/pinpoint-bootstrap-2.2.3-NCP-RC1.jar", "-Dpinpoint.applicationName=MainBackEnd", "-Dpinpoint.agentId=bemain", "/app.jar"]

