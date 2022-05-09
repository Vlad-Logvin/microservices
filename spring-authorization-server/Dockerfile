FROM amazoncorretto:17-alpine-jdk
ADD build/libs/spring-authorization-server-1.0-SNAPSHOT.jar spring-authorization-server.jar
ENTRYPOINT ["java","-jar","spring-authorization-server.jar"]
