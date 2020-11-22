# switched to debian image to be able to use plantuml, graphviz and dot
FROM debian:stable

RUN apt-get update && apt-get install -y plantuml graphviz && rm -rf /var/cache/apt/archives/*
RUN apt-get update && apt-get install -y maven && rm -rf /var/cache/apt/archives/*

COPY . /app
WORKDIR /app
RUN mvn clean install -DskipTests=true -Dgpg.skip -Dmaven.javadoc.skip=true -B -V
RUN echo -e '#! /bin/bash\njava -jar /app/openapi2puml.jar $*' > /usr/local/bin/swagger2puml\
 && chmod +x /usr/local/bin/swagger2puml

ENV LANG en_US.UTF-8
ENTRYPOINT [ "java", "-jar", "openapi2puml.jar" ]
