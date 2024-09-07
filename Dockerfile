FROM openjdk:17-jdk

WORKDIR /app

COPY target/manage-education-0.0.1-SNAPSHOT.jar /app/manage-education.jar

COPY src/main/resources/serviceAccountKey.json /app/config/serviceAccountKey.json

ENV GOOGLE_APPLICATION_CREDENTIALS=/app/config/serviceAccountKey.json

EXPOSE 8080

CMD ["java", "-jar", "manage-education.jar"]