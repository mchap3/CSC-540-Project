# Use a base image with Java installed
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY csc540project.jar /app/

# Use Docker exec -it [container-name] java -jar csc540project.jar to run inside docker console
# CMD ["java", "-jar", "csc540project.jar"]