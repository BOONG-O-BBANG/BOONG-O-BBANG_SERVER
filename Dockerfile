# OpenJDK 17을 기반으로 하는 공식 Java 이미지를 사용합니다.
FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/boongobbang-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} docker-springboot.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/docker-springboot.jar"]