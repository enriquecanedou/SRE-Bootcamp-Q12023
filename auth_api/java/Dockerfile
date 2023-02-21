#base image
FROM maven:3-jdk-8-alpine

# Add your instructions to dockerize the application
# App must run on port 8000
COPY . /src/main/java
WORKDIR /src/main/java
RUN ["javac", "App.java"]
ENTRYPOINT ["java", "App.java"]