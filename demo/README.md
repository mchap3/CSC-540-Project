## Docker compose project Demo

To run the demo application as a Docker compose project:

- include environment variables for compose file
- build and start the project using `docker compose up -d`
- execute the included runnable JAR using `docker exec -it <container-name> java -jar csc540project.jar`
- NOTE: database data is rewritten at the start of each application launch