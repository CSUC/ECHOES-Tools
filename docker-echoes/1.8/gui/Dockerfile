FROM openjdk:8-slim-buster
LABEL maintainer="Albert Martínez <albert.martinez@csuc.cat>"

RUN apt-get update; apt-get install -y curl apt-utils vim iputils-ping \
    && curl -sL https://deb.nodesource.com/setup_14.x | bash - \
    && apt-get install -y nodejs \
    && curl -L https://www.npmjs.com/install.sh | sh \
    && npm install pm2 -g \
    && apt-get install -y maven \
    && apt-get install -y git

RUN addgroup --system echoes
RUN adduser --system echoes --ingroup echoes

USER echoes:echoes

WORKDIR /home/echoes/
RUN git clone -b feature/docker-echoes https://github.com/CSUC/ECHOES-Tools.git
WORKDIR /home/echoes/ECHOES-Tools

COPY --chown=echoes:echoes ./env/auth0-variables.js /home/echoes/ECHOES-Tools/echoes-gui/echoes-gui-client/auth0-variables.js
COPY --chown=echoes:echoes ./env/auth0.env /home/echoes/ECHOES-Tools/echoes-gui/echoes-gui-server/src/main/resources/auth0.env
COPY --chown=echoes:echoes ./env/.env /home/echoes/ECHOES-Tools/echoes-gui/echoes-gui-client/.env
COPY --chown=echoes:echoes ./env/echoes-gui-server.default.conf /home/echoes/ECHOES-Tools/echoes-gui/echoes-gui-config/src/main/resources/echoes-gui-server.conf
COPY --chown=echoes:echoes ./env/rabbitmq.defaults.conf /home/echoes/ECHOES-Tools/echoes-gui/echoes-gui-config/src/main/resources/rabbitmq.conf

RUN git submodule update --init --recursive \
    && mvn validate \
    && mvn install -DskipTests

#ENTRYPOINT ["pm2", "start", "bin/echoes-gui-client.json"]