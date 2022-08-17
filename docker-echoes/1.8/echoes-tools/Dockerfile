# docker build -t csuc/echoes-docker:1.8 .
FROM openjdk:8-alpine
MAINTAINER Albert Martínez <albert.martinez@csuc.cat>

RUN apk add --no-cache bash

# Create a group and user
RUN addgroup -S echoes && adduser -S echoes -G echoes

USER echoes
WORKDIR /home/echoes

# INSTALL
ADD https://github.com/CSUC/ECHOES-Tools/releases/download/1.8.3/Analyse-1.3.3.jar /home/echoes/analyse.jar
ADD https://github.com/CSUC/ECHOES-Tools/releases/download/1.8.3/Publish-1.3.7.jar /home/echoes/publish.jar
ADD https://github.com/CSUC/ECHOES-Tools/releases/download/1.8.3/Quality-0.0.3.jar /home/echoes/quality.jar
ADD https://github.com/CSUC/ECHOES-Tools/releases/download/1.8.3/Transformation-1.3.8.jar /home/echoes/transformation.jar

USER root
RUN chown -R echoes:echoes /home/echoes

USER echoes
