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

### Optional: Clear database
If you want to start with a clean database, then running the following command will drop all schema objects in the database.
```shell
 mvn flyway:clean -Dspring.profiles.active=local -Dflyway.url=jdbc:postgresql://localhost:5432/gymroster -Dflyway.user=postgres -Dflyway.password=gympass -Dflyway.cleanDisabled=false
```

### Optional: Run migration
After clearing the database, run the migration script(s).
```shell
mvn flyway:migrate -Dspring.profiles.active=local -Dflyway.url=jdbc:postgresql://localhost:5432/gymroster -Dflyway.user=postgres -Dflyway.password=gympass
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

If you need to rebuild the Docker image and start the container, then use the `-build` flag.
```shell
mvn clean package -DskipTests
docker compose up --build
```

If you want to run the app container in the background (i.e., detached mode), then use the `-d` flag.
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
Note that your app container won't be able to communicate with the database image locally unless started via docker-compose.
But, for informational purposes, refer to [app-docker-configuration.md](app-docker-configuration.md) document for information on setting up an app docker container.