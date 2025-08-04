# Local Development Environment
This document provide steps for setting up a development environment for developing and running this module locally.

### Table of Contents
* [Prerequisites](#prerequisites)
* [Installing](#installing)
* [Building](#building)
* [Configuring](#configuring)
* [Running](#running)
* [Accessing the database](#accessing-the-database)

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

To stop the server, issue a Control-C command in the shell where the service is running.

## Accessing the database

#### To view logs:
```shell
docker logs gym-roster-postgres
```

#### To connect to PostgreSQL outside of the container:
```shell
psql -h localhost -p 5432 -U postgres -d gymroster
```

#### To connect to PostgreSQL inside the container:
Access the container's shell:
```shell
docker exec -it gym-roster-postgres bash
```

Use the container's psql installation:
```shell
psql -U postgres
```
