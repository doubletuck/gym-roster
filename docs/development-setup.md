
# Development Setup
This document provides information for a developer to get their local development environment configured so that they can do active development on this module. Note that the instructions are geared for a macOS environment. 

## Software needed
* Homebrew - A package manager for installing software libraries and packages.
* Java 21 - The programming language used to develop this service.
* Maven 3.9.x - The build tool used for this Java service.
* Docker

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

## Archive
You may see a message similar to below when you finish installing the JDK.
```
==> openjdk@21
For the system Java wrappers to find this JDK, symlink it with
  sudo ln -sfn /usr/local/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk

openjdk@21 is keg-only, which means it was not symlinked into /usr/local,
because this is an alternate version of another formula.

If you need to have openjdk@21 first in your PATH, run:
  echo 'export PATH="/usr/local/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc

For compilers to find openjdk@21 you may need to set:
  export CPPFLAGS="-I/usr/local/opt/openjdk@21/include"
```
  