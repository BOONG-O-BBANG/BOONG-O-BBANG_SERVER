# OpenJDK 17을 기반으로 하는 공식 Java 이미지를 사용합니다.
FROM adoptopenjdk/openjdk17:latest

# 작업 디렉토리를 설정합니다.
WORKDIR /app

# 서브모듈을 Docker 이미지에 추가합니다.
COPY ./BOONG-O-BBANG_ENV /app/BOONG-O-BBANG_ENV

# 소스 코드를 Docker 이미지에 추가합니다.
COPY . /app

# Gradle을 사용하여 프로젝트를 빌드합니다.
RUN ./gradlew build

# 빌드된 JAR 파일을 복사합니다. 프로젝트에 따라 파일 이름이 다를 수 있습니다.
COPY build/libs/boongobbang-0.0.1-SNAPSHOT.jar /app/app.jar

# 컨테이너가 실행될 때 실행할 명령을 지정합니다.
CMD ["java", "-jar", "app.jar"]
