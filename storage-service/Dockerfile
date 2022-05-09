FROM amazoncorretto:17-alpine-jdk
ADD build/libs/storage-service-1.0-SNAPSHOT.jar storage-service.jar
ENTRYPOINT ["java","-jar","storage-service.jar"]
