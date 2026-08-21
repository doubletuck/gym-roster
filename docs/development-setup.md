
# Development Setup
This document provides information for a developer to get their local development environment configured so that they can do active development on this module. The instructions are specific to a macOS environment. 

### Table of Contents
* [Required Software](#required-software)
* [Install Homebrew](#install-homebrew)
* [Install Java](#install-java)
* [Install Maven](#install-maven)
* [Install Docker](#install-docker)
* [Install PostgreSQL](#install-postgresql)
* [Install psql](#install-psql)
* [Create database](#create-database)
* [Setup GitHub personal access token](#setup-github-personal-access-token)

## Required Software
* Homebrew - A package manager for installing software libraries and packages.
* Java 25 - The programming language used to develop this service.
* Maven 3.9.x - The build tool used for this Java service.
* Docker
* Postgres

## Install Homebrew

Homebrew is a package manager for macOS. Go to the [Homebrew](https://brew.sh/) website for information on its installation. 

## Install Java

### 1. Install JDK using Homebrew
To install Java 25, run the following command in your terminal:

```shell
brew install openjdk@25
```

Brew will not overwrite an existing JDK installation, nor will it make this installation the default on your `PATH`. This project does not rely on `JAVA_HOME` or a system-default `java`. Instead it pins its own build to a specific JDK using a [Maven toolchain](https://maven.apache.org/guides/mini/guide-using-toolchains.html), configured next. This means you can have other JDK versions installed and set as your shell default without affecting this project's build.

### 2. Register the JDK in a Maven toolchain
Find the path to the JDK you just installed:
```shell
brew --prefix openjdk@25
```
This will print something like `/opt/homebrew/opt/openjdk@25`.

If you don't already have a `~/.m2/toolchains.xml` file, create one. Add a `<toolchain>` entry for JDK 25, using the path from above appended with `/libexec/openjdk.jdk/Contents/Home`. Below shows the contents of an example `toolchains.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<toolchains xmlns="http://maven.apache.org/TOOLCHAINS/1.1.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/TOOLCHAINS/1.1.0 https://maven.apache.org/xsd/toolchains-1.1.0.xsd">
    <toolchain>
        <type>jdk</type>
        <provides>
            <version>25</version>
        </provides>
        <configuration>
            <jdkHome>/opt/homebrew/opt/openjdk@25/libexec/openjdk.jdk/Contents/Home</jdkHome>
        </configuration>
    </toolchain>
</toolchains>
```

If you already have other `<toolchain>` entries in that file, then just add the toolchain block for JDK 25 alongside them.

### 3. Confirm the configuration
From the project root, run a build:
```shell
mvn clean install
```
You should see `BUILD SUCCESS`. If the toolchain isn't found, Maven will fail fast with a "Cannot find matching toolchain" error rather than silently falling back to whatever JDK is on your `PATH`.

### A note on Lombok and JDK 25
This project's `pom.xml` sets `<proc>full</proc>` on `maven-compiler-plugin`. This works around a [known upstream Lombok bug](https://github.com/projectlombok/lombok/issues/3846): on JDK 23+, `javac`'s default annotation-processing mode silently drops the getters/setters/etc. that Lombok generates, without any error. If you ever see a wall of "cannot find symbol: method getX()/setX()" errors referencing classes annotated with `@Getter`/`@Setter`/`@Data`, check that this setting is still in place before assuming something else is wrong.

## Install Maven

```shell
brew install maven
```

## Install Docker
Docker allows for the creation, packaging and execution of a managed environments for applications. Go to the [Docker](https://docs.docker.com/get-started/get-docker/) website for information on its installation.

## Install PostgreSQL

### 1. Download the PostgreSQL image
```shell
docker pull postgres:18-alpine
```

### 2. Create and start a container from the PostgreSQL image
```shell
docker run --name gym-roster-postgres -p 5432:5432 \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=gympass \
  -e POSTGRES_DB=gymroster \
  -v gym-roster-data:/var/lib/postgresql \
  -d postgres:18-alpine
```
Where:
* `docker run`
   * The docker command used to run a container based on an image.
* `--name gym-roster-postgres`
   * Assigns the name "gym-roster-postgres" to the container that's being created from the `run` command.
* `-e POSTGRES_USER=postgres`
   * The `-e` flag sets an environment variable inside the container.
   * The superuser. Note, the absence of this environment variable will default to a superuser name called `postgres`.
* `-e POSTGRES_PASSWORD=gympass`
   * The Postgres server will use this password for the superuser in `POSTGRES_USER`.
* `-e POSTGRES_DB=gymroster`
   * The database that will be created on startup and owned by the superuser.
* `-v gym-roster-data:/var/lib/postgresql/data`
   * Maps a Docker-managed volume named `gym-roster-data`to the director inside the container where Postgres actually stores its files. If the container is removed, the volume with your data will remain and can be reattached.
* `-d`
   * The `-d` flag tells docker to run the container in detached mode which means it will run in the background. 
* `postgres:18-alpine`
   * The image that docker will use to create the container. The image is pulled from Docker Hub if it's not already available locally. 

Note, the absence of `POSTGRES_USER=<username>` means that the superuser name will default to `postgres`.

## Install psql
Psql is a command line client tool for querying the PostgreSQL database and is an optional step as there are many tools for interfacing with the database.

Homebrew's package for the PostgreSQL client tools is libpq, which includes psql, pg_dump, and other client utilities.)
```shell
brew install libpq
```

Finally, symlink psql (and other libpq tools) into /usr/local/bin. (Note: libpq does not install itself in the /usr/local/bin directory. Thus, you need to link them to the directory to use the installed binaries.
```shell
brew link --force libpq
```

To connect to psql, run the command below. You will be prompted for your password.
```shell
psql -h localhost -p 5432 -U postgres
```

## Setup GitHub personal access token

This project uses the [gym-common](https://github.com/doubletuck/gym-common) library which is hosted as a GitHub package. This project's [pom.xml](../pom.xml) has a repository setting that refers to the this package repository. Because GitHub packages require authentication to access the artifacts, you will need to have a token that allows your account to access packages. This will be needed for fetching libraries locally as well as running workflow actions used by this project.

#### Generate GitHub token

1. Go to `Settings` in your GitHub developer account
1. Click the `Credentials` menu option
1. Click the `Personal access tokens (classix)` menu option
1. Click on the `Generate new token` button to view the create page
1. The values below are guidelines, but do not have to what is provided **except** for the `Select scopes`:
   - **Note**: `Package registry reader token`
   - **Expiration**: No expiration
   - **Select scopes**: Select `read:packages`
1. Press the `Generate token` button
1. *Copy the personal access token* **immediately** as it will not be accessible again


#### Update local maven settings.xml file

If you don't already have a `~/.m2/settings.xml` file then create one and copy the block below into the file.

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>your-github-username</username>
            <password>your-github-access-token</password>
        </server>
    </servers>
</settings>
```

Replace `your-github-username` with the username of your GitHub developer account where you generated the personal access token.

Replace `your-github-access-token` with the generated token value that you copied in the steps above.

