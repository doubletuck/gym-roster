
# Development Setup
This document provides information for a developer to get their local development environment configured so that they can do active development on this module. Note that the instructions are geared for a macOS environment. 

## Software needed
* Homebrew - A package manager for installing software libraries and packages.
* Java 21 - The programming language used to develop this service.
* Maven 3.9.x - The build tool used for this Java service.
* Docker
* Postgres

## Install Homebrew

Homebrew is a package manager for macOS. Go to the [Homebrew](https://brew.sh/) website for information on its installation. 

## Install Java

### 1. Check the JDK version
Java is the programmatic language used to create this service and it is likely that your OS will already have Java installed.

To check which version of Java is installed, run:
```shell
java --version
```

Example return:
```text
% java --version
openjdk 17.0.1 2021-10-19
OpenJDK Runtime Environment (build 17.0.1+12-39)
OpenJDK 64-Bit Server VM (build 17.0.1+12-39, mixed mode, sharing)
```

If you already have Java 21 installed, then you can skip this section.

### 2. Install JDK using Homebrew
If your version is not Java 21 or greater or if you don't have Java installed, then run:
```shell
brew install openjdk@21
```

### 3. Set JAVA_HOME in your shell
Brew will not overwrite the existing JDK installation, nor will it make this installation the default. To use the newly installed version of JDK, set the `JAVA_HOME` variable in the `.zshrc`.

Add the following to the `~/.zshrc` file:
```text
export JAVA_HOME="/usr/local/opt/openjdk@21"
export PATH="$JAVA_HOME/bin:$PATH"
```

### 4. Reload the configuration
Reload the shell so that the environment variables are loaded into your current shell.
```shell
source ~/.zshrc
```

### 5. Confirm the configuration
**Confirm the `JAVA_HOME` environment variable:**
```shell
echo $JAVA_HOME
```
The output should be:
```text
/usr/local/opt/openjdk@21
```

**Confirm that the java version:**
```shell
java -version
```
The output should be similar to:
```text
openjdk version "21.0.5" 2024-10-15
OpenJDK Runtime Environment Homebrew (build 21.0.5)
OpenJDK 64-Bit Server VM Homebrew (build 21.0.5, mixed mode, sharing)
```

## Install Maven

```shell
brew install maven
```

## Install Docker
Docker allows for the creation, packaging and execution of a managed environments for applications. Go to the [Docker](https://docs.docker.com/get-started/get-docker/) website for information on its installation.

## Install Postgres
### 1. Download the Postgres image
```shell
docker pull postgres:17-alpine
```

### 2. Create and start a container from the Postgres image
```shell
docker run --name gym-roster-postgres -p 5432:5432 -e POSTGRES_PASSWORD=gympass -d postgres:17-alpine 
```
Where:
* `docker run`
   * The docker command used to run a container based on an image.
* `--name gym-roster-postgres`
   * Assigns the name "gym-roster-postgres" to the container that's being created from the `run` command.
* `-e POSTGRES_PASSWORD=gympass`
   * The `-e` flag sets an environment variable inside the container. `POSTGRES_PASSWORD` is required by image.
   * The Postgres server will use this password for the user account called `postgres`.
* `-d`
   * The `-d` flag tells docker to run the container in detached mode which means it will run in the background. 
* `postgres:17-alpine`
   * The image that docker will use to create the container. The image is pulled from Docker Hub if it's not already available locally. 

## Access the Postgres container
#### To view logs:
```shell
docker logs gym-roster-postgres
```

#### To access the container's shell:
```shell
docker exec -it gym-roster-postgres bash
```

#### To connect to Postgres: Inside the container, you can use the psql command to interact with the Postgres database:
Access the container shell as shown above and then use the `psql` CLI. 
```shell
psql -U postgres
```
This will open a PostgreSQL interactive shell for the postgres user, using the `gympass` you specified earlier.

#### References:
* [How to Use the Postgres Docker Official Image](https://www.docker.com/blog/how-to-use-the-postgres-docker-official-image/)

## Install psql (Optional)
Homebrew's package for the PostgreSQL client tools is libpq, which includes psql, pg_dump, and other client utilities.)
```shell
brew install libpq
```

Finally, symlink psql (and other libpq tools) into /usr/local/bin. (Note: libpq does not install itself in the /usr/local/bin directory. Thus, you need to link them to the directory to use the installed binaries.
```shell
brew link --force libpq
```

## To connect:
```shell
psql -h localhost -p 5432 -U postgres
```

#### References:
* Instructions came from [Install psql without PostgreSQL on MacOS](https://www.linkedin.com/pulse/install-psql-without-postgresql-macos-dexter-hardy/)

