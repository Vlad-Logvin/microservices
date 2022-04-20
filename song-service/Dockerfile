FROM amazoncorretto:17-alpine-jdk
ADD build/libs/song-service-1.0-SNAPSHOT.jar song-service.jar
ENTRYPOINT ["java","-jar","song-service.jar"]
