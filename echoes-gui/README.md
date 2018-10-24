# ECHOES-GUI

## Requirements

|                   |      version    | 
|-------------------|-----------------|
|MongoDB server     |   >= 4.0.3      |
|RabbitMQ server    |   >= 3.6.11     |
|Node.js            |   >= 8.12.0     |
|npm                |   >= 6.4.1      |
|PM2                |   >= 3.2.2      |

## INSTALL

To install ECHOES GUI follow this instructions:

```bash
mvn clean install -DskipTests 
```

ECHOES GUI uses Auth0 for access delegation. Auth0 provides authentication and authorization as a service, you can find more information in https://auth0.com/.

To configure the access you need to:

1. Copy files and edit 'echoes-gui-config/src/main/resources/auth0.env.example' to 'echoes-gui-config/src/main/resources/auth0.env'

```bash
DOMAIN = {YOUR_DOMAIN}
CLIENT_ID = {YOUR_CLIENT_ID}
CLIENT_SECRET = {YOUR_CLIENT_SECRET}

```
Review setting and fill it with you configuration data.

2. Copy files and edit 'echoes-gui-client/.env.example' to 'echoes-gui-client/.env'

```bash
AUTH0_AUDIENCE={API_IDENTIFIER}
AUTH0_DOMAIN={DOMAIN}
```
Review setting and fill it with you configuration data.

3. Copy files and edit 'echoes-gui-client/auth0-variables.js.example' to 'echoes-gui-client/auth0-variables.js'

```bash
var AUTH0_CLIENT_ID='{CLIENT_ID}'; 
var AUTH0_DOMAIN='{DOMAIN}'; 
var AUTH0_CALLBACK_URL='{CALLBACK}';
var AUTH0_AUDIENCE='{API_IDENTIFIER}';
```
Review setting and fill it with you configuration data.

## RUN (PM2)

Use PM2 to run all processs.

PM2 is an advanced process manager for production Node.js applications. Load balancer, logs facility, startup script, micro service management, at a glance. You can find more information in http://pm2.keymetrics.io/.

```bash
pm2 start bin/echoes-gui-client.json
```

### MONITORING
PM2 gives you also a simple way to monitor the resource usage of your application.  You can monitor memory and CPU easily and straight from your terminal:

```bash
pm2 monit
```

### LOGS

PM2 allows you to easily manage your application’s logs. You can display the logs coming from all your applications in real-time, flush them, and reload them. There are also different ways to configure how PM2 will handle your logs (separated in different files, merged, with timestamp…) without modifying anything in your code.

```bash
pm2 logs
```
