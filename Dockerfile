FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/your-app-name-3.2.6.jar /app/your-app-name-3.2.6.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/your-app-name-3.2.6.jar"]
