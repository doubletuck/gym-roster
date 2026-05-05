
# Docker Commands Cheatsheet

## List images
```shell
docker images
```

##  List containers

To list all running containers:
```shell
docker ps
```

To see all containers:
```shell
docker ps -a
```

## Stop a container
Run `docker stop <container_id_or_name>`.

You can find the container id by running the `ps` command.
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
## Remove a container
First, stop the container.

```text
docker container rm <container-id>
```

## Remove an image
```text
docker image rm <image-id>
```
