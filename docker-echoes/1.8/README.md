    ## SET ENVIRONMENT
```
cp .env.example .env
```
## RUN ECHOES
```
docker-compose -f docker-compose-echoes.yml run -d echoes
```
```
docker exec -it [echoes-container-id] bash
```
## UP HADOOP
```
docker-compose -f docker-compose-hadoop.yml up -d
```
## UP YASGUI
```
docker-compose -f docker-compose-yasgui.yml up -d
```
## BLAZEGRAPH DATABASE
- [nawer/blazegraph](https://hub.docker.com/r/nawer/blazegraph)
- [DrSnowbird/blazegraph](https://github.com/DrSnowbird/blazegraph-docker)
- [lyrasis/blazegraph](https://hub.docker.com/r/lyrasis/blazegraph)
- [nawerprod](https://github.com/DrSnowbird/blazegraph-docker)

## EXEC CONTAINER 
```
docker exec -it [container-id] bash
```

## DELETE IMAGES, CONTAINERS  AND VOLUMES
```
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
docker rmi -f $(docker images -a -q)
docker volume rm $(docker volume ls -q --filter dangling=true)
```
