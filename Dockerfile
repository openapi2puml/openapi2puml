# Docker multi-stage build

# 1. Building the App with Maven
FROM maven:3-jdk-11

ADD . /openapi2puml
WORKDIR /openapi2puml

# Just echo so we can see, if everything is there :)
RUN ls -l

# Run Maven build
RUN mvn clean install -DskipTests

# DC - test if we can new see jar.....
RUN ls -l
RUN pwd

# TODO - this part makes the final image include only the JDK but it misses
# the correct copy of the jar to the final image. Currently building the whole project and dumping it to the
# final image
# Just using the build artifact and then removing the build-container
# FROM openjdk:11-jdk

# DC - copy jar somewhere I can see it
# COPY openapi2puml.jar /openapi2puml/openapi2puml.jar
# VOLUME /tmp

# FINAL COMMAND to be executed when a container is created
# Add options when running with `docker run -it`
ENTRYPOINT [ "java", "-jar", "/openapi2puml/openapi2puml.jar" ]
