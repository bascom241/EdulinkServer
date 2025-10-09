# Use an official Maven Image to build the spring booot app
FROM maven:3.8.4-openjdk-17 AS build

# set the working directory
WORKDIR /app

# copy the pom.xml and install dependencies
COPY pom.xml .


#Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# use an official OpenJDK image to run the application
FROM openjdk:17-jdk-slim

# set the working directory
WORKDIR /app

# copy the built JAR file from the build stage
COPY --from=build /app/target/EdulinkServer-0.0.1-SNAPSHOT.jar .

# expose port 8080
EXPOSE 8080

# Specify the command to run the application i.e running the jar file inside the container
ENTRYPOINT ["java", "-jar", "/app/EdulinkServer-0.0.1-SNAPSHOT.jar"]


## now we need to build and create an image using this docker file
## build the image using ## docker build -t edulink-deployment .
## tag the application ## docker tag edulink-deployment:latest bascotee/edulink-deployment:latest
## push to deployment docker push bascotee/edulink-deployment:latest
