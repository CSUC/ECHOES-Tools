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

```bash
mvn clean install -DskipTests 
```

Copy files and edit 'echoes-gui-config/src/main/resources/auth0.env.example' to 'echoes-gui-config/src/main/resources/auth0.env'

```bash
DOMAIN = {YOUR_DOMAIN}
CLIENT_ID = {YOUR_CLIENT_ID}
CLIENT_SECRET = {YOUR_CLIENT_SECRET}

```
'echoes-gui-client/.env.example' to 'echoes-gui-client/.env'

```bash
AUTH0_AUDIENCE={API_IDENTIFIER}
AUTH0_DOMAIN={DOMAIN}
```

'echoes-gui-client/auth0-variables.js.example' to 'echoes-gui-client/auth0-variables.js'

```bash
var AUTH0_CLIENT_ID='{CLIENT_ID}'; 
var AUTH0_DOMAIN='{DOMAIN}'; 
var AUTH0_CALLBACK_URL='{CALLBACK}';
var AUTH0_AUDIENCE='{API_IDENTIFIER}';
```

https://auth0.com/


## RUN (PM2)

Advanced process manager for production Node.js applications. Load balancer, logs facility, startup script, micro service management, at a glance.

http://pm2.keymetrics.io/

```bash
pm2 start bin/echoes-gui-client.json
```

### MONITORING

```bash
pm2 monit
```

### LOGS

```bash
pm2 logs
```
