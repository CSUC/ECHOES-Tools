#!/usr/bin/env bash

BASE_DIR=`dirname $0`

ECHOES_GUI_CLIENT="${BASE_DIR}"/echoes-gui-client.json
ECHOES_GUI_CONSUMER="${BASE_DIR}"/echoes-gui-consumer/target/echoes-gui-consumer-0.0.1.jar
ECHOES_GUI_SERVER="${BASE_DIR}"/echoes-gui-server/target/echoes-gui-server.jar

echo $ECHOES_GUI_CONSUMER
java -jar "${ECHOES_GUI_CONSUMER}" &

echo $ECHOES_GUI_SERVER
java -jar "${ECHOES_GUI_SERVER}" &

echo $ECHOES_GUI_CLIENT
pm2 start "${ECHOES_GUI_CLIENT}" &