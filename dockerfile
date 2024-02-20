# Use the specified OpenJDK 19 base image
FROM openjdk:19

# Set the JAR file and properties file arguments
ARG JAR_FILE=build/libs/*.jar
ARG PROPERTIES

ARG PINPOINTLICENSE
ENV URL $PINPOINTLICENSE

# Download and extract the pinpoint agent
RUN curl -L -o agent.tar.gz https://github.com/pinpoint-apm/pinpoint/releases/download/v2.5.3/pinpoint-agent-2.5.3.tar.gz \
    && tar xvf agent.tar.gz -C / \
    && mv /pinpoint-agent-2.5.3 /pinpoint-agent \
    && echo ""$PINPOINTLICENSE"" > /pinpoint-agent/pinpoint.license

RUN sed -i 's#profiler.transport.grpc.collector.ip=127.0.0.1#profiler.transport.grpc.collector.ip=collect.pinpoint.ncloud.com#' /pinpoint-agent/pinpoint-root.config
RUN sed -i 's#profiler.collector.ip=127.0.0.1#profiler.collector.ip=collect.pinpoint.ncloud.com#' /pinpoint-agent/pinpoint-root.config

RUN sed -i -e 's#profiler.transport.grpc.agent.ssl.enable=false#profiler.transport.grpc.agent.ssl.enable=true#' \
       -e 's#profiler.transport.grpc.metadata.ssl.enable=false#profiler.transport.grpc.metadata.ssl.enable=true#' \
       -e 's#profiler.transport.grpc.stat.ssl.enable=false#profiler.transport.grpc.stat.ssl.enable=true#' \
       -e 's#profiler.transport.grpc.span.ssl.enable=false#profiler.transport.grpc.span.ssl.enable=true#' \
       -e 's#profiler.transport.grpc.ssl.enable=false#profiler.transport.grpc.ssl.enable=true#' /pinpoint-agent/pinpoint-root.config

# Copy the JAR file into the image
COPY ${JAR_FILE} app.jar

# Copy the application.yml properties file into the image
COPY ${PROPERTIES} application.yml

# Set the entry point for the container

ENTRYPOINT ["java", "-jar", "-javaagent:/pinpoint-agent/pinpoint-bootstrap-2.5.3.jar", "-Dpinpoint.applicationName=MainBackEnd", "-Dpinpoint.agentId=bemain", "-Dpinpoint.config=/pinpoint-agent/pinpoint-root.config", "/app.jar"]

