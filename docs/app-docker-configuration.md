
# Setting up and running the application in Docker

Because containers have isolated networks where they need to be explicitly linked or put on the same network, the application and the database containers cannot run at the same time unless they are started in a docker compose. See the [Running in Docker locally](./development-run.md#running-in-docker-locally) section for `docker compose` instructions.


## Build the application Docker image
Change into the root directory of this project.
```shell
cd gym-roster
```

Run Docker build to create an image of the application: `docker build -t <image-name>:<image-tag> .` For example:
```shell
docker build -t gym-roster:latest .
```

## Run the application Docker container
To run the Docker container, database connection information will need to be passed in as environment variables. 
The following environment variables are needed to properly run the container.

| Variable              | Local Environment Values                   | Description                  |
|-----------------------|--------------------------------------------|------------------------------|
| DB_URL                | jdbc:postgresql://localhost:5432/gymroster | Database connection URL.     |
| DB_USERNAME           | postgres                                   | Database connection username |
| DB_PASSWORD           | gympass                                    | Database connection password |                     
| SPRING_PROFILE_ACTIVE | local                                      | Spring properties profile to activate |

The command:
```shell
docker run -d -p 8080:8080 \
           -e DB_URL=jdbc:postgresql://localhost:5432/gymroster \
           -e DB_USERNAME=postgres \
           -e DB_PASSWORD=gympass \
           -e SPRING_PROFILE_ACTIVE=local \
           gym-roster:latest
```
* `-d`: Runs the container in the background (detached mode).
* `-p 8080:8080`: Maps port 8080 on your machine to port 8080 in the container (if your application uses that port).
* `-e` <ENV_VARIABLE>=<VALUE>: An environment variable to pass into the container.
* `gym-roster:latest`: The image name and tag.

To ping, go to http://127.0.0.1:8080/health, or in a terminal:
```shell
curl http://localhost:8080/health
```
