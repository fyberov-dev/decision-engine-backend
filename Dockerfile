FROM eclipse-temurin:25-jre-alpine

RUN mkdir /opt/app

COPY build/libs/backend-*.jar /opt/app/app.jar

CMD ["java", "-jar", "/opt/app/app.jar"]