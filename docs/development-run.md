# Local Development Environment

## Prerequisites

Refer to [Development Setup](docs/development-setup.md) for prerequisite software and local environment setup information.

## Installing
```text
git clone git@github.com:whyceewhite/gym-roster.git
cd gym-roster
```

## Building
```shell
mvn install 
```

## Configuring
The [src/main/resources/application.yml](../src/main/resources/application.yml) contains configuration values used by the Spring framework. The `application.yml` file is the default file from which all `application-{env}.yml` files will inherit.

| properties file       | environment                                                                                |
|-----------------------|--------------------------------------------------------------------------------------------|
| application.yml       | The default properties file. The other environment files can override values in this file. |
| application-local.yml | Used for local development on a workstation.                                               |
| application-dev.yml   | Used for deploying to a cloud based dev environment.                                       |
| application-prod.yml  | Used for deploying to a cloud-based production environment.                                |


## Running

### Environment Variables
To run the Docker image, you will need to provide values for the following environment variables used for connecting to the database and setting the environment.

| Variable              | Local Environment Values                   |
|-----------------------|--------------------------------------------|
| DB_URL                | jdbc:postgresql://localhost:5432/gymroster |
| DB_USERNAME           | postgres                                   |
| DB_PASSWORD           | gympass                                    |
| SPRING_PROFILE_ACTIVE | local                                      |

The `local` profile defaults to the aforementioned values. If running the application in a different, then the environment variables will need to be provided.

### Running locally
```shell
mvn spring-boot:run -Dspring.profiles.active=local
```

or 

```shell
java -jar target/gym-roster.jar --spring.profiles.active=local
```

To stop the server, issue a Control-C command in the shell where the service is running.

### Running in Docker
If running both the app and the database containers, then realize that containers have isolated networks unless explicitly linked or put on the same network. A docker compose should be used to start all the services at once.

Build the app's Docker image: `docker build -t <image-name>:<image-tag> .`

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
