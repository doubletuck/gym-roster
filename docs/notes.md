# Notes
Random notes about the project that need to be captured because the configuration or setup only happens infrequently and finding the solution was not straightforward.

### Using github workflow with maven dependencies from a private github package registriy
This project has a dependency on the double-tuck/gym-common library which is generated and stored in github packages.
Workflows that download the pom.xml's libraries have to be able to read from the pacakge registry otherwise a 401 unauthorized error will occur when fetching the artifact. The org developers should have a personal access token with `read:packages` access. This article outlines the steps:
https://www.schakko.de/2020/12/19/using-github-workflow-with-maven-dependencies-from-a-private-github-package-registry/
https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry
https://docs.github.com/en/actions/tutorials/authenticate-with-github_token#using-the-github_token-in-a-workflow