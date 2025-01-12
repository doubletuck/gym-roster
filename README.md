# Gym Roster Service

## About
Gym Roster provides a service to maintain college gymnastics teams, athletes, staff and rosters.

## Local Development

### System Requirements
* Java 21+
* Maven 3.9.x
* Docker

### Setup
Refer to [Development Setup](docs/development-setup.md).

### Installing
```text
git clone git@github.com:whyceewhite/gym-roster.git
cd gym-roster
```

### Building
```shell
mvn install 
```

### Running
```shell
mvn spring-boot:run
```

To ping, go to http://127.0.0.1:8080/health, or in a terminal:
```shell
curl http://localhost:8080/health
```

To stop the server, issue a Control-C command in the shell where the service is running.

### Building a docker image
```shell
docker build -t gym-roster:latest .
```

To see a list of images:
```shell
docker images
```

### Running the docker container
```shell
docker run -d -p 8080:8080 gym-roster:latest
```
* `-d`: Runs the container in the background (detached mode).
* `-p 8080:8080`: Maps port 8080 on your machine to port 8080 in the container (if your application uses that port).
* `gym-roster:latest`: The image name and tag built.

To see a list of running containers:
```shell
docker ps
```

To ping, go to http://127.0.0.1:8080/health, or in a terminal:
```shell
curl http://localhost:8080/health
```


### Stopping the docker container
Run `docker stop <container_id_or_name>`.

Find the container id:
```shell
docker ps
```
```text
% docker ps
CONTAINER ID   IMAGE               COMMAND                  CREATED          STATUS          PORTS                    NAMES
a11df8b6959f   gym-roster:latest   "/__cacert_entrypoin…"   38 seconds ago   Up 38 seconds   0.0.0.0:8080->8080/tcp   naughty_carson
```

```text
docker stop a11df8b6959f
```
