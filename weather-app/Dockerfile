FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /opt/app

# Copy the jar file into the container
COPY ./target/weather-app-0.0.1-SNAPSHOT.jar weather-app.jar

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "/opt/app/weather-app.jar"]
