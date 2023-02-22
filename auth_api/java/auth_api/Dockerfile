FROM maven:3-jdk-8-alpine

WORKDIR /app
COPY pom.xml /app
COPY src /app/src
RUN mvn clean install
COPY ./target/auth-api-1.0-SNAPSHOT-jar-with-dependencies.jar /taget/auth-api-1.0-SNAPSHOT-jar-with-dependencies.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "target/auth-api-1.0-SNAPSHOT-jar-with-dependencies.jar"]