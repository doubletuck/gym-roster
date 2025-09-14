# Local Development Environment
This document provides steps for setting up a development environment for developing and running this module locally.

### Table of Contents
* [Prerequisites](#prerequisites)
* [Installing](#installing)
* [Configuring](#configuring)
* [Building](#building)
* [Running](#running)
* [Running in Docker locally](#running-in-docker-locally)

## Prerequisites

Refer to [Development Setup](development-setup.md) for prerequisite software, database and local environment setup information.

## Installing
```text
git clone git@github.com:doubletuck/gym-roster.git
cd gym-roster
```

## Configuring
The [src/main/resources/application.yml](../src/main/resources/application.yml) contains configuration values used by the Spring framework. The `application.yml` file is the default file from which all `application-{env}.yml` files will inherit.

| properties file       | environment                                                                               |
|-----------------------|-------------------------------------------------------------------------------------------|
| application.yml       | The default properties file. The other environment files can override values in this file. |
| application-local.yml | Used for local development on a workstation.                                              |
| application-dev.yml   | Used for deploying to a cloud based dev environment.                                      |
| application-prod.yml  | Used for deploying to a cloud-based production environment.                               |

## Creating the database schema
Prior to running the application, you will need to have the database started. Review the [Development Setup](development-setup.md) guide if the database is not already set up.

The database schema will automatically be created using Flyway when the application is started as described in the [Running](#running) section. The [database/migration](../src/main/resources/db/migration) directory contains the scripts for establishing the schema.

Optional: If you want to start with a clean database, then running the following command will drop all objects in the database and recreate the schema according to the migration scripts.
```shell
 mvn flyway:clean \
   -Dspring.profiles.active=local \ 
   -Dflyway.url=jdbc:postgresql://localhost:5432/gymroster \
   -Dflyway.user=postgres \
   -Dflyway.password=gympass \
   -Dflyway.cleanDisabled=false
```

## Building
```shell
mvn install 
```

## Running
The `local` application properties profile, [application-local.yml](../src/main/resources/application-local.yml), defaults to local development environment values. If you followed the development setup guide, then the configuration will work as is. If you modified your setup (i.e., changed the database name or password), then you will need to adjust the properties values appropriately.

```shell
mvn spring-boot:run -Dspring.profiles.active=local
```

or 

```shell
java -jar target/gym-roster.jar --spring.profiles.active=local
```

The app will be accessible via http://localhost:8080. Do http://localhost:8080/actuator to test.

To stop the server, issue a Control-C command in the shell where the service is running.

## Running in Docker locally
If running both the app and the database containers, then realize that containers have isolated networks unless explicitly linked or put on the same network. 
As a result, the `docker-compose` will allow both this app container and the database container to run in concert and communicate with each other.

Using this docker compose option is good when doing local development on other services (i.e., a UI) that interfaces with this service and, thus, needs it to be running.

#### Initiate docker compose

```shell
cd gym-roster
docker compose up
```

If you want to run the app container in the background (i.e., detached mode) then use the `-d` flag.
```shell
docker compose up -d
```

#### Stop docker compose
```shell
docker compose down
```

#### Restart docker compose
```shell
docker compose start
```

The app will be accessible via http://localhost:8080. Do http://localhost:8080/actuator to test.

### Building a standalone Docker app image
> NOTE: Locally, your app container won't be able to communicate with the db image unless started via docker-compose. The information below is simply informational.

To build the app's Docker image: `docker build -t <image-name>:<image-tag> .`

For example:
```shell
docker build -t gym-roster:latest .
```

Run the Docker container.
```shell
docker run -d -p 8080:8080 gym-roster:latest
```
* `-d`: Runs the container in the background (detached mode).
* `-p 8080:8080`: Maps port 8080 on your machine to port 8080 in the container (if your application uses that port).
* `gym-roster:latest`: The image name and tag.


Run the Docker container with the environment variable values needed to connect to the database in the relevant environment.
```shell
docker run \
           -e DB_URL=jdbc:postgresql://localhost:5432/gymroster \
           -e DB_USERNAME=postgres \
           -e DB_PASSWORD=gympass \
           -e SPRING_PROFILE_ACTIVE=local \
           gym-roster:latest
```

To ping, go to http://127.0.0.1:8080/health, or in a terminal:
```shell
curl http://localhost:8080/health
```