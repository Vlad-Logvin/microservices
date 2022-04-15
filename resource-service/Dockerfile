FROM amazoncorretto:17-alpine-jdk
ADD web/build/libs/web-0.0.1-SNAPSHOT.jar resource-service.jar
ENTRYPOINT ["java","-jar","resource-service.jar"]
