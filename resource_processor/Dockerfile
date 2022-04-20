FROM amazoncorretto:17-alpine-jdk
ADD build/libs/resource_processor-1.0-SNAPSHOT.jar resource-processor.jar
ENTRYPOINT ["java","-jar","resource-processor.jar"]
