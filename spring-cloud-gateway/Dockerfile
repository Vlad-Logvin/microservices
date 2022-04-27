FROM amazoncorretto:17-alpine-jdk
ADD build/libs/spring-cloud-gateway-1.0-SNAPSHOT.jar spring-cloud-gateway.jar
ENTRYPOINT ["java","-jar","spring-cloud-gateway.jar"]
